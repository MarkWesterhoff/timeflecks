package core;

import java.util.Date;
import java.util.Objects;

/**
 * Class to represent the block of time that a current Scheduleable holds.
 * @author Andrew
 *
 */
public class PlaceholderScheduleable implements Scheduleable
{
	private String name;
	private Date startTime;
	private Date endTime;
	
	public PlaceholderScheduleable(String name, Date startTime, Date endTime) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(startTime);
		Objects.requireNonNull(endTime);
		
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public Date getStartTime()
	{
		return this.startTime;
	}

	@Override
	public Date getEndTime()
	{
		return this.endTime;
	}

	@Override
	public long getDuration()
	{
		return this.endTime.getTime() - this.startTime.getTime();
	}

	@Override
	public boolean isScheduled()
	{
		return false;
	}

	@Override
	public boolean isCompleted()
	{
		return false;
	}

}
