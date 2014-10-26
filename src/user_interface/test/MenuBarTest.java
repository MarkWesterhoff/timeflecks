package user_interface.test;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.junit.Test;

import user_interface.MenuBar;

public class MenuBarTest
{
	@Test
	public void showMenuBarTest()
	{
		MenuBar menu = new MenuBar();
				
		try
		{
			JFrame newFrame = new JFrame("Menu Bar Test");
			
			newFrame.setJMenuBar(menu);

			newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			newFrame.setSize(400, 400);
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
}