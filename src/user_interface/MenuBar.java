package user_interface;

import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.GlobalLogger;

import javax.swing.*;

import core.TaskList;

public class MenuBar extends JMenuBar implements ActionListener
{

	private JMenu filemenu, editmenu, helpmenu;
	private JMenuItem menuItem;

	private Logger logger;

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new MenuBar with the items for the Timeflecks application
	 */
	public MenuBar()
	{
		super();
		logger = GlobalLogger.getLogger();

		createAndAddFileMenu();

		// TODO Add back in the edit menu if we want one
		// createAndAddEditMenu();

		createAndAddHelpMenu();

	}

	/**
	 * Creates the file menu for the menu bar
	 */
	private void createAndAddFileMenu()
	{
		logger.logp(Level.INFO, "MenuBar", "createAndAddFileMenu",
				"Creating file menu");

		// Create the file menu
		filemenu = new JMenu("File");
		filemenu.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_F));

		getAccessibleContext().setAccessibleDescription("File menu");

		add(filemenu);

		// Open menu item
		menuItem = new JMenuItem("Open", KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Open file");
		menuItem.addActionListener(this);

		filemenu.add(menuItem);

		logger.logp(Level.INFO, "MenuBar", "createAndAddFileMenu",
				"Added open menu");

		// Add a separator, like many applications have
		filemenu.addSeparator();

		// Save menu item

		menuItem = new JMenuItem("Save", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save file");
		menuItem.addActionListener(this);

		filemenu.add(menuItem);

		logger.logp(Level.INFO, "MenuBar", "createAndAddFileMenu",
				"Added save menu");

		// Save as... menu item

		menuItem = new JMenuItem("Save As...");
		menuItem.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_A));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
		menuItem.addActionListener(this);

		filemenu.add(menuItem);

		logger.logp(Level.INFO, "MenuBar", "createAndAddFileMenu",
				"Added save as... menu");

		logger.logp(Level.INFO, "MenuBar", "createAndAddFileMenu",
				"File menu generation complete");
	}

	/**
	 * Creates the edit menu for the menu bar
	 */
	private void createAndAddEditMenu()
	{
		logger.logp(Level.INFO, "MenuBar", "createAndAddEditMenu",
				"Creating edit menu");

		// Create the edit menu
		editmenu = new JMenu("Edit");
		editmenu.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_E));

		getAccessibleContext().setAccessibleDescription("Edit menu");

		add(editmenu);

		// Open menu item
		menuItem = new JMenuItem("Edit Task...", KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Edit task properties");
		menuItem.addActionListener(this);

		editmenu.add(menuItem);

		logger.logp(Level.INFO, "MenuBar", "createAndAddEditMenu",
				"Added edit menu");

	}

	/**
	 * Creates the help menu for the menu bar
	 */
	private void createAndAddHelpMenu()
	{
		logger.logp(Level.INFO, "MenuBar", "createAndAddHelpMenu",
				"Creating help menu");

		// Create the file menu
		helpmenu = new JMenu("Help");
		helpmenu.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_H));

		getAccessibleContext().setAccessibleDescription("Help menu");

		add(helpmenu);

		// Open menu item
		menuItem = new JMenuItem("No Help For You!", KeyEvent.VK_H);

		// No keyboard shortcut for this
		// menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_,
		// InputEvent.CTRL_DOWN_MASK));

		menuItem.getAccessibleContext().setAccessibleDescription(
				"No help available");
		menuItem.addActionListener(this);

		helpmenu.add(menuItem);

		logger.logp(Level.INFO, "MenuBar", "createAndAddHelpMenu",
				"Added help menu");

	}

	public void performOpenCommand()
	{
		// Show jFileChooser
		
		// Make sure that it is a .db file
		
		// Make sure it is a different .db file
		
		// Save all tasks and events
		
		// new Application
		// Ask SQLConnector to load it in, taskList, update GUI
		// Closes current window
	}
	
	public void performSaveCommand()
	{
		TaskList list = TaskList.getInstance();
		
		try
		{
			list.saveAllTasksAndEvents();
		}
		catch (SQLException e)
		{
			logger.logp(Level.WARNING, "MenuBar", "performSaveCommand",
					"Save command generated SQLException. Showing dialog.");
			
			JOptionPane.showMessageDialog(this,
					"Database Error. (1300)\nYour tasks were not saved. Please try again, or check your databse file.",
					"Database Error", JOptionPane.ERROR_MESSAGE);
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "MenuBar", "performSaveCommand",
					"Save command generated IOException. Showing dialog.");
			
			// Trouble serializing objects
			JOptionPane.showMessageDialog(this,
					"Object Serialization Error. (1301)\nYour tasks were not saved. Please try again, or check your databse file.",
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void performSaveAsCommand()
	{
		// TODO : Save as must only allow .db files
	}
	
	public void performEditCommand()
	{
		// TODO This should edit the currently selected task? 
	}
	
	public void performNoHelpCommand()
	{
		
	}

	
	/**
	 * actionPerformed interprets the event from any menu item being pressed and performs the appropriate action
	 */
	public void actionPerformed(ActionEvent e)
	{
		logger.logp(Level.INFO, "MenuBar", "actionPerformed", "actionPerformed"
				+ "\nCommand: " + e.getActionCommand());

		String cmd = e.getActionCommand();

		if (cmd.equalsIgnoreCase("Open"))
		{
			logger.logp(Level.INFO, "MenuBar", "actionPerformed",
					"Open menu item selected");
			performOpenCommand();
		}
		else if (cmd.equalsIgnoreCase("Save"))
		{
			logger.logp(Level.INFO, "MenuBar", "actionPerformed",
					"Save menu item selected");
			performSaveCommand();
		}
		else if (cmd.equalsIgnoreCase("Save As..."))
		{
			logger.logp(Level.INFO, "MenuBar", "actionPerformed",
					"Save as... menu item selected");
			performSaveAsCommand();
		}
		else if (cmd.equalsIgnoreCase("Edit Task..."))
		{
			logger.logp(Level.INFO, "MenuBar", "actionPerformed",
					"Edit task... menu item selected");
			performEditCommand();
		}
		else if (cmd.equalsIgnoreCase("No Help For You!"))
		{
			logger.logp(Level.INFO, "MenuBar", "actionPerformed",
					"No Help For You! menu item selected");
			performNoHelpCommand();
		}
	}
}