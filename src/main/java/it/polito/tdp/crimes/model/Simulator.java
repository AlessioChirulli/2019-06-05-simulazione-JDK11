package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.db.EventsDao;
import it.polito.tdp.crimes.model.Evento.EventType;

public class Simulator {

	PriorityQueue<Evento> queue;
	
	//modello del mondo
	Graph<Integer,DefaultWeightedEdge> grafo;
	EventsDao dao;
	
	//parametri di input
	int N;
	int anno;
	int mese;
	int giorno;
	Map<Integer,Integer> agenti;
	
	//valori calcolati
	int malGestiti=0;
	
	public Simulator(int N,int anno,int mese, int giorno,Graph<Integer,DefaultWeightedEdge> grafo) {
		queue=new PriorityQueue<Evento>();
		dao=new EventsDao();
		this.agenti=new HashMap<>();
		this.N=N;
		this.anno=anno;
		this.mese=mese;
		this.giorno=giorno;
		this.grafo=grafo;
		malGestiti=0;
		
		for(Integer d : this.grafo.vertexSet()) {
			this.agenti.put(d,0);
		}
		Integer minD=dao.getMin(anno);
			agenti.put(minD,N);
		for(Event e: dao.getEventiForDay(anno, mese, giorno)) {
			queue.add(new Evento(e.getDistrict_id(),e.getOffense_category_id(),e.getReported_date(),EventType.CRIMINE));
		}
	}
	
	public int run() {
		while(!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			processEvent(e);
		}
		return malGestiti;
	}

	private void processEvent(Evento e) {
		switch(e.getType()) {
		case CRIMINE:
			System.out.println("NUOVO CRIMINE! " + e.getDistrict_id()+" ALLE "+e.getOra());
			double distanza;
			Integer partenza=cercaAgente(e.getDistrict_id());
			if(partenza!=null) {
				this.agenti.put(partenza,agenti.get(partenza)-1);
				if(e.getDistrict_id()==partenza)
					distanza=0;
				else
					distanza=grafo.getEdgeWeight(grafo.getEdge(e.getDistrict_id(),partenza));	
				long secondi=(long)((distanza*1000)/(60/3.6));
				LocalDateTime arrivo=e.getOra().plusSeconds(secondi);
				if(arrivo.isAfter(e.getOra().plusMinutes(15))) {
					malGestiti++;
					System.out.println("CRIMINE MAL GESTITO " + e.getDistrict_id()+" PER RITARDO");
				}
				queue.add(new Evento(e.getDistrict_id(),e.getCryme(),arrivo,EventType.ARRIVOAGENTE));
				break;
			}else {
				System.out.println("CRIMINE MAL GESTITO " + e.getDistrict_id()+" PER MANCANZA AGENTI");
				malGestiti++;
			}
		case ARRIVOAGENTE:
			System.out.println("ARRIVA AGENTE PER CRIMINE! " + e.getDistrict_id()+" ALLE "+e.getOra());
			LocalDateTime fine=e.getOra().plusSeconds(this.getDurata(e.getCryme()));
			this.queue.add(new Evento(e.getDistrict_id(),e.getCryme(),fine,EventType.FINEGESTIONE));
			break;
		case FINEGESTIONE:
			System.out.println("CRIMINE " + e.getDistrict_id() + " GESTITO"+" ALLE "+e.getOra());
			agenti.put(e.getDistrict_id(), agenti.get(e.getDistrict_id())+1);
			break;
		}
		
	}
	
	private Integer cercaAgente(int district_id) {
		double distanza=Double.MAX_VALUE;
		Integer distretto=null;
		for(Integer agente:agenti.keySet()) {
			if(agenti.get(agente)>0) {
			if(district_id==agente) {
				distanza=0;
				distretto=agente;
			}
			else if(grafo.getEdgeWeight(grafo.getEdge(district_id, agente))<distanza ) {
				distanza=grafo.getEdgeWeight(grafo.getEdge(district_id,agente));
				distretto=agente;
			}
			}
		}
		return distretto;
	}
	
	private Long getDurata(String offense_category_id) {
		if(offense_category_id.equals("all_other_crimes")) {
			Random r = new Random();
			if(r.nextDouble() > 0.5)
				return Long.valueOf(2*60*60);
			else
				return Long.valueOf(1*60*60);
		} else {
			return Long.valueOf(2*60*60);
		}
	}

}
