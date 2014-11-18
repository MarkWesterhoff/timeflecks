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
	private	JPanel calendarContainer;
	private JScrollPane scrollPane;

	private boolean showWeekView;
	private Date mainDate;

	public MainWindow()
	{
		super();

		panel = null;
		cpanels = new ArrayList<CalendarPanel>();
		calendarContainer = null;
		scrollPane = null;

		showWeekView = true;
		mainDate = new Date();

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
		
		calendarContainer = new JPanel();
		calendarContainer.setLayout(new BorderLayout());
		
		CalendarControlPanel controlPanel = new CalendarControlPanel(
				showWeekView);
		calendarContainer.add(controlPanel, BorderLayout.NORTH);

		// Add the calendar & controlPanel
		addCalendar(showWeekView, mainDate);
		
		getContentPane().add(calendarContainer);
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

		// TODO Remove this
		scrollPane.setPreferredSize(new Dimension(730, 420));

		calendarContainer.add(scrollPane, BorderLayout.SOUTH);
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

	/**
	 * Perform a deep refresh that rebuilds all of the panels. This is necessary
	 * whenever the date being displayed changes, or the week/day view changes.
	 * 
	 */
	public void refreshPanels()
	{
		for (CalendarPanel p : cpanels)
		{
			scrollPane.getViewport().remove(p);
		}

		this.calendarContainer.remove(scrollPane);

		cpanels.clear();
		scrollPane = null;

		// TODO Fix for showing the current date
		addCalendar(this.isShowingWeekView(), this.getMainDate());

		// We need to revalidate and repaint the entire new main window, not
		// just the panels as we usually do with refresh.

		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.EVERYTHING_NEEDS_REFRESH);
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

	/**
	 * Set whether or not to show the week view on the Calendar.
	 * 
	 * Note that this triggers the DAY_WEEK_VIEW_SWITCHED event.
	 * 
	 * @param newWeekView
	 *            Whether to show the week view
	 */
	public void setShowWeekView(boolean newWeekView)
	{
		showWeekView = newWeekView;

		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.DAY_WEEK_VIEW_SWITCHED);
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
			this.refreshPanels();
		}
		else if (t.equals(TimeflecksEvent.DATE_LEFT_ONE_BUTTON))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
					"eventPosted",
					"MainWindow responding to date left button event.");
			this.bumpDateLeft(1);
		}
		else if (t.equals(TimeflecksEvent.DATE_TODAY_BUTTON))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
					"eventPosted",
					"MainWindow responding to date today button event.");
			this.bumpDateToday();
		}
		else if (t.equals(TimeflecksEvent.DATE_RIGHT_ONE_BUTTON))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
					"eventPosted",
					"MainWindow responding to date right button event.");
			this.bumpDateRight(1);
		}
		else if (t.equals(TimeflecksEvent.DATE_LEFT_SEVEN_BUTTON))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
					"eventPosted",
					"MainWindow responding to date week left button event.");
			this.bumpDateLeft(7);
		}
		else if (t.equals(TimeflecksEvent.DATE_RIGHT_SEVEN_BUTTON))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MainWindow",
					"eventPosted",
					"MainWindow responding to date week right button event.");
			this.bumpDateRight(7);
		}
		else
		{
			// We want to silently ignore all events that we aren't prepared to
			// handle.
			// GlobalLogger
			// .getLogger()
			// .logp(Level.WARNING, "MainWindow", "eventPosted",
			// "MainWindow responding to unknown event. No action will be taken");
		}
	}

	public void bumpDateLeft(int numDays)
	{
		Date d = getMainDate();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, -1 * numDays);
		d = c.getTime();
		setMainDate(d);
	}

	public void bumpDateToday()
	{
		setMainDate(new Date());
	}

	public void bumpDateRight(int numDays)
	{
		Date d = getMainDate();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, 1 * numDays);
		d = c.getTime();
		setMainDate(d);
	}

	public Date getMainDate()
	{
		return mainDate;
	}

	public void setMainDate(Date mainDate)
	{
		this.mainDate = mainDate;

		GlobalLogger.getLogger().logp(Level.INFO, "MainWindow", "setMainDate",
				"Setting date to " + getMainDate());

		// We need to refresh the dates that are shown.
		this.refreshPanels();
	}
}
