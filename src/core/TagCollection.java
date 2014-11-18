package core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class TagCollection
{
	private HashSet<String> tags;

	/**
	 * Constructor for TagManager. Initializes collection of tags with a new
	 * empty collection.
	 */
	public TagCollection()
	{
		tags = new HashSet<String>();
	}

	/**
	 * Adds a new tag to the list of tags if it is not already contained within
	 * the list.
	 * 
	 * @param tag
	 *            the tag to add
	 */
	public void addTag(String tag)
	{
		Objects.requireNonNull(tag);

		if (!tags.contains(tag))
		{
			tags.add(tag);
		}
	}

	/**
	 * Removes a tag from the collection of tags.
	 * 
	 * @param tag
	 * @return
	 */
	public boolean removeTag(String tag)
	{
		return tags.remove(tag);
	}

	/**
	 * Clears the collection of tags.
	 */
	public void clearTags()
	{
		tags.clear();
	}

	/**
	 * Returns an Iterable representing the collection of tags.
	 * 
	 * @return
	 */
	public final Collection<String> getTags()
	{
		return tags;
	}
}
