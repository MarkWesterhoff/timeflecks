package core.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import logging.GlobalLogger;

import org.junit.Test;

import core.*;

public class RecurrenceTests
{

	@Test
	public void testRecurrence() {
		int currentTaskNum = Timeflecks.getSharedApplication().getTaskList().getTasks().size();
		Task t = new Task("taskname");
		Date sun = new Date(1415572208000L);  //Sunday, November 9, 2014 @ 5:30
		Calendar c = new GregorianCalendar();
		c.setTime(sun);
		assertEquals("Test date should be a Sunday", 
					c.get(Calendar.DAY_OF_WEEK),Calendar.SUNDAY);
		t.setDueDate(c.getTime());
		c.add(Calendar.DATE, 100);
		GlobalLogger.getLogger().logp(Level.INFO, "test.core.RecurrenceTests", "testRecurrence()"
				, "Testing with end date " + c.getTime().toString());
		
		Recurrence r = new WeeklyRecurrence(t,c.getTime());
		assertEquals("Recurrence should add 15 new tasks for weekly",
				Timeflecks.getSharedApplication().getTaskList().getTasks().size(),15 + currentTaskNum);
		currentTaskNum += 15;
		assertTrue("Last date should be less than recurrence date",
				Timeflecks.getSharedApplication().getTaskList().getTasks()
					.get(currentTaskNum-1).getDueDate().compareTo(c.getTime()) < 1);
		
		Recurrence r2 = new DailyRecurrence(t,c.getTime());
		assertEquals("Recurrence should add 101 new tasks for daily",
				Timeflecks.getSharedApplication().getTaskList().getTasks().size(),101 + currentTaskNum);
		currentTaskNum += 101;
		assertTrue("Last date should be less than recurrence date",
				Timeflecks.getSharedApplication().getTaskList().getTasks()
					.get(currentTaskNum-1).getDueDate().compareTo(c.getTime()) < 1);
		
		Recurrence r3 = new MonthlyRecurrence(t,c.getTime());
		assertEquals("Recurrence should add 4 new tasks for Monthly",
				Timeflecks.getSharedApplication().getTaskList().getTasks().size(),4 + currentTaskNum);
		currentTaskNum += 4;
		assertTrue("Last date should be less than recurrence date",
				Timeflecks.getSharedApplication().getTaskList().getTasks()
					.get(currentTaskNum-1).getDueDate().compareTo(c.getTime()) < 1);
		
		Recurrence r4 = new WeekDayRecurrence(t,c.getTime());
		assertEquals("Recurrence should add 72 new tasks for WeekDays",
				Timeflecks.getSharedApplication().getTaskList().getTasks().size() - currentTaskNum, 72);
		currentTaskNum += 72;
		assertTrue("Last date should be less than recurrence date",
				Timeflecks.getSharedApplication().getTaskList().getTasks()
					.get(currentTaskNum-1).getDueDate().compareTo(c.getTime()) < 1);
		
	}
}
