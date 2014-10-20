package core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

//I'm not sure if this should / shouldn't be a singleton...
public class TaskList
{
	static TaskList instance;
	
	public static TaskList getInstance()
	{
		if (instance == null)
		{
			instance = new TaskList();
		}
		return instance;
	}
	
	// Class implementation
	
	private ArrayList<Task> tasks;
	private ArrayList<Event> events;
	
	private TaskList()
	{
		tasks = new ArrayList<Task>();
		events = new ArrayList<Event>();
	}

	public ArrayList<Task> getTasks()
	{
		return tasks;
	}

	public ArrayList<Event> getEvents()
	{
		return events;
	}
	
	public void setTasks(ArrayList<Task> tasks)
	{
		this.tasks = tasks;
	}
	
	public void setEvents(ArrayList<Event> events)
	{
		this.events = events;
	}
	
		
	/**
	 * Saves all tasks to the database.
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void saveAllTasksAndEvents() throws SQLException, IOException {
		for (Task t : tasks) {
			t.saveToDatabase();
		}
		
		for (Event e : events) {
			e.saveToDatabase();
		}
	}

}
