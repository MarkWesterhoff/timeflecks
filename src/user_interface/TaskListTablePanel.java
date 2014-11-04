package user_interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import logging.GlobalLogger;
import core.Task;
import core.Timeflecks;

public class TaskListTablePanel extends JPanel implements ActionListener, MouseListener
{
	private static final long serialVersionUID = 1L;

	private static final int MIN_COMPLETED_COLUMN_WIDTH = 60;
	private static final int PREFERRED_COMPLETED_COLUMN_WIDTH = 40;
	private static final int MIN_NAME_COLUMN_WIDTH = 30;
	private static final int PREFERRED_NAME_COLUMN_WIDTH = 200;

	private final JTable table;

	private LinkedHashMap<String, Comparator<Task>> comboMap;
	private JButton newTaskButton;
	private JButton editTaskButton;
	private JButton deleteTaskButton;
	private JButton upButton;
	private JButton downButton;

	public TaskListTablePanel(TableModel tableModel)
	{
		super();

		Objects.requireNonNull(tableModel);

		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		// Combobox for Sorting
		// construct the JComboBox
		comboMap = new LinkedHashMap<String, Comparator<Task>>();
		comboMap.put("Name", Task.nameComparator);
		comboMap.put("Manual", Task.manualComparator);
		comboMap.put("Due Date", Task.dueDateComparator);
		comboMap.put("Priority", Task.priorityComparator);
		JComboBox<String> sortList = new JComboBox<String>(comboMap.keySet()
				.toArray(new String[comboMap.size()]));
		sortList.setActionCommand("dropdownsort");
		sortList.addActionListener(this);

		JLabel spacer = new JLabel("    ");

		JLabel sortLabel = new JLabel("Sort By: ");

		JPanel sortSet = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setHgap(0);
		layout.setVgap(0);
		sortSet.setLayout(layout);

		sortSet.add(sortLabel);
		sortSet.add(sortList);
		sortSet.add(spacer);

		// Up and down bump buttons
		upButton = createIconedButton("resources/up.png", "Move Up");
		downButton = createIconedButton("resources/down.png", "Move Down");

		// Goes with buttons
		sortSet.add(upButton);
		sortSet.add(downButton);

		topPanel.add(sortSet, BorderLayout.EAST);

		// Add the new task and the edit task buttons
		newTaskButton = new JButton("New Task");
		editTaskButton = new JButton("Edit Task");
		deleteTaskButton = new JButton("Delete Task");

		newTaskButton.setActionCommand("New Task");
		editTaskButton.setActionCommand("Edit Task");
		deleteTaskButton.setActionCommand("Delete Task");

		newTaskButton.addActionListener(this);
		editTaskButton.addActionListener(this);
		deleteTaskButton.addActionListener(this);

		JPanel newButtonPanel = new JPanel();

		// layout.setVgap(8);

		newButtonPanel.setLayout(layout);

		newButtonPanel.add(newTaskButton);
		newButtonPanel.add(editTaskButton);
		newButtonPanel.add(deleteTaskButton);

		JLabel spacer2 = new JLabel("        ");
		newButtonPanel.add(spacer2);

		topPanel.add(newButtonPanel, BorderLayout.WEST);

		// For now, we just add a spacer
		// topPanel.setPreferredSize(new Dimension(600, 50));

		add(topPanel, BorderLayout.NORTH);

		// Actual table
		table = new JTable(tableModel);

		// Set column widths
		table.getColumnModel().getColumn(0)
				.setMinWidth(MIN_COMPLETED_COLUMN_WIDTH);
		table.getColumnModel().getColumn(0)
				.setPreferredWidth(PREFERRED_COMPLETED_COLUMN_WIDTH);
		table.getColumnModel().getColumn(1).setMinWidth(MIN_NAME_COLUMN_WIDTH);
		table.getColumnModel().getColumn(1)
				.setPreferredWidth(PREFERRED_NAME_COLUMN_WIDTH);

		JScrollPane scroll = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(false);
		table.setDragEnabled(true);
		table.setDropMode(DropMode.INSERT_ROWS);
		table.setTransferHandler(new TaskListTransferHandler(table));
		table.addMouseListener(this);
		add(scroll, BorderLayout.CENTER);

	}

	private JButton createIconedButton(String iconPath, String buttonName)
	{
		ImageIcon buttonIcon = new ImageIcon(iconPath);
		JButton button;
		if (buttonIcon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE)
		{
			button = new JButton(buttonIcon);
			GlobalLogger.getLogger().logp(Level.INFO, "TaskListTabelPanel",
					"createIconedButton()",
					buttonName + " icon successfully loaded");
		}
		else
		{
			button = new JButton(buttonName);
			GlobalLogger.getLogger().logp(Level.WARNING, "TaskListTabelPanel",
					"TaskListTablePanel()",
					buttonName + " icon could not be loaded");
		}
		button.setActionCommand(buttonName);
		button.setEnabled(false);
		button.addActionListener(this);
		return button;
	}

	/**
	 * Fires an event to redraw the table.
	 */
	public void refresh()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
				"refresh()", "Refreshing TaskListTableModel" + this.table);
		Timeflecks.getSharedApplication().getTaskList().sort();
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	// handle changing of SortList
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("dropdownsort"))
		{

			// TODO Resolve this warning
			JComboBox<String> box = (JComboBox<String>) (e.getSource());
			String switchOn = (String) box.getSelectedItem();
			Timeflecks.getSharedApplication().getTaskList()
					.setTaskComparator(comboMap.get(switchOn));
			refresh();
			if (switchOn == "Manual")
			{
				upButton.setEnabled(true);
				downButton.setEnabled(true);
			}
			else
			{
				upButton.setEnabled(false);
				downButton.setEnabled(false);
			}

		}
		else if (e.getActionCommand().equals("Move Up"))
		{

			int row = table.getSelectedRow();
			if (row > 0)
			{
				Timeflecks.getSharedApplication().getTaskList().getTasks()
						.get(row).setOrdering(row - 1);
				Timeflecks.getSharedApplication().getTaskList().getTasks()
						.get(row - 1).setOrdering(row);
				refresh();
				GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
						"actionPerformed()",
						"Task in row " + row + " bumped up.");
				
				try
				{
					Timeflecks.getSharedApplication().getTaskList().getTasks()
					.get(row).saveToDatabase();
					Timeflecks.getSharedApplication().getTaskList().getTasks()
					.get(row - 1).saveToDatabase();
				}
				catch (SQLException a)
				{
					GlobalLogger.getLogger().logp(
							Level.WARNING,
							"TaskListTablePanel",
							"actionPerformed",
							"SQLException caught when saving task to database.\nSQL State:\n"
									+ a.getSQLState() + "\nMessage:\n"
									+ a.getMessage());

					JOptionPane
							.showMessageDialog(
									this,
									"Database Error. (1603)\nYour task was not saved. Please try again, or check your database file.",
									"Database Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (IOException a)
				{
					GlobalLogger.getLogger().logp(
							Level.WARNING,
							"TaskListTablePanel",
							"actionPerformed",
							"IOException caught when saving task to database.\nMessage:\n"
									+ a.getLocalizedMessage());

					// Trouble serializing objects
					JOptionPane
							.showMessageDialog(
									this,
									"Object Serialization Error. (1604)\nYour task was not saved. Please try again, or check your database file.",
									"Database Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			table.getSelectionModel().setSelectionInterval(row - 1, row - 1);
		}
		else if (e.getActionCommand().equals("Move Down"))
		{

			int row = table.getSelectedRow();
			if (row > -1 && row < table.getRowCount() - 1)
			{
				Timeflecks.getSharedApplication().getTaskList().getTasks()
						.get(row).setOrdering(row + 1);
				Timeflecks.getSharedApplication().getTaskList().getTasks()
						.get(row + 1).setOrdering(row);
				refresh();
				GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
						"actionPerformed()",
						"Task in row " + row + " bumped down.");
				table.getSelectionModel()
						.setSelectionInterval(row + 1, row + 1);
				
				try
				{
					Timeflecks.getSharedApplication().getTaskList().getTasks()
					.get(row).saveToDatabase();
					Timeflecks.getSharedApplication().getTaskList().getTasks()
					.get(row + 1).saveToDatabase();
				}
				catch (SQLException a)
				{
					GlobalLogger.getLogger().logp(
							Level.WARNING,
							"TaskListTablePanel",
							"actionPerformed",
							"SQLException caught when saving task to database.\nSQL State:\n"
									+ a.getSQLState() + "\nMessage:\n"
									+ a.getMessage());

					JOptionPane
							.showMessageDialog(
									this,
									"Database Error. (1601)\nYour task was not saved. Please try again, or check your database file.",
									"Database Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (IOException a)
				{
					GlobalLogger.getLogger().logp(
							Level.WARNING,
							"TaskListTablePanel",
							"actionPerformed",
							"IOException caught when saving task to database.\nMessage:\n"
									+ a.getLocalizedMessage());

					// Trouble serializing objects
					JOptionPane
							.showMessageDialog(
									this,
									"Object Serialization Error. (1602)\nYour task was not saved. Please try again, or check your database file.",
									"Database Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if (e.getActionCommand().equals("New Task"))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
					"actionPerformed(ActionEvent)",
					"New task button pressed. Bringing up NewTaskPanel.");

			NewTaskPanel p = new NewTaskPanel();
			p.displayFrame();
		}
		else if (e.getActionCommand().equals("Edit Task"))
		{
			GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
					"actionPerformed(ActionEvent)",
					"Edit task button pressed. Bringing up EditTaskPanel.");

			int row = table.getSelectedRow();
			if (row >= 0 && row < table.getRowCount())
			{
				NewTaskPanel p = new NewTaskPanel(Timeflecks
						.getSharedApplication().getTaskList().getTasks()
						.get(row));
				p.displayFrame();
			}
			else
			{
				// This happens if there are no tasks selected

				// TODO Grey out the Edit Task Button if there are no tasks
				// selected

				GlobalLogger.getLogger().logp(Level.WARNING,
						"TaskListTablePanel", "actionPerformed(ActionEvent)",
						"Selected row is out of bounds for the current table.");
			}

		}
		else if (e.getActionCommand().equals("Delete Task"))
		{
			int row = table.getSelectedRow();
			if (row >= 0 && row < table.getRowCount())
			{
				GlobalLogger
						.getLogger()
						.logp(Level.INFO, "TaskListTablePanel",
								"actionPerformed(ActionEvent)",
								"Delete task button pressed. Deleting currently selected task.");

				// We need to prompt the user to see if they want to
				// delete the task.
				Object[] options = { "Delete Task", "Cancel" };
				int reply = JOptionPane.showOptionDialog(this,
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
					catch (SQLException a)
					{
						GlobalLogger.getLogger().logp(
								Level.WARNING,
								"TaskListTablePanel",
								"actionPerformed()",
								"SQLException caught when deletingd task from database.\nSQL State:\n"
										+ a.getSQLState() + "\nMessage:\n"
										+ a.getMessage());

						JOptionPane
								.showMessageDialog(
										Timeflecks.getSharedApplication()
												.getMainWindow(),
										"Database Error. (1102)\nYour task was not deleted. Please try again, or check your database file.",
										"Database Error",
										JOptionPane.ERROR_MESSAGE);
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

	public JTable getTable()
	{
		return table;
	}

	public static void main(String[] args)
	{
		Timeflecks.getSharedApplication().getTaskList()
				.addTask(new Task("task1"));
		Task task2 = new Task("task2");
		task2.setCompleted(true);
		Timeflecks.getSharedApplication().getTaskList().addTask(task2);

		JFrame frame = new JFrame();
		TaskListTablePanel tltp = new TaskListTablePanel(
				new TaskListTableModel());
		// TODO Is this right?

		frame.getContentPane().add(tltp, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	/*
	 * Handles a double click in the task list
	 */
	public void mouseClicked(MouseEvent e) {
	    if (e.getClickCount() == 2) {
	      JTable target = (JTable)e.getSource();
	      int row = target.getSelectedRow();
	      int column = target.getSelectedColumn();
	      // do some action if appropriate column
	      NewTaskPanel p = new NewTaskPanel(Timeflecks
					.getSharedApplication().getTaskList().getTasks()
					.get(row));
			p.displayFrame();
	    }
	  }
	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
