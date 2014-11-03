package user_interface.test;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.*;

import org.junit.Test;

import user_interface.TaskComponent;
import core.Task;

public class TaskComponentTest
{

	@Test
	public void testTaskComponent()
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