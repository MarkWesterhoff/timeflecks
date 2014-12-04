package core.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import core.*;

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
}
