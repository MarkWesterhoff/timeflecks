package core.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import core.Priority;
import core.Task;

public class TaskTest
{

	/**
	 * Test method for task completion.
	 */
	@Test
	public void testCompletion()
	{
		Task test = new Task("Test task");
		assertFalse("Tasks should not start as completed", test.isCompleted());
		test.setCompleted(true);
		assertTrue("setComplete(true) should complete a task",
				test.isCompleted());
		test.setCompleted(false);
		assertFalse("setCompleted(false) uncomplete a complete task",
				test.isCompleted());
	}

	/**
	 * Test method for {@link Task#getPriority()}.
	 */
	@Test
	public void testPriority()
	{

		Task test = new Task("Test task");
		assertTrue("Tasks should be set to default as no priority",
				test.getPriority() == Priority.NO_PRIORITY_SELECTED);
		test.setPriority(Priority.HIGH_PRIORITY);
		assertTrue("Setting tasks to high priority doesn't change them",
				test.getPriority() == Priority.HIGH_PRIORITY);
		Task test2 = new Task("Lower priority task");
		test2.setPriority(Priority.LOW_PRIORITY);
		assertTrue("Setting tasks to low priority doesn't change them",
				test2.getPriority() == Priority.LOW_PRIORITY);
		assertTrue("Higher priority task is greater than lower priority task",
				test.getPriority().getValue() > test2.getPriority().getValue());
		Task test3 = new Task("Middle priority task");
		test3.setPriority(Priority.MEDIUM_PRIORITY);
		assertTrue("Setting tasks to medium priority doesn't change them",
				test3.getPriority() == Priority.MEDIUM_PRIORITY);
		assertTrue("High priority task is greater than medium priority task",
				test.getPriority().getValue() > test3.getPriority().getValue());
		assertTrue("Medium priority task is greater than lower priority task",
				test3.getPriority().getValue() > test2.getPriority().getValue());
	}

	/**
	 * Test method for {@link Task#getTags()}.
	 */
	@Test
	public void testTags()
	{
		Task test = new Task("Test task");
		assertEquals("Tasks should start with no tags", test.getTags().size(),
				0);
		test.addTag("Green");
		assertEquals("Adding a tag should increase the size of the tags", test
				.getTags().size(), 1);
		assertTrue("hasTag() should return whether the task has a tag",
				test.hasTag("Green"));
		assertFalse(
				"hasTag() should not return true when asked about a non-existant tag",
				test.hasTag("Red"));
		test.addTag("Blue");
		assertEquals("Adding a tag should increase the size of the tags", test
				.getTags().size(), 2);
		assertTrue("hasTag() should return whether the task has a tag",
				test.hasTag("Green"));
		assertTrue("hasTag() should return whether the task has a tag",
				test.hasTag("Blue"));
		assertFalse(
				"hasTag() should not return true when asked about a non-existant tag",
				test.hasTag("Red"));
	}

	/**
	 * Test method for {@link Task#getDueDate()}.
	 */
	@Test
	public void testDueDate()
	{
		Task test = new Task("Test task");
		assertEquals(
				"A new task should not start with a due date unless specified",
				test.getDueDate(), null);
		Date currentTime = new Date();
		test.setDueDate(currentTime);
		assertEquals("adding a due date should change it to the value passed in",test.getDueDate(),currentTime);
	}

	@Test
	public void testScheduledTimes()
	{
		Task test = new Task("Test task");
		assertFalse(
				"non-scheduled tasks should not return true in isScheduled()",
				test.isScheduled());
		Date currentDate = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(currentDate);
		c.add(Calendar.MINUTE, 60);
		test.setStartTime(currentDate);
		assertEquals("added start time should equal value passed in",test.getStartTime(),currentDate);
		test.setDuration(60 * 60 * 1000);
		assertEquals("added duration should be set to value passed in",test.getDuration(),60*60*1000);
		assertEquals("End time is calculated incorrectly.", test.getEndTime(),c.getTime());
		assertTrue("scheduled tasks should return true in isScheduled()", test.isScheduled());
	}

}
