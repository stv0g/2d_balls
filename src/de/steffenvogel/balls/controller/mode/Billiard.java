package de.steffenvogel.balls.controller.mode;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import de.steffenvogel.balls.controller.Game;
import de.steffenvogel.balls.model.Ball;
import de.steffenvogel.balls.model.BallList;
import de.steffenvogel.balls.model.Barrier;
import de.steffenvogel.balls.model.Hole;
import de.steffenvogel.balls.model.Level;
import de.steffenvogel.balls.model.State;
import de.steffenvogel.balls.view.Gui;

public class Billiard extends Game {
	private BallList full, half;
	private Ball white, black;

	public Billiard(Level level, State state) {
		super(level, state);

		collisionWithBalls = true;
		collisionWithBarriers = true;
		gravity = 50000;
		friction = 0.98;
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
		// TODO Auto-generated method stub

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
