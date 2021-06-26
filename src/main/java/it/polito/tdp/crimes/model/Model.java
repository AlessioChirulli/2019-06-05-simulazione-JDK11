package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	Graph<Integer,DefaultWeightedEdge> grafo;
	Simulator sim;
	
	public Model() {
		dao=new EventsDao();
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public List<Integer> getAnni(){
		return dao.getAnno();
	}
	
	public String creaGrafo (int anno) {
		Graphs.addAllVertices(grafo, dao.getVertex());
		for(Distretto d1: dao.getCentri(anno)) {
			for(Distretto d2: dao.getCentri(anno)) {
				if(d2.getId()!=d1.getId() && grafo.getEdge(d1.getId(), d2.getId())==null ) {
					Double distanza=LatLngTool.distance(new LatLng(d1.getLatitudine(),d2.getLongitudine()),new LatLng(d2.getLatitudine(),d2.getLongitudine()),LengthUnit.KILOMETER);
				
					Graphs.addEdgeWithVertices(grafo,d1.getId(), d2.getId(), distanza);
				}
			}
		}
		
		return "GRAFO CREATO!\n #VERTICI: "+grafo.vertexSet().size()+"\n#ARCHI: "+grafo.edgeSet().size()+"\n";
	}
	
	public String getAdiacenze() {
		String s= new String();
		for(Integer d: grafo.vertexSet()) {
			s+=d+"\n";
		List<Vicino> result= new ArrayList<>();
		for(Integer d2: Graphs.neighborListOf(grafo, d)) {
			result.add(new Vicino(d2,grafo.getEdgeWeight(grafo.getEdge(d2, d))));
		}
		Collections.sort(result);
		for(Vicino i : result) {
			s+= i.getId()+" - "+i.getDistanza()+"\n";
		}
		}
		return s;
	}
	
	public int simula(int anno,int mese,int giorno,int N) {
		sim=new Simulator(N,anno,mese,giorno,grafo);
		return sim.run();
	}
}
