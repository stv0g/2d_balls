package de.steffenvogel.balls.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 3710344818603193892L;
	
	public JMenu smLoad, mnGame, mnHelp;

	public MenuBar() {
		mnGame = new JMenu("Spiel");
		mnHelp = new JMenu("?");

		smLoad = new JMenu("starten");
		JMenuItem miQuit = new JMenuItem("Beenden");
		JMenuItem miHelp = new JMenuItem("Hilfe");
		JMenuItem miAbout = new JMenuItem("Über");
		
		this.add(mnGame);
		this.add(mnHelp);
		
		mnGame.add(smLoad);
		mnGame.add(miQuit);
		mnHelp.add(miHelp);
		mnHelp.add(miAbout);
	}
}
