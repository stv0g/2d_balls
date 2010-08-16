package de.steffenvogel.balls.model;


import java.util.concurrent.CopyOnWriteArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BarrierList extends CopyOnWriteArrayList<Barrier> {
	
	private static final long serialVersionUID = 8921059530813919037L;
	
	BallList balls;
	
	public BarrierList(BallList balls) {
		this.balls = balls;
	}
	
	public boolean add(Barrier barrier) {
		balls.add(barrier.start);
		balls.add(barrier.end);
		return super.add(barrier);
	}
	
	public void remove(Barrier barrier) {
		super.remove(barrier);
		balls.remove(barrier.start);
		balls.remove(barrier.end);
	}
	
	public void clear() {
		for (Barrier barrier : this) {
			remove(barrier);
		}
	}
	
	public Element toXml(Document doc) {
		Element xmlBarriers = doc.createElement("barriers");
		BallList barrierBalls = new BallList();
		
		/*for (Barrier barrier : this) {
			xmlBarriers.appendChild(barrier.toXml(doc));
		}*/
		
		// TODO complete xml generation
		for (Barrier barrier : this) {
			for (Barrier subBarrier : this) {
				if (barrier.start == subBarrier.start) {
					barrierBalls.add(barrier.start);
				}
				if (barrier.start == subBarrier.end) {
					barrierBalls.add(subBarrier.start);
					barrierBalls.add(barrier.start);
					barrierBalls.add(barrier.end);
				}
				if (barrier.end == subBarrier.start) {
				}
				if (barrier.end == subBarrier.end) {
				}
			}
		}
		
		return xmlBarriers;
	}

}
