package user_interface;

import java.awt.event.*;
import java.io.*;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import logging.GlobalLogger;
import utility.FileUtility;
import core.*;

/**
 * Controller to handle events from the menu bar.
 * 
 */
public class MenuBarController implements ActionListener
{
	private MenuBar menu;

	public MenuBarController(MenuBar menu)
	{
		this.menu = menu;
	}

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
		else if (cmd.equalsIgnoreCase("Save Database File"))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "Save Database File menu item selected");
			performSaveCommand();
		}
		else if (cmd.equalsIgnoreCase("Change Save Location..."))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed",
					"Change Save Location... menu item selected");
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
		else if (cmd.equalsIgnoreCase("Delete Task..."))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "Delete task... menu item selected");
			performDeleteTaskCommand();
		}
		else if (cmd.equalsIgnoreCase("New Event..."))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "New event... menu item selected");
			performNewEventCommand();
		}
		else if (cmd.equalsIgnoreCase("No Help For You!"))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
					"actionPerformed", "No Help For You! menu item selected");
			performNoHelpCommand();
		}
	}

	public void performOpenCommand()
	{
		// Show jFileChooser

		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Timeflecks Database Files", "db");
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(menu);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();

			try
			{
				Timeflecks.getSharedApplication().getTaskList()
						.saveAllTasksAndEvents();
			}
			catch (Exception e)
			{
				ExceptionHandler.handleDatabaseSaveException(e, this.getClass()
						.getName(), "performOpenCommand", "1300");
			}

			// Make sure it is a different .db file
			try
			{
				if (selectedFile.getCanonicalPath().equals(
						Timeflecks.getSharedApplication().getCurrentFile()
								.getCanonicalPath()))
				{
					// If they are the same file
					JOptionPane.showMessageDialog(menu,
							"This is the currently open file.",
							"Currently Open File", JOptionPane.WARNING_MESSAGE);

					return; // Do nothing for this part
				}
			}
			catch (IOException e)
			{
				ExceptionHandler.handleDatabaseCompareException(e, this
						.getClass().getName(), "performOpenCommand", "1500");
			}

			try
			{
				Timeflecks.getSharedApplication()
						.openDatabaseFile(selectedFile); // Shouldn't this be
															// handled here?
															// (return success
															// or failure?)
			}
			catch (Exception e)
			{
				ExceptionHandler.handleDatabaseOpenException(e,
						"MenuBarController", "performOpenCommand", "1310");
			}

			Timeflecks.getSharedApplication().postNotification(
					TimeflecksEvent.GENERAL_REFRESH);
		}
	}

	public void performSaveCommand()
	{
		try
		{
			Timeflecks.getSharedApplication().getTaskList()
					.saveAllTasksAndEvents();
		}
		catch (Exception e)
		{
			ExceptionHandler.handleDatabaseSaveException(e, this.getClass()
					.getName(), "performSaveCommand()", "1300");
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
			int returnVal = fileChooser.showSaveDialog(menu);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
						"performSaveAsCommand",
						"User chose file " + fileChooser.getSelectedFile());

				File selectedFile = fileChooser.getSelectedFile();
				if (FileUtility.fileExistsAndIsNotSame(selectedFile))
				{
					// We need to prompt the user to see if they want to
					// overwrite the file.
					Object[] options = { "Overwrite File", "Choose New File" };
					int reply = JOptionPane
							.showOptionDialog(
									menu,
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
									menu,
									"Invalid filename. (1200)\nPlease make sure that your file is valid and has the extension \".db\".",
									"Invalid Filename",
									JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception e)
				{
					ExceptionHandler.handleDatabaseSaveException(e, this
							.getClass().getName(), "performSaveAsCommand()",
							"1300");
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
		TaskPanelActionListener.addNewTask();
	}

	public void performEditCommand()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"performEditCommand()",
				"Edit task command issued. Bringing up NewTaskPanel.");
		TaskPanelActionListener.editSelectedTask(Timeflecks
				.getSharedApplication().getMainWindow().getTablePanel());
	}

	public void performDeleteTaskCommand()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"performDeleteTaskCommand()",
				"Delete task command issued. Bringing up confirmation.");
		TaskPanelActionListener.deleteSelectedTasks(Timeflecks
				.getSharedApplication().getMainWindow().getTablePanel());
	}

	public void performNewEventCommand()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"performNewEventCommand()",
				"New Event command issued. Bringing up NewEventPanel.");
		TaskPanelActionListener.addNewEvent();
	}

	public void performNoHelpCommand()
	{
		JOptionPane.showMessageDialog(menu,
				"Help is not available in this version of the application.",
				"No Help Available", JOptionPane.INFORMATION_MESSAGE);
	}
}
