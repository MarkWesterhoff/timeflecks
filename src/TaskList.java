import java.util.ArrayList;

//I'm not sure if this should / shouldn't be a singleton...
public class TaskList
{

	private TaskList()
	{

	}

	static TaskList instance;
	private ArrayList<TimeObject> taskEvents;

	public ArrayList<TimeObject> getTaskEvents()
	{
		return taskEvents;
	}

	public void setTaskEvents(ArrayList<TimeObject> taskEvents)
	{
		this.taskEvents = taskEvents;
	}

	public void addTimeObject(TimeObject t)
	{
		taskEvents.add(t);
	}

	public static TaskList getTaskInstance()
	{
		if (instance == null)
		{
			instance = new TaskList();
		}
		return instance;
	}

}
