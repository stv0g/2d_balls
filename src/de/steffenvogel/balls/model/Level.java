package de.steffenvogel.balls.model;

import java.awt.Dimension;


import java.io.File;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import de.steffenvogel.balls.view.Field;

@SuppressWarnings("restriction")
public class Level extends Observable {
	public BallList balls = new BallList();
	public BarrierList barriers = new BarrierList(balls);
	public HoleList holes = new HoleList();
	
	public String name;
	public boolean resizable;
	
	public VirtualDimension size;
	
	public Level() {
		load();
	}
	
	public class VirtualDimension {
		public long width, height;
		
		public VirtualDimension(long width, long height) {
			this.width = width;
			this.height = height;
		}
		
		public VirtualDimension() {
			width = height = 0;
		}
		
		public void set(Dimension dim) {
			width = dim.width * Field.VIRTUAL_RESOLUTION;
			height = dim.height * Field.VIRTUAL_RESOLUTION;
		}
		
		public Dimension toDimension() {
			return new Dimension(Math.round(width / Field.VIRTUAL_RESOLUTION), Math.round(height / Field.VIRTUAL_RESOLUTION));
		}
	}
	
	public void load() {
		this.size = new VirtualDimension(80000, 60000);
		
		this.balls.clear();
		this.barriers.clear();
		this.holes.clear();
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void load(File file) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);
			
			balls.clear();
			holes.clear();
			barriers.clear();
			
			NodeList xmlBalls = document.getElementsByTagName("ball");
			for (int i = 0; i < xmlBalls.getLength(); i++) {
				Element xml = (Element) xmlBalls.item(i);
				balls.add(new Ball(xml));
			}
			
			NodeList xmlBarriers = document.getElementsByTagName("barrier");
			for (int i = 0; i < xmlBarriers.getLength(); i++) {
				Element xml = (Element) xmlBarriers.item(i);
				barriers.add(new Barrier(balls.get(Integer.valueOf(xml.getAttribute("start"))), balls.get(Integer.valueOf(xml.getAttribute("end")))));
			}
			
			NodeList xmlHoles = document.getElementsByTagName("hole");
			for (int i = 0; i < xmlHoles.getLength(); i++) {
				Element xml = (Element) xmlHoles.item(i);
				holes.add(new Hole(xml));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setChanged();
		this.notifyObservers();
	}

	public void save(File file) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			Element xmlLevel = doc.createElement("level");
			doc.appendChild(xmlLevel);

			xmlLevel.appendChild(balls.toXml(doc));
			xmlLevel.appendChild(holes.toXml(doc));
			xmlLevel.appendChild(barriers.toXml(doc));

			XMLSerializer serializer = new XMLSerializer();
			serializer.setOutputCharStream(new java.io.FileWriter(file));
			serializer.serialize(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
