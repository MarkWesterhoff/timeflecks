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

		JFrame newFrame = new JFrame("Menu Bar Test");

		newFrame.setJMenuBar(menu);

		newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		newFrame.setSize(400, 400);
		newFrame.setAutoRequestFocus(true);
		newFrame.setResizable(true);

		newFrame.setVisible(true);
		
		// Clean up
		newFrame.dispose();
	}
}