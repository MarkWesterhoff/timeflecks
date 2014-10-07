import java.util.Date;

public class Event extends TimeObject {
	public Event(String name, Date startTime, long duration){
		super(name);
		this.startTime = startTime;
		this.duration = duration;
	}
}
