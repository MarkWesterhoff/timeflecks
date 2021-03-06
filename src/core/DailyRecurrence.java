package core;

import java.util.*;

public class DailyRecurrence extends Recurrence
{
	public DailyRecurrence(Task protoTask, Date toDate)
	{
		super(protoTask, toDate);
	}

	public Date getNextTime(Date fromDate)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

}
