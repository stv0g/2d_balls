package de.steffenvogel.balls.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import de.steffenvogel.balls.model.State;

public class StatusBar extends JPanel implements Observer {
	
	public JLabel score, nick, time, level, game;

	private static final long serialVersionUID = 1L;

	public StatusBar() {
		this.setPreferredSize(new Dimension(-1, 22));
        this.setBorder(LineBorder.createGrayLineBorder());
        this.setLayout(new GridLayout(1, 4));
        
        score = new JLabel();
        nick = new JLabel();
        time = new JLabel();
        level = new JLabel();
        game = new JLabel();
        
        this.add(score);
        this.add(nick);
        this.add(time);
        this.add(level);
        this.add(game);
	}
	
	public void update(State state) {
		score.setText("Punkte: " + state.score);
		nick.setText("Nick: " + state.nick);
		level.setText("Level: " + state.level);
		
		SimpleDateFormat tf = new SimpleDateFormat("mm:ss.SSS");
		
		time.setText("Zeit: " + tf.format(state.time));
		game.setText("Spiel: " + state.game.getSimpleName());
	}

	@Override
	public void update(Observable o, Object arg) {
		this.update((State) o);		
	}
}
