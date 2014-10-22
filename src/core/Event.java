package core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.GlobalLogger;

import database.IDGenerator;
import database.SQLiteConnector;


/**
 * 
 * Represents an event on the calendar
 * 
 */
public class Event implements Scheduleable
{

	public Event(String name, Date startTime, long duration)
	{
		id = IDGenerator.getNextID();
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		this.logger = GlobalLogger.getLogger();
		logger.logp(Level.INFO, "core.Event","core.Event()","Creating event " + this.name + " with id " + id);
	}
	
	protected String name;
	protected Date startTime;
	protected String description;
	protected final long id;

	protected transient Logger logger;

	/*
	 * Java Date class has <code>getTime()</code>, <code>setTime()</code>, which
	 * takes a <code>long</code>, so math can be done. In milliseconds.
	 */
	protected long duration;
	
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
	 * @throws SQLException when there is a problem writing to the database
	 * @throws IOException when there is a problem with serialization
	 */
	public void saveToDatabase() throws SQLException, IOException {
		logger.logp(Level.INFO, "core.Event", "core.Event.saveToDatabase()", "Saving " 
				+ this.name + " to database.");
		
		SQLiteConnector conn = SQLiteConnector.getInstance();
		//conn.saveSerializedEvent(this);
	}
}
