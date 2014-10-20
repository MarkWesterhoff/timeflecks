package core;

import java.util.Date;

/**
 * 
 * Represents an event on the calendar
 * 
 */
public class Event extends TimeObject
{
	private static final long serialVersionUID = 1L;

	public Event(String name, Date startTime, long duration)
	{
		super(name);
		this.startTime = startTime;
		this.duration = duration;
	}
}
