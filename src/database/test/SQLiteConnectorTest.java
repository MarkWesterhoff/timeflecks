package database.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
	 * @throws IOException 
	 */
	@Test
	public void testConstructor() throws SQLException, IOException {
		// Positive tests
		//TODO: SQLiteConnector.getInstance();
	}
	
	/**
	 * Tests switchDatabase functionality to set up a new DB.
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testSwitchDatabase() throws SQLException, IOException, ClassNotFoundException {
		//TODO: fix testSwitchDatabase()
		/* 
		ArrayList<File> files = new ArrayList<File>();
		 
		files.add(new File("testSwitchDatabase1.db"));
		files.add(new File("testSwitchDatabase2.db"));
		files.add(new File("testSwitchDatabase3.db"));
		
		// Positive tests
		SQLiteConnector.switchDatabase(files.get(0));
		SQLiteConnector.getInstance().serializeAndSave(new Task("task1"));
		SQLiteConnector.switchDatabase(files.get(1));
		SQLiteConnector.getInstance().serializeAndSave(new Task("task2"));
		SQLiteConnector.switchDatabase(files.get(2));
		Task task = new Task("task3");
		SQLiteConnector.getInstance().serializeAndSave(task);
		
		Task returnTask = SQLiteConnector.getInstance().getSerializedTask(task.getId());
		assertEquals("Names should match", task.getName(), returnTask.getName());
		
		
		// Negative tests
		try {
			SQLiteConnector.switchDatabase(new File("test1.notdb"));
			fail("Expected IllegalArgumentException because "
					+ "of wrong extension");
		}
		catch (IllegalArgumentException i){
			// expected
		}
		
		try {
			SQLiteConnector.switchDatabase(new File(""));
			fail("Expected IllegalArgumentException because "
					+ "of no extension");
		}
		catch (IllegalArgumentException i){
			// expected
		}
		
		try {
			SQLiteConnector.switchDatabase(new File("asdf"));
			fail("Expected IllegalArgumentException because "
					+ "of wrong extension");
		}
		catch (IllegalArgumentException i){
			// expected
		}
		
		for (File f : files) {
			Files.delete(f.toPath());
		}
		*/
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
		//TODO: fix testSaveAndLoad()
		/*
		SQLiteConnector.switchDatabase(new File("testSaveAndLoad.db"));
		
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
		*/
		
	}
	
	/**
	 * Tests functionality of the SQLiteConnector.delete method.
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void testDelete() throws SQLException, IOException, ClassNotFoundException {
		//TODO: testDelete
		/*
		SQLiteConnector.switchDatabase(new File("testDelete.db"));
		
		
		Task task = new Task("task1");
		Task task2 = new Task("task2");
		SQLiteConnector.getInstance().serializeAndSave(task);
		SQLiteConnector.getInstance().serializeAndSave(task2);
		
		// Attempt to delete
		SQLiteConnector.getInstance().delete(task.getId());
		
		// Try to get still existing task back
		SQLiteConnector.getInstance().getSerializedTask(task2.getId());
		
		try {
			SQLiteConnector.getInstance().getSerializedTask(task.getId());
			fail("Getting a deleted task should not succeed");
		}
		catch (SQLException s) {
			// expected
		}
		*/
	}
	
	/**
	 * Stress test to test database saving speed.
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Ignore
	@Test
	public void stressTestSerialization() throws SQLException, IOException {
		//TODO: fix stressTestSerialization()
		/*
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
		*/
	}
}
