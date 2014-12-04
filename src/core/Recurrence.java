package core;

import java.util.*;
import java.util.logging.Level;

import logging.GlobalLogger;

/**
 * A class to generate recurring Tasks.
 * 
 */
public abstract class Recurrence
{
	ArrayList<Task> tasks;

	/**
	 * Note: Assumes prototype date follows pattern.
	 * 
	 * @param protoTask
	 * @param toDate
	 * @throws IllegalArgumentException
	 */
	public Recurrence(Task protoTask, Date toDate)
	{
		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"Recurrence(Task,  Date)", "Constructing Recurrence");
		tasks = new ArrayList<Task>();
		if (protoTask.getDueDate() == null && protoTask.getStartTime() == null)
		{
			throw new IllegalArgumentException(
					"prototype tasks should have a set start date or due date");
		}
		Task currentTask = protoTask;

		// Advance based upon whichever one is present. If both are present,
		// then we will advance until both of them are past the end time.
		while ((currentTask.getDueDate() != null && currentTask.getDueDate()
				.compareTo(toDate) <= 0)
				|| (currentTask.getStartTime() != null && currentTask
						.getStartTime().compareTo(toDate) <= 0))
		{
			tasks.add(currentTask);
			Task nextTask = new Task(currentTask);

			// Move forward both the start time and the due date, if applicable
			if (currentTask.getStartTime() != null)
			{
				nextTask.setStartTime(getNextTime(currentTask.getStartTime()));
			}
			if (currentTask.getDueDate() != null)
			{
				nextTask.setDueDate(getNextTime(currentTask.getDueDate()));
			}

			GlobalLogger.getLogger().logp(Level.INFO, "core.Recurrence",
					"Recurrence(Task, Date)",
					"created new task: " + nextTask.getId());

			currentTask = nextTask;
		}
	}

	// DOES NOT RETURN NULL
	public abstract Date getNextTime(Date fromDate);

	public ArrayList<Task> getTasks()
	{
		return tasks;
	}
}