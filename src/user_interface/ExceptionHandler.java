package user_interface;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import core.Timeflecks;
import logging.GlobalLogger;

/**
 * Contains exception handling logic.
 * 
 */
public class ExceptionHandler
{

	public static void handleDatabaseCompareException(Exception e,
			String className, String originFunction, String ID)
	{
		GlobalLogger.getLogger().logp(Level.WARNING, className, originFunction,
				"File system error. Cannot compare new file to current file.");

		// Trouble serializing objects
		JOptionPane
				.showMessageDialog(
						Timeflecks.getSharedApplication().getMainWindow(),
						"File System Error. ("
								+ ID
								+ ")\nUnable to compare the new file to the current file. Please select a different file and try again.",
						"File System Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void handleNoDateException(IllegalArgumentException e,
			String className, String originFunction, String ID)
	{
		Objects.requireNonNull(e);
		Objects.requireNonNull(className);
		Objects.requireNonNull(originFunction);
		Objects.requireNonNull(ID);
		GlobalLogger
				.getLogger()
				.logp(Level.WARNING,
						className,
						originFunction,
						"IllegalArgumentException for repeating task. Likely no due/start date set. Prompting user.\nException Message: "
								+ e.getMessage());

		JOptionPane
				.showMessageDialog(
						Timeflecks.getSharedApplication().getMainWindow(),
						"You must specify either a start date or a due date for a repeating task.",
						"Start or Due Date Required",
						JOptionPane.WARNING_MESSAGE);
	}

	public static void handleDatabaseOpenException(Exception e,
			String className, String originFunction, String ID)
	{
		Objects.requireNonNull(e);
		Objects.requireNonNull(className);
		Objects.requireNonNull(originFunction);
		Objects.requireNonNull(ID);
		if (e instanceof SQLException)
		{

			GlobalLogger.getLogger().logp(Level.WARNING, className,
					originFunction,
					"Open command generated SQLException. Showing dialog.");

			JOptionPane.showMessageDialog(Timeflecks.getSharedApplication()
					.getMainWindow(), "Database Error. " + ID
					+ "\nCould not create new database.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
		else if (e instanceof IOException)
		{
			GlobalLogger.getLogger().logp(Level.WARNING, className,
					originFunction,
					"Open command generated IOException. Showing dialog.");

			// Trouble serializing objects
			JOptionPane.showMessageDialog(Timeflecks.getSharedApplication()
					.getMainWindow(), "Object Serialization Error. (" + ID
					+ ")\nCould not save empty database file.",
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
		else if (e instanceof ClassNotFoundException)
		{
			GlobalLogger
					.getLogger()
					.logp(Level.WARNING, className, originFunction,
							"ClassNotFoundException. Could not read objects out from database.");
		}
	}

	public static void handleDatabaseSaveException(Exception e,
			String className, String originFunction, String ID)
	{
		Objects.requireNonNull(e);
		Objects.requireNonNull(className);
		Objects.requireNonNull(originFunction);
		Objects.requireNonNull(ID);

		if (e instanceof IOException)
		{
			GlobalLogger.getLogger().logp(
					Level.WARNING,
					className,
					originFunction,
					"IOException caught when saving task to database.\nMessage:\n"
							+ e.getLocalizedMessage());

			// Trouble serializing objects
			JOptionPane
					.showMessageDialog(
							Timeflecks.getSharedApplication().getMainWindow(),
							"Object Serialization Error. ("
									+ ID
									+ ")\nYour task was not saved. Please try again, or check your database file.",
							"Database Error", JOptionPane.ERROR_MESSAGE);

		}
		else if (e instanceof SQLException)
		{
			GlobalLogger.getLogger().logp(
					Level.WARNING,
					className,
					originFunction,
					"SQLException caught when saving task to database.\nSQL State:\n"
							+ ((SQLException) e).getSQLState() + "\nMessage:\n"
							+ e.getMessage());

			JOptionPane
					.showMessageDialog(
							Timeflecks.getSharedApplication().getMainWindow(),
							"Database Error. ("
									+ ID
									+ ")\nYour task was not saved. Please try again, or check your database file.",
							"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void handleDatabaseDeleteException(Exception e,
			String className, String originFunction, String ID)
	{
		Objects.requireNonNull(e);
		Objects.requireNonNull(className);
		Objects.requireNonNull(originFunction);
		Objects.requireNonNull(ID);

		if (e instanceof SQLException)
		{
			GlobalLogger.getLogger().logp(
					Level.WARNING,
					className,
					originFunction,
					"SQLException caught when deleting task from database.\nSQL State:\n"
							+ ((SQLException) e).getSQLState() + "\nMessage:\n"
							+ e.getMessage());

			JOptionPane
					.showMessageDialog(
							Timeflecks.getSharedApplication().getMainWindow(),
							"Database Error. ("
									+ ID
									+ ")\nYour task was not deleted. Please try again, or check your database file.",
							"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
