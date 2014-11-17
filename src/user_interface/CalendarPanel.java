package user_interface;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.*;

import core.*;
import logging.GlobalLogger;

public class CalendarPanel extends JPanel
{
	private boolean drawTimes;
	private boolean drawRightSideLine;

	private Date date;
	private ArrayList<Scheduleable> itemsToPaint;

	/**
	 * Auto generated default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a CalendarPanel, which is ready to draw on a given graphics
	 * context. This is a single day view.
	 * 
	 * @param date
	 *            The date that this CalendarPanel is to represent.
	 * @param drawTimes
	 *            Whether or not to draw the times on the left of the panel.
	 * @param drawRightSideLine
	 *            Whether or not to draw the line on the right hand side.
	 * @param width
	 *            The preferred width for this component
	 * @param height
	 *            The preferred height for this component
	 */
	public CalendarPanel(Date date, boolean drawTimes,
			boolean drawRightSideLine, int width, int height)
	{
		super();

		this.drawTimes = drawTimes;
		this.drawRightSideLine = drawRightSideLine;

		this.setDate(date);
		setItemsToPaint(new ArrayList<Scheduleable>());
		refresh();

		setBorder(BorderFactory.createEmptyBorder());
		// setBorder(BorderFactory.createLineBorder(Color.red));

		this.setPreferredSize(new Dimension(width, height));

		// TODO keep this from breaking if you make it too small (down in the
		// math)
		this.setMinimumSize(new Dimension(width, height));
	}

	/**
	 * Paint the component on the specified graphics context.
	 * 
	 * @param g
	 *            The graphics context on which to draw the CalendarPanel
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Dimension d = this.getSize();

		// Draw the times here
		if (drawTimes)
		{
			// We are going to draw from time = 12am - 11pm
			// 23 because we are adding 23 hours to this
			// 1 2 3 4 5 6 7 8 9 10 11 12 1 2 3 4 5 6 7 8 9 10 11
			// 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23

			int hr = 12;
			String ampm = "am";
			for (int insetFromTop = d.height / 24; insetFromTop < d.height; insetFromTop += d.height / 24)
			{
				// Draw a time at this height in from the top
				g.drawString("" + hr + ampm, 4, insetFromTop);

				if (hr == 12 && ampm.equals("am"))
				{
					hr = 1;
					ampm = "am";
				}
				else if (hr == 11 && ampm.equals("am"))
				{
					hr = 12;
					ampm = "pm";
				}
				else if (hr == 12 && ampm.equals("pm"))
				{
					hr = 1;
					ampm = "pm";
				}
				else if (hr == 11 && ampm.equals("pm"))
				{
					// We want to explicitly be done here
					break;
				}
				else
				{
					hr += 1;
				}
			}
		}

		// Draw the horizontal lines

		// ==================================================================
		// Change these to change how far in from the edges the lines are
		int insetFromLeft = 0; // See manual 10 later
		int insetFromRight = 8;
		// ==================================================================

		int leftInset = insetFromLeft;
		int rightInset = insetFromRight;

		if (drawTimes)
		{
			insetFromLeft += 8;
			// Our longest string is any two digit pm/am string, here we will
			// use 12pm
			leftInset = g.getFontMetrics(g.getFont()).stringWidth("12pm")
					+ insetFromLeft;
		}

		// Get the height of the font to draw the lines
		Graphics2D g2 = (Graphics2D) g;
		FontRenderContext frc = g2.getFontRenderContext();
		int fontHeight = (int) g2.getFont().getLineMetrics("12pm", frc)
				.getHeight();

		if (drawRightSideLine)
		{
			rightInset = 0 + insetFromRight;
		}

		for (int insetFromTop = d.height / 24 - (fontHeight / 2); insetFromTop < d.height
				- (fontHeight / 2); insetFromTop += d.height / 24)
		{
			g.drawLine(leftInset, insetFromTop, d.width - rightInset,
					insetFromTop);
		}

		// Now we have the lines and we have the times

		// --------------------------------------------------------------------------------
		// Draw the date at the top

		String dayOfWeek = new SimpleDateFormat("EEEE").format(this.getDate());
		String date = new SimpleDateFormat("MM/dd/yyyy").format(this.getDate());

		g.drawString(dayOfWeek, 2, fontHeight);
		g.drawString(date, 2, 2 * fontHeight);

		// ---------------------------------------------------------------------------------

		// We also have the option to display a line on the right hand side
		if (drawRightSideLine)
		{
			g.drawLine(d.width - 4, d.height / 24 / 2, d.width - 4, d.height
					- d.height / 24 / 2);
		}

		// Go through and draw any tasks at the appropriate place
		for (Scheduleable t : itemsToPaint)
		{
			int firstInset = d.height / 24 - (fontHeight / 2);

			int hourIncrement = d.height / 24;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(t.getStartTime());
			double taskHours = (double) calendar.get(Calendar.HOUR_OF_DAY)
					+ (double) calendar.get(Calendar.MINUTE) / 60.0;

			double durationInHours = (t.getDuration() / 1000.0 / 60.0 / 60.0) % 24.0;

			Rectangle frame = new Rectangle(leftInset, firstInset
					+ (int) (taskHours * hourIncrement), d.width - rightInset
					- leftInset, (int) (durationInHours * hourIncrement));

			TaskComponent tc = new TaskComponent(t, frame);
			tc.paint(g);
		}
	}

	/**
	 * Refreshes the calendar view, re-adds the tasks for that day from the
	 * TaskList. Warning: This may be time-intensive for large TaskLists
	 */
	public void refresh()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "CalendarPanel", "refresh",
				"Refresh called.");

		// We need to go through the task list and update all items that match
		// the current date

		// Clear it out first
		itemsToPaint.clear();

		for (Scheduleable t : Timeflecks.getSharedApplication().getTaskList()
				.getTasks())
		{
			if (t.isScheduled())
			{
				// Same day will just return false if they are null
				if (sameDay(t.getStartTime(), this.getDate()))
				{
					// They are on the same day
					itemsToPaint.add(t);
				}
			}
		}

		for (Scheduleable t : Timeflecks.getSharedApplication().getTaskList()
				.getEvents())
		{
			if (t.isScheduled())
			{
				// Same day will just return false if they are null
				if (sameDay(t.getStartTime(), this.getDate()))
				{
					// They are on the same day
					itemsToPaint.add(t);
					System.out.println("ADDED EVENT TO PAINT");
				}
			}
		}

		// Now we have the list of items to paint, we want to make sure that
		// they are painted in paintComponent.

		this.repaint();
	}

	/**
	 * Utility function to determine if two dates fall upon the same day.
	 * 
	 * @param firstDate
	 *            The first date to compare
	 * @param secondDate
	 *            The second date to compare
	 * @return true if the two dates are on the same day, false otherwise.
	 */
	public boolean sameDay(Date firstDate, Date secondDate)
	{
		if (firstDate == null || secondDate == null)
		{
			return false;
		}

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(firstDate);
		c2.setTime(secondDate);

		boolean same = c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);

		return same;
	}

	/**
	 * Getter for the itemsToPaint list.
	 * 
	 * @return the itemsToPaint list
	 */
	public ArrayList<Scheduleable> getItemsToPaint()
	{
		return itemsToPaint;
	}

	/**
	 * Setter for the itemsToPaint list.
	 * 
	 * @param itemsToPaint
	 *            The list of tasks to paint that should be set as itemsToPaint.
	 */
	public void setItemsToPaint(ArrayList<Scheduleable> itemsToPaint)
	{
		this.itemsToPaint = itemsToPaint;
	}

	/**
	 * Getter for the date for the CalendarPanel.
	 * 
	 * @return The date for the CalendarPanel
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * Setter for the date for the CalendarPanel.
	 * 
	 * @param date
	 *            The date to be set in the CalendarPanel
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

}