package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;

import javax.swing.SwingUtilities;

import logging.GlobalLogger;

public class FilteringManager implements TimeflecksEventResponder
{
	private ArrayList<Task> tasks;
	private TagFilterComparator tagFilterComparator;
	private TagCollection tagCollection;
	private Comparator<Task> taskComparator;

	public FilteringManager(TagFilterComparator tagFilterComparator,
			TagCollection tagCollection)
	{
		Objects.requireNonNull(tagFilterComparator);
		Objects.requireNonNull(tagCollection);

		this.taskComparator = Task.defaultComparator;
		this.tagCollection = tagCollection;
		this.tagFilterComparator = tagFilterComparator;
		
		tasks = new ArrayList<Task>();
		
		final FilteringManager thisFiltertingManager = this;
		
		SwingUtilities.invokeLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				Timeflecks.getSharedApplication().registerForTimeflecksEvents(thisFiltertingManager);
				
			}
		});
	}

	public ArrayList<Task> getFilteredTaskList()
	{
		return this.tasks;
	}

	public TagCollection getTagCollection()
	{
		return this.tagCollection;
	}

	/**
	 * Set the TaskComparator used to sort the list of tasks.
	 * 
	 * @param taskComparator
	 *            the TaskComparator to use
	 */
	public void setTaskComparator(Comparator<Task> taskComparator)
	{
		Objects.requireNonNull(taskComparator);
		this.taskComparator = taskComparator;
	}

	/**
	 * Repopulates the list of tasks to hold based on the tags the user has
	 * selected.
	 */
	public void repopulateFilteredTasks()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "FilteringManager",
				"repopulateFilteredTasks()", "Repopulating filtered task list");

		// Clone of ArrayList<Task> is ArrayList<Task>, so this warning is moot.
		@SuppressWarnings("unchecked")
		ArrayList<Task> allTasks = (ArrayList<Task>) Timeflecks
				.getSharedApplication().getTaskList().getTasks().clone();

		Collection<String> tags = this.tagCollection.getTags();

		// Filter by tags as long as at least one is selected.
		if (!tags.isEmpty())
		{
			allTasks = FilteringManager.filterTasks(allTasks, tags,
					this.tagFilterComparator);
		}

		Collections.sort(allTasks, this.taskComparator);

		this.tasks = allTasks;
	}

	/**
	 * Filters a list of Tasks and returns a the same (now modified) list of
	 * those tasks that satisfy the conditions set by the filterMethod.
	 * 
	 * @param tasks
	 *            the list of Tasks to filter
	 * @param filterItems
	 *            the items to filter by
	 * @param filterMethod
	 *            the method that defines which Tasks are included
	 * @return the list of filtered Tasks
	 */
	private static <E> ArrayList<Task> filterTasks(ArrayList<Task> tasks,
			final Iterable<E> filterItems,
			final FilterComparator<E> filterMethod)
	{
		Objects.requireNonNull(tasks);
		Objects.requireNonNull(filterItems);
		Objects.requireNonNull(filterMethod);

		ArrayList<Task> filteredTasks = new ArrayList<Task>();

		for (Task task : tasks)
		{
			for (E filterItem : filterItems)
			{
				if (filterMethod.matchesFilter(task, filterItem))
				{
					filteredTasks.add(task);
					break;
				}
			}
		}

		return filteredTasks;
	}

	@Override
	public void eventPosted(TimeflecksEvent t)
	{
		if(t.equals(TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST)) {
			this.repopulateFilteredTasks();
			Timeflecks.getSharedApplication().postNotification(TimeflecksEvent.GENERAL_REFRESH);
		}
		else {
			// Don't care about other events
		}
	}

}
