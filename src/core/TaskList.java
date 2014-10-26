package core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.GlobalLogger;

//I'm not sure if this should / shouldn't be a singleton...
public class TaskList
{
	
	// Class implementation
	
	private ArrayList<Task> tasks;
	private ArrayList<Event> events;
	private transient Logger logger;
	private Comparator<Task> taskComparator;
	
	public TaskList()
	{
		tasks = new ArrayList<Task>();
		events = new ArrayList<Event>();
		this.logger = GlobalLogger.getLogger();
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
	
	public void addEvent(Event e) {
		logger.logp(Level.INFO, "core.TaskList", "core.TaskList.addEvent(e)",
				"Adding new event with id " + e.getId() + " to event list");
		events.add(e);
	}
	
	public void addTask(Task t) {
		logger.logp(Level.INFO, "core.TaskList", "core.TaskList.addTask(e)",
				"Adding new task with id " + t.getId() + " to task list");
		tasks.add(t);
	}
	
	// DEPRECATED, use sort() and setTaskComparator
	
	public void sortTasks(Comparator<Task> taskComp) {
		Collections.sort(tasks,taskComp);
		logger.logp(Level.INFO, "core.TaskList", "core.TaskList.sortTasks()",
				"Sorting task list");
	}
	
	public void sort() {
		Collections.sort(tasks,taskComparator);
		logger.logp(Level.INFO, "core.TaskList", "core.TaskList.sortTasks()",
				"Sorting task list");
	}
	
		
	/**
	 * Saves all tasks to the database.
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void saveAllTasksAndEvents() throws SQLException, IOException {
		logger.logp(Level.INFO, "core.TaskList", 
				"core.TaskList.saveAllTasksAndEvents",
				"saving all tasks and events to database");
		for (Task t : tasks) {
			t.saveToDatabase();
		}
		
		for (Event e : events) {
			e.saveToDatabase();
		}
	}

	public void setTaskComparator(Comparator<Task> taskComparator)
	{
		this.taskComparator = taskComparator;
	}

}
