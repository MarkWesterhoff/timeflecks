package user_interface;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import core.Timeflecks;

import logging.GlobalLogger;

public class ExceptionHandler
{

	public static void handleDatabaseSaveException(Exception e, Object origin,
			String originFunction, String ID)
	{
		if (e instanceof IOException)
		{
			GlobalLogger.getLogger().logp(
					Level.WARNING,
					origin.getClass().getName(),
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
					origin.getClass().getName(),
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
			Object origin, String originFunction, String ID)
	{
		if (e instanceof SQLException)
		{
			GlobalLogger.getLogger().logp(
					Level.WARNING,
					origin.getClass().getName(),
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
