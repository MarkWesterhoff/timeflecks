package core;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

import logging.GlobalLogger;

/**
 * 
 * Represents either an Event or a Task
 * 
 */
public abstract class TimeObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/* Static variable and method to deal with giving each TimeObject a unique
	 * id.
	 */
	private static long taskCount = 0;
	private static long getNextId() {
		if (taskCount == Long.MAX_VALUE) {
			// can we recover from this?
			throw new RuntimeException("Exceeded maximum number of tasks/events.");
		}
		
		long id = taskCount;
		taskCount++;
		return id;
	}
	
	/* Member Variables */
	protected String name;
	protected Date startTime;
	protected String description;
	protected final long id;
	
	// transient logger will be ignored during serialization
	protected transient Logger logger;
	

	/*
	 * Basic constructor, can support other input later (the rest are optional)
	 */
	public TimeObject(String name)
	{
		id = TimeObject.getNextId();
		this.name = name;
		logger = GlobalLogger.getLogger();
	}
	
	
	
	/*
	 * Java Date class has <code>getTime()</code>, <code>setTime()</code>, which
	 * takes a <code>long</code>, so math can be done. In milliseconds.
	 */
	protected long duration;

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
		this.startTime = startTime;
	}

	/**
	 * 
	 * @return the time at which the TimeObject should be finished
	 */
	public Date getEndTime()
	{
		return new Date(startTime.getTime() + duration);
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
	
	/**
	 * Saves this TimeObject to the database.
	 * 
	 * @throws SQLException when there is a problem writing to the database
	 * @throws IOException when there is a problem with serialization
	 */
	public void saveToDatabase() throws SQLException, IOException {
		logger.logp(Level.INFO, "TimeObject", "saveToDatabase", "Saving " 
				+ this.name + " to database.");
		
		SQLiteConnector conn = SQLiteConnector.getInstance();
		conn.saveSerializedTimeObject(this);
	}
}