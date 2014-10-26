package core;

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
	
	public Timeflecks()
	{
		taskList = new TaskList();
		dbConnector = new SQLiteConnector();
		IDGenerator = new IDGenerator();
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
	
	
	
	
	
	
	public static void main(String[] args)
	{
		

	}

}
