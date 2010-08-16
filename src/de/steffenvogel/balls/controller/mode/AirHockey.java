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

public class AirHockey extends Game {
	
	private Ball dodger = null;
	private Vector2d mousePosition;
	
	public AirHockey(Level level, State state) {
		super(level, state);

		collisionWithBalls = true;
		collisionWithBarriers = false;
		gravity = 70000;
		friction = 0.978;
		borderBehaviour = BorderBehaviour.REFLECT;
		
		for (int i = 0; i < 6; i++)
			level.balls.add(Ball.random(level));
	}
	
	protected void process() {
		if (this.dodger != null) {
			dodger.orientation = mousePosition.sub(dodger.position);		
			super.process();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = Field.point2Vector(e.getPoint());
		if (this.dodger == null) {
			this.dodger = new Ball(mousePosition, new Vector2d(0, 0), 20 * Field.VIRTUAL_RESOLUTION, 0, Color.red);
			while (!level.balls.add(this.dodger));
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	protected void ballCollision(Ball b1, Ball b2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void ballInHole(Ball ball, Hole hole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void barrierCollision(Ball ball, Barrier barrier) {
		// TODO Auto-generated method stub
		
	}

}
