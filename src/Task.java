import java.util.Date;


public class Task extends TimeObject {
	private boolean completed;
	private Date dueDate;
	private int priority;
	
	public boolean isScheduled() {
		return !(startTime == null);
	}
	public boolean isCompleted() {
		return completed;
	}
	public void complete() {
		this.completed = true;
	}
	public void uncomplete() {
		this.completed = false;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
}
