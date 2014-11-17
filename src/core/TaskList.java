package core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

import logging.GlobalLogger;

public class TaskList
{

	// Class implementation

	private ArrayList<Task> tasks;
	private ArrayList<Event> events;
	

	public TaskList()
	{
		this(new ArrayList<Task>(), new ArrayList<Event>());
	}

	public TaskList(ArrayList<Task> tasks, ArrayList<Event> events)
	{
		Objects.requireNonNull(tasks);
		Objects.requireNonNull(events);

		this.tasks = tasks;
		this.events = events;
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
		Objects.requireNonNull(tasks);
		this.tasks = tasks;
	}

	public void setEvents(ArrayList<Event> events)
	{
		Objects.requireNonNull(events);
		this.events = events;
	}

	public void addEvent(Event e)
	{
		Objects.requireNonNull(e);

		GlobalLogger.getLogger().logp(Level.INFO, "core.TaskList",
				"core.TaskList.addEvent(e)",
				"Adding new event with id " + e.getId() + " to event list");
		events.add(e);
	}

	public void addTask(Task t)
	{
		Objects.requireNonNull(t);
		GlobalLogger.getLogger().logp(Level.INFO, "core.TaskList",
				"core.TaskList.addTask(e)",
				"Adding new task with id " + t.getId() + " to task list");
		tasks.add(t);
	}

	/**
	 * Saves all tasks to the database.
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public void saveAllTasksAndEvents() throws SQLException, IOException
	{
		GlobalLogger.getLogger().logp(Level.INFO, "core.TaskList",
				"core.TaskList.saveAllTasksAndEvents",
				"saving all tasks and events to database");
		
		Timeflecks.getSharedApplication().getDBConnector()
				.serializeAndSave(this.tasks);

		Timeflecks.getSharedApplication().getDBConnector()
		.serializeAndSave(this.events);
	}


}
