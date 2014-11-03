package database;

/**
 * Represents the types of objects that can be saved to this database.
 */
public enum SerializableType
{
	TASK('t'), EVENT('e');

	private final char type;

	SerializableType(char t)
	{
		this.type = t;
	}

	public char getValue()
	{
		return type;
	}
}