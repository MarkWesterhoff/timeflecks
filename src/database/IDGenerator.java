package database;

public class IDGenerator
{
	private long currentID = 0;
	
	public IDGenerator() {
		currentID = 0;
	}
	public IDGenerator(long ID) {
		currentID = ID;
	}
	
	public long getNextID(){
		if (currentID == Long.MAX_VALUE) {
			// can we recover from this?
			throw new RuntimeException("Exceeded maximum number of tasks/events.");
		}
		long id = currentID;
		currentID++;
		return id;
	}
}
