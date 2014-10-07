import java.util.Date;

/**
 * 
 * Represents an event on the calendar
 * 
 */
public class Event extends TimeObject
{
	public Event(String name, Date startTime, long duration)
	{
		super(name);
		this.startTime = startTime;
		this.duration = duration;
	}
}
