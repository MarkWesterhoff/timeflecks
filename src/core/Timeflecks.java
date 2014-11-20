package core;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import user_interface.MainWindow;
import logging.GlobalLogger;
import database.*;

public class Timeflecks
{
	// Singleton behavior

	static Timeflecks instance;
	static final Object initializerLock = new Object();

	public static Timeflecks getSharedApplication()
	{
		synchronized (initializerLock)
		{
			if (instance == null)
			{
				instance = new Timeflecks();
			}
		}

		return instance;
	}

	// Instance
	private TaskList taskList;
	private SQLiteConnector dbConnector;
	private IDGenerator idGenerator;
	private File currentFile;
	private MainWindow mainWindow;
	private TimeflecksEventManager eventManager;
	private FilteringManager filteringManager;
	
	public Timeflecks()
	{
		eventManager = new TimeflecksEventManager();

		taskList = new TaskList();
		
		this.filteringManager = new FilteringManager(new TagFilterComparator(),
				new TagCollection(), new SearchFilterComparator());

		try
		{
			openDatabaseFile(new File("calendar1.db"));
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
		catch (ClassNotFoundException e)
		{
			GlobalLogger
					.getLogger()
					.logp(Level.WARNING, "Timeflecks", "Timeflecks",
							"ClassNotFoundException. Could not read objects out from database.");
		}

		setMainWindow(new MainWindow());
	}

	public TaskList getTaskList()
	{
		return taskList;
	}

	public void setTaskList(TaskList taskList)
	{
		Objects.requireNonNull(taskList);
		this.taskList = taskList;
	}
	
	public FilteringManager getFilteringManager() {
		return this.filteringManager;
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
		Objects.requireNonNull(idGenerator);
		this.idGenerator = idGenerator;
	}
	
	public File getCurrentFile()
	{
		return currentFile;
	}

	public void setCurrentFile(File currentFile)
	{
		Objects.requireNonNull(currentFile);
		this.currentFile = currentFile;
	}

	public void openDatabaseFile(File newFile) throws SQLException,
			IOException, ClassNotFoundException
	{
		Objects.requireNonNull(newFile);
		setDBConnector(new SQLiteConnector(newFile, false));
		setCurrentFile(newFile);

		long highestID = getDBConnector().getHighestID();

		IDGenerator newGenerator = new IDGenerator(highestID + 1);
		setIdGenerator(newGenerator);

		TaskList newList = loadTaskListFromConnector(getDBConnector());
		setTaskList(newList);

		if (this.getMainWindow() != null)
		{
			this.getMainWindow().refresh();
		}
	}

	public TaskList loadTaskListFromConnector(SQLiteConnector connector)
			throws SQLException, IOException, ClassNotFoundException
	{
		Objects.requireNonNull(connector);
		ArrayList<Task> tasksFromFile = connector.getAllTasks();
		ArrayList<Event> eventsFromFile = connector.getAllEvents();

		TaskList newList = new TaskList(tasksFromFile, eventsFromFile);

		return newList;
	}

	public void saveDatabaseFileAs(File newFile) throws SQLException,
			IOException
	{
		Objects.requireNonNull(newFile);
		setDBConnector(new SQLiteConnector(newFile, true));
		setCurrentFile(newFile);

		getTaskList().saveAllTasksAndEvents();
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}

	public void setMainWindow(MainWindow mainWindow)
	{
		if (this.mainWindow != mainWindow)
		{
			this.mainWindow.dispose();
		}
		this.mainWindow = mainWindow;
	}

	// Now anyone can register to receive the timeflecks events, and anyone at
	// all can post simply by getting the shared application.
	public void registerForTimeflecksEvents(TimeflecksEventResponder t)
	{
		if (eventManager == null)
		{
			GlobalLogger.getLogger().logp(Level.SEVERE, "Timeflecks",
					"registerForTimeflecksEvents",
					"ERROR: eventManager was null.");
		}
		eventManager.addListener(t);
	}

	public void deregister(TimeflecksEventResponder t)
	{
		if (eventManager == null)
		{
			GlobalLogger.getLogger().logp(Level.SEVERE, "Timeflecks",
					"deregister", "ERROR: eventManager was null.");
		}

		eventManager.removeListener(t);
	}

	public void postNotification(TimeflecksEvent e)
	{
		if (eventManager == null)
		{
			GlobalLogger.getLogger().logp(Level.SEVERE, "Timeflecks",
					"postNotification", "ERROR: eventManager was null.");
		}

		eventManager.postEvent(e);
	}

	public static void main(String[] args)
	{
		// This variable is not used. Simply creating the application is
		// sufficient to run the application.
		@SuppressWarnings("unused")
		Timeflecks application = Timeflecks.getSharedApplication();
	}

}
