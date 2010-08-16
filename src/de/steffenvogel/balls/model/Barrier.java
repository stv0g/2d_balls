package de.steffenvogel.balls.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class Barrier implements Renderable {
	public Ball start, end;

	public Barrier(Ball start, Ball end) {
		this.start = start;
		this.end = end;
	}
	
	
	// TODO unused ?
	public Element toXml(Document doc) {
		Element xml = doc.createElement("barrier");
		
		xml.appendChild(start.toXml(doc));
		xml.appendChild(end.toXml(doc));
		
		return xml;
	}
}
