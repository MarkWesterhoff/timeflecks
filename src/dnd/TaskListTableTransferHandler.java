package dnd;

import java.awt.datatransfer.*;
import java.util.ArrayList;
import java.util.logging.Level;

import core.Task;
import core.Timeflecks;
import core.TimeflecksEvent;

import javax.swing.*;

import user_interface.ExceptionHandler;
import logging.GlobalLogger;

public class TaskListTableTransferHandler extends TransferHandler
{
	private static final long serialVersionUID = 1L;

	/**
	 * Import into the table is allowed for all Tasks.
	 */
	public boolean canImport(TransferHandler.TransferSupport info)
	{
		GlobalLogger.getLogger().logp(Level.FINE, this.getClass().getName(),
				"canImport", "Can import called.");
		if (!info.isDrop())
		{
			return false;
		}

		// Check for Task flavor
		if (!info.isDataFlavorSupported(new DataFlavor(Task.class, "Task")))
		{
			return false;
		}

		return true;
	}

	/**
	 * Bundle up the task from the table for export.
	 */
	protected Transferable createTransferable(JComponent c)
	{

		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"createTransferable", "createTransferable called.");

		// Get the object at the currently pointed at coordinates
		// Get it's task
		// Return a transferable that holds that task
		JTable myTable = ((JTable) c);
		int row = myTable.getSelectedRow();
		Task task = Timeflecks.getSharedApplication().getFilteringManager()
				.getFilteredTaskList().get(row);

		return task;
	}

	/**
	 * We support only move actions.
	 */
	public int getSourceActions(JComponent c)
	{
		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"getSourceActions", "getSourceActions called.");
		return TransferHandler.MOVE;
	}

	/**
	 * Importing data into the table unschedules the Task.
	 */
	public boolean importData(TransferHandler.TransferSupport info)
	{
		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"importData", "importData called.");

		if (!info.isDrop())
		{
			return false;
		}

		// Get the task that is being dropped.
		Transferable t = info.getTransferable();
		Task taskRef;
		try
		{
			taskRef = (Task) t.getTransferData(new DataFlavor(Task.class,
					"Task"));
		}
		catch (Exception e)
		{
			return false;
		}

		if (taskRef == null)
		{
			GlobalLogger.getLogger().logp(Level.INFO,
					this.getClass().getName(), "importData",
					"Failed to get task from import data");
			return false;
		}

		ArrayList<Task> tasks = Timeflecks.getSharedApplication().getTaskList()
				.getTasks();
		for (Task t1 : tasks)
		{
			if (t1.getId() == taskRef.getId())
			{
				taskRef = t1;
				break;
			}
		}

		// Unschedule the Task
		taskRef.setStartTime(null);

		try
		{
			taskRef.saveToDatabase();
		}
		catch (Exception ex)
		{
			ExceptionHandler.handleDatabaseSaveException(ex, this,
					"importData", "2102");
		}

		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);

		return true;
	}

	/**
	 * Simply log this for now, we don't have anything required to do here.
	 */
	protected void exportDone(JComponent c, Transferable data, int action)
	{
		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"exportDone", "Export done called. Doing nothing");
	}

}
