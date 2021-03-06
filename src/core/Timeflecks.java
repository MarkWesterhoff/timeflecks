package core;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

import user_interface.*;
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

	public static boolean isMac()
	{
		String osName = System.getProperty("os.name");
		return osName.contains("OS X");
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
		catch (Exception e)
		{
			ExceptionHandler.handleDatabaseOpenException(e, this.getClass()
					.getName(), "Timeflecks", "1600");
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

	public FilteringManager getFilteringManager()
	{
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
			this.postNotification(TimeflecksEvent.CHANGED_POSSIBLE_TAGS);
			this.postNotification(TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);
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
		if (this.mainWindow != null && this.mainWindow != mainWindow)
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
			GlobalLogger.getLogger().logp(Level.SEVERE,
					this.getClass().getName(),
					"registerForTimeflecksEvents(TimeflecksEventResponder)",
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
		Timeflecks.getSharedApplication();
	}
}
