package core;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WeekDayRecurrence extends Recurrence
{

	public WeekDayRecurrence(Task protoTask, Date toDate)
	{
		//check to make sure we're passed a weekday
		super(WeekDayRecurrence.getWeekDayTask(protoTask),toDate);
	}
	
	private static Task getWeekDayTask(Task t) {
		Calendar c = new GregorianCalendar();
		c.setTime(t.getDueDate());
		switch(c.get(Calendar.DAY_OF_WEEK)) {
			case(Calendar.SATURDAY):
				c.add(Calendar.DATE, 2);
				break;
			case(Calendar.SUNDAY):
				c.add(Calendar.DATE, 1);
				break;
		}
		t.setDueDate(c.getTime());
		return t;
	}

	public Date getNextTime(Date fromDate)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(fromDate);
		switch(c.get(Calendar.DAY_OF_WEEK)) {
		case(Calendar.FRIDAY):
			c.add(Calendar.DATE,3);
			break;
		case(Calendar.SATURDAY): //Shouldn't occur...
			c.add(Calendar.DATE,2);
			break;
		default:
			c.add(Calendar.DATE,1);
		}
		return c.getTime();
	}
}