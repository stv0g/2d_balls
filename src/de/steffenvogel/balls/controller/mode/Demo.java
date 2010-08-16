package de.steffenvogel.balls.controller.mode;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;

import de.steffenvogel.balls.controller.Game;
import de.steffenvogel.balls.model.Ball;
import de.steffenvogel.balls.model.Barrier;
import de.steffenvogel.balls.model.Hole;
import de.steffenvogel.balls.model.Level;
import de.steffenvogel.balls.model.State;
import de.steffenvogel.balls.view.Field;
import de.steffenvogel.balls.view.Sound;
import de.steffenvogel.util.Vector2d;

public class Demo extends Game {
	private Ball pressed, moved;
	private Barrier barrier;

	public Demo(Level level, State state) {
		super(level, state);

		collisionWithBalls = true;
		collisionWithBarriers = true;
		resizable = true;
		gravity = 15000;
		friction = 1;
		borderBehaviour = BorderBehaviour.REFLECT;
		
		for (int i = 0; i < 6; i++)
			this.level.balls.add(Ball.random(this.level));
	}

	@Override
	protected void ballCollision(Ball b1, Ball b2) {
		state.addPoints((int) (b1.size + b2.size)/Field.VIRTUAL_RESOLUTION);
	}

	@Override
	protected void ballInHole(Ball ball, Hole hole) {
		state.addPoints(5000);
	}

	@Override
	protected void barrierCollision(Ball ball, Barrier barrier) {
		//level.barriers.remove(barrier);
		
		state.addPoints(300);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'r':
			level.balls.clear();
			level.barriers.clear();
			level.holes.clear();
			break;
		case 's':
			for (Ball ball : level.balls)
				ball.stop();
			break;
		case 'f':
			level.save(new File("level.xml"));
			break;
		case 'l':
			level.load(new File("level.xml"));
			break;
		case 'n':
			for (int i = 0; i < 6; i++)
				level.balls.add(Ball.random(level));
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
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
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		long size;
		double mass;
		Color color;

		if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
			size = 1 * Field.VIRTUAL_RESOLUTION;
			mass = 0;
			color = Color.green;
		} else {
			size = 20 * Field.VIRTUAL_RESOLUTION;
			mass = Math.round(Math.PI * Math.pow(10 * Field.VIRTUAL_RESOLUTION, 2));
			color = Ball.getColorFromSize(20 * Field.VIRTUAL_RESOLUTION);
		}

		// Snapping
		if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
			Vector2d mouse = Field.point2Vector(e.getPoint());
			for (Ball b : level.balls) {
				if (b.position.distance(mouse) < 2000) {
					pressed = b;
					break;
				}
			}
		}

		if (pressed == null) {
			pressed = new Ball(Field.point2Vector(e.getPoint()), new Vector2d(0, 0), size, mass, color);
		}
		moved = new Ball(pressed);

		// TODO check collision between start and endpoint of barrier
		if (level.balls.checkCollision(pressed) == false) {
			if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
				barrier = new Barrier(pressed, moved);
				level.barriers.add(barrier);
			} else if (e.getButton() == MouseEvent.BUTTON1) {
				level.balls.add(pressed);
			}
		} else {
			pressed = null;
			moved = null;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (pressed != null) {
			if (barrier == null) {
				pressed.orientation = pressed.position.sub(moved.position).scMultp(0.1);
			}
		}

		pressed = null;
		moved = null;
		barrier = null;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (pressed != null) {
			if (pressed.size + 2 * Field.VIRTUAL_RESOLUTION * e.getWheelRotation() > Ball.minSize && pressed.size + 2 * Field.VIRTUAL_RESOLUTION * e.getWheelRotation() < Ball.maxSize) {
				pressed.size += 2 * e.getWheelRotation() * Field.VIRTUAL_RESOLUTION;
				pressed.mass = Math.PI * Math.pow(pressed.size, 2);
				pressed.color = Ball.getColorFromSize(pressed.size);
			}
		}
	}
}
