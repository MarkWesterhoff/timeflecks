package core;

import java.io.File;

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

	public Timeflecks()
	{
		taskList = new TaskList();
		dbConnector = new SQLiteConnector();
		idGenerator = new IDGenerator();
		setCurrentFile(new File("calendar1.db"));
		
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

	public SQLiteConnector getDbConnector()
	{
		return dbConnector;
	}

	public void setDbConnector(SQLiteConnector dbConnector)
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
	
	
	
	
	
	public static void main(String[] args)
	{

	}

}
