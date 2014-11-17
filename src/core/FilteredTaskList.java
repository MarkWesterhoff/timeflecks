package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;

import logging.GlobalLogger;

public class FilteredTaskList
{
	private ArrayList<Task> tasks;
	private Comparator<Task> taskComparator;
	
	public FilteredTaskList() {
		this.taskComparator = Task.manualComparator;
	}
	
	public ArrayList<Task> getTasks()
	{
		if(this.tasks == null) {
			this.repopulate();
		}
		
		return this.tasks;
	}
	

	public void setTaskComparator(Comparator<Task> taskComparator)
	{
		this.taskComparator = taskComparator;
	}
	
	public void sort()
	{
		Collections.sort(tasks, taskComparator);
		GlobalLogger.getLogger().logp(Level.INFO, "core.TaskList",
				"core.TaskList.sortTasks()", "Sorting task list");
	}
	
	public void repopulate() {
		tasks = Timeflecks.getSharedApplication().getTaskList().getTasks();
	}	
}
