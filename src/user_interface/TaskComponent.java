package user_interface;

import java.awt.*;
import java.awt.font.FontRenderContext;

import javax.swing.*;

import core.Task;

public class TaskComponent extends JComponent
{

	/**
	 * Auto generated default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private Task task;
	private Rectangle frame;

	public TaskComponent(Task taskToDraw, Rectangle newFrame)
	{
		super();

		task = taskToDraw;
		frame = newFrame;

		setBorder(BorderFactory.createLineBorder(Color.black));

		this.setPreferredSize(new Dimension(frame.width, frame.height));

		// TODO keep this from breaking if you make it too small (down in the
		// math)
		this.setMinimumSize(new Dimension(frame.width, frame.height));
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// The frame will get updated each time when we do this, so we can just
		// go off of that.

		// Draw the rectangle first, so the string shows up on top of it
		g2.setPaint(Color.white);
		g2.fillRect(frame.x, frame.y, frame.width, frame.height);
		g2.setPaint(Color.black);
		g2.drawRect(frame.x, frame.y, frame.width, frame.height);

		// Draw the string title of the task
		FontRenderContext frc = g2.getFontRenderContext();
		int fontHeight = (int) g2.getFont().getLineMetrics(task.getName(), frc)
				.getHeight();

		// TODO implement wrapping of names

		final int leftInset = 2;
		final int topInset = 2 + fontHeight;

		// TODO This should be changed to draw components within the bounds of
		// the component and that's it and not require knowledge of its frame,
		// and then it will be given a place to draw by the calendar.

		// Note that it is our job not to draw outside of our insets...
		g2.drawString(task.getName(), frame.x + getInsets().left + leftInset,
				frame.y + getInsets().top + topInset);
	}

	public void setFrame(Rectangle newFrame)
	{
		frame = newFrame;
	}

	public static void main(String[] args)
	{
		try
		{
			Task t = new Task("New Task Blah Blah Blah");
			TaskComponent p = new TaskComponent(t, new Rectangle(10, 10, 200,
					100));
			JPanel panel = new JPanel();
			panel.add(p);
			panel.setPreferredSize(new Dimension(400, 400));
			JFrame newFrame = new JFrame("Task Component Test");

			newFrame.getContentPane().add(p);

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