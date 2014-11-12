package core;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Managers the filtering if lists of Tasks. 
 * 
 * @author Andrew
 * 
 */
public class TaskFilterManager
{
	/**
	 * Filters a list of Tasks and returns a list of those tasks that satisfy
	 * the conditions set by the filterMethod.
	 * 
	 * @param tasks
	 *            the list of Tasks to filter
	 * @param filterItems
	 *            the items to filter by
	 * @param filterMethod
	 *            the method that defines which Tasks are included
	 * @return the list of filtered Tasks
	 */
	static <E> List<Task> getFilteredTasks(final List<Task> tasks,
			final List<E> filterItems, final FilterComparator<E> filterMethod)
	{
		Objects.requireNonNull(tasks);
		Objects.requireNonNull(filterItems);

		List<Task> filteredTasks = new LinkedList<Task>();

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
}
