package Renommeur;

import java.awt.Window;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

public class Renommeur extends SingleFrameApplication {

	/**
	 * At startup create and show the main frame of the application.
	 */
	@Override
	protected void startup() {
		show(new AppFrame(this));
	}

	/**
	 * This method is to initialize the specified window by injecting resources.
	 * Windows shown in our application come fully initialized from the GUI
	 * builder, so this additional configuration is not needed.
	 */
	@Override
	protected void configureWindow(Window root) {
	}

	/**
	 * A convenient static getter for the application instance.
	 * 
	 * @return the instance of Renommeur
	 */
	public static Renommeur getApplication() {
		return Application.getInstance(Renommeur.class);
	}

	/**
	 * Main method launching the application.
	 */
	public static void main(String[] args) {
		Renommeur.args = args;
		launch(Renommeur.class, args);
	}

	public static String[] args;

}
