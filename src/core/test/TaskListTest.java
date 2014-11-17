package core.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import core.Event;
import core.Priority;
import core.Task;
import core.TaskList;

public class TaskListTest
{

	@Test
	public void testTaskListModification()
	{
		TaskList tl = new TaskList();
		assertEquals("taskList should start with no elements.", tl.getTasks()
				.size(), 0);
		Task t = new Task("My first task.");
		tl.addTask(t);
		assertEquals("adding a task should add an element.", tl.getTasks()
				.size(), 1);
		assertEquals(
				"The element added to a tasklist should be the added element",
				tl.getTasks().get(0), t);
		Task t2 = new Task("My second task.");
		tl.addTask(t2);
		assertEquals("adding a task should add an element.", tl.getTasks()
				.size(), 2);
		assertEquals(
				"The element added to a tasklist should be the added element",
				tl.getTasks().get(1), t2);
		assertEquals(
				"Previous tasks should not be disrupted on the adding of a new task.",
				tl.getTasks().get(0), t);
	}

	@Test
	public void testEventListModification()
	{
		TaskList tl = new TaskList();
		assertEquals("taskList should start with no elements.", tl.getEvents()
				.size(), 0);
		Event e = new Event("My first event.", new Date(), new Date());
		tl.addEvent(e);
		assertEquals("adding a task should add an element.", tl.getEvents()
				.size(), 1);
		assertEquals(
				"The element added to an eventlist should be the added element",
				tl.getEvents().get(0), e);
		Event e2 = new Event("My second event.", new Date(), new Date());
		tl.addEvent(e2);
		assertEquals("adding an event should add an element.", tl.getEvents()
				.size(), 2);
		assertEquals(
				"The element added to an eventlist should be the added element",
				tl.getEvents().get(1), e2);
		assertEquals(
				"Previous events should not be disrupted on the adding of a new event.",
				tl.getEvents().get(0), e);
	}

	@Test
	public void testTaskSorting()
	{
		TaskList tl = new TaskList();
		Task t1 = new Task("DEF");
		Task t2 = new Task("ABC");
		Task t3 = new Task("GHI");
		t3.setPriority(Priority.MEDIUM_PRIORITY);
		t2.setPriority(Priority.HIGH_PRIORITY);
		t1.setPriority(Priority.LOW_PRIORITY);
		Date d = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		t3.setDueDate(c.getTime());
		c.add(Calendar.MINUTE, 23);
		t1.setDueDate(c.getTime());
		c.add(Calendar.SECOND, 546);
		t2.setDueDate(c.getTime());

		tl.addTask(t1);
		tl.addTask(t2);
		tl.addTask(t3);

		assertEquals("task list should add in manual order",
				tl.getTasks().get(0), t1);
		assertEquals("task list should add in manual order",
				tl.getTasks().get(1), t2);
		assertEquals("task list should add in manual order",
				tl.getTasks().get(2), t3);
		tl.sort();
		assertEquals("task list should start with manual sort", tl.getTasks()
				.get(0), t1);
		assertEquals("task list should start with manual sort", tl.getTasks()
				.get(1), t2);
		assertEquals("task list should start with manual sort", tl.getTasks()
				.get(2), t3);
		tl.setTaskComparator(Task.nameComparator);
		tl.sort();
		assertEquals("Name comparator should alphabatize tasks", tl.getTasks()
				.get(0), t2);
		assertEquals("Name comparator should alphabatize tasks", tl.getTasks()
				.get(1), t1);
		assertEquals("Name comparator should alphabatize tasks", tl.getTasks()
				.get(2), t3);
		tl.setTaskComparator(Task.manualComparator);
		tl.sort();
		assertEquals(
				"sorting with manual sort should return tasks to original order",
				tl.getTasks().get(0), t1);
		assertEquals(
				"sorting with manual sort should return tasks to original order",
				tl.getTasks().get(1), t2);
		assertEquals(
				"sorting with manual sort should return tasks to original order",
				tl.getTasks().get(2), t3);
		tl.setTaskComparator(Task.priorityComparator);
		tl.sort();
		assertEquals(
				"Tasks should be sorted with highest priority tasks first.", tl
						.getTasks().get(0), t2);
		assertEquals(
				"Tasks should be sorted with highest priority tasks first.", tl
						.getTasks().get(1), t3);
		assertEquals(
				"Tasks should be sorted with highest priority tasks first.", tl
						.getTasks().get(2), t1);
		tl.setTaskComparator(Task.dueDateComparator);
		tl.sort();
		assertEquals("Tasks should be sorted with soonest due date first.", tl
				.getTasks().get(0), t3);
		assertEquals("Tasks should be sorted with soonest due date first.", tl
				.getTasks().get(1), t1);
		assertEquals("Tasks should be sorted with soonest due date first.", tl
				.getTasks().get(2), t2);
	}

	@Test
	public void testSaveAllTasks()
	{
		// fail("Not yet implemented");
	}

}
