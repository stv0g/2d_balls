package de.steffenvogel.balls.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Observable;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import de.steffenvogel.balls.controller.mode.Demo;

public class State extends Observable implements ActionListener {

	public enum Status {
		RUNNING, PAUSED, STOPPED
	}
	
	private static final int TIME_RESOLUTION = 10;
	
	public Class<?> game;
	public int score;
	public String nick;

	public long time; // playtime in ms;
	public Status status;
	
	public int level;
	
	private Timer timer;

	public State() {
		timer = new Timer(TIME_RESOLUTION, this);
		
		reset();
	}

	public void reset() {
		this.stop();
		this.score = 0;
		this.time = 0;
		this.game = Demo.class;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void gameOver() {
		JOptionPane.showMessageDialog(null, "Sorry! Du hast verloren!",
			"GameOver", JOptionPane.WARNING_MESSAGE);
		this.stop();
	}
	
	public void pause() {
		this.status = Status.PAUSED;
		timer.stop();
	}
	
	public void stop() {
		this.status = Status.STOPPED;
		timer.stop();
	}
	
	public void start() {
		this.status = Status.RUNNING;
		timer.start();
	}
	
	public void addPoints(int points) {
		score += points;
		
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.time += TIME_RESOLUTION;
		
		this.setChanged();
		this.notifyObservers();
	}
}
