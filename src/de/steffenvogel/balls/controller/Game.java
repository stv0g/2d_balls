package de.steffenvogel.balls.controller;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.ListIterator;
import java.util.TimerTask;

import de.steffenvogel.balls.model.Ball;
import de.steffenvogel.balls.model.Barrier;
import de.steffenvogel.balls.model.Hole;
import de.steffenvogel.balls.model.Level;
import de.steffenvogel.balls.model.State;
import de.steffenvogel.balls.model.State.Status;
import de.steffenvogel.util.Vector2d;

public abstract class Game extends TimerTask implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	public enum BorderBehaviour {
		REFLECT, BEAM, REMOVE
	}

	protected Level level;
	protected State state;
	
	public BorderBehaviour borderBehaviour = BorderBehaviour.REMOVE;

	public boolean collisionWithBalls, collisionWithBarriers;
	public boolean beamBalls;
	public boolean resizable;
	public int gravity = 150000;
	public double friction;
	
	public Game(Level level, State state) {
		this.level = level;
		this.state = state;
	}

	protected abstract void ballCollision(Ball b1, Ball b2);

	protected abstract void ballInHole(Ball ball, Hole hole);

	protected abstract void barrierCollision(Ball ball, Barrier barrier);

	synchronized protected void process() {
		for (Ball b : level.balls) {
			if (b.orientation.length() > Ball.getMaxSpeed()) {
				b.orientation = b.orientation.scMultp(Ball.getMaxSpeed() / b.orientation.length());
			}
			
			if (b.position.x < -b.size || b.position.x > level.size.width + b.size || b.position.y < -b.size || b.position.y > level.size.height + b.size) {
				level.balls.remove(b);
				System.out.println("ball lost!");
			} else {
				b.move(1f);
			}
		}
		
		for (Ball b : level.balls) {
			processBall(b);
		}
	}
	
	protected void processBall(Ball b) {
		// border behaviour
		switch (this.borderBehaviour) {
		case BEAM:
			if (b.position.x < 0)
				b.position.x += level.size.width;
			else if (b.position.x > level.size.width)
				b.position.x = 0;
			
			if (b.position.y < 0)
				b.position.y += level.size.height;
			else if (b.position.y > level.size.height)
				b.position.y = 0;
			break;
			
		case REFLECT:
			if (b.position.x < b.size) { 
				float back = 1 - ((b.size - b.position.x) / (float) b.orientation.x);
				b.move(-back);
				b.orientation.x *= -1;
				b.move(back);
			}
			else if (b.position.x > level.size.width - b.size) {
				float back = 1 - ((b.size - level.size.width + b.position.x) / (float) b.orientation.x);
				b.move(-back);
				b.orientation.x *= -1;
				b.move(back);
			}
			
			if (b.position.y < b.size) {
				float back = 1 - ((b.size - b.position.y) / (float) b.orientation.y);
				b.move(-back);
				b.orientation.y *= -1;
				b.move(back);
			}
			else if (b.position.y > level.size.height - b.size) {
				float back = 1 - ((b.size - level.size.height + b.position.y) / (float) b.orientation.y);
				b.move(-back);
				b.orientation.y *= -1;
				b.move(back);
			}
			break;
			
		case REMOVE:
			// std behavior. ball already has been removed!
			break;
		}

		// Collision with other balls
		if (collisionWithBalls) {
			for (ListIterator<Ball> it = level.balls.listIterator(level.balls.indexOf(b) + 1); it.hasNext();) {
				Ball b2 = it.next();

				if (b.checkCollision(b2)) {
					// backsteppin
					int i = 0;
					float backStepSize = (float) 1 / 10;
					while (b.checkCollision(b2) && i <= 10) { // TODO check for infinity loop
						b.move(-backStepSize);
						b2.move(-backStepSize);
						i++;
					}
					b.collide(b2);	// collision
					ballCollision(b, b2);	// hook for gamemodes
					b.move(backStepSize * i);	// undo backsteppin
					b2.move(backStepSize * i);	// undo backsteppin
					
					
					// alternative
					Vector2d v = b2.orientation.sub(b.orientation);
					Vector2d bp = v.scMultp(v.length() / b.orientation.Multp(v));
					Vector2d b2p = v.scMultp(v.length() / b2.orientation.Multp(v));
					Vector2d n = b2p.sub(bp);
					
					float back = s
					
				}
			}
		}

		// Collision with barriers
		if (collisionWithBarriers) {
			for (Barrier barrier : level.barriers) {
				if (b.checkCollision(barrier)) {

					// TODO check for infinity loop
					int i = 0;
					float backStepSize = (float) 1 / 10;
					while (b.checkCollision(barrier) && i <= 10) {
						b.move(-backStepSize);
						i++;
					}

					b.collide(barrier);	//collision
					barrierCollision(b, barrier);	// hook for gamemodes
					b.move(backStepSize * i);	// undo backsteppin
				}
			}
		}

		// Collision with holes
		for (Hole hole : level.holes) {
			if (hole.position.distance(b.position) < (hole.size - b.size)) {
				level.balls.remove(b);
				ballInHole(b, hole);
			} else if (this.gravity != 0) {
				hole.gravitate(b, this.gravity);
			}
		}

		// Reibung
		if (b.orientation.length() < 5) {
			b.stop();
		} else {
			b.orientation = b.orientation.scMultp(friction);
		}
	}

	public void run() {
		synchronized (state) {
			if (state.status == Status.RUNNING) {
				process();
			}
		}
	}
}
