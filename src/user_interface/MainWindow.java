package user_interface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.logging.Level;

import javax.swing.*;

import logging.GlobalLogger;
import core.Timeflecks;
import core.TimeflecksEvent;
import core.TimeflecksEventResponder;

public class MainWindow extends JFrame implements TimeflecksEventResponder
{
	private static final long serialVersionUID = 1L;

	private TaskListTablePanel panel;
	private ArrayList<CalendarPanel> cpanels;
	private JScrollPane scrollPane;

	private boolean showWeekView;

	public MainWindow()
	{
		super();

		panel = null;
		cpanels = new ArrayList<CalendarPanel>();
		scrollPane = null;
		showWeekView = true;

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

				// When it is created, the main window will register itself as a
				// listener for TimeflecksEvents
				Timeflecks.getSharedApplication().registerForTimeflecksEvents(
						Timeflecks.getSharedApplication().getMainWindow());

				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.GENERAL_REFRESH);
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

		JPanel calendarControlButtons = new JPanel();
		calendarControlButtons.setLayout(new BorderLayout());

		final JToggleButton dayButton = new JToggleButton("Day");
		final JToggleButton weekButton = new JToggleButton("Week");

		if (showWeekView)
		{
			dayButton.setSelected(false);
			weekButton.setSelected(true);
		}
		else
		{
			dayButton.setSelected(true);
			weekButton.setSelected(false);
		}

		dayButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (dayButton.isSelected())
				{
					weekButton.setSelected(false);
					showWeekView = false;
				}
				else
				{
					weekButton.setSelected(true);
					showWeekView = true;
				}

				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.DAY_WEEK_VIEW_SWITCHED);
			}
		});

		weekButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (weekButton.isSelected())
				{
					dayButton.setSelected(false);
					showWeekView = true;
				}
				else
				{
					dayButton.setSelected(true);
					showWeekView = false;
				}

				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.DAY_WEEK_VIEW_SWITCHED);
			}
		});

		JPanel dayWeekPanel = new JPanel();
		FlowLayout panelLayout = new FlowLayout();
		panelLayout.setHgap(0);
		panelLayout.setVgap(0);
		dayWeekPanel.setLayout(panelLayout);

		dayWeekPanel.add(dayButton);
		dayWeekPanel.add(weekButton);

		calendarControlButtons.add(dayWeekPanel, BorderLayout.WEST);

		getContentPane().add(calendarControlButtons);

		// TODO Fix for showing the correct date
		// Add the calendar
		addCalendar(showWeekView, new Date());

	}

	/**
	 * Adds the calendar panel to the MainWindow starting from date. The week
	 * view will start with the selected date as the leftmost panel, the single
	 * day view will just use that date.
	 * 
	 * @param weekView
	 *            Whether to use week view or not. If not, it will use the day
	 *            view.
	 * @param date
	 *            The date to use as the basis for the calendar.
	 */
	public void addCalendar(boolean weekView, Date date)
	{
		JPanel container = new JPanel();

		FlowLayout panelLayout = new FlowLayout();
		panelLayout.setHgap(0);
		panelLayout.setVgap(0);
		container.setLayout(panelLayout);

		scrollPane = new JScrollPane(container);

		if (weekView == true)
		{
			int width = 100;
			int height = 1000;

			Date d = date;
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			// cal.add(Calendar.DATE, -1);
			// d = cal.getTime();

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
		}
		else
		{
			int width = 700;
			int height = 1000;

			CalendarPanel p = new CalendarPanel(date, true, false, width,
					height);

			cpanels.add(p);
			container.add(p);
		}

		scrollPane.setPreferredSize(new Dimension(730, 440));

		getContentPane().add(scrollPane);
	}

	public void displayFrame()
	{
		// TODO: Remove this
		setSize(1330, 520);
		setMinimumSize(new Dimension(1330, 520));

		pack();

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

	public ArrayList<CalendarPanel> getCpanels()
	{
		return cpanels;
	}

	public TaskListTablePanel getTablePanel()
	{
		return panel;
	}

	public boolean isShowingWeekView()
	{
		return showWeekView;
	}

	public void switchViewToShowWeekView(boolean showWeekView)
	{
		this.showWeekView = showWeekView;

		for (CalendarPanel p : cpanels)
		{
			scrollPane.getViewport().remove(p);
		}

		this.getContentPane().remove(scrollPane);

		cpanels.clear();
		scrollPane = null;

		// TODO Fix for showing the current date
		addCalendar(showWeekView, new Date());

		// We need to revalidate and repaint the entire new main window, not
		// just the panels as we usually do with refresh.

		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.EVERYTHING_NEEDS_REFRESH);
	}

	public void eventPosted(TimeflecksEvent t)
	{
		if (t.equals(TimeflecksEvent.GENERAL_REFRESH))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
					"eventPosted",
					"MainWindow responding to general refresh event.");
			this.refresh();

		}
		else if (t.equals(TimeflecksEvent.EVERYTHING_NEEDS_REFRESH))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
					"eventPosted",
					"MainWindow responding to everything needs refresh event.");
			this.revalidate();
			this.repaint();
			this.refresh();
		}
		else if (t.equals(TimeflecksEvent.DAY_WEEK_VIEW_SWITCHED))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
					"eventPosted",
					"MainWindow responding to day week view switched event.");
			this.switchViewToShowWeekView(showWeekView);
		}
		else
		{
			GlobalLogger
					.getLogger()
					.logp(Level.WARNING, "MainWindow", "eventPosted",
							"MainWindow responding to unknown event. No action will be taken");
		}
	}
}
