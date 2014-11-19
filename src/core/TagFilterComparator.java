package core;

import java.util.Collection;
import java.util.Objects;

/**
 * Concrete extension of FilterComparator for tags.
 * 
 * @author Andrew
 */
public class TagFilterComparator extends FilterComparator<Collection<String>>
{
	/**
	 * Returns true if the task is tagged with the tag.
	 * 
	 * @param task
	 *            the task to check
	 * @param tag
	 *            the tag to check for
	 * @return true if the task is tagged with the tag; false otherwise
	 */
	public boolean matchesFilter(Task task, Collection<String> tags)
	{
		Objects.requireNonNull(task);
		Objects.requireNonNull(tags);
		
		// If the Task is tagged with any of the tags in the list, it matches
		for (String tag : tags)
		{
			if (task.hasTag(tag))
			{
				return true;
			}
		}
		return false;
	}

}
