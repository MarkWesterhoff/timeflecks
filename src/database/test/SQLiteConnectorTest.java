package database.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;

import core.Event;
import core.Task;
import database.SQLiteConnector;
import logging.GlobalLogger;

public class SQLiteConnectorTest {
	
	@Test
	public void testConstructor() throws SQLException {
		// Positive tests
		SQLiteConnector.getInstance();
	}
	
	@Test
	public void testSaveAndLoad() throws SQLException, IOException, ClassNotFoundException {
		SQLiteConnector conn = SQLiteConnector.getInstance();
		
		Task task = new Task("task1");
		conn.saveSerializedTimeObject(t);
		
		Event event = new Event("event1", new Date(), 1);
		conn.saveSerializedTimeObject(e);
		
		// Positive tests for Task
		Task returnTask = conn.getSerializedTimeObject(t.getId());
		
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
		Event returnEvent = (Event)conn.getSerializedTimeObject(e.getId());
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
}
