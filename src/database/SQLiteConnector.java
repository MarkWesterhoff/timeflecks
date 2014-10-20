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

import logging.GlobalLogger;
import utility.ByteUtility;

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
	
	private static final String SQL_CREATE_SERIALIZE_TABLE = 
			"CREATE TABLE IF NOT EXISTS 'serialized_time_objects' ("
			+ "'id' int(11) PRIMARY KEY NOT NULL,"
			+ "'serialized_time_object' blob )";
	
	private static final String SQL_CLEAR_SERIALIZE_TABLE = 
			"DELETE FROM 'serialized_time_objects'";
	
	private static final String SQL_INSERT_SERIALIZED_TIMEOBJECT = 
			"INSERT OR REPLACE "
			+ "INTO 'serialized_time_objects' (id, serialized_time_object) "
			+ "VALUES (?, ?)";
	
	private static final String SQL_SELECT_SERIALIZED_TIMEOBJECT = 
			"SELECT serialized_time_object "
			+ "FROM 'serialized_time_objects' "
			+ "WHERE id = ? "
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
		instance.logger = GlobalLogger.getLogger();
		
		logger.logp(Level.INFO, "SQLiteConnector", "SQLiteConnector", 
				"Constructing SQLiteConnector");
		Connection c = this.getConnection();
		try {
			Statement stmt = c.createStatement();
			stmt.execute(SQL_CREATE_SERIALIZE_TABLE);
			stmt.execute(SQL_CLEAR_SERIALIZE_TABLE);
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
	 * Serializes the TimeObject to a byte array and saves it to the database.
	 * 
	 * @param obj the TimeObject to serialize and save
	 * @throws SQLException when there is a problem saving to the database
	 * @throws IOException when there is a problem serializing the TimeObject to bytes
	 */
	public void saveSerializedTimeObject(TimeObject obj) throws SQLException, IOException {
		logger.logp(Level.INFO, "SQLiteConnector", "saveSerializedTimeObject", 
				"Serializing and saving TimeObject " + obj.getName());
		
		Connection c = this.getConnection();
		try {
			PreparedStatement stmt = c.prepareStatement(SQL_INSERT_SERIALIZED_TIMEOBJECT);
			stmt.setLong(1, obj.id);
			stmt.setBytes(2, ByteUtility.getBytes(obj));
			
			stmt.executeUpdate();
			stmt.close();
		}
		finally {
			c.close();
		}
	}
	
	/**
	 * 
	 * @param id the id of the serialized TimeObject to get from the database
	 * @return the deserialized TimeObject
	 * @throws SQLException if there is a problem reading from the database
	 * @throws ClassNotFoundException if there is a problem deserializing the object
	 * @throws IOException if there is a problem with the stream of bytes representing
	 * 						the serialized object
	 */
	public TimeObject getSerializedTimeObject(long id) throws SQLException, ClassNotFoundException, IOException {
		logger.logp(Level.INFO, "SQLiteConnector", "getSerializedTimeObject", 
				"Getting and deserializing TimeObject with id " + Long.toString(id));
		
		Connection c = this.getConnection();
		try {
			PreparedStatement stmt = c.prepareStatement(SQL_SELECT_SERIALIZED_TIMEOBJECT);
			stmt.setLong(1, id);
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
			
			return (TimeObject)deserializedObject; 
		}
		finally {
			c.close();
		}
	}
}
