package user_interface;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.*;

import core.*;
import dnd.CalendarTransferHandler;
import logging.GlobalLogger;

public class CalendarPanel extends JPanel implements MouseMotionListener,
		MouseListener
{
	private boolean drawTimes;
	private boolean drawRightSideLine;

	private Date date;
	private ArrayList<Scheduleable> itemsToPaint;

	int topInset;

	CalendarTransferHandler transferHandler;

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
		topInset = 0;

		this.setDate(date);
		setItemsToPaint(new ArrayList<Scheduleable>());
		refresh();

		setBorder(BorderFactory.createEmptyBorder());
		// setBorder(BorderFactory.createLineBorder(Color.red));

		this.setPreferredSize(new Dimension(width, height));

		// TODO keep this from breaking if you make it too small (down in the
		// math)
		this.setMinimumSize(new Dimension(width, height));

		transferHandler = new CalendarTransferHandler();

		this.setTransferHandler(transferHandler);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

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

		this.topInset = d.height / 24 - (fontHeight / 2);

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

		FontMetrics fm = g.getFontMetrics();
		int dayWidth = fm.stringWidth(dayOfWeek);
		int dateWidth = fm.stringWidth(date);

		g.drawString(dayOfWeek, (int) (d.getWidth() / 2.0 - (dayWidth / 2.0)),
				fontHeight);
		g.drawString(date, (int) (d.getWidth() / 2.0 - (dateWidth / 2.0)),
				2 * fontHeight);

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

			int hourIncrement = d.height / 24;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(t.getStartTime());
			double taskHours = (double) calendar.get(Calendar.HOUR_OF_DAY)
					+ (double) calendar.get(Calendar.MINUTE) / 60.0;

			double durationInHours = (t.getDuration() / 1000.0 / 60.0 / 60.0) % 24.0;

			Rectangle frame = new Rectangle(leftInset, this.topInset
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

		for (Scheduleable t : Timeflecks.getSharedApplication()
				.getFilteringManager().getFilteredTaskList())
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

	public Date getDateForPoint(Point p)
	{
		if (!this.contains(p))
		{
			return null;
		}

		if (p.getY() < topInset)
		{
			return null;
		}

		Dimension d = this.getSize();

		double hourIncrement = d.height / 24;

		double hourValue = (p.getY() - topInset) / hourIncrement;// - 0.84;

		// Make returnDate with the proper day, now we need to set the hours,
		// mins, and secs
		Date returnDate = new Date(this.date.getTime());

		Calendar c = Calendar.getInstance();
		c.setTime(returnDate);
		c.set(Calendar.HOUR_OF_DAY, (int) hourValue);

		double preciseMinutes = ((hourValue - (int) hourValue) * 60.0);
		int minutes = (int) ((hourValue - (int) hourValue) * 60.0);

		c.set(Calendar.MINUTE, minutes);
		c.set(Calendar.SECOND, (int) ((preciseMinutes - minutes) * 60.0));

		returnDate = c.getTime();

		// System.out.println("topInset: " + topInset + "\nhourIncrement: " +
		// hourIncrement + "\nhourValue: " + hourValue + "\nreturnDate: " +
		// returnDate + "\npreciseMinutes: " + preciseMinutes + "\nminutes: " +
		// minutes + "\n");

		return returnDate;
	}

	private MouseEvent firstMouseEvent = null;

	@Override
	public void mousePressed(MouseEvent e)
	{
		firstMouseEvent = e;
		e.consume();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (firstMouseEvent != null)
		{
			e.consume();

			int action = TransferHandler.MOVE;

			int dx = Math.abs(e.getX() - firstMouseEvent.getX());
			int dy = Math.abs(e.getY() - firstMouseEvent.getY());
			// Arbitrarily define a 5-pixel shift as the
			// official beginning of a drag.
			if (dx > 5 || dy > 5)
			{
				// This is a drag, not a click.
				JComponent c = (JComponent) e.getSource();
				// Tell the transfer handler to initiate the drag.
				TransferHandler handler = c.getTransferHandler();
				handler.exportAsDrag(c, firstMouseEvent, action);
				firstMouseEvent = null;
			}
		}
	}

	private Point currentPoint;

	@Override
	public void mouseMoved(MouseEvent e)
	{
		currentPoint = e.getPoint();
		e.consume();
	}

	public Point getCurrentMousePoint()
	{
		return currentPoint;
	}

	public Task getTaskUnderMouse(Point p)
	{
		if ( p == null)
		{
			return null;
		}
		
		
		for (Scheduleable t : itemsToPaint)
		{
			if (t instanceof Task)
			{
				int insetFromLeft = 0; // See manual 10 later
				int insetFromRight = 8;

				Dimension d = this.getSize();

				int hourIncrement = d.height / 24;

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(t.getStartTime());
				double taskHours = (double) calendar.get(Calendar.HOUR_OF_DAY)
						+ (double) calendar.get(Calendar.MINUTE) / 60.0;

				double durationInHours = (t.getDuration() / 1000.0 / 60.0 / 60.0) % 24.0;

				Rectangle frame = new Rectangle(insetFromLeft, this.topInset
						+ (int) (taskHours * hourIncrement), d.width
						- insetFromRight - insetFromLeft,
						(int) (durationInHours * hourIncrement));

				if (frame.contains(p))
				{
					// We already checked instanceof, so this is ok
					return (Task) t;
				}
			}
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "CalendarPanel", "mouseClicked", "mouse clicked.");
		
		// TODO maybe use taskComponent all through all of this
		
		if (e.getClickCount() == 2 && this.getTaskUnderMouse(this.getCurrentMousePoint()) != null){
			Task toEdit = this.getTaskUnderMouse(this.getCurrentMousePoint());
			if (toEdit != null)
			{
				// Check again in case the mouse moved
				NewTaskPanel p = new NewTaskPanel(toEdit);
				p.displayFrame();
			}
		}
		e.consume();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}
}
