package de.steffenvogel.balls.model;

import java.util.concurrent.CopyOnWriteArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HoleList extends CopyOnWriteArrayList<Hole> {

	private static final long serialVersionUID = 379892301442116885L;

	public HoleList() {
		// TODO Auto-generated constructor stub
	}
	
	public Element toXml(Document doc) {
		Element xmlHoles = doc.createElement("holes");

		for (Hole hole : this) {
			xmlHoles.appendChild(hole.toXml(doc));
		}
		
		return xmlHoles;
	}
}
