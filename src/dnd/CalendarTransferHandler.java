package dnd;

import java.awt.Point;
import java.awt.datatransfer.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import core.PlaceholderScheduleable;
import core.Task;
import core.Timeflecks;
import core.TimeflecksEvent;

import javax.swing.*;

import user_interface.CalendarPanel;
import user_interface.ExceptionHandler;
import logging.GlobalLogger;

public class CalendarTransferHandler extends TransferHandler
{
	private static final long serialVersionUID = 1L;

	public boolean canImport(TransferHandler.TransferSupport info)
	{
		GlobalLogger.getLogger().logp(Level.FINE, "CalendarTransferHandler",
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

		Point dropPoint = info.getDropLocation().getDropPoint();

		if (info.getComponent() instanceof CalendarPanel)
		{
			CalendarPanel p = (CalendarPanel) (info.getComponent());
			Date date = p.getDateForPoint(dropPoint);

			if (date == null)
			{
				// If this was invalid in any way, return false
				return false;
			}
			else
			{
				// Check if there are any Tasks or Events that are under the
				// current drop location
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
				
				if (taskRef instanceof Task)
				{
					Date newTime = p.getDateForPoint(dropPoint);
					
					// Create a temporary Task to represent where the current one would be dropped
					Task temp = (Task)taskRef;
					PlaceholderScheduleable hold = new PlaceholderScheduleable(temp.getName(), newTime, new Date(newTime.getTime() + temp.getDuration()));
					
					if (p.conflictingScheduleableExists(hold))
					{
						return false;
					}
				}
				return true;
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * Bundle up the task for export
	 */
	protected Transferable createTransferable(JComponent c)
	{

		GlobalLogger.getLogger().logp(Level.INFO, "CalendarTransferHandler",
				"createTransferable", "createTransferable called.");

		// Get the object at the currently pointed at coordinates
		// Get it's task
		// Return a transferable that holds that task
		if (c instanceof CalendarPanel)
		{
			CalendarPanel p = (CalendarPanel) c;

			Task t = p.getTaskUnderMouse(p.getCurrentMousePoint());

			if (t == null)
			{
				return null;
			}
			else
			{
				return t;
			}
		}
		return null;
	}

	/**
	 * We support only move actions.
	 */
	public int getSourceActions(JComponent c)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "CalendarTransferHandler",
				"getSourceActions", "getSourceActions called.");
		return TransferHandler.MOVE;
	}

	/**
	 * Perform the actual import. Note that we only support drag and drop.
	 */
	public boolean importData(TransferHandler.TransferSupport info)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "CalendarTransferHandler",
				"importData", "importData called.");

		if (!info.isDrop())
		{
			return false;
		}

		// We need to get the drop location
		DropLocation dl = info.getDropLocation();

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
					"CalendarTransferHandler", "importData",
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

		// Now we just set the currently selected time from the dropLocation in
		// the Task
		Point selectedPoint = dl.getDropPoint();

		// We know that it is a valid time, so we just got that time and now we
		// are going to set it into the start time for the task.

		if (!(info.getComponent() instanceof CalendarPanel))
		{
			return false;
		}

		CalendarPanel p = (CalendarPanel) (info.getComponent());
		Date date = p.getDateForPoint(selectedPoint);

		if (date == null)
		{
			// If this was invalid in any way, return false
			return false;
		}

		// We actually got the date, so we are good to go
		taskRef.setStartTime(date);

		// Otherwise, let it keep its original duration
		if (!taskRef.isScheduled())
		{
			taskRef.setDuration(1000 * 60 * 60); // Set the duration to 1 hr,
													// which is 1000ms = 1s * 60
													// = 1 min * 60 = 1hr
		}

		try
		{
			taskRef.saveToDatabase();
		}
		catch (Exception ex)
		{
			ExceptionHandler.handleDatabaseSaveException(ex, this.getClass()
					.getName(), "importData", "2102");
		}

		// System.out.println("Time: " + taskRef.getStartTime());

		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);

		// Timeflecks.getSharedApplication().postNotification(TimeflecksEvent.EVERYTHING_NEEDS_REFRESH);

		return true;
	}

	/**
	 * Simply log this for now, we don't have anything required to do here.
	 */
	protected void exportDone(JComponent c, Transferable data, int action)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "CalendarTransferHandler",
				"exportDone", "Export done called. Doing nothing");
	}
}
