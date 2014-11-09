package core;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import logging.GlobalLogger;

//look up static interface...?
public abstract class Recurrence
{
	ArrayList<Task> tasks;
	//ASSUMES PROTOTYPE DATE FOLLOWS PATTERN
	public Recurrence(Task protoTask, Date toDate)
	{
		tasks = new ArrayList<Task>();
		if (protoTask.getDueDate() == null)
		{
			throw new IllegalArgumentException(
					"prototype tasks should have a set due date");
		}
		Task currentTask = protoTask;
		while (currentTask.getDueDate().compareTo(toDate) <= 0)
		{
			Timeflecks.getSharedApplication().getTaskList()
					.addTask(currentTask);
			tasks.add(currentTask);
			Task nextTask = new Task(currentTask);
			nextTask.setDueDate(getNextTime(currentTask.getDueDate()));
			GlobalLogger.getLogger().logp(Level.INFO,"core.Recurrence",
					"Recurrence(Task, Date)","created new task with" +
							" due date " + nextTask.getDueDate().toString());
			currentTask = nextTask;
		}
	}

	// DOES NOT RETURN NULL
	public abstract Date getNextTime(Date fromDate);
	
	public ArrayList<Task> getTasks() {
		return tasks;
	}
}