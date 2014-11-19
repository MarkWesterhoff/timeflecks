package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import logging.GlobalLogger;
import core.Task;
import core.Timeflecks;
import core.TimeflecksEvent;

public class TaskPanelActionListener implements ActionListener
{
	private TaskListTablePanel mainPanel;

	public TaskPanelActionListener(TaskListTablePanel mainPanel)
	{
		this.mainPanel = mainPanel;
	}

	public static void editSelectedTask(TaskListTablePanel mainPanel)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
				"editSelectedTask()", "Bringing up EditTaskPanel.");
		Task selectedTask = mainPanel.getSelectedTask();
		if (selectedTask != null)
		{
			NewTaskPanel p = new NewTaskPanel(selectedTask);
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
						.getFilteringManager().getFilteredTaskList().get(row - 1);

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
					ExceptionHandler.handleDatabaseSaveException(ex, this,
							"actionPerformed", "1603");
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
					ExceptionHandler.handleDatabaseSaveException(ex, this,
							"actionPerformed", "1605");
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
			int row = mainPanel.getTable().getSelectedRow();
			if (row >= 0 && row < mainPanel.getTable().getRowCount())
			{
				GlobalLogger
						.getLogger()
						.logp(Level.INFO, "TaskListTablePanel",
								"actionPerformed(ActionEvent)",
								"Delete task button pressed. Deleting currently selected task.");

				// We need to prompt the user to see if they want to
				// delete the task.
				Object[] options = { "Delete Task", "Cancel" };
				int reply = JOptionPane.showOptionDialog(mainPanel,
						"Are you sure you wish to delete this task?",
						"Confirm Delete", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[1]);

				if (reply == JOptionPane.YES_OPTION)
				{
					// The user selected to delete the Task
					GlobalLogger.getLogger().logp(Level.INFO,
							"TaskListTablePanel", "performSaveAsCommand",
							"User elected to overwrite existing file.");

					// Grab the Task from the Filtered list of Tasks that the
					// Panel displays and delete that Task from the main
					// TaskList
					Task task = Timeflecks.getSharedApplication()
							.getFilteringManager().getFilteredTaskList()
							.get(row);

					boolean removed = Timeflecks.getSharedApplication()
							.getTaskList().getTasks().remove(task);

					if (!removed)
					{
						GlobalLogger
								.getLogger()
								.logp(Level.WARNING, "TaskPanelActionListener",
										"actionPerformed(ActionEvent)",
										"Selected Task for deletion does not exist in application's TaskList.");
					}
					
					Timeflecks.getSharedApplication().postNotification(
							TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);
					
					try
					{
						Timeflecks.getSharedApplication().getDBConnector()
								.delete(task.getId());
					}
					catch (Exception ex)
					{
						ExceptionHandler.handleDatabaseDeleteException(ex,
								this, "actionPerformed()", "1102");
					}
					
				}
				else
				{
					// User selected to not delete task
					GlobalLogger
							.getLogger()
							.logp(Level.INFO, "TaskListTablePanel",
									"performSaveAsCommand",
									"User declined to overwrite file, prompting for new file choice.");
				}
			}
			else
			{
				// This happens if there are no tasks selected

				// TODO Gray out the Delete Task Button if there are no tasks
				// selected

				GlobalLogger.getLogger().logp(Level.WARNING,
						"TaskListTablePanel", "actionPerformed(ActionEvent)",
						"Selected row is out of bounds for the current table.");
			}
		}
		else
		{
			GlobalLogger.getLogger().logp(Level.WARNING, "TaskListTablePanel",
					"actionPerformed()",
					"Action command " + e.getActionCommand() + " not found");
		}

	}
}
