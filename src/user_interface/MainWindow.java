package user_interface;

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

				displayFrame();
			}
		});

	}

	public void addComponents()
	{

	}

	public void displayFrame()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

}
