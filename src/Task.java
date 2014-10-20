import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * Represents a single scheduleable task -- the heart of the
 *         application.
 * 
 */

public class Task extends TimeObject
{
	private static final long serialVersionUID = 1L;
	
	public final static int HIGH_PRIORITY = 2;
	public final static int MEDIUM_PRIORITY = 1;
	public final static int LOW_PRIORITY = 0;
	public final static int NO_PRIORITY_SELECTED = -1;
	
	private boolean completed;
	private Date dueDate;
	private int priority;
	private ArrayList<String> tags;
	private long ordering;

	public Task(String name)
	{
		super(name);
		completed = false;
		setOrdering(id);
	}

	/*
	 * A bunch of getters and setters, as well as a couple of additional
	 * functions.
	 */

	public boolean isCompleted()
	{
		return completed;
	}

	public void complete()
	{
		this.completed = true;
	}

	public void uncomplete()
	{
		this.completed = false;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public Date getDueDate()
	{
		return dueDate;
	}

	public void setDueDate(Date dueDate)
	{
		this.dueDate = dueDate;
	}

	public ArrayList<String> getTags()
	{
		return tags;
	}

	public void setTags(ArrayList<String> tags)
	{
		this.tags = tags;
	}

	public boolean hasTag(String tag)
	{
		return tags.contains(tag);
	}

	public void addTag(String tagname)
	{
		tags.add(tagname);
	}

	public long getOrdering()
	{
		return ordering;
	}

	public void setOrdering(long ordering)
	{
		this.ordering = ordering;
	}
}
