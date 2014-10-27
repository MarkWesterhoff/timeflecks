package core.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import core.Event;
import core.Task;

public class EventTest
{

	@Test
	public void testScheduledTimes()
	{
		Date currentDate = new Date();
		Event test = new Event("Test event",currentDate,60*1000);
		assertEquals("added start time should equal value passed in",test.getStartTime(),currentDate);
		assertEquals("added duration should be set to value passed in",test.getDuration(),60*1000);

		Calendar c = new GregorianCalendar();
		c.setTime(currentDate);
		c.add(Calendar.SECOND, 60);
		assertEquals("End time is calculated incorrectly.", test.getEndTime(),c.getTime());
		test.setStartTime(c.getTime());
		assertEquals("Changing the start time correctly changes the values",test.getStartTime(),c.getTime());
		c.add(Calendar.SECOND, 60);
		assertEquals("Changing the start time keeps the end time correctly changed",test.getEndTime(),c.getTime());
		
		test.setDuration(60 * 60 * 1000);
		assertEquals("added duration should be set to value passed in",test.getDuration(),60*60*1000);
		c.add(Calendar.MINUTE, 59);
		assertEquals("End time is calculated incorrectly.", test.getEndTime(),c.getTime());
	}


}
