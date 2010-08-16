package de.steffenvogel.balls.controller.mode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.Timer;

import de.steffenvogel.balls.controller.Game;
import de.steffenvogel.balls.model.Ball;
import de.steffenvogel.balls.model.Barrier;
import de.steffenvogel.balls.model.Hole;
import de.steffenvogel.balls.model.Level;
import de.steffenvogel.balls.model.State;
import de.steffenvogel.balls.model.State.Status;
import de.steffenvogel.balls.view.Field;
import de.steffenvogel.util.Vector2d;

public class Dodge extends Game implements ActionListener {
	
	private Ball dodger;
	long lastAdded = System.currentTimeMillis();
	
	public Dodge(Level level, State state) {
		super(level, state);

		collisionWithBalls = true;
		collisionWithBarriers = false;
		gravity = 70000;
		friction = 1.00002;
		borderBehaviour = BorderBehaviour.REFLECT;
		
		dodger = new Ball(new Vector2d(0, 0), new Vector2d(0, 0), 20 * Field.VIRTUAL_RESOLUTION, 0, Color.red);
		level.balls.add(dodger);
		
		new Timer(3000, this).start();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		dodger.position = Field.point2Vector(e.getPoint());
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
		if (b1 == dodger || b2 == dodger) {
			state.gameOver();
		}
	}

	@Override
	protected void ballInHole(Ball ball, Hole hole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void barrierCollision(Ball ball, Barrier barrier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (state.status == Status.RUNNING) {
			level.balls.add(Ball.random(level));
			state.addPoints(100);
		}
	}

}
