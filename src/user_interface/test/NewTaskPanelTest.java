package user_interface.test;

import java.awt.*;

import javax.swing.*;

import org.junit.Test;

import core.Task;
import user_interface.NewTaskPanel;

public class NewTaskPanelTest
{
	@Test
	public void newPanelTest()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				//TODO: remove (debug)
				System.out.println("HELLO! 5");
				JFrame newFrame = new JFrame("Timeflecks - Add New Task");
				Container c = newFrame.getContentPane();

				NewTaskPanel p = new NewTaskPanel();

				c.add(p, BorderLayout.CENTER);

				newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				newFrame.pack();

				// newFrame.setSize(374, 450);
				newFrame.setAutoRequestFocus(true);
				newFrame.setResizable(false);

				newFrame.setVisible(true);

				// Clean up
				newFrame.dispose();
				System.out.println("END 5");
			}
		});
	}
	
	@Test
	public void newPanelTest2()
	{
		Task task1 = new Task("task 1");
		NewTaskPanel p = new NewTaskPanel(task1);

		NewTaskPanel p1 = new NewTaskPanel();

		p.displayFrame();
		p1.displayFrame();
		
		// Clean up
		p.dispose();
		p1.dispose();
	}
}
