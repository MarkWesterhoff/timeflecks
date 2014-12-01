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
import core.Timeflecks;
import database.SQLiteConnector;

public class SQLiteConnectorTest {
	
	/**
	 * Tests the constructor of the SQLiteConnector.
	 *
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testConstructor() throws SQLException, IOException, ClassNotFoundException {
		// Make new clean database
		new File("testConstructor.db").delete();
		Timeflecks.getSharedApplication().setDBConnector(new SQLiteConnector(new File("testConstructor.db"), true));
		
		ArrayList<File> files = new ArrayList<File>(); 
		files.add(new File("testSwitchDatabase1.db"));
		files.add(new File("testSwitchDatabase2.db"));
		files.add(new File("testSwitchDatabase3.db"));
		
		// Positive tests
		new SQLiteConnector(files.get(0), true).serializeAndSave(new Task("task1"));
		new SQLiteConnector(files.get(1), true).serializeAndSave(new Task("task2"));
		
		Task task = new Task("task3");
		SQLiteConnector conn = new SQLiteConnector(files.get(2), true);
		conn.serializeAndSave(task);
		
		Task returnTask = conn.getSerializedTask(task.getId());
		assertEquals("Names should match", task.getName(), returnTask.getName());
		
		
		// Negative tests
		try {
			new SQLiteConnector(new File("test1.notdb"), true);
			fail("Expected IllegalArgumentException because "
					+ "of wrong extension");
		}
		catch (IllegalArgumentException i){
			// expected
		}
		
		try {
			new SQLiteConnector(new File(""), true);
			fail("Expected IllegalArgumentException because "
					+ "of no extension");
		}
		catch (IllegalArgumentException i){
			// expected
		}
		
		try {
			new SQLiteConnector(new File("asdf"), true);
			fail("Expected IllegalArgumentException because "
					+ "of wrong extension");
		}
		catch (IllegalArgumentException i){
			// expected
		}
		
		for (File f : files) {
			Files.delete(f.toPath());
		}
		
		// Clean up
		Timeflecks.getSharedApplication().getMainWindow().dispose();
		new File("testConstructor.db").delete();
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
		// Make new clean database
		new File("testSaveAndLoad.db").delete();
		Timeflecks.getSharedApplication().setDBConnector(new SQLiteConnector(new File("testSaveAndLoad.db"), true));
		
		Task task = new Task("task1");
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(task);
		
		Event event = new Event("event1", new Date(), new Date());
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(event);
		
		// Positive tests for Task
		Task returnTask = Timeflecks.getSharedApplication().getDBConnector().getSerializedTask(task.getId());
		
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
		Event returnEvent = (Event)Timeflecks.getSharedApplication().getDBConnector().getSerializedEvent(event.getId());
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
		
		// Clean up
		Timeflecks.getSharedApplication().getMainWindow().dispose();
		new File("testSaveAndLoad.db").delete();
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
		// Make new clean database
		new File("testDelete.db").delete();
		Timeflecks.getSharedApplication().setDBConnector(new SQLiteConnector(new File("testDelete.db"), true));
		
		Task task = new Task("task1");
		Task task2 = new Task("task2");
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(task);
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(task2);
		
		// Attempt to delete
		Timeflecks.getSharedApplication().getDBConnector().delete(task.getId());
		
		// Try to get still existing task back
		Timeflecks.getSharedApplication().getDBConnector().getSerializedTask(task2.getId());
		
		try {
			Timeflecks.getSharedApplication().getDBConnector().getSerializedTask(task.getId());
			fail("Getting a deleted task should not succeed");
		}
		catch (SQLException s) {
			// expected
		}
		
		// Clean up
		Timeflecks.getSharedApplication().getMainWindow().dispose();
		new File("testDelete.db").delete();
	}
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Test
	public void testGetHighestId() throws SQLException, IOException {
		// Clear the db before testing
		new File("testGetHighestId.db").delete();
		Timeflecks.getSharedApplication().setDBConnector(new SQLiteConnector(new File("testGetHighestId.db"), true));
		
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(new Task("t1a"));
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(new Task("t1b"));
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(new Task("t1c"));
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(new Event("e1a", new Date(), new Date()));
		
		
		Task t2 = new Task("t2");
		Timeflecks.getSharedApplication().getDBConnector().serializeAndSave(t2);
		
		long maxId = Timeflecks.getSharedApplication().getDBConnector().getHighestID();
		assertEquals("Max ID should be equal to highestID", t2.getId(), maxId);
		
		// Clean up
		Timeflecks.getSharedApplication().getMainWindow().dispose();
		new File("testGetHighestId.db").delete();
	}
	
	/**
	 * Stress test to test database saving speed.
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Ignore
	@Test
	public void stressTestSerialization() throws SQLException, IOException, ClassNotFoundException {
		// Make new database
		new File("stressTestSerialization.db").delete();
		Timeflecks.getSharedApplication().openDatabaseFile(
				new File("stressTestSerialization.db"));
		
		//Initialize everything
		new Task("asdf").saveToDatabase();
		
		int numTasks = 1000;
		
		for(int i = 0; i < numTasks; ++i) {
			Task t = new Task("task" + Integer.toString(i));
			t.setDescription("Description of the task.");
			t.setDueDate(new Date());
			t.addTag("tag1");
			t.addTag("tag2");
			t.addTag("tag3");
			t.addTag("tag4");
			t.setStartTime(new Date());
			Timeflecks.getSharedApplication().getTaskList().addTask(t);
			
			Event e = new Event("event" + Integer.toString(i), new Date(), new Date());
			e.setDescription("Description of the event.");
			Timeflecks.getSharedApplication().getTaskList().addEvent(e);
		}
		
		// Time saving of all tasks
		long time = System.currentTimeMillis();
		Timeflecks.getSharedApplication().getTaskList().saveAllTasksAndEvents();
		time = System.currentTimeMillis() - time;
		
		// Time retrieving of all tasks
		long time2 = System.currentTimeMillis();
		Timeflecks.getSharedApplication()
				.getDBConnector().getAllTasks();
		Timeflecks.getSharedApplication()
				.getDBConnector().getAllEvents();
		time2 = System.currentTimeMillis() - time2;
		
		System.out.println("Time to save all Objects: " 
					+ Float.toString((float)time/1000));
		System.out.println("Objects saved per second: " 
					+ Float.toString(numTasks*2/((float)time/1000)));
		
		System.out.println("Time to retrieve all Objects: " 
					+ Float.toString((float)time2/1000));
		System.out.println("Objects retreived per second: " 
					+ Float.toString(numTasks*2/((float)time2/1000)));
		
		// Clean up and delete file
		Timeflecks.getSharedApplication().getMainWindow().dispose();
		new File("stressTestSerialization.db").delete();
	}
}
