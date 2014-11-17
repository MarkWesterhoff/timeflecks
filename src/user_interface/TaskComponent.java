package user_interface;

import java.awt.*;
import java.awt.font.FontRenderContext;

import javax.swing.*;

import utility.GUIUtility;
import core.*;
import core.Event;

public class TaskComponent extends JComponent
{

	/**
	 * Auto generated default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private Scheduleable item;
	private Rectangle frame;

	/**
	 * Create a new task component with a current item that is being drawn and
	 * the frame that it will be painted with.
	 * 
	 * @param itemToDraw
	 *            The item that will be represented by this TaskComponent
	 * @param newBounds
	 *            The frame for this TaskComponent
	 */
	public TaskComponent(Scheduleable itemToDraw, Rectangle newBounds)
	{
		super();

		item = itemToDraw;
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
//		if (item.isCompleted())
//		{
//			g.setColor(Color.getHSBColor(0f, 0f, .94f));
//		}
//		else
//		{
//			g.setColor(Color.white);
//		}
		
		if (item instanceof Event)
		{
			g.setColor(Color.getHSBColor(36f / 360f, .42f, 1f));
		}
		if (item instanceof Task)
		{
			if (item.isCompleted())
			{
				g.setColor(Color.getHSBColor(206f / 360f, .10f, .94f));
			}
			else
			{
				g.setColor(Color.getHSBColor(206f / 360f, .42f, 1f));
			}
		}

		// Draw the colored fill, then a black frame on the component
		g.fillRect(frame.x, frame.y, frame.width, frame.height);
		g.setColor(Color.black);
		g.drawRect(frame.x, frame.y, frame.width, frame.height);

		// NOTE That if it doesn't show up, make sure the duration isn't
		// zero

		Graphics2D g2 = (Graphics2D) g;

		// Draw the string title of the item
		FontRenderContext frc2 = g2.getFontRenderContext();
		int fontHeight2 = (int) g2.getFont()
				.getLineMetrics(item.getName(), frc2).getHeight();

		final int textLeftInset = 2;
		final int textTopInset = 2 + fontHeight2;

		// TODO This should be changed to draw components within the bounds
		// of the component and that's it and not require knowledge of its
		// frame, and then it will be given a place to draw by the calendar.
		GUIUtility.drawString(g, item.getName(), frame.x + getInsets().left
				+ textLeftInset, frame.y + getInsets().top + textTopInset,
				frame.width);
	}
}
