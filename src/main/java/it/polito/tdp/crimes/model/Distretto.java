package it.polito.tdp.crimes.model;

public class Distretto {

	Integer id;
	double latitudine;
	double longitudine;
	
	
	
	public Distretto(Integer id, double latitudine, double longitudine) {
		super();
		this.id = id;
		this.latitudine = latitudine;
		this.longitudine = longitudine;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public double getLatitudine() {
		return latitudine;
	}
	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}
	public double getLongitudine() {
		return longitudine;
	}
	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}
	
	
}
