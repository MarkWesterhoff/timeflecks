package core;

import java.util.Objects;

/**
 * Concrete extension of FilterComparator for tags.
 * 
 * @author Andrew
 * 
 */
public class TagFilterComparator extends FilterComparator<String>
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
	@Override
	public boolean matchesFilter(Task task, String tag)
	{
		Objects.requireNonNull(task);
		Objects.requireNonNull(tag);
		
		if (task.hasTag(tag))
		{
			return true;
		}
		return false;
	}

}
