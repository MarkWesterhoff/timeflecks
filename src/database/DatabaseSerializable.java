package database;

import java.io.Serializable;

public interface DatabaseSerializable extends Serializable
{
	public long getId();
	public SerializableType getType();
}
