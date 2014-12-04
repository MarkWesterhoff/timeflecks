package user_interface;

import java.awt.event.*;
import java.util.List;
import java.util.logging.Level;

import javax.swing.*;

import logging.GlobalLogger;
import core.*;

/**
 * Action listener for responding to TaskListTablePanel events.
 * 
 */
public class TaskPanelActionListener implements ActionListener
{
	private TaskListTablePanel mainPanel;

	public TaskPanelActionListener(TaskListTablePanel mainPanel)
	{
		this.mainPanel = mainPanel;
	}

	public static void editSelectedTask(TaskListTablePanel mainPanel)
	{

		List<Task> selectedTasks = mainPanel.getSelectedTasks();

		if (selectedTasks.size() == 0)
		{
			GlobalLogger.getLogger().logp(Level.WARNING, "TaskListTablePanel",
					"editSelectedTask(TaskListTablePanel)",
					"There are no rows selected to edit.");
			return;
		}

		for (Task task : selectedTasks)
		{
			GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
					"editSelectedTask()",
					"Bringing up EditTaskPanel for Task " + task.getName());
			NewTaskPanel p = new NewTaskPanel(task);
			p.displayFrame();
		}
	}

	public static void addNewTask()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
				"addNewTask()", "Bringing up NewTaskPanel.");
		NewTaskPanel p = new NewTaskPanel();
		p.displayFrame();
	}

	public static void addNewEvent()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
				"addNewEvent()", "Bringing up NewEventPanel.");
		NewEventPanel p = new NewEventPanel();
		p.displayFrame();
	}

	public static void deleteSelectedTasks(TaskListTablePanel mainPanel)
	{
		List<Task> selectedTasks = mainPanel.getSelectedTasks();

		if (selectedTasks.size() == 0)
		{
			// TODO Gray out the Delete Task Button if there are no tasks
			// selected

			GlobalLogger.getLogger().logp(Level.WARNING, "TaskListTablePanel",
					"actionPerformed(ActionEvent)",
					"There are no rows selected to delete.");
			return;
		}

		boolean deleteAll = false;
		// Option to delete all Tasks without being asked individually
		if (selectedTasks.size() > 1)
		{
			Object[] options = {
					"Delete " + Integer.toString(selectedTasks.size())
							+ " Tasks", "Cancel All", "Confirm Individually" };

			int reply = JOptionPane.showOptionDialog(
					mainPanel,
					"Are you sure you wish to delete these "
							+ Integer.toString(selectedTasks.size())
							+ " Tasks?", "Confirm Delete",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[1]);
			if (reply == 0)
			{
				// Delete all Tasks without promtping was selected
				GlobalLogger.getLogger().logp(
						Level.INFO,
						"TaskListTablePanel",
						"actionPerformed(ActionEvent)",
						"Deleting " + Integer.toString(selectedTasks.size())
								+ " Tasks without further promtping.");

				deleteAll = true;
			}
			else if (reply == 1)
			{
				// Cancel all deletions was selected
				GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
						"actionPerformed(ActionEvent)",
						"User cancelled deletion of Tasks.");

				return;
			}
			else if (reply == 2)
			{
				GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
						"actionPerformed(ActionEvent)",
						"User selected to be prompted individually.");
			}
		}

		// Perform delete for each Task the user selected
		for (Task task : selectedTasks)
		{
			if (!deleteAll)
			{
				// Prompt the user to see if they are sure they want to delete
				// the task.
				GlobalLogger
						.getLogger()
						.logp(Level.INFO, "TaskListTablePanel",
								"actionPerformed(ActionEvent)",
								"Delete task button pressed. Prompting for currently selected task.");

				Object[] options = { "Delete Task", "Cancel" };

				int reply = JOptionPane.showOptionDialog(
						mainPanel,
						"Are you sure you wish to delete the task \""
								+ task.getName() + "\"?", "Confirm Delete",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				if (reply == JOptionPane.NO_OPTION)
				{
					// User selected to not delete task
					GlobalLogger.getLogger().logp(Level.INFO,
							"TaskListTablePanel",
							"actionPerformed(ActionEvent)",
							"User cancelled Task deletion.");
					continue;
				}
			}

			deleteTask(task);
		}

		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);

		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.CHANGED_POSSIBLE_TAGS);
	}

	public static void deleteTask(Task task)
	{
		// The user selected to delete the Task
		GlobalLogger.getLogger().logp(Level.INFO, "TaskPanelActionListener",
				"deleteTask(Task)",
				"User elected to delete Task " + task.getName());

		boolean removed = Timeflecks.getSharedApplication().getTaskList()
				.getTasks().remove(task);

		if (!removed)
		{
			GlobalLogger
					.getLogger()
					.logp(Level.WARNING, "TaskPanelActionListener",
							"deleteTask(Task)",
							"Selected Task for deletion does not exist in application's TaskList.");
		}

		try
		{
			Timeflecks.getSharedApplication().getDBConnector()
					.delete(task.getId());
		}
		catch (Exception ex)
		{
			ExceptionHandler.handleDatabaseDeleteException(ex,
					"TaskPanelActionListener", "deleteTask(Task)", "1102");
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("dropdownsort"))
		{
			// TODO Resolve this warning
			JComboBox<String> box = (JComboBox<String>) (e.getSource());
			String switchOn = (String) box.getSelectedItem();

			Timeflecks.getSharedApplication().getFilteringManager()
					.setTaskComparator(mainPanel.getComboMap().get(switchOn));

			Timeflecks.getSharedApplication().postNotification(
					TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);

			mainPanel.refresh();
			mainPanel.setBumpButtonsVisibility(switchOn.equals("Manual"));
		}
		else if (e.getActionCommand().equals("Move Up"))
		{

			int row = mainPanel.getTable().getSelectedRow();
			if (row > 0)
			{
				// Swap the orders of the selected Task and the one above it
				Task originalTask = Timeflecks.getSharedApplication()
						.getFilteringManager().getFilteredTaskList().get(row);
				Task previousTask = Timeflecks.getSharedApplication()
						.getFilteringManager().getFilteredTaskList()
						.get(row - 1);

				long originalOrdering = originalTask.getOrdering();
				long previousOrdering = previousTask.getOrdering();

				originalTask.setOrdering(previousOrdering);
				previousTask.setOrdering(originalOrdering);

				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);

				mainPanel.refresh();
				GlobalLogger.getLogger().logp(Level.INFO,
						this.getClass().getName(), "actionPerformed()",
						"Task in row " + row + " bumped up.");
				mainPanel.getTable().getSelectionModel()
						.setSelectionInterval(row - 1, row - 1);
				try
				{
					originalTask.saveToDatabase();
					previousTask.saveToDatabase();
				}
				catch (Exception ex)
				{
					ExceptionHandler.handleDatabaseSaveException(ex, this
							.getClass().getName(), "actionPerformed", "1603");
				}
			}
		}
		else if (e.getActionCommand().equals("Move Down"))
		{

			int row = mainPanel.getTable().getSelectedRow();
			if (row > -1 && row < mainPanel.getTable().getRowCount() - 1)
			{
				// Swap the orders of the selected Task and the one below it
				Task originalTask = Timeflecks.getSharedApplication()
						.getFilteringManager().getFilteredTaskList().get(row);
				Task nextTask = Timeflecks.getSharedApplication()
						.getFilteringManager().getFilteredTaskList()
						.get(row + 1);

				long originalOrdering = originalTask.getOrdering();
				long nextOrdering = nextTask.getOrdering();

				originalTask.setOrdering(nextOrdering);
				nextTask.setOrdering(originalOrdering);

				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);

				mainPanel.refresh();
				GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
						"actionPerformed()",
						"Task in row " + row + " bumped down.");
				mainPanel.getTable().getSelectionModel()
						.setSelectionInterval(row + 1, row + 1);

				try
				{
					originalTask.saveToDatabase();
					nextTask.saveToDatabase();
				}
				catch (Exception ex)
				{
					ExceptionHandler.handleDatabaseSaveException(ex, this
							.getClass().getName(), "actionPerformed", "1605");
				}
			}
		}
		else if (e.getActionCommand().equals("New Task"))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
					"actionPerformed(ActionEvent)", "New task button pressed.");
			addNewTask();
		}
		else if (e.getActionCommand().equals("Edit Task"))
		{
			editSelectedTask(mainPanel);
		}
		else if (e.getActionCommand().equals("Delete Task"))
		{
			deleteSelectedTasks(mainPanel);
		}
		else
		{
			GlobalLogger.getLogger().logp(Level.WARNING, "TaskListTablePanel",
					"actionPerformed()",
					"Action command " + e.getActionCommand() + " not found");
		}

	}
}
