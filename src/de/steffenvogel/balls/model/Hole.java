package de.steffenvogel.balls.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.steffenvogel.util.Vector2d;

public class Hole implements Renderable {

	public Vector2d position;
	public int size;
	public double mass;
	
	public Hole(Vector2d position, int size, double mass) {
		this.position = position;
		this.size = size;
		this.mass = mass;
	}
	
	public Hole(Element xml) {
		this.size = new Integer(xml.getAttribute("size"));
		this.position = new Vector2d((Element) xml.getElementsByTagName("position").item(0));
	}
	
	public Element toXml(Document doc) {
		Element xml = doc.createElement("hole");
		xml.setAttribute("size", new Integer(size).toString());
		xml.appendChild(position.toXml("position", doc));
		
		return xml;
	}
	
	
	// TODO geschwindigkeit bei iteration beachten!
	public void gravitate(Ball b, int gravity) {
		double r2 = position.sqDistance(b.position);
		if (mass / (double) r2 > 1 / (double) gravity) {
			Vector2d r = position.sub(b.position);
			b.orientation = b.orientation.add(r.scMultp(gravity * (1 / r.length())).scMultp(mass / r2));
		}
	}
}
