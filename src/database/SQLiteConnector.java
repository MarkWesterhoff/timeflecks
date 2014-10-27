package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

import core.Task;
import core.Event;
import logging.GlobalLogger;
import utility.ByteUtility;
import utility.FileUtility;

/**
 * Represents the types of objects that can be saved to this database.
 */
enum SerializableTypes
{
	TASK('t'), EVENT('e');

	private final char type;

	SerializableTypes(char t)
	{
		this.type = t;
	}

	public char getValue()
	{
		return type;
	}
}

public final class SQLiteConnector
{
	// Load JDBC database connector class
	static
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static final String SQL_DROP_SERIALIZE_TABLE = "DROP TABLE IF EXISTS 'serialized_time_objects'";

	private static final String SQL_CREATE_SERIALIZE_TABLE = "CREATE TABLE IF NOT EXISTS 'serialized_time_objects' ("
			+ "'id' INT(11) PRIMARY KEY NOT NULL,"
			+ "'type' CHAR(1) NOT NULL,"
			+ "'serialized_time_object' BLOB NOT NULL )";

	private static final String SQL_CLEAR_SERIALIZE_TABLE = "DELETE FROM 'serialized_time_objects'";

	private static final String SQL_INSERT_OR_REPLACE_SERIALIZED_OBJECT = "INSERT OR REPLACE "
			+ "INTO 'serialized_time_objects' (id, type, serialized_time_object) "
			+ "VALUES (?, ?, ?)";

	private static final String SQL_SELECT_SERIALIZED_TIMEOBJECT = "SELECT serialized_time_object "
			+ "FROM 'serialized_time_objects' "
			+ "WHERE id = ? "
			+ "AND type = ? " + "LIMIT 1";

	private static final String SQL_DELETE_SERIALIZED_TIMEOBJECT = "DELETE FROM serialized_time_objects "
			+ "WHERE id = ?";

	private static final String SQL_GET_ALL_OBJECTS = "SELECT serialized_time_object "
			+ "FROM 'serialized_time_objects'" + "WHERE type = ?";

	private static final String SQL_GET_HIGHEST_ID = "SELECT MAX(id) "
			+ "FROM 'serialized_time_objects' ";

	private static final String defaultDatabasePath = "calendar1.db";
	private static String databasePath = defaultDatabasePath;

	/**
	 * Constructor for SQLiteConnector. Calls other constructor with default
	 * save file location.
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public SQLiteConnector() throws SQLException, IOException
	{
		this(new File(defaultDatabasePath), false);
	}

	/**
	 * Constructor for SQLiteConnector. Connects to the database and creates the
	 * table for storing TimeObjects. Deletes all rows from the table.
	 * 
	 * @param databaseFile the database file to save to
	 * @param clearDatabase true if SQLiteConnector should clear the database
	 * @throws SQLException
	 * @throws IOException
	 */
	public SQLiteConnector(File databaseFile, boolean clearDatabase)
			throws SQLException, IOException
	{
		GlobalLogger.getLogger().logp(
				Level.INFO,
				"SQLiteConnector",
				"SQLiteConnector",
				"Constructing SQLiteConnector to DB: "
						+ databaseFile.getCanonicalPath());

		// Check to make sure it's a .db file
		if (databaseFile == null
				|| !FileUtility.getFileExtension(databaseFile).equals(".db"))
		{
			throw new IllegalArgumentException("newDatabasePath");
		}

		databasePath = databaseFile.getCanonicalPath();

		// Initialize database
		Connection c = this.getConnection();
		try
		{
			Statement stmt = c.createStatement();
			if (clearDatabase)
			{
				GlobalLogger.getLogger().logp(
						Level.INFO,
						"SQLiteConnector",
						"SQLiteConnector()",
						"Dropping serialized_objects table");
				stmt.execute(SQL_DROP_SERIALIZE_TABLE);
			}

			GlobalLogger.getLogger().logp(
					Level.INFO,
					"SQLiteConnector",
					"SQLiteConnector()",
					"Creating serialized_objects table");
			stmt.execute(SQL_CREATE_SERIALIZE_TABLE);
			stmt.close();
		}
		finally
		{
			c.close();
		}
	}

	/**
	 * Gets the connection to the SQLite database.
	 * 
	 * @return a Connection object to the SQLite database
	 * @throws SQLException
	 *             when there is a problem connecting to the SQLite database
	 */
	private Connection getConnection() throws SQLException
	{
		GlobalLogger.getLogger().logp(Level.INFO, "SQLiteConnector", "getConnection",
				"Establishing conenction with database " + databasePath);
		String connectionString = "jdbc:sqlite:" + databasePath;
		return DriverManager.getConnection(connectionString);
	}

	/**
	 * Drops the table for serializing objects, if it exists.
	 * 
	 * @throws SQLException
	 *             when there is a problem dropping the table
	 */
	private void dropSerializeTable() throws SQLException
	{
		Connection c = this.getConnection();
		try
		{
			Statement stmt = c.createStatement();
			stmt.execute(SQL_DROP_SERIALIZE_TABLE);
			stmt.close();
		}
		finally
		{
			c.close();
		}
	}

	/**
	 * Serializes the Task to a byte array and saves it to the database.
	 * 
	 * @param task
	 *            the Task to serialize and save
	 * @throws SQLException
	 *             when there is a problem saving to the database
	 * @throws IOException
	 *             when there is a problem serializing the Task to bytes
	 */
	public void serializeAndSave(Task task) throws SQLException, IOException
	{
		Objects.requireNonNull(task);

		GlobalLogger.getLogger().logp(Level.INFO, "SQLiteConnector", "serializeAndSave(Task)",
				"Serializing and saving Task \"" + task.getName() + "\"");

		this.save(task.getId(), SerializableTypes.TASK.getValue(),
				ByteUtility.getBytes(task));
	}

	/**
	 * Serializes the Event to a byte array and saves it to the database.
	 * 
	 * @param event
	 *            the Event to serialize and save
	 * @throws SQLException
	 *             when there is a problem saving to the database
	 * @throws IOException
	 *             when there is a problem serializing the Event to bytes
	 */
	public void serializeAndSave(Event event) throws SQLException, IOException
	{
		Objects.requireNonNull(event);

		GlobalLogger.getLogger().logp(Level.INFO, "SQLiteConnector", "serializeAndSave(Event)",
				"Serializing and saving Event \"" + event.getName() + "\"");

		this.save(event.getId(), SerializableTypes.EVENT.getValue(),
				ByteUtility.getBytes(event));
	}

	/**
	 * 
	 * @param id
	 *            the id of the object to save
	 * @param type
	 *            the type of the object to save
	 * @param serializedObject
	 *            the string of bytes representing the serialized object
	 * @throws SQLException
	 *             when there is a problem writing to the database
	 */
	private void save(long id, char type, byte[] serializedObject)
			throws SQLException
	{
		Objects.requireNonNull(serializedObject);

		GlobalLogger.getLogger().logp(Level.INFO, "SQLiteConnector", "save",
				"Saving object with id = " + Long.toString(id) + " and type = "
						+ Character.toString(type));

		Connection c = this.getConnection();
		try
		{
			PreparedStatement stmt = c
					.prepareStatement(SQL_INSERT_OR_REPLACE_SERIALIZED_OBJECT);

			stmt.setLong(1, id);
			stmt.setString(2, Character.toString(type));
			stmt.setBytes(3, serializedObject);

			stmt.executeUpdate();
			stmt.close();
		}
		finally
		{
			c.close();
		}
	}

	/**
	 * Reads and deserializes a Task from the database
	 * 
	 * @param id
	 *            the id of the serialized Task to get from the database
	 * @return the deserialized Task
	 * @throws SQLException
	 *             if there is a problem reading from the database
	 * @throws ClassNotFoundException
	 *             if there is a problem deserializing the object
	 * @throws IOException
	 *             if there is a problem with the stream of bytes representing
	 *             the serialized object
	 */
	public Task getSerializedTask(long id) throws SQLException,
			ClassNotFoundException, IOException
	{
		GlobalLogger.getLogger().logp(Level.INFO, "SQLiteConnector", "getSerializedTask",
				"Getting and deserializing Task with id " + Long.toString(id));

		Connection c = this.getConnection();
		try
		{
			PreparedStatement stmt = c
					.prepareStatement(SQL_SELECT_SERIALIZED_TIMEOBJECT);
			stmt.setLong(1, id);
			stmt.setString(2,
					Character.toString(SerializableTypes.TASK.getValue()));
			ResultSet rs = stmt.executeQuery();
			rs.next();

			byte[] buf = rs.getBytes(1);
			Object deserializedObject = ByteUtility.getObject(buf);

			rs.close();
			stmt.close();

			return (Task) deserializedObject;
		}
		finally
		{
			c.close();
		}
	}

	/**
	 * Reads and deserializes a Task from the database
	 * 
	 * @param id
	 *            the id of the serialized Task to get from the database
	 * @return the deserialized Task
	 * @throws SQLException
	 *             if there is a problem reading from the database
	 * @throws ClassNotFoundException
	 *             if there is a problem deserializing the object
	 * @throws IOException
	 *             if there is a problem with the stream of bytes representing
	 *             the serialized object
	 */
	public Event getSerializedEvent(long id) throws SQLException,
			ClassNotFoundException, IOException
	{
		GlobalLogger.getLogger().logp(Level.INFO, "SQLiteConnector", "getSerializedEvent",
				"Getting and deserializing Event with id " + Long.toString(id));

		Connection c = this.getConnection();
		try
		{
			PreparedStatement stmt = c
					.prepareStatement(SQL_SELECT_SERIALIZED_TIMEOBJECT);
			stmt.setLong(1, id);
			stmt.setString(2,
					Character.toString(SerializableTypes.EVENT.getValue()));
			ResultSet rs = stmt.executeQuery();
			rs.next();

			byte[] buf = rs.getBytes(1);
			Object deserializedObject = ByteUtility.getObject(buf);

			rs.close();
			stmt.close();

			return (Event) deserializedObject;
		}
		finally
		{
			c.close();
		}
	}

	/**
	 * Deletes the entry for the specified id from the database.
	 * 
	 * @param id
	 *            the id of the serialized object from the database
	 * @throws SQLException
	 *             when there is a problem deleting
	 */
	public void delete(long id) throws SQLException
	{
		GlobalLogger.getLogger().logp(Level.INFO, "SQLiteConnector", "delete",
				"Deleting Task/Event with id " + Long.toString(id));

		Connection c = this.getConnection();
		try
		{
			PreparedStatement stmt = c
					.prepareStatement(SQL_DELETE_SERIALIZED_TIMEOBJECT);
			stmt.setLong(1, id);
			stmt.executeUpdate();
			stmt.close();
		}
		finally
		{
			c.close();
		}
	}

	/**
	 * Gets all serialized tasks from the database, deserializes them, and
	 * returns them.
	 * 
	 * @return an ArrayList of all tasks in the database
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ArrayList<Task> getAllTasks() throws SQLException,
			ClassNotFoundException, IOException
	{
		ArrayList<Task> tasks = new ArrayList<Task>();

		// Get all Tasks from the database
		Connection c = this.getConnection();
		ResultSet rs = null;
		try
		{
			PreparedStatement stmt = c.prepareStatement(SQL_GET_ALL_OBJECTS);
			stmt.setString(1,
					Character.toString(SerializableTypes.TASK.getValue()));
			rs = stmt.executeQuery();
			stmt.close();
		}
		finally
		{
			c.close();
		}

		// Deserialize the objects and add to list of Tasks
		while (rs.next())
		{
			byte[] buf = rs.getBytes(1);
			Object deserializedObject = ByteUtility.getObject(buf);
			tasks.add((Task) deserializedObject);
		}
		rs.close();

		return tasks;
	}

	/**
	 * Gets all serialized events from the database, deserializes them, and
	 * returns them.
	 * 
	 * @return an ArrayList of all events in the database
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ArrayList<Event> getAllEvents() throws SQLException,
			ClassNotFoundException, IOException
	{
		ArrayList<Event> events = new ArrayList<Event>();

		// Get all Events from the database
		Connection c = this.getConnection();
		ResultSet rs = null;
		try
		{
			PreparedStatement stmt = c.prepareStatement(SQL_GET_ALL_OBJECTS);
			stmt.setString(1,
					Character.toString(SerializableTypes.EVENT.getValue()));
			rs = stmt.executeQuery();
			stmt.close();
		}
		finally
		{
			c.close();
		}

		// Deserialize the objects and add to list of Events
		while (rs.next())
		{
			byte[] buf = rs.getBytes(1);
			Object deserializedObject = ByteUtility.getObject(buf);
			events.add((Event) deserializedObject);
		}
		rs.close();

		return events;
	}

	/**
	 * Gets the highest ID used in the database and returns it.
	 * 
	 * @return the highest id
	 * @throws SQLException
	 */
	public long getHighestID() throws SQLException
	{
		// Query database for maximum id
		Connection c = this.getConnection();
		ResultSet rs = null;
		try
		{
			PreparedStatement stmt = c.prepareStatement(SQL_GET_HIGHEST_ID);
			rs = stmt.executeQuery();
			stmt.close();
		}
		finally
		{
			c.close();
		}
		rs.next();

		long highestId = -1;
		// Check for 0 rows in the database
		if (!rs.isAfterLast())
		{
			highestId = rs.getLong(1);
		}

		rs.close();
		return highestId;
	}
}
