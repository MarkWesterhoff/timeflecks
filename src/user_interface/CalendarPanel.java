package user_interface;

import java.awt.*;

import javax.swing.*;

import core.Task;

public class CalendarPanel extends JPanel
{
	private boolean drawTimes;
	private boolean drawRightSideLine;
	private int height;
	private int width;
	
	/**
	 * Auto generated default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	public CalendarPanel(boolean drawTimes, boolean drawRightSideLine, int width, int height)
	{
		super();
		
		this.drawTimes = drawTimes;
		this.drawRightSideLine = drawRightSideLine;
		this.height = height;
		this.width = width;
		
		setBorder(BorderFactory.createEmptyBorder());
		// setBorder(BorderFactory.createLineBorder(Color.red));
		
		this.setPreferredSize(new Dimension(width, height));
		
		// TODO keep this from breaking if you make it too small (down in the math)
		this.setMinimumSize(new Dimension(width, height));
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// Allows us to draw before we actually have a size, but we should always have a size anyways
		Dimension d = this.getSize();
//		if(d.width == 0 || d.height == 0)
//		{
//			d.width = this.width;
//			d.height = this.height;
//		}
		
		// Draw the times here
		if (drawTimes)
		{
			// We are going to draw from time = 12am - 11pm
			// 23 because we are adding 23 hours to this
			// 1 2 3 4 5 6 7 8 9 10 11 12 1  2  3  4  5  6  7  8  9  10 11
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
		final int insetFromLeft = 0; // See manual 10 later
		final int insetFromRight = 0;
		// ==================================================================
		
		
		int leftInset = insetFromLeft;
		int rightInset = insetFromRight;
		
		if (drawTimes)
		{
			// Our longest string is any two digit pm/am string, here we will use 12pm
			leftInset = g.getFontMetrics(g.getFont()).stringWidth("12pm") + insetFromLeft + 10;
		}
		
		if (drawRightSideLine)
		{
			rightInset = 8 + insetFromRight;
		}
		
		for (int insetFromTop = d.height / 24; insetFromTop < d.height; insetFromTop += d.height / 24)
		{
			g.drawLine(leftInset, insetFromTop, d.width - rightInset, insetFromTop);
		}
		
		// Now we have the lines and we have the times
		
		// We also have the option to display a line on the right hand side
		if (drawRightSideLine)
		{
			g.drawLine(d.width - 4, d.height / 24 / 2, d.width - 4, d.height - d.height / 24 / 2);
		}
		
		// Go through and draw any tasks at the appropriate place
	}
	
	public static void main(String[] args)
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
			
			for (int i = 0; i < 7; i ++)
			{
				CalendarPanel p; 
				if (i == 0)
				{
					p = new CalendarPanel(true, true, width, height);
				}
				else if (i == 6)
				{
					p = new CalendarPanel(false, false, width, height);
				}
				else
				{
					p = new CalendarPanel(false, true, width, height);
				}
				
				container.add(p);
				
			}
			
		//	container.setSize(400,400);
			
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

}