package database;

public class IDGenerator
{
	private static long currentID = 0;
	public static long getNextID(){
		if (currentID == Long.MAX_VALUE) {
			// can we recover from this?
			throw new RuntimeException("Exceeded maximum number of tasks/events.");
		}
		long id = currentID;
		currentID++;
		return id;
	}
}
