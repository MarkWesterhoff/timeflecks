package user_interface.test;

import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.*;

import org.junit.Test;

import user_interface.CalendarPanel;

public class CalendarPanelTest
{

	/**
	 * Auto generated default serial version UID
	 */
	private static final long serialVersionUID = 1L;

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
					p = new CalendarPanel(new Date(), false, false, width, height);
				}
				else
				{
					p = new CalendarPanel(new Date(), false, true, width, height);
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
	
	public static void main(String[] args)
	{
		try
		{
			JFrame newFrame = new JFrame("Calendar Test");

			CalendarPanel p = new CalendarPanel(new Date(), true, true, 400, 400);
			

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