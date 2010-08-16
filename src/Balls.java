import de.steffenvogel.balls.controller.Loader;
import de.steffenvogel.balls.controller.mode.Demo;
import de.steffenvogel.balls.model.Level;
import de.steffenvogel.balls.model.State;
import de.steffenvogel.balls.view.Gui;

public class Balls {
	public static void main(String[] args) {
		
		State state = new State();						// Model
		Level level = new Level();						// Model
		Gui gui = new Gui(level, state);				// View
		Loader loader = new Loader(level, state, gui);	// Controller
		
		loader.load(Demo.class);
	}
}
