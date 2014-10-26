package core;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import user_interface.MainWindow;
import logging.GlobalLogger;
import database.*;

public class Timeflecks
{
	// Singleton behavior

	static Timeflecks instance;

	public static Timeflecks getSharedApplication()
	{
		if (instance == null)
		{
			instance = new Timeflecks();
		}
		return instance;
	}

	// Instance
	private TaskList taskList;
	private SQLiteConnector dbConnector;
	private IDGenerator idGenerator;
	private File currentFile;
	private MainWindow mainWindow;

	public Timeflecks()
	{
		taskList = new TaskList();

		try
		{
			dbConnector = new SQLiteConnector();
		}
		catch (SQLException e)
		{
			GlobalLogger.getLogger().logp(Level.WARNING, "Timeflecks",
					"Timeflecks",
					"Open command generated SQLException. Showing dialog.");

			JOptionPane.showMessageDialog(getMainWindow(),
					"Database Error. (1600)\nCould not create new database.",
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e)
		{
			GlobalLogger.getLogger().logp(Level.WARNING, "Timeflecks",
					"Timeflecks",
					"Open command generated IOException. Showing dialog.");

			// Trouble serializing objects
			JOptionPane
					.showMessageDialog(
							getMainWindow(),
							"Object Serialization Error. (1601)\nCould not save empty database file.",
							"Database Error", JOptionPane.ERROR_MESSAGE);
		}

		idGenerator = new IDGenerator();
		setCurrentFile(new File("calendar1.db"));

		setMainWindow(new MainWindow());

		// TODO Be able to seed ID generator with an ID
		// TODO be able to get max ID in database from sqliteconnector
		// TODO
	}

	public TaskList getTaskList()
	{
		return taskList;
	}

	public void setTaskList(TaskList taskList)
	{
		this.taskList = taskList;
	}

	public SQLiteConnector getDBConnector()
	{
		return dbConnector;
	}

	public void setDBConnector(SQLiteConnector dbConnector)
	{
		this.dbConnector = dbConnector;
	}

	public IDGenerator getIdGenerator()
	{
		return idGenerator;
	}

	public void setIdGenerator(IDGenerator idGenerator)
	{
		this.idGenerator = idGenerator;
	}

	public File getCurrentFile()
	{
		return currentFile;
	}

	public void setCurrentFile(File currentFile)
	{
		this.currentFile = currentFile;
	}

	public void openDatabaseFile(File newFile) throws SQLException, IOException
	{
		setDBConnector(new SQLiteConnector(newFile));
		setCurrentFile(newFile);
		setTaskList(new TaskList());

		long highestID = getDBConnector().getHighestID();

		IDGenerator newGenerator = new IDGenerator(highestID);
		setIdGenerator(newGenerator);

		TaskList newList = loadTaskListFromConnector(getDBConnector());
	}

	public TaskList loadTaskListFromConnector(SQLiteConnector connector)
	{
		ArrayList<Task> tasksFromFile = connector.getAllTasks();
		ArrayList<Event> eventsFromFile = connector.getAllEvents();

		TaskList newList = new TaskList(tasksFromFile, eventsFromFile);

		return newList;
	}

	public void saveDatabaseFileAs(File newFile) throws SQLException,
			IOException
	{
		setDBConnector(new SQLiteConnector(newFile));
		setCurrentFile(newFile);

		getTaskList().saveAllTasksAndEvents();
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}

	public void setMainWindow(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}

	public static void main(String[] args)
	{
		Timeflecks application = Timeflecks.getSharedApplication();
	}

}
