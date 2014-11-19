package core;

import java.util.Objects;

/**
 * Concrete extension of FilterComparator for tags.
 * 
 * @author Andrew
 */
public class SearchFilterComparator extends FilterComparator<String>
{
	/**
	 * Filter matching for searching. Checks if the Task's name has the
	 * searchText in it.
	 * 
	 * @param task
	 *            the Task to search the name of
	 * @param searchText
	 *            the text String to search by
	 * @return true if the Task's name contains the searchText
	 */
	public boolean matchesFilter(Task task, String searchText)
	{
		Objects.requireNonNull(task);
		Objects.requireNonNull(searchText);

		// Check if search string is contained inside the Task's name
		if (task.getName().indexOf(searchText) > -1)
		{
			return true;
		}

		return false;
	}

}
