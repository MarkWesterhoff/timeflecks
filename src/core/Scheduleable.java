package core;
import java.util.Date;


/**
 * 
 * @author Timeflecks
 * Represents an event or task that can be placed on a calendar
 * 
 */
public interface Scheduleable
{
	
	public String getName();
	public Date getStartTime();
	public Date getEndTime();
	public long getDuration();
}
