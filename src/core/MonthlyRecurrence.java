package core;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MonthlyRecurrence extends Recurrence
{

	public MonthlyRecurrence(Task protoTask, Date toDate)
	{
		super(protoTask, toDate);
	}

	public Date getNextTime(Date fromDate)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(fromDate);
		c.add(Calendar.MONTH, 1);
		return c.getTime();
	}

}
