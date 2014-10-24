package core;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.GlobalLogger;

import database.IDGenerator;
import database.SQLiteConnector;

/**
 * 
 * Represents a single scheduleable task -- the heart of the
 *         application.
 * 
 */

public class Task implements Scheduleable, Serializable
{	
	private static final long serialVersionUID = 1L;
	
	public final static int HIGH_PRIORITY = 2;
	public final static int MEDIUM_PRIORITY = 1;
	public final static int LOW_PRIORITY = 0;
	public final static int NO_PRIORITY_SELECTED = -1;
	
	protected String name;
	protected Date startTime;
	protected String description;
	protected final long id;

	/*
	 * Java Date class has <code>getTime()</code>, <code>setTime()</code>, which
	 * takes a <code>long</code>, so math can be done. In milliseconds.
	 */
	protected long duration;
	
	private boolean completed;
	private Date dueDate;
	private int priority;
	private ArrayList<String> tags;
	private long ordering;

	protected transient Logger logger;
	
	public Task(String name)
	{
		id = IDGenerator.getNextID();
		this.name = name;
		completed = false;
		setOrdering(id);
		this.logger = GlobalLogger.getLogger();
		logger.logp(Level.INFO, "core.Task", "core.Task()", 
				"Creating task " + this.name + " with id " + id);
		priority = NO_PRIORITY_SELECTED;
		tags = new ArrayList<String>();
	}

	/*
	 * A bunch of getters and setters, as well as a couple of additional
	 * functions.
	 */

	public long getId() {
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		if(startTime == null) {
			logger.logp(Level.INFO, "core.Task", 
					"core.Task.setStartTime(startTime)",
					"Resetting start time of task with id " + id + ".");
		} else {
			logger.logp(Level.INFO, "core.Task", 
					"core.Task.setStartTime(startTime)",
					"Setting start time to task with id " + id + "as " + 
							startTime.toString());
		}
		this.startTime = startTime;
	}

	/**
	 * 
	 * @return the time at which the TimeObject should be finished
	 */
	public Date getEndTime()
	{
		if (startTime == null) {
			return null;
		}
		else {
			return new Date(startTime.getTime() + duration);
		}
	}

	/**
	 * 
	 * @return whether the object has assigned a start time
	 */
	public boolean isScheduled()
	{
		return !(startTime == null);
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
		logger.logp(Level.INFO, "core.Task", "core.Task.addTag(tagname)", 
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
	
	public final static Comparator<Task> nameComparator = new Comparator<Task>() {
		public int compare(Task t1, Task t2) {
			return t1.name.compareTo(t2.name);
		}
	};
	
	public final static Comparator<Task> dueDateComparator = new Comparator<Task>() {
		public int compare(Task t1, Task t2) {
			// put non-scheduled tasks at the end
			if(t1.dueDate == null) {
				return (t1.dueDate == t2.dueDate)?0:1;
			} else if(t2.dueDate == null) {
				return -1;
			} else {
				return t1.dueDate.compareTo(t2.dueDate);
			}
		}
	};
	
	public final static Comparator<Task> priorityComparator = new Comparator<Task>() {
		public int compare(Task t1, Task t2) {
			return -Integer.compare(t1.priority, t2.priority);
		}
	};
	
	public final static Comparator<Task> manualComparator = new Comparator<Task>() {
		public int compare(Task t1, Task t2) {
			return Long.compare(t1.ordering, t2.ordering);
		}
	};
	
	/**
	 * Saves this TimeObject to the database.
	 * 
	 * @throws SQLException when there is a problem writing to the database
	 * @throws IOException when there is a problem with serialization
	 */
	public void saveToDatabase() throws SQLException, IOException {
		logger.logp(Level.INFO, "core.Task", "core.Task.saveToDatabase()", "Saving " 
				+ this.name + " to database.");
		
		SQLiteConnector sqlConn = SQLiteConnector.getInstance();
		sqlConn.serializeAndSave(this);
	}
}
