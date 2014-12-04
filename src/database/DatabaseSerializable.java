package database;

import java.io.Serializable;

/**
 * Contract for a class that is able to be serialized and identified by the
 * database.
 * 
 */
public interface DatabaseSerializable extends Serializable
{
	public long getId();

	public SerializableType getType();
}
