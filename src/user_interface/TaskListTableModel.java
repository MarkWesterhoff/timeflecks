package user_interface;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import logging.GlobalLogger;
import core.Task;
import core.Timeflecks;

public class TaskListTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the TaskListTableModel. Takes a TaskList that holds the
	 * Tasks.
	 * 
	 * @param taskList
	 *            The TaskList holding the Tasks for the table model
	 */
	public TaskListTableModel()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "TaskListTableModel",
				"TaskListTableModel", "Constructing TaskListTableModel.");
	}

	/**
	 * Accessor for the number of rows in the table.
	 * 
	 * @return the number of rows in the table.
	 */
	public int getRowCount()
	{
		return Timeflecks.getSharedApplication().getTaskList().getTasks().size();
	}

	/**
	 * Accessor for the number of columns in the table.
	 * 
	 * @return the number of columns in the table.
	 */
	public int getColumnCount()
	{
		return 2;
	}

	/**
	 * Gets the name of the specified column.
	 * 
	 * @param col
	 *            the index of the column
	 * @return the name of the column
	 */
	public String getColumnName(int col)
	{
		switch (col) {
		case 0:
			return "Completed";
		case 1:
			return "Name";
		default:
			return "Unknown Column";
		}
	}

	/**
	 * Returns true if the specified cell is editable.
	 * 
	 * @param row
	 *            the row index of the cell
	 * @param col
	 *            the column index of the cell
	 * @return true if the specified cell is editable.
	 */
	public boolean isCellEditable(int row, int col)
	{
		return col == 0; // isCompleted
				//|| col == 1; // name
	}

	/**
	 * Sets the value at the specified cell.
	 * 
	 * @param value
	 *            the value to set the cell to
	 * @param row
	 *            the row index of the cell
	 * @param col
	 *            the column index of the cell
	 */
	public void setValueAt(Object value, int row, int col)
	{
		GlobalLogger.getLogger().logp(
				Level.INFO,
				"TaskListTableView",
				"setValueAt",
				String.format("Setting value at (%d, %d) to %s", row, col,
						value));
		Task task = (Task) Timeflecks.getSharedApplication().getTaskList().getTasks().get(row);
		switch (col) {
		case 0:
			task.setCompleted((Boolean) value);
			Timeflecks.getSharedApplication().getMainWindow().refresh();
			break;
		case 1:
			task.setName((String) value);
			Timeflecks.getSharedApplication().getMainWindow().refresh();
			break;
		}
		try
		{
			task.saveToDatabase();
			GlobalLogger.getLogger().logp(Level.INFO, "TaskListTableModel",
					"setValueAt", "Saved modified task to database.");
		}
		catch (SQLException a)
		{
			GlobalLogger.getLogger()
					.logp(Level.WARNING,
							"TaskListTableModel",
							"setValueAt",
							"SQLException caught when saving modified task to database.\nSQL State:\n"
									+ a.getSQLState() + "\nMessage:\n"
									+ a.getMessage());

			JOptionPane
					.showMessageDialog(
							Timeflecks.getSharedApplication().getMainWindow(),
							"Database Error. (1202)\nYour task was not saved. Please try again, or check your database file.",
							"Database Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException a)
		{
			GlobalLogger.getLogger().logp(
					Level.WARNING,
					"NewTaskPanel",
					"actionPerformed",
					"IOException caught when saving task to database.\nMessage:\n"
							+ a.getLocalizedMessage());

			// Trouble serializing objects
			JOptionPane
					.showMessageDialog(
							Timeflecks.getSharedApplication().getMainWindow(),
							"Object Serialization Error. (1203)\nYour task was not saved. Please try again, or check your database file.",
							"Database Error", JOptionPane.ERROR_MESSAGE);
		}

		fireTableCellUpdated(row, col);
	}

	/**
	 * Gets the class held by each column.
	 * 
	 * @param c
	 *            the column index
	 * @return the class held by the specified column
	 */
	@Override
	public Class<?> getColumnClass(int c)
	{
		switch (c) {
		case 0:
			return Boolean.class;
		case 1:
			return String.class;
		}
		return null;
	}

	/**
	 * This method returns the data at the specified row and column in the table
	 * 
	 * @param rowIndex
	 *            the index of the row within the table model
	 * @param columnIndex
	 *            0 for completed, 1 for name
	 */
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Task task = Timeflecks.getSharedApplication().getTaskList().getTasks().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return task.isCompleted();
		case 1:
			return task.getName();
		}
		return null;
	}
}
