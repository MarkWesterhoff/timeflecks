package user_interface;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.*;

import logging.GlobalLogger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.Timeflecks;

public class MenuBar extends JMenuBar implements ActionListener
{

	private JMenu filemenu, editmenu, helpmenu;
	private JMenuItem menuItem;

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

		createAndAddFileMenu();

		// TODO Add back in the edit menu if we want one
		createAndAddEditMenu();

		// createAndAddHelpMenu();

	}

	/**
	 * Creates the file menu for the menu bar
	 */
	private void createAndAddFileMenu()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "Creating file menu");

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

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "Added open menu");

		// Add a separator, like many applications have
		filemenu.addSeparator();

		// Save menu item

		menuItem = new JMenuItem("Save", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save file");
		menuItem.addActionListener(this);

		filemenu.add(menuItem);

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "Added save menu");

		// Save as... menu item

		menuItem = new JMenuItem("Save As...");
		menuItem.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_A));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
		menuItem.addActionListener(this);

		filemenu.add(menuItem);

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "Added save as... menu");

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "File menu generation complete");
	}

	/**
	 * Creates the edit menu for the menu bar
	 */
	private void createAndAddEditMenu()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddEditMenu", "Creating edit menu");

		// Create the edit menu
		editmenu = new JMenu("Edit");
		editmenu.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_E));

		getAccessibleContext().setAccessibleDescription("Edit menu");

		add(editmenu);

		// Edit menu items
		menuItem = new JMenuItem("New Task...", KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Create new task");
		menuItem.addActionListener(this);

		editmenu.add(menuItem);

		menuItem = new JMenuItem("Edit Task...", KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Edit task properties");
		menuItem.addActionListener(this);

		editmenu.add(menuItem);

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddEditMenu", "Added edit menu");

	}

	/**
	 * Creates the help menu for the menu bar
	 */
	private void createAndAddHelpMenu()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddHelpMenu", "Creating help menu");

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

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddHelpMenu", "Added help menu");

	}

	public void performOpenCommand()
	{
		// Show jFileChooser

		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Timeflecks Database Files", "db");
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();

			try
			{
				Timeflecks.getSharedApplication().getTaskList()
						.saveAllTasksAndEvents();
			}
			catch (SQLException e)
			{
				GlobalLogger.getLogger().logp(Level.WARNING, "MenuBar",
						"performOpenCommand",
						"Open command generated SQLException. Showing dialog.");

				JOptionPane
						.showMessageDialog(
								this,
								"Database Error. (1300)\nYour tasks were not saved. Please try again, or check your database file.",
								"Database Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (IOException e)
			{
				GlobalLogger.getLogger().logp(Level.WARNING, "MenuBar",
						"performOpenCommand",
						"Open command generated IOException. Showing dialog.");

				// Trouble serializing objects
				JOptionPane
						.showMessageDialog(
								this,
								"Object Serialization Error. (1301)\nYour tasks were not saved. Please try again, or check your database file.",
								"Database Error", JOptionPane.ERROR_MESSAGE);
			}

			// Make sure it is a different .db file
			try
			{
				if (selectedFile.getCanonicalPath().equals(
						Timeflecks.getSharedApplication().getCurrentFile()
								.getCanonicalPath()))
				{
					// If they are the same file
					JOptionPane.showMessageDialog(this,
							"This is the currently open file.",
							"Currently Open File", JOptionPane.WARNING_MESSAGE);

					return; // Do nothing for this part
				}
			}
			catch (IOException e)
			{
				GlobalLogger
						.getLogger()
						.logp(Level.WARNING, "MenuBar", "performOpenCommand",
								"File system error. Cannot compare new file to current file.");

				// Trouble serializing objects
				JOptionPane
						.showMessageDialog(
								this,
								"File System Error. (1500)\nUnable to compare the new file to the current file. Please select a different file and try again.",
								"File System Error", JOptionPane.ERROR_MESSAGE);
			}

			try
			{
				Timeflecks.getSharedApplication()
						.openDatabaseFile(selectedFile);
			}
			catch (SQLException e)
			{
				GlobalLogger.getLogger().logp(Level.WARNING, "MenuBar",
						"performOpenCommand",
						"Open command generated SQLException. Showing dialog.");

				GlobalLogger.getLogger().logp(Level.WARNING, "MenuBar",
						"performOpenCommand",
						"Stack Trace:\n" + e.getStackTrace());

				JOptionPane
						.showMessageDialog(
								this,
								"Database Error. (1310)\nCould not open database file.",
								"Database Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (IOException e)
			{
				GlobalLogger.getLogger().logp(Level.WARNING, "MenuBar",
						"performOpenCommand",
						"Open command generated IOException. Showing dialog.");

				// Trouble serializing objects
				JOptionPane
						.showMessageDialog(
								this,
								"Object Serialization Error. (1311)\nCould not read items from database file.",
								"Database Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (ClassNotFoundException e)
			{
				GlobalLogger.getLogger().logp(Level.WARNING, "MenuBar",
						"performOpenCommand",
						"Open command generated IOException. Showing dialog.");

				// Trouble serializing objects
				JOptionPane
						.showMessageDialog(
								this,
								"Object Serialization Error. (1312)\nClass not found when reading from database.",
								"Database Error", JOptionPane.ERROR_MESSAGE);
			}

			// TODO Check that this makes a full repaint
			this.getTopLevelAncestor().invalidate();
		}
	}

	public void performSaveCommand()
	{
		try
		{
			Timeflecks.getSharedApplication().getTaskList()
					.saveAllTasksAndEvents();
		}
		catch (SQLException e)
		{
			GlobalLogger.getLogger().logp(Level.WARNING, "MenuBar",
					"performSaveCommand",
					"Save command generated SQLException. Showing dialog.");

			JOptionPane
					.showMessageDialog(
							this,
							"Database Error. (1300)\nYour tasks were not saved. Please try again, or check your database file.",
							"Database Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e)
		{
			GlobalLogger.getLogger().logp(Level.WARNING, "MenuBar",
					"performSaveCommand",
					"Save command generated IOException. Showing dialog.");

			// Trouble serializing objects
			JOptionPane
					.showMessageDialog(
							this,
							"Object Serialization Error. (1301)\nYour tasks were not saved. Please try again, or check your database file.",
							"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void performSaveAsCommand()
	{
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Timeflecks Database Files", "db");
		fileChooser.setFileFilter(filter);

		boolean success = false;

		do
		{
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
						"performSaveAsCommand",
						"User chose file " + fileChooser.getSelectedFile());

				File selectedFile = fileChooser.getSelectedFile();
				// TODO Make sure the user is warned if the file exists and is
				// not the same file
				if (fileExistsAndIsNotSame(selectedFile))
				{
					// We need to prompt the user to see if they want to
					// overwrite the file.
					Object[] options = { "Overwrite File", "Choose New File" };
					int reply = JOptionPane
							.showOptionDialog(
									this,
									"This file already exists. Would you like to overwrite it with the current data file?",
									"File Exists", JOptionPane.DEFAULT_OPTION,
									JOptionPane.WARNING_MESSAGE, null, options,
									options[1]);

					if (reply == JOptionPane.YES_OPTION)
					{
						// The user selected to overwrite the file
						GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
								"performSaveAsCommand",
								"User elected to overwrite existing file.");

						// We just keep going
					}
					else
					{
						// User selected to not overwrite file
						GlobalLogger
								.getLogger()
								.logp(Level.INFO, "MenuBar",
										"performSaveAsCommand",
										"User declined to overwrite file, prompting for new file choice.");

						success = false;
						continue;
					}

				}

				// Take the file that comes in and try, catch
				// IllegalArgumentException, show alert asking for new file -
				// they can go back and cancel
				// Call switch
				try
				{
					Timeflecks.getSharedApplication().saveDatabaseFileAs(
							selectedFile);

					success = true;
				}
				catch (IllegalArgumentException e)
				{
					GlobalLogger
							.getLogger()
							.logp(Level.WARNING,
									"MenuBar",
									"performSaveAsCommand",
									"Illegal argument from SQLiteConnector when attempting to switch database. Showing dialog, prompting for proper extension.");
					success = false;

					JOptionPane
							.showMessageDialog(
									this,
									"Invalid filename. (1200)\nPlease make sure that your file is valid and has the extension \".db\".",
									"Invalid Filename",
									JOptionPane.ERROR_MESSAGE);
				}
				catch (SQLException e)
				{
					GlobalLogger
							.getLogger()
							.logp(Level.WARNING, "MenuBar",
									"performSaveAsCommand",
									"Save As command generated SQLException. Showing dialog.");

					JOptionPane
							.showMessageDialog(
									this,
									"Database Error. (1300)\nYour tasks were not saved. Please try again, or check your database file.",
									"Database Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (IOException e)
				{
					GlobalLogger
							.getLogger()
							.logp(Level.WARNING, "MenuBar",
									"performSaveAsCommand",
									"Save As command generated IOException. Showing dialog.");

					// Trouble serializing objects
					JOptionPane
							.showMessageDialog(
									this,
									"Object Serialization Error. (1301)\nYour tasks were not saved. Please try again, or check your database file.",
									"Database Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
						"performSaveAsCommand",
						"User cancelled Save As operation, continuing.");
				success = true;
			}
		} while (!success);
	}

	public void performNewCommand()
	{
		// This should probably be a method for someone who manages this stuff
		// TODO Same with the file handling
		
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"performNewCommand()",
				"New task command issued. Bringing up NewTaskPanel.");
		
		NewTaskPanel p = new NewTaskPanel();
		p.displayFrame();
	}
	
	public void performEditCommand()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"performEditCommand()",
				"Edit task command issued. Bringing up NewTaskPanel.");
		
		JTable table = Timeflecks.getSharedApplication().getMainWindow().getTablePanel().getTable();
		
		int row = table.getSelectedRow();
		if (row >= 0 && row < table.getRowCount())
		{
			NewTaskPanel p = new NewTaskPanel(Timeflecks.getSharedApplication().getTaskList().getTasks()
					.get(row));
			p.displayFrame();
		}
		else
		{
			// This happens if there are no tasks selected
			
			// TODO Grey out the Edit Task Button if there are no tasks selected
			
			GlobalLogger.getLogger().logp(Level.WARNING, "MenuBar",
					"performEditCommand()",
					"Selected row is out of bounds for the current table.");
		}
	}

	public void performNoHelpCommand()
	{
		JOptionPane.showMessageDialog(this,
				"Help is not available in this version of the application.",
				"No Help Available", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Checks against the existence of the file (same file is ok).
	 * 
	 * @param newFile
	 *            The file to check
	 * @return True if there is a problem. False if there is no problem
	 */
	public boolean fileExistsAndIsNotSame(File newFile)
	{
		if (newFile.exists())
		{
			// The file exists
			if (Timeflecks.getSharedApplication().getCurrentFile()
					.equals(newFile))
			{
				return false;
			}
			else
			{
				// If it exists and is not the same, then we have an error, and
				// need
				// to prompt the user.
				return true;
			}

		}
		else
		{
			return false;
		}
	}

	/**
	 * actionPerformed interprets the event from any menu item being pressed and
	 * performs the appropriate action
	 */
	public void actionPerformed(ActionEvent e)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar", "actionPerformed",
				"actionPerformed" + "\nCommand: " + e.getActionCommand());

		String cmd = e.getActionCommand();

		if (cmd.equalsIgnoreCase("Open"))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "Open menu item selected");
			performOpenCommand();
		}
		else if (cmd.equalsIgnoreCase("Save"))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "Save menu item selected");
			performSaveCommand();
		}
		else if (cmd.equalsIgnoreCase("Save As..."))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "Save as... menu item selected");
			performSaveAsCommand();
		}
		else if (cmd.equalsIgnoreCase("New Task..."))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "New task... menu item selected");
			performNewCommand();
		}
		else if (cmd.equalsIgnoreCase("Edit Task..."))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "Edit task... menu item selected");
			performEditCommand();
		}
		else if (cmd.equalsIgnoreCase("No Help For You!"))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "No Help For You! menu item selected");
			performNoHelpCommand();
		}
	}
}