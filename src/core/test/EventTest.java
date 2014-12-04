package core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import core.Event;

public class EventTest
{

	@Test
	public void testScheduledTimes()
	{
		Date currentDate = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(currentDate);
		c.add(Calendar.SECOND, 60);
		Event test = new Event("Test event", currentDate, c.getTime());
		assertEquals("added start time should equal value passed in",
				test.getStartTime(), currentDate);
		assertEquals("added duration is calculated incorrectly",
				test.getDuration(), 60 * 1000);

		assertEquals("End time is calculated incorrectly.", test.getEndTime(),
				c.getTime());
		test.setStartTime(c.getTime());
		assertEquals("Changing the start time correctly changes the values",
				test.getStartTime(), c.getTime());
		assertEquals(
				"Changing the start time keeps the end time correctly changed",
				test.getEndTime(), c.getTime());

		c.add(Calendar.MINUTE, 59);
		test.setEndTime(c.getTime());
		assertEquals("added duration should be set to value passed in",
				test.getDuration(), 60 * 59 * 1000);
		assertEquals("End time is calculated incorrectly.", test.getEndTime(),
				c.getTime());
	}

}
