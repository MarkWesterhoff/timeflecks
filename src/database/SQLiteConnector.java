package database;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.logging.Level;

import core.Task;
import core.Event;
import logging.GlobalLogger;
import utility.ByteUtility;

/**
 * Represents the types of objects that can be saved to this database.
 */
enum SerializableTypes {
	TASK('t'), EVENT('e');
	
	private final char type;
	SerializableTypes(char t) { this.type = t; }
	public char getValue() { return type; }
}

public final class SQLiteConnector {
	// Load JDBC database connector class 
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static final String SQL_DROP_SERIALIZE_TABLE = 
			"DROP TABLE IF EXISTS 'serialized_time_objects'";
	
	private static final String SQL_CREATE_SERIALIZE_TABLE = 
			"CREATE TABLE IF NOT EXISTS 'serialized_time_objects' ("
			+ "'id' INT(11) PRIMARY KEY NOT NULL,"
			+ "'type' CHAR(1) NOT NULL,"
			+ "'serialized_time_object' BLOB NOT NULL )";
	
	private static final String SQL_CLEAR_SERIALIZE_TABLE = 
			"DELETE FROM 'serialized_time_objects'";
	
	private static final String SQL_INSERT_OR_REPLACE_SERIALIZED_OBJECT = 
			"INSERT OR REPLACE "
			+ "INTO 'serialized_time_objects' (id, type, serialized_time_object) "
			+ "VALUES (?, ?, ?)";
	
	private static final String SQL_SELECT_SERIALIZED_TIMEOBJECT = 
			"SELECT serialized_time_object "
			+ "FROM 'serialized_time_objects' "
			+ "WHERE id = ? "
			+ "AND type = ? "
			+ "LIMIT 1";

	/* SQLiteConnector is a singleton */
	private static SQLiteConnector instance;
	
	public static SQLiteConnector getInstance() throws SQLException {
		if (instance == null) {
			instance = new SQLiteConnector();
		}
		return instance;
	}
	
	private transient Logger logger;
	
	/**
	 * Constructor for SQLiteConnector. Connects to the database and creates
	 * the table for storing TimeObjects. Deletes all rows from the table.
	 *  
	 * @throws SQLException
	 */
	private SQLiteConnector() throws SQLException { 
		this.logger = GlobalLogger.getLogger();
		
		logger.logp(Level.INFO, "SQLiteConnector", "SQLiteConnector", 
				"Constructing SQLiteConnector");
		Connection c = this.getConnection();
		try {
			Statement stmt = c.createStatement();
			stmt.execute(SQL_DROP_SERIALIZE_TABLE);
			stmt.execute(SQL_CREATE_SERIALIZE_TABLE);
			stmt.close();
		}
		finally {
			c.close();
		}
	}
	
	/**
	 * Gets the connection to the SQLite database.
	 * 
	 * @return a Connection object to the SQLite database
	 * @throws SQLException when there is a problem connecting to the SQLite database
	 */
	private Connection getConnection() throws SQLException {
		logger.logp(Level.INFO, "SQLiteConnector", "getConnection", 
				"Establishing conenction with database test.db");
		String connectionString = "jdbc:sqlite:test.db";
		return DriverManager.getConnection(connectionString);
	}
	
	/**
	 * Drops the table for serializing objects, if it exists.
	 * 
	 * @throws SQLException when there is a problem dropping the table
	 */
	private void dropSerializeTable() throws SQLException {
		Connection c = this.getConnection();
		try {
			Statement stmt = c.createStatement();
			stmt.execute(SQL_DROP_SERIALIZE_TABLE);
			stmt.close();
		}
		finally {
			c.close();
		}
	}
	
	/**
	 * Serializes the Task to a byte array and saves it to the database.
	 * 
	 * @param task the Task to serialize and save
	 * @throws SQLException when there is a problem saving to the database
	 * @throws IOException when there is a problem serializing the Task to bytes
	 */
	public void serializeAndSave(Task task) throws SQLException, IOException {
		logger.logp(Level.INFO, "SQLiteConnector", "serializeAndSave(Task)", 
				"Serializing and saving Task \"" + task.getName() + "\"");
		
		this.save(task.getId(), SerializableTypes.TASK.getValue(), 
				ByteUtility.getBytes(task));
	}
	
	/**
	 * Serializes the Event to a byte array and saves it to the database.
	 * 
	 * @param event the Event to serialize and save
	 * @throws SQLException when there is a problem saving to the database
	 * @throws IOException when there is a problem serializing the Event to bytes
	 */
	public void serializeAndSave(Event event) throws SQLException, IOException {
		logger.logp(Level.INFO, "SQLiteConnector", "serializeAndSave(Event)", 
				"Serializing and saving Event \"" + event.getName() + "\"");
		
		this.save(event.getId(), SerializableTypes.EVENT.getValue(), 
				ByteUtility.getBytes(event));
	}
	
	/**
	 * 
	 * @param id the id of the object to save
	 * @param type the type of the object to save
	 * @param serializedObject the string of bytes representing the serialized object
	 * @throws SQLException when there is a problem writing to the database
	 */
	private void save(long id, char type, byte[] serializedObject) throws SQLException {
		logger.logp(Level.INFO, "SQLiteConnector", "save", 
				"Saving object with id = " + Long.toString(id) + " and type = "
				+ Character.toString(type));
		
		Connection c = this.getConnection();
		try {
			PreparedStatement stmt = c.prepareStatement(SQL_INSERT_OR_REPLACE_SERIALIZED_OBJECT);
			
			stmt.setLong(1, id);
			stmt.setString(2, Character.toString(type));
			stmt.setBytes(3, serializedObject);
			
			stmt.executeUpdate();
			stmt.close();
		}
		finally {
			c.close();
		}
	}
	
	/**
	 * Reads and deserializes a Task from the database
	 * 
	 * @param id the id of the serialized Task to get from the database
	 * @return the deserialized Task
	 * @throws SQLException if there is a problem reading from the database
	 * @throws ClassNotFoundException if there is a problem deserializing the object
	 * @throws IOException if there is a problem with the stream of bytes representing
	 * 						the serialized object
	 */
	public Task getSerializedTask(long id) throws SQLException, ClassNotFoundException, IOException {
		logger.logp(Level.INFO, "SQLiteConnector", "getSerializedTask", 
				"Getting and deserializing Task with id " + Long.toString(id));
		
		Connection c = this.getConnection();
		try {
			PreparedStatement stmt = c.prepareStatement(SQL_SELECT_SERIALIZED_TIMEOBJECT);
			stmt.setLong(1, id);
			stmt.setString(2, Character.toString(SerializableTypes.TASK.getValue()));
			ResultSet rs = stmt.executeQuery();
			rs.next();
			
			byte[] buf = rs.getBytes(1);
			ObjectInputStream inStream = null;
			if(buf != null) {
				inStream = new ObjectInputStream(new ByteArrayInputStream(buf));
			}
			
			Object deserializedObject = inStream.readObject();
			
			rs.close();
			stmt.close();
			
			return (Task)deserializedObject; 
		}
		finally {
			c.close();
		}
	}
	
	/**
	 * Reads and deserializes a Task from the database
	 * 
	 * @param id the id of the serialized Task to get from the database
	 * @return the deserialized Task
	 * @throws SQLException if there is a problem reading from the database
	 * @throws ClassNotFoundException if there is a problem deserializing the object
	 * @throws IOException if there is a problem with the stream of bytes representing
	 * 						the serialized object
	 */
	public Event getSerializedEvent(long id) throws SQLException, ClassNotFoundException, IOException {
		logger.logp(Level.INFO, "SQLiteConnector", "getSerializedEvent", 
				"Getting and deserializing Event with id " + Long.toString(id));
		
		Connection c = this.getConnection();
		try {
			PreparedStatement stmt = c.prepareStatement(SQL_SELECT_SERIALIZED_TIMEOBJECT);
			stmt.setLong(1, id);
			stmt.setString(2, Character.toString(SerializableTypes.EVENT.getValue()));
			ResultSet rs = stmt.executeQuery();
			rs.next();
			
			byte[] buf = rs.getBytes(1);
			
			Object deserializedObject = ByteUtility.getObject(buf);
			
			rs.close();
			stmt.close();
			
			return (Event)deserializedObject; 
		}
		finally {
			c.close();
		}
	}
}
