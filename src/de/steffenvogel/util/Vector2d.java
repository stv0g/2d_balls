package de.steffenvogel.util;

import java.awt.Point;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Vector2d {

	public long x, y;

	public Vector2d(long x, long y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d(Vector2d vec) {
		this.x = vec.x;
		this.y = vec.y;
	}
	
	public Vector2d(Element xml) {
		this.x = new Long(xml.getAttribute("x"));
		this.y = new Long(xml.getAttribute("y"));
	}
	
	public Vector2d(Point point) {
		this.x = point.x;
		this.y = point.y;
	}
	
	public long sqLength() {
		return x*x+y*y;
	}
	
	public double length() {
		return Math.sqrt(sqLength());
	}

	public double distance(Vector2d to) {
		return Math.sqrt(sqDistance(to));
	}

	public long sqDistance(Vector2d to) {
		Vector2d vec = this.sub(to);
		
		return vec.sqLength();
	}
	
	public Vector2d add(Vector2d sum) {
		return new Vector2d(x+sum.x, y+sum.y);
	}
	
	public Vector2d sub(Vector2d sum) {
		return new Vector2d(x-sum.x, y-sum.y);
	}
	
	public Vector2d neg() {
		return new Vector2d(-x, -y);
	}
	
	public long Multp(Vector2d prod) {
		return this.x * prod.x + this.y * prod.y;
	}
	
	public Vector2d scMultp(double scalar) {
		return new Vector2d(Math.round(x * scalar), Math.round(y * scalar));
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public Element toXml(String tagName, Document doc) {
		Element elm =  doc.createElement(tagName);
		elm.setAttribute("x", new Long(x).toString());
		elm.setAttribute("y", new Long(y).toString());
		
		return elm;
	}
}
