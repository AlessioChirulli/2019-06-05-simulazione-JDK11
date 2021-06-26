package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{

	enum EventType{
		CRIMINE,
		ARRIVOAGENTE,
		FINEGESTIONE,
	};
	
	int district_id;
	String cryme;
	LocalDateTime ora;
	EventType type;


	
	public Evento(int district_id, String cryme, LocalDateTime ora, EventType type) {
		super();
		this.district_id = district_id;
		this.cryme = cryme;
		this.ora = ora;
		this.type = type;
	}

	public LocalDateTime getOra() {
		return ora;
	}

	public void setOra(LocalDateTime ora) {
		this.ora = ora;
	}



	public String getCryme() {
		return cryme;
	}



	public void setCryme(String cryme) {
		this.cryme=cryme;
	}



	public int getDistrict_id() {
		return district_id;
	}



	public void setDistrict_id(int district_id) {
		this.district_id = district_id;
	}



	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return ora.compareTo(o.getOra());
	};
	
}
