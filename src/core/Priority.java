package core;

public enum Priority {
	HIGH_PRIORITY(2), 
	MEDIUM_PRIORITY(1), 
	LOW_PRIORITY(0), 
	NO_PRIORITY_SELECTED(-1);
	
	private final int value;
	
	private Priority(int val) { 
		this.value = val; 
	}
	
	public int getValue() { 
		return this.value; 
	}
}
