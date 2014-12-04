package core;

/**
 * Abstract class defining contract for objects used to do filtering on lists of
 * tasks.
 * 
 * @param <E>
 *            The object type to filter by
 */
public abstract class FilterComparator<E>
{
	/**
	 * 
	 * @param task
	 *            the task to check
	 * @param element
	 *            the element to filter by
	 * @return true if the task matches the filter (and should be included)
	 */
	public abstract boolean matchesFilter(Task task, E element);
}
