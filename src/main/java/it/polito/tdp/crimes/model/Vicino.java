package it.polito.tdp.crimes.model;

public class Vicino  implements Comparable<Vicino>{
Integer id;
double distanza;
public Vicino(Integer id, double distanza) {
	super();
	this.id = id;
	this.distanza = distanza;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public double getDistanza() {
	return distanza;
}
public void setDistanza(double distanza) {
	this.distanza = distanza;
}
@Override
public int compareTo(Vicino o) {
	// TODO Auto-generated method stub
	return (int) (this.distanza-o.getDistanza());
}

}
