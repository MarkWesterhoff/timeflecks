package core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;

import database.DatabaseSerializable;
import database.SerializableType;
import logging.GlobalLogger;

/**
 * 
 * Represents an event on the calendar
 * 
 */
public class Event implements Scheduleable, DatabaseSerializable
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

	public Event(String name, Date startTime, long duration)
	{
		Objects.requireNonNull(name);
		Objects.requireNonNull(startTime);
		
		id = Timeflecks.getSharedApplication().getIdGenerator().getNextID();
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		GlobalLogger.getLogger().logp(Level.INFO, "core.Event", "core.Event()",
				"Creating event " + this.name + " with id " + id);
	}

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
			GlobalLogger.getLogger().logp(Level.WARNING, "core.Event",
					"core.Event.setStartTime()",
					"Start time should not be set to null for events");
			throw new IllegalArgumentException(
					"Events should not have a null start time.");
		}
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
	 * @throws SQLException
	 *             when there is a problem writing to the database
	 * @throws IOException
	 *             when there is a problem with serialization
	 */
	public void saveToDatabase() throws SQLException, IOException {
		GlobalLogger.getLogger().logp(Level.INFO, "core.Event", "core.Event.saveToDatabase()", "Saving " 
				+ this.name + " to database.");
		
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(this);
	}

	@Override
	public SerializableType getType()
	{
		return SerializableType.EVENT;
	}
}
