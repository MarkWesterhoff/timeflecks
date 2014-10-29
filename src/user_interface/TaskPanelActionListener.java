package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import logging.GlobalLogger;
import core.Task;
import core.Timeflecks;

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
			Timeflecks.getSharedApplication().getTaskList()
					.setTaskComparator(mainPanel.getComboMap().get(switchOn));
			mainPanel.refresh();
			mainPanel.setBumpButtonsVisibility(switchOn == "Manual");

		}
		else if (e.getActionCommand().equals("Move Up"))
		{

			int row = mainPanel.getTable().getSelectedRow();
			if (row > 0)
			{
				long originalOrdering = Timeflecks.getSharedApplication()
						.getTaskList().getTasks().get(row).getOrdering();
				long newOrdering = Timeflecks.getSharedApplication()
						.getTaskList().getTasks().get(row - 1).getOrdering();
				Timeflecks.getSharedApplication().getTaskList().getTasks()
						.get(row).setOrdering(newOrdering);
				Timeflecks.getSharedApplication().getTaskList().getTasks()
						.get(row - 1).setOrdering(originalOrdering);
				mainPanel.refresh();
				GlobalLogger.getLogger().logp(Level.INFO,
						this.getClass().getName(), "actionPerformed()",
						"Task in row " + row + " bumped up.");
				mainPanel.getTable().getSelectionModel()
						.setSelectionInterval(row - 1, row - 1);
				try
				{
					Timeflecks.getSharedApplication().getTaskList().getTasks()
							.get(row).saveToDatabase();
					Timeflecks.getSharedApplication().getTaskList().getTasks()
							.get(row - 1).saveToDatabase();
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
				long originalOrdering = Timeflecks.getSharedApplication()
						.getTaskList().getTasks().get(row).getOrdering();
				long newOrdering = Timeflecks.getSharedApplication()
						.getTaskList().getTasks().get(row + 1).getOrdering();
				Timeflecks.getSharedApplication().getTaskList().getTasks()
						.get(row).setOrdering(newOrdering);
				Timeflecks.getSharedApplication().getTaskList().getTasks()
						.get(row + 1).setOrdering(originalOrdering);
				mainPanel.refresh();
				GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
						"actionPerformed()",
						"Task in row " + row + " bumped down.");
				mainPanel.getTable().getSelectionModel()
						.setSelectionInterval(row + 1, row + 1);

				try
				{
					Timeflecks.getSharedApplication().getTaskList().getTasks()
							.get(row).saveToDatabase();
					Timeflecks.getSharedApplication().getTaskList().getTasks()
							.get(row + 1).saveToDatabase();
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

					Task t = Timeflecks.getSharedApplication().getTaskList()
							.getTasks().remove(row);
					try
					{
						Timeflecks.getSharedApplication().getDBConnector()
								.delete(t.getId());
					}
					catch (Exception ex)
					{
						ExceptionHandler.handleDatabaseDeleteException(ex,
								this, "actionPerformed()", "1102");
					}
					Timeflecks.getSharedApplication().getMainWindow().refresh();
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
