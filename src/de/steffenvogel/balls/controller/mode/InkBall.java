package de.steffenvogel.balls.controller.mode;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import de.steffenvogel.balls.controller.Game;
import de.steffenvogel.balls.model.Ball;
import de.steffenvogel.balls.model.Barrier;
import de.steffenvogel.balls.model.Hole;
import de.steffenvogel.balls.model.Level;
import de.steffenvogel.balls.model.State;
import de.steffenvogel.balls.view.Field;
import de.steffenvogel.util.Vector2d;

public class InkBall extends Game {
	
	private Ball pressed, moved;
	private Barrier barrier;
	
	public InkBall(Level level, State state) {
		super(level, state);

		collisionWithBalls = true;
		collisionWithBarriers = true;
		gravity = 70000;
		borderBehaviour = BorderBehaviour.BEAM;
		
		level.holes.add(new Hole(new Vector2d(Math.round(level.size.width / 2), Math.round(level.size.height / 2)), 3000, 6000));
		
		while (!level.balls.add(Ball.random(level)));
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Snapping
		Vector2d mouse = Field.point2Vector(e.getPoint());
		for (Ball b : level.balls) {
			if (b.position.distance(mouse) < 2000) {
				pressed = b;
				break;
			}
		}

		if (pressed == null) {
			pressed = new Ball(Field.point2Vector(e.getPoint()), new Vector2d(0, 0), 1 * Field.VIRTUAL_RESOLUTION, 0, Color.white);
		}
		moved = new Ball(pressed);

		// TODO check collision between start and endpoint of barrier
		if (level.balls.checkCollision(pressed) == false) {
			barrier = new Barrier(pressed, moved);
			level.barriers.add(barrier);
		} else {
			pressed = null;
			moved = null;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (pressed != null) {
			moved.position = Field.point2Vector(e.getPoint());

			// Snapping
			if (barrier != null) {
				Ball snap = null;
				Vector2d mouse = Field.point2Vector(e.getPoint());

				for (Ball b : level.balls) {
					if (b.position.distance(mouse) < 2000 && b != moved && b != barrier.start) {
						snap = b;
						break;
					}
				}

				if (snap != null) {
					if (level.balls.contains(moved)) {
						level.balls.remove(moved);
					}
					barrier.end = snap;
				} else {
					if (level.balls.contains(moved) == false) {
						level.balls.add(moved);
					}
					barrier.end = moved;
				}

				if (pressed.position.distance(moved.position) > 100 && e.getModifiersEx() == MouseEvent.BUTTON2_DOWN_MASK) {
					level.barriers.add(new Barrier(pressed, moved));
					pressed = moved;
					moved = new Ball(moved);
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void ballCollision(Ball b1, Ball b2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void ballInHole(Ball ball, Hole hole) {
		while (!level.balls.add(Ball.random(level)));
		state.addPoints(100);
	}

	@Override
	protected void barrierCollision(Ball ball, Barrier barrier) {
		// TODO Auto-generated method stub
		
	}

}
