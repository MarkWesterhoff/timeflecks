package user_interface;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;

import javax.swing.*;

import logging.GlobalLogger;
import core.*;

/**
 * Buttons for manipulating the Calendar.
 * 
 */
public class CalendarControlPanel extends JPanel implements
		TimeflecksEventResponder
{

	private static final long serialVersionUID = 1L;

	private JToggleButton dayButton;
	private JToggleButton weekButton;

	private JButton dateLeftButton;
	private JButton dateTodayButton;
	private JButton dateRightButton;
	private JButton dateWeekLeftButton;
	private JButton dateWeekRightButton;

	public CalendarControlPanel(boolean showWeekView)
	{
		setLayout(new BorderLayout());

		addDayWeekToggle(showWeekView);

		addDateControlButtons();

		final CalendarControlPanel cp = this;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				Timeflecks.getSharedApplication().registerForTimeflecksEvents(
						cp);
			}
		});
	}

	public void addDayWeekToggle(boolean showWeekView)
	{
		dayButton = new JToggleButton("Day");

		// JAVA BUG: If this starts with "W" it doesn't calculate the size, so
		// we hard-code it below. Because that doesn't work on all platforms, we
		// simply cause the button to be a little extra wide by starting the
		// button title with a space.
		weekButton = new JToggleButton(" Week");

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
					Timeflecks.getSharedApplication().getMainWindow()
							.setShowWeekView(false);
				}
				else
				{
					weekButton.setSelected(true);
					Timeflecks.getSharedApplication().getMainWindow()
							.setShowWeekView(true);
				}
			}
		});

		weekButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (weekButton.isSelected())
				{
					dayButton.setSelected(false);
					Timeflecks.getSharedApplication().getMainWindow()
							.setShowWeekView(true);
				}
				else
				{
					dayButton.setSelected(true);
					Timeflecks.getSharedApplication().getMainWindow()
							.setShowWeekView(false);
				}
			}
		});

		JPanel dayWeekPanel = new JPanel();
		FlowLayout panelLayout = new FlowLayout();
		panelLayout.setHgap(0);
		panelLayout.setVgap(0);
		dayWeekPanel.setLayout(panelLayout);

		dayWeekPanel.add(dayButton);
		dayWeekPanel.add(weekButton);

		this.add(dayWeekPanel, BorderLayout.WEST);

	}

	public void addDateControlButtons()
	{
		dateLeftButton = new JButton("<");
		dateTodayButton = new JButton("Today");
		dateRightButton = new JButton(">");

		dateWeekLeftButton = new JButton("<<");
		dateWeekRightButton = new JButton(">>");

		// By posting the notifications in this manner we adhere to good design
		// principles, because anyone in the application that needs to know can
		// respond to changes in the date displayed on the calendar.
		// Technically, the calendar doesn't even have to respond to this by
		// changing its date.

		dateLeftButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.DATE_LEFT_ONE_BUTTON);
			}
		});

		dateTodayButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.DATE_TODAY_BUTTON);
			}
		});

		dateRightButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.DATE_RIGHT_ONE_BUTTON);
			}
		});

		dateWeekLeftButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.DATE_LEFT_SEVEN_BUTTON);
			}
		});

		dateWeekRightButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.DATE_RIGHT_SEVEN_BUTTON);
			}
		});

		JPanel dateControlPanel = new JPanel();
		FlowLayout panelLayout = new FlowLayout();
		panelLayout.setHgap(0);
		panelLayout.setVgap(0);
		dateControlPanel.setLayout(panelLayout);

		dateControlPanel.add(dateWeekLeftButton);
		dateControlPanel.add(dateLeftButton);
		dateControlPanel.add(dateTodayButton);
		dateControlPanel.add(dateRightButton);
		dateControlPanel.add(dateWeekRightButton);

		this.add(dateControlPanel, BorderLayout.EAST);
	}

	/**
	 * Updates which button is selected to match proper day/week view
	 * 
	 * @param showWeekView
	 *            Whether to select the week view button
	 */
	public void updateButtons(boolean showWeekView)
	{
		weekButton.setSelected(showWeekView);
		dayButton.setSelected(!showWeekView);
	}

	public void eventPosted(TimeflecksEvent t)
	{
		if (t.equals(TimeflecksEvent.DAY_WEEK_VIEW_SWITCHED))
		{
			GlobalLogger
					.getLogger()
					.logp(Level.INFO, "CalendarControlPanel", "eventPosted",
							"Calendar control panel responding to day week view switched event.");
			this.updateButtons(Timeflecks.getSharedApplication()
					.getMainWindow().isShowingWeekView());
		}
		else
		{
			// We want to silently ignore all of the events that we aren't ready
			// for
		}
	}
}
