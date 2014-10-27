package user_interface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import core.Timeflecks;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import logging.GlobalLogger;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private TaskListTablePanel panel;
	private ArrayList<CalendarPanel> cpanels;

	public MainWindow()
	{
		super();

		panel = null;
		cpanels = new ArrayList<CalendarPanel>();

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				setTitle("Timeflecks");

				// Set the layout manager
				setLayout(new FlowLayout());

				addComponents();
				GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
						"MainWindow", "Added components");

				displayFrame();
				GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
						"MainWindow", "Displaying frame");
				
				Timeflecks.getSharedApplication().getMainWindow().refresh();
			}
		});
	}

	public void addComponents()
	{
		// Add the menu bar
		MenuBar menu = new MenuBar();
		setJMenuBar(menu);

		// Add the task list panel
		TaskListTableModel taskListTableModel = new TaskListTableModel();
		panel = new TaskListTablePanel(taskListTableModel);

		getContentPane().add(panel);

		// Add the calendar
		addCalendar(true);

	}

	public void addCalendar(boolean weekView)
	{
		if (weekView == true)
		{
			// CalendarPanel p = new CalendarPanel(true, true, 100, 1000);

			JPanel container = new JPanel();

			FlowLayout panelLayout = new FlowLayout();
			panelLayout.setHgap(0);
			panelLayout.setVgap(0);
			container.setLayout(panelLayout);

			JScrollPane s = new JScrollPane(container);

			int width = 100;
			int height = 1000;

			Date d = new Date();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			d = cal.getTime();

			for (int i = 0; i < 7; i++)
			{
				CalendarPanel p;
				if (i == 0)
				{
					p = new CalendarPanel(d, true, true, width, height);
				}
				else if (i == 6)
				{
					p = new CalendarPanel(d, false, false, width, height);
				}
				else
				{
					p = new CalendarPanel(d, false, true, width, height);
				}

				cal.add(Calendar.DATE, 1);
				d = cal.getTime();

				cpanels.add(p);
				container.add(p);
			}

			s.setPreferredSize(new Dimension(730, 440));

			getContentPane().add(s);
		}
		else
		{
			JPanel container = new JPanel();

			FlowLayout panelLayout = new FlowLayout();
			panelLayout.setHgap(0);
			panelLayout.setVgap(0);
			container.setLayout(panelLayout);

			JScrollPane s = new JScrollPane(container);

			int width = 700;
			int height = 1000;

			Date d = new Date();
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -1); We want the current day
			d = cal.getTime();
			
			CalendarPanel p = new CalendarPanel(d, true, false, width, height);
			
			cpanels.add(p);
			container.add(p);

			s.setPreferredSize(new Dimension(730, 440));

			getContentPane().add(s);
		}
	}

	public void displayFrame()
	{
		// TODO: Remove this
		setSize(1330, 520);
		setMinimumSize(new Dimension(1330, 520));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void refresh()
	{
		// Refresh all calendar panels
		// Could also store in the list

		for (CalendarPanel p : cpanels)
		{
			p.refresh();
		}

		// Refresh the table
		if (panel != null)
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MainWindow", "refresh",
					"About to refresh panel " + panel);

			panel.refresh();
		}
		else
		{
			GlobalLogger.getLogger().logp(Level.WARNING, "MainWindow",
					"refresh", "TaskListTablePanel named \"panel\" was null.");
		}

	}

	// public static void main(String[] args)
	// {
	// MainWindow window = new MainWindow();
	//
	// window.setVisible(true);
	// }

	public ArrayList<CalendarPanel> getCpanels()
	{
		return cpanels;
	}

	public TaskListTablePanel getTablePanel()
	{
		return panel;
	}

}
