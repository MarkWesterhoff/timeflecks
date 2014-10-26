package database.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import core.Event;
import core.Task;
import core.TaskList;
import database.SQLiteConnector;

public class SQLiteConnectorTest {
	
	/**
	 * Tests the constructor of the SQLiteConnector.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testConstructor() throws SQLException {
		// Positive tests
		SQLiteConnector.getInstance();
	}
	
	/**
	 * Tests serializeAndSave(), getSerializedTask(), and getSerializedEvent()
	 * to ensure that Tasks and Events may be saved and fetched with the same
	 * properties. 
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void testSaveAndLoad() throws SQLException, IOException, ClassNotFoundException {
		SQLiteConnector conn = SQLiteConnector.getInstance();
		
		Task task = new Task("task1");
		conn.serializeAndSave(task);
		
		Event event = new Event("event1", new Date(), 1);
		conn.serializeAndSave(event);
		
		// Positive tests for Task
		Task returnTask = conn.getSerializedTask(task.getId());
		
		assertEquals("Descriptions should match", task.getDescription(), 
				returnTask.getDescription());
		assertEquals("Due dates should match", task.getDueDate(), 
				returnTask.getDueDate());
		assertEquals("Durations should match", task.getDuration(), 
				returnTask.getDuration());
		assertEquals("End times should match", task.getEndTime(), 
				returnTask.getEndTime());
		assertEquals("Ids should match", task.getId(), returnTask.getId());
		assertEquals("Names should match", task.getName(), returnTask.getName());
		assertEquals("Priority should match", task.getPriority(), 
				returnTask.getPriority());
		assertEquals("Start times should match", task.getStartTime(), 
				returnTask.getStartTime());
		assertEquals("Tags should match", task.getTags(), returnTask.getTags());
		
		// Positive tests for Event
		Event returnEvent = (Event)conn.getSerializedEvent(event.getId());
		assertEquals("Descriptions should match", event.getDescription(), 
				returnEvent.getDescription());
		assertEquals("Duration should match", event.getDuration(), 
				returnEvent.getDuration());
		assertEquals("End times should match", event.getEndTime(),
				returnEvent.getEndTime());
		assertEquals("Ids should match", event.getId(), returnEvent.getId());
		assertEquals("Names should match", event.getName(), 
				returnEvent.getName());
		assertEquals("Start times should match", event.getStartTime(), 
				returnEvent.getStartTime());
		
		// Negative tests
		
	}
	
	/**
	 * Stress test to test database saving speed.
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Ignore
	@Test
	public void stressTestSerialization() throws SQLException, IOException {
		int numTasks = 100;
		
		TaskList taskList = TaskList.getInstance();
		for(int i = 0; i < numTasks/2; ++i) {
			Task t = new Task("task" + Integer.toString(i));
			t.setDescription("Description of the task.");
			t.setDueDate(new Date());
			t.addTag("tag1");
			t.addTag("tag2");
			t.addTag("tag3");
			t.addTag("tag4");
			t.setStartTime(new Date());
			taskList.addTask(t);
		}
		
		for(int i = 0; i < numTasks/2; ++i) {
			Event e = new Event("event" + Integer.toString(i), new Date(), 100);
			e.setDescription("Description of the event.");
			taskList.addEvent(e);
		}
		
		// Time saving of all tasks
		long time = System.currentTimeMillis();
		
		taskList.saveAllTasksAndEvents();
		
		time = System.currentTimeMillis() - time;
		System.out.println("Time to save all Objects: " 
					+ Float.toString((float)time/1000));
		System.out.println("Objects saved per second: " 
					+ Float.toString(numTasks/((float)time/1000)));
	}
}
