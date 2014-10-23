package user_interface.test;

import java.awt.*;
import javax.swing.*;
import javax.swing.UIManager.*;

import org.junit.Test;

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

				try
				{
					for (LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels())
					{
						if ("Nimbus".equals(info.getName()))
						{
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				}
				catch (Exception e)
				{
					// If Nimbus is not available, you can set the GUI to
					// another look and feel.

					// TODO: Remove this
					System.err
							.println("ERROR: Nimbus not found. Using default look and feel");
				}

				try
				{
					JFrame newFrame = new JFrame("Timeflecks - Add New Task");
					Container c = newFrame.getContentPane();

					NewTaskPanel p = new NewTaskPanel();

					c.add(p, BorderLayout.CENTER);

					newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					newFrame.pack();

					newFrame.setSize(374, 400);
					newFrame.setAutoRequestFocus(true);
					newFrame.setResizable(false);

					newFrame.setVisible(true);

				}
				catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Unable to " + e);
				}
			}
		});
	}
}
