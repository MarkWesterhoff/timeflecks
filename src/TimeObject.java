import java.util.Date;

/**
 * 
 * Represents either an Event or a Task
 * 
 */
public abstract class TimeObject
{
	/* Static variable and method to deal with giving each TimeObject a unique
	 * id.
	 */
	private static long taskCount = 0;
	public static long getNextId() {
		if (taskCount == Long.MAX_VALUE) {
			// can we recover from this?
			throw new RuntimeException("Exceeded maximum number of tasks/events");
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
	

	/*
	 * Basic constructor, can support other input later (the rest are optional)
	 */
	public TimeObject(String name)
	{
		id = TimeObject.getNextId();
		this.name = name;
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
}