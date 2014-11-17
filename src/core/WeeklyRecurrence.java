package core;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WeeklyRecurrence extends Recurrence
{

	public WeeklyRecurrence(Task protoTask, Date toDate)
	{
		super(protoTask, toDate);
	}

	public Date getNextTime(Date fromDate)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 7);
		return c.getTime();
	}
}