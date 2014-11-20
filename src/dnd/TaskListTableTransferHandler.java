package dnd;

import java.awt.datatransfer.*;
import java.util.logging.Level;

import core.Task;
import core.Timeflecks;

import javax.swing.*;

import logging.GlobalLogger;

public class TaskListTableTransferHandler extends TransferHandler
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Can't import into the table.
	 */
	public boolean canImport(TransferHandler.TransferSupport info)
	{
		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"canImport", "Can import called.");

		return false;
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
		JTable myTable = ((JTable)c);
		int row = myTable.getSelectedRow();
		Task task = Timeflecks.getSharedApplication().getFilteringManager().getFilteredTaskList().get(row);
		
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
	 * Can't import data into the table.
	 */
	public boolean importData(TransferHandler.TransferSupport info)
	{
		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"importData", "importData called.");

		return false;
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
