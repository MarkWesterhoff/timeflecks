package user_interface;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.util.logging.Level;
import logging.GlobalLogger;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	public MainWindow()
	{
		
		
		super();

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{

				setTitle("Timeflecks");

				addComponents();
				GlobalLogger.getLogger().logp(Level.INFO, "MainWindow", "MainWindow",
						"Added components");

				displayFrame();
				GlobalLogger.getLogger().logp(Level.INFO, "MainWindow", "MainWindow",
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
