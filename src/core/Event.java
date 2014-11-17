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
	private Date endTime;
	private String description;
	private final long id;

	public Event(String name, Date startTime, Date endTime)
	{
		Objects.requireNonNull(name);
		Objects.requireNonNull(startTime);

		id = Timeflecks.getSharedApplication().getIdGenerator().getNextID();
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
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
		return endTime;
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
		return endTime.getTime() - startTime.getTime();
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

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
		GlobalLogger.getLogger().logp(Level.INFO, "core.Event",
				"core.Event.saveToDatabase()",
				"Saving " + this.name + " to database.");

		Timeflecks.getSharedApplication().getDBConnector()
				.serializeAndSave(this);
	}

	public SerializableType getType()
	{
		return SerializableType.EVENT;
	}

	/**
	 * Note: All events should return true for this value. If they aren't going
	 * to, they will print a warning through log level WARNING.
	 * 
	 * @return whether the object has assigned a start time and end time.
	 */
	public boolean isScheduled()
	{
		if (startTime == null || endTime == null)
		{
			GlobalLogger
					.getLogger()
					.logp(Level.WARNING, "core.Event", "isScheduled()",
							"Start time or end time was null on an event checking scheduled status.");
			return false;
		}
		else
		{
			// We should always return true from this method.
			return true;
		}
	}

	/**
	 * For now we make Events never be completed, although, we could make them
	 * be completed if they are being shown in the past.
	 * 
	 * @return False to indicate that events are never considered "complete"
	 */
	public boolean isCompleted()
	{
		return false;
	}
}
