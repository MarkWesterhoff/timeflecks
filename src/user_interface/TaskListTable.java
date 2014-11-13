package user_interface;

import javax.swing.JTable;

/**
 * Wrapper for JTable that requires a TaskListTableModel.
 * 
 * @author Andrew
 */
public class TaskListTable extends JTable
{
	private static final long serialVersionUID = 1L;
	private TaskListTableModel taskListTableModel;

	public TaskListTable(TaskListTableModel tltm)
	{
		super(tltm);
		this.taskListTableModel = tltm;
	}

	/**
	 * Gets the TaskListTableModel used to construct the JTable.
	 */
	@Override
	public TaskListTableModel getModel()
	{
		return taskListTableModel;
	}
}
