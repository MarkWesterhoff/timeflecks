package user_interface.test;

import static org.junit.Assert.fail;

import java.awt.FlowLayout;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import javax.swing.*;

import org.junit.Test;

import core.*;
import user_interface.CalendarPanel;

public class CalendarPanelTest
{

	@Test
	public void testCalendarPanel()
	{
		try
		{
			JFrame newFrame = new JFrame("Calendar Test");
			JPanel container = new JPanel();

			FlowLayout panelLayout = new FlowLayout();
			panelLayout.setHgap(0);
			panelLayout.setVgap(0);
			container.setLayout(panelLayout);

			JScrollPane s = new JScrollPane(container);

			int width = 150;
			int height = 1000;

			for (int i = 0; i < 7; i++)
			{
				CalendarPanel p;
				if (i == 0)
				{
					p = new CalendarPanel(new Date(), true, true, width, height);
				}
				else if (i == 6)
				{
					p = new CalendarPanel(new Date(), false, false, width,
							height);
				}
				else
				{
					p = new CalendarPanel(new Date(), false, true, width,
							height);
				}

				container.add(p);

			}

			// container.setSize(400,400);

			newFrame.getContentPane().add(s);

			newFrame.pack();

			newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			newFrame.setSize(400, 400);
			newFrame.setAutoRequestFocus(true);
			newFrame.setResizable(true);

			newFrame.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to " + e);
		}

	}

	@Test
	public void testCalendarPanelWithEvents()
	{
		Timeflecks.getSharedApplication().getTaskList()
				.setEvents(new ArrayList<Event>());

		Date d = new Date();
		Date f = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(f);
		c.set(Calendar.HOUR_OF_DAY, 6);
		c.set(Calendar.MINUTE, 0);
		d = c.getTime();
		c.add(Calendar.HOUR, 2);
		f = c.getTime();

		Event testEvent = new Event("Test", d, f);

		Timeflecks.getSharedApplication().getTaskList().addEvent(testEvent);

		try
		{
			Timeflecks.getSharedApplication().getTaskList()
					.saveAllTasksAndEvents();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			fail("Unexpected SQLException.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail("Unexpected IOException.");
		}

		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			fail("Unexpected InterruptedException.");
		}

		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.EVERYTHING_NEEDS_REFRESH);
	}

	@Test
	public void testCalendarPanelLarge()
	{
		try
		{
			JFrame newFrame = new JFrame("Calendar Test");

			CalendarPanel p = new CalendarPanel(new Date(), true, true, 400,
					400);

			newFrame.getContentPane().add(p);

			newFrame.pack();

			newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			newFrame.setSize(400, 400);
			newFrame.setAutoRequestFocus(true);
			newFrame.setResizable(true);

			newFrame.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to " + e);
		}
	}

}