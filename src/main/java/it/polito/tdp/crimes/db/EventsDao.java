package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Distretto;
import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.Evento;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getAnno() {
		String sql = "SELECT DISTINCT YEAR(reported_date) as year "
				+ " FROM events " 
				+" ORDER BY year ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getInt("year"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getVertex(){
		String sql = "SELECT DISTINCT district_id "
				+ "FROM events "
				+ "ORDER BY district_id";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getInt("district_id"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Distretto> getCentri(int anno){
		String sql = "SELECT DISTINCT e.district_id,AVG(e.geo_lon) as lon,AVG(e.geo_lat) as lat "
				+ "FROM events e "
				+ "WHERE YEAR(e.reported_date)=? "
				+ "GROUP BY e.district_id,YEAR(e.reported_date) "
				+ "ORDER BY e.district_id";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			
			List<Distretto> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Distretto(res.getInt("district_id"),res.getDouble("lat"),res.getDouble("lon")));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Event> listEvents(LocalDate date){
		String sql = "SELECT * "
				+ "FROM events e "
				+ "WHERE YEAR(e.reported_date)=? AND MONTH(e.reported_date)=? AND DAY(e.reported_date)=? "
				+ "ORDER BY e.reported_date" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, date.getYear());
			st.setInt(2, date.getMonthValue());
			st.setInt(3, date.getDayOfMonth());
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public int getMin(int anno) {
		String sql="SELECT e.district_id,SUM(e.is_crime) "
				+ "FROM events e "
				+ "WHERE YEAR(e.reported_date) = ? "
				+ "GROUP BY e.district_id "
				+ "ORDER BY SUM(e.is_crime)";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				conn.close();
				return res.getInt("e.district_id");
			} else {
				conn.close();
				return -1;
			}
			
			
		} catch(Throwable t) {
			t.printStackTrace();
			return -1;
		}
	}
	
	public List<Event> getEventiForDay(int anno,int mese,int giorno){
		String sql="SELECT * "
				+ "FROM events e "
				+ "WHERE year(e.reported_date)=? AND  month(e.reported_date)=? AND  day(e.reported_date)=? ";
		List<Event> result=new ArrayList<Event>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(new Event(res.getLong("incident_id"),
						res.getInt("offense_code"),
						res.getInt("offense_code_extension"), 
						res.getString("offense_type_id"), 
						res.getString("offense_category_id"),
						res.getTimestamp("reported_date").toLocalDateTime(),
						res.getString("incident_address"),
						res.getDouble("geo_lon"),
						res.getDouble("geo_lat"),
						res.getInt("district_id"),
						res.getInt("precinct_id"), 
						res.getString("neighborhood_id"),
						res.getInt("is_crime"),
						res.getInt("is_traffic")));
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
