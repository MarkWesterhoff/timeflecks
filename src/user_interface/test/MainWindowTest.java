package user_interface.test;

import org.junit.Test;

import user_interface.MainWindow;

public class MainWindowTest
{
	
	@Test
	public void showMainWindowTest()
	{
		MainWindow window = new MainWindow();
		
		window.setVisible(true);
	}
}