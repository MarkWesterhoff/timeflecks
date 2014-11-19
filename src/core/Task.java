package core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;

import utility.StringUtility;
import database.DatabaseSerializable;
import database.SerializableType;
import logging.GlobalLogger;

/**
 * 
 * Represents a single Scheduleable task -- the heart of the application.
 * 
 */

public class Task implements Scheduleable, DatabaseSerializable
{
	private static final long serialVersionUID = 1L;

	private String name;
	private Date startTime;
	private String description;
	private final long id;

	/*
	 * Java Date class has <code>getTime()</code>, <code>setTime()</code>, which
	 * takes a <code>long</code>, so math can be done. In milliseconds.
	 */
	private long duration;

	private boolean completed;
	private Date dueDate;
	private Priority priority;
	private ArrayList<String> tags;
	private long ordering;

	public Task(String name)
	{
		Objects.requireNonNull(name);

		id = Timeflecks.getSharedApplication().getIdGenerator().getNextID();
		this.name = name;
		completed = false;
		setOrdering(id);
		GlobalLogger.getLogger().logp(Level.INFO, "core.Task", "core.Task()",
				"Creating task " + this.name + " with id " + id);
		priority = Priority.NO_PRIORITY_SELECTED;
		tags = new ArrayList<String>();
	}

	// We don't want this warning, we know what it will be and we don't need to check below. 
	@SuppressWarnings("unchecked")
	public Task(Task t)
	{
		Objects.requireNonNull(t);
		id = Timeflecks.getSharedApplication().getIdGenerator().getNextID();
		setOrdering(id);
		// TODO: Research copy constructors in Java (why isn't this a thing...?)
		this.name = t.name;
		this.completed = t.completed;
		this.description = t.description;
		this.priority = t.priority;
		this.duration = t.duration;
		this.description = t.description;
		
		this.tags = (ArrayList<String>) t.getTags().clone();
	}

	/*
	 * A bunch of getters and setters, as well as a couple of additional
	 * functions.
	 */

	public long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		Objects.requireNonNull(name);
		this.name = name;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		if (startTime == null)
		{
			GlobalLogger.getLogger().logp(Level.INFO, "core.Task",
					"core.Task.setStartTime(startTime)",
					"Resetting start time of task with id " + id + ".");
		}
		else
		{
			GlobalLogger.getLogger().logp(
					Level.INFO,
					"core.Task",
					"core.Task.setStartTime(startTime)",
					"Setting start time to task with id " + id + "as "
							+ startTime.toString());
		}
		this.startTime = startTime;
	}

	/**
	 * 
	 * @return the time at which the TimeObject should be finished
	 */
	public Date getEndTime()
	{
		if (startTime == null)
		{
			return null;
		}
		else
		{
			return new Date(startTime.getTime() + duration);
		}
	}

	/**
	 * 
	 * @return whether the object has assigned a start time and a duration
	 */
	public boolean isScheduled()
	{
		return (startTime != null && duration != 0);
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public long getDuration()
	{
		return duration;
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean value)
	{
		this.completed = value;
	}

	public Priority getPriority()
	{
		return priority;
	}

	public void setPriority(Priority priority)
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

	public String getTagsAsString()
	{
		return StringUtility.join(tags, ',');
	}

	public void setTags(ArrayList<String> tags)
	{
		Objects.requireNonNull(tags);
		this.tags = tags;
	}

	public boolean hasTag(String tag)
	{
		Objects.requireNonNull(tag);
		return tags.contains(tag);
	}

	public void addTag(String tagname)
	{
		Objects.requireNonNull(tagname);
		if (tagname.equals(""))
		{
			throw new IllegalArgumentException("tagname");
		}

		GlobalLogger.getLogger().logp(Level.INFO, "core.Task",
				"core.Task.addTag(tagname)",
				"Adding new tag " + tagname + " to task with id " + id);
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

	/**
	 * Create comparator objects
	 */

	public final static Comparator<Task> nameComparator = new Comparator<Task>()
	{
		public int compare(Task t1, Task t2)
		{
			return t1.name.compareToIgnoreCase(t2.name);
		}
	};

	public final static Comparator<Task> dueDateComparator = new Comparator<Task>()
	{
		public int compare(Task t1, Task t2)
		{
			// put non-scheduled tasks at the end
			if (t1.dueDate == null)
			{
				return (t1.dueDate == t2.dueDate) ? 0 : 1;
			}
			else if (t2.dueDate == null)
			{
				return -1;
			}
			else
			{
				return t1.dueDate.compareTo(t2.dueDate);
			}
		}
	};

	public final static Comparator<Task> priorityComparator = new Comparator<Task>()
	{
		public int compare(Task t1, Task t2)
		{
			return -Integer.compare(t1.priority.getValue(),
					t2.priority.getValue());
		}
	};

	public final static Comparator<Task> manualComparator = new Comparator<Task>()
	{
		public int compare(Task t1, Task t2)
		{
			return Long.compare(t1.ordering, t2.ordering);
		}
	};

	public final static Comparator<Task> defaultComparator = manualComparator;

	/**
	 * Saves this TimeObject to the database.
	 * 
	 * @throws SQLException
	 *             when there is a problem writing to the database
	 * @throws IOException
	 *             when there is a problem with serialization
	 */
	public void saveToDatabase() throws SQLException, IOException
	{
		GlobalLogger.getLogger().logp(Level.INFO, "core.Task",
				"core.Task.saveToDatabase()",
				"Saving " + this.name + " to database.");

		Timeflecks.getSharedApplication().getDBConnector()
				.serializeAndSave(this);
	}

	public SerializableType getType()
	{
		return SerializableType.TASK;
	}
}
