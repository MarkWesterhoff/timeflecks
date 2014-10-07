import java.util.Date;


public abstract class TimeObject {
	protected String name;
	protected Date startTime;
	protected String description;
	protected int id;
	private static int taskCount = 0;
	/*
	 * Basic constructor, can support other input later (the rest are optional)
	 */
	public TimeObject(String name) {
		id = taskCount;
		taskCount++;
		this.name = name;
	}
	/**
	 * Java Date class has <code>getTime()</code>, <code>setTime()</code>, 
	 * which takes a <code>long</code>, so math can be done.
	 * In milliseconds.
	 */
	protected long duration; 
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return new Date(startTime.getTime() + duration);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
}