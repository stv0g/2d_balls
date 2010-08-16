package de.steffenvogel.balls.view;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import de.steffenvogel.balls.model.Level;
import de.steffenvogel.balls.model.State;

public class Gui extends JFrame implements Observer {
	private static final long serialVersionUID = -7936315723587248620L;

	public Field field;
	public MenuBar menuBar;
	public StatusBar statusBar; 
	
	public Gui(Level level, State state) {
		super();
		
		// window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setIconImage(new ImageIcon(Gui.class.getResource("images/balls.png")).getImage());
		this.setTitle("2DBalls");
		
		// menu
		menuBar = new MenuBar();
		this.setJMenuBar(menuBar);
		
		// field
		field = new Field(level);
		this.add(field, BorderLayout.CENTER);
		
		// statusbar
		statusBar = new StatusBar();
		this.add(statusBar, BorderLayout.SOUTH);
		
		// render thread
		Thread thread = new Thread(field);
		thread.start();
		this.setVisible(true);
		
		// observer
		state.addObserver(statusBar);
		level.addObserver(this);
		level.addObserver(field);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.pack();
	}
}
