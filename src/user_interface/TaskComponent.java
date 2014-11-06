package user_interface;

import java.awt.*;
import java.awt.font.FontRenderContext;

import javax.swing.*;

import utility.GUIUtility;
import core.Task;

public class TaskComponent extends JComponent
{

	/**
	 * Auto generated default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private Task task;
	private Rectangle frame;

	/**
	 * Create a new task component with a current task that is being drawn and
	 * the frame that it will be painted with.
	 * 
	 * @param taskToDraw
	 *            The task that will be represented by this TaskComponent
	 * @param newBounds
	 *            The frame for this TaskComponent
	 */
	public TaskComponent(Task taskToDraw, Rectangle newBounds)
	{
		super();

		task = taskToDraw;
		frame = newBounds;

		setBorder(BorderFactory.createEmptyBorder());
		// setBorder(BorderFactory.createLineBorder(Color.black));

		setPreferredSize(new Dimension(newBounds.width, newBounds.height));
	}

	/**
	 * Paint the TaskComponent in the specified graphics G
	 * 
	 * @param g
	 *            The graphics in which to paint the TaskComponent
	 */
	public void paint(Graphics g)
	{
		super.paintComponent(g);

		// Draw the rectangle first, so the string shows up on top of it
		if (task.isCompleted())
		{
			g.setColor(Color.getHSBColor(0f, 0f, .94f));
		}
		else
		{
			g.setColor(Color.white);
		}
		g.fillRect(frame.x, frame.y, frame.width, frame.height);
		g.setColor(Color.black);
		g.drawRect(frame.x, frame.y, frame.width, frame.height);

		// NOTE That if it doesn't show up, make sure the duration isn't
		// zero

		Graphics2D g2 = (Graphics2D) g;

		// Draw the string title of the task
		FontRenderContext frc2 = g2.getFontRenderContext();
		int fontHeight2 = (int) g2.getFont()
				.getLineMetrics(task.getName(), frc2).getHeight();

		final int textLeftInset = 2;
		final int textTopInset = 2 + fontHeight2;

		// TODO This should be changed to draw components within the bounds
		// of the component and that's it and not require knowledge of its
		// frame, and then it will be given a place to draw by the calendar.
		GUIUtility.drawString(g, task.getName(), frame.x + getInsets().left
				+ textLeftInset, frame.y + getInsets().top + textTopInset,
				frame.width);
	}
}
