package user_interface;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import logging.GlobalLogger;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private Logger logger;

	public MainWindow()
	{
		super();

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				logger = GlobalLogger.getLogger();

				setTitle("Timeflecks");

				addComponents();
				logger.logp(Level.INFO, "MainWindow", "MainWindow",
						"Added components");

				displayFrame();
				logger.logp(Level.INFO, "MainWindow", "MainWindow",
						"Displaying frame");
			}
		});

	}

	public void addComponents()
	{
		// Add the menu bar
		MenuBar menu = new MenuBar();
		setJMenuBar(menu);

	}

	public void displayFrame()
	{
		// TODO: Remove this
		setSize(400, 400);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setVisible(true);
	}

	public static void main(String[] args)
	{
		MainWindow window = new MainWindow();

		window.setVisible(true);
	}

}
