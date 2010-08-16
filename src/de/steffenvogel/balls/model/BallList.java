package de.steffenvogel.balls.model;

import java.util.concurrent.CopyOnWriteArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BallList extends CopyOnWriteArrayList<Ball> {
	
	private static final long serialVersionUID = -8941543143009333597L;

	@Override
	public boolean add(Ball b) {
		if (checkCollision(b) == false) {
			super.add(b);
			return true;
		} else {
			return false;
		}
	}

	public boolean checkCollision(Ball b) {
		boolean collision = false;
		for (Ball ball : this) {
			if (b.checkCollision(ball)) {
				collision = true;
			}
		}

		return collision;
	}

	public void clear() {
		for (Ball ball : this) {
			remove(ball);
		}
	}

	public Element toXml(Document doc) {
		Element xmlBalls = doc.createElement("balls");

		for (Ball ball : this) {
			xmlBalls.appendChild(ball.toXml(doc));
		}

		return xmlBalls;
	}
}
