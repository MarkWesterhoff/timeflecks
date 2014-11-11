package core;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TaskTagFilterer
{
	List<String> tags;

	/**
	 * Constructor for TaskTagFilterer.
	 */
	public TaskTagFilterer()
	{
		tags = new LinkedList<String>();
	}

	/**
	 * Adds a tag to the list of tags to filter by.
	 * 
	 * @param tag
	 *            the tag to add
	 */
	public void addTag(String tag)
	{
		Objects.requireNonNull(tag);
		tags.add(tag);
	}

	/**
	 * Removes a tag from the list of tags to filter by.
	 * 
	 * @param tag
	 *            the tag to remove
	 */
	public void removeTask(String tag)
	{
		Objects.requireNonNull(tag);
		tags.remove(tag);
	}

	/**
	 * Clears the list of tags to filter by.
	 */
	public void clearTasks()
	{
		tags.clear();
	}

	/**
	 * Returns the tasks that are tagged with one of the input tags.
	 * 
	 * @param tasks
	 *            the tasks to be filtered
	 * @param tags
	 *            the tags that we want tasks tagged with
	 * @return the filtered list of tasks
	 */
	public List<Task> getFilteredTasks(List<Task> tasks)
	{
		Objects.requireNonNull(tasks);

		List<Task> filteredTasks = new LinkedList<Task>();

		for (Task task : tasks)
		{
			for (String tag : tags)
			{
				if (task.hasTag(tag))
				{
					filteredTasks.add(task);
					break;
				}
			}
		}

		return filteredTasks;
	}
}
