package core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

//I'm not sure if this should / shouldn't be a singleton...
public class TaskList
{

	private TaskList()
	{
		taskEvents = new ArrayList<TimeObject>();
	}

	static TaskList instance;
	private ArrayList<TimeObject> taskEvents;

	public ArrayList<TimeObject> getTaskEvents()
	{
		return taskEvents;
	}

	public void setTaskEvents(ArrayList<TimeObject> taskEvents)
	{
		this.taskEvents = taskEvents;
	}

	public void addTimeObject(TimeObject t)
	{
		taskEvents.add(t);
	}

	public static TaskList getTaskInstance()
	{
		if (instance == null)
		{
			instance = new TaskList();
		}
		return instance;
	}
	
	/**
	 * Saves all tasks to the database.
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void saveAllTasks() throws SQLException, IOException {
		for (TimeObject to : taskEvents) {
			to.saveToDatabase();
		}
	}

}
