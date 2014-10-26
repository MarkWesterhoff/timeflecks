package user_interface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import core.Timeflecks;

import java.awt.Dimension;
import java.awt.FlowLayout;
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
				
				// Set the layout manager
				setLayout(new FlowLayout());

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
		
		// Add the task list panel
		TaskListTableModel taskListTableModel = new TaskListTableModel(Timeflecks.getSharedApplication().getTaskList());
		TaskListTablePanel taskListTablePanel = new TaskListTablePanel(taskListTableModel);
		
		getContentPane().add(taskListTablePanel);
		
		// Add the calendar
//		CalendarPanel p = new CalendarPanel(true, true, 100, 1000);
		
		JPanel container = new JPanel();

		FlowLayout panelLayout = new FlowLayout();
		panelLayout.setHgap(0);
		panelLayout.setVgap(0);
		container.setLayout(panelLayout);

		JScrollPane s = new JScrollPane(container);

		int width = 100;
		int height = 1000;

		for (int i = 0; i < 7; i++)
		{
			CalendarPanel p;
			if (i == 0)
			{
				p = new CalendarPanel(true, true, width, height);
			}
			else if (i == 6)
			{
				p = new CalendarPanel(false, false, width, height);
			}
			else
			{
				p = new CalendarPanel(false, true, width, height);
			}

			container.add(p);

		}
		
		s.setPreferredSize(new Dimension(500, 400));
		
		getContentPane().add(s);

	}

	public void displayFrame()
	{
		// TODO: Remove this
		setSize(1000, 400);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setVisible(true);
	}

	public static void main(String[] args)
	{
		MainWindow window = new MainWindow();

		window.setVisible(true);
	}

}
