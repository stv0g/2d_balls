package de.steffenvogel.balls.controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;
import java.util.Timer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import de.steffenvogel.balls.controller.mode.AirHockey;
import de.steffenvogel.balls.controller.mode.Billiard;
import de.steffenvogel.balls.controller.mode.ChainRxn;
import de.steffenvogel.balls.controller.mode.Demo;
import de.steffenvogel.balls.controller.mode.Dodge;
import de.steffenvogel.balls.controller.mode.Golf;
import de.steffenvogel.balls.controller.mode.InkBall;
import de.steffenvogel.balls.controller.mode.Pong;
import de.steffenvogel.balls.model.Level;
import de.steffenvogel.balls.model.State;
import de.steffenvogel.balls.view.Gui;

public class Loader implements WindowListener, ComponentListener {
	private State state;
	private Level level;
	private Gui gui;
	private Timer timer;
	public Game game; // Sub-Controller
	
	private abstract class StateAction extends AbstractAction {
		private static final long serialVersionUID = -3159579754353760125L;
		protected State state;
		
		public StateAction(State state, String title) {
			super(title);
			this.state = state;
		}
	}
	
	private class LoadAction extends AbstractAction {
		private static final long serialVersionUID = 5692117074325799909L;
		Class<?> game;
		Loader loader;
		
		public LoadAction(Loader loader, Class<?> game) {
			super(game.getSimpleName());
			
			try {
				ImageIcon ico = new ImageIcon(Gui.class.getResource("images/" + game.getSimpleName().toLowerCase() + ".png"));
				ico.setImage(ico.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT)); 
				
				this.putValue(Action.SMALL_ICON, ico);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			
			this.game = game;
			this.loader = loader;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			this.loader.load(this.game);
		}
		
	}

	public Loader(Level level, State state, Gui gui) {
		this.level = level;
		this.state = state;
		this.gui = gui;
		
		gui.field.addComponentListener(this);
		gui.addWindowListener(this);
		
		this.loadActions();
	}
	
	private void loadActions() {
		gui.menuBar.mnGame.add(new StateAction(this.state, "Starten") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				this.state.start();
			}
		});
		gui.menuBar.mnGame.add(new StateAction(this.state, "Pausieren") {
			@Override
			public void actionPerformed(ActionEvent e) {
				this.state.pause();
			}
		});
		gui.menuBar.mnGame.addSeparator();
		gui.menuBar.mnGame.add(new AbstractAction("Beenden") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		gui.menuBar.smLoad.add(new LoadAction(this, AirHockey.class));
		gui.menuBar.smLoad.add(new LoadAction(this, Billiard.class));
		gui.menuBar.smLoad.add(new LoadAction(this, ChainRxn.class));
		gui.menuBar.smLoad.add(new LoadAction(this, Demo.class));
		gui.menuBar.smLoad.add(new LoadAction(this, Dodge.class));
		gui.menuBar.smLoad.add(new LoadAction(this, Golf.class));
		gui.menuBar.smLoad.add(new LoadAction(this, InkBall.class));
		gui.menuBar.smLoad.add(new LoadAction(this, Pong.class));
	}

	public void load(Class<?> game) {
		// remove listeners
		for (MouseListener listener : gui.field.getListeners(MouseListener.class)) gui.field.removeMouseListener(listener);
		for (MouseMotionListener listener : gui.field.getListeners(MouseMotionListener.class)) gui.field.removeMouseMotionListener(listener);
		for (MouseWheelListener listener : gui.field.getListeners(MouseWheelListener.class)) gui.field.removeMouseWheelListener(listener);
		for (KeyListener listener : gui.getListeners(KeyListener.class)) gui.removeKeyListener(listener);
		
		
		// create timer thread
		if (this.timer != null) {
			this.timer.cancel();
			this.timer.purge();
		}
		this.timer = new Timer("controller");
		
		// reset level and state
		this.level.load();
		this.state.reset();
		this.state.game = game;
		this.state.nick = JOptionPane.showInputDialog("Bitte gebe deinen Namen an", this.state.nick);
		
		try {
			Class<?> partypes[] = new Class[2];
			partypes[0] = Level.class;
			partypes[1] = State.class;
			Constructor<?> ctor = game.getConstructor(partypes);
			Object arglist[] = new Object[2];
			arglist[0] = level;
			arglist[1] = state;
			this.game = (Game) ctor.newInstance(arglist);
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		
		// add listeners
		gui.field.addMouseListener(this.game);
		gui.field.addMouseMotionListener(this.game);
		gui.field.addMouseWheelListener(this.game);
		gui.addKeyListener(this.game);
		
		this.gui.setIconImage(new ImageIcon(Gui.class.getResource("images/" + game.getSimpleName().toLowerCase() + ".png")).getImage());
		this.gui.setTitle("2DBalls - " + game.getSimpleName());

		// start
		this.timer.scheduleAtFixedRate(this.game, 0, 30);
		state.start();
		
		System.out.println("game loaded");	
	}

	@Override
	public void windowActivated(WindowEvent e) {
		this.state.start();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		this.state.pause();
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		this.level.size.set(gui.field.getSize());	
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
