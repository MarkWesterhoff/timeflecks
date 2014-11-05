package core;

import java.util.Date;

//look up static interface...?
public abstract class Recurrence
{
	public Recurrence(Task protoTask, Date toDate)
	{
		if (protoTask.getDueDate() == null)
		{
			throw new IllegalArgumentException(
					"prototype tasks should have a set due date");
		}
		Task currentTask = protoTask;
		while (currentTask.getDueDate().compareTo(toDate) >= 0)
		{
			Timeflecks.getSharedApplication().getTaskList()
					.addTask(currentTask);
			Task nextTask = new Task(currentTask);
			nextTask.setDueDate(getNextTime(currentTask.getDueDate()));
			currentTask = nextTask;
		}
	}

	// DOES NOT RETURN NULL
	public abstract Date getNextTime(Date fromDate);
}