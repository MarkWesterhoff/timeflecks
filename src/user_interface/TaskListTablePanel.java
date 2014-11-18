package user_interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import logging.GlobalLogger;
import core.Task;
import core.Timeflecks;

public class TaskListTablePanel extends JPanel
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

	public TaskListTablePanel(TaskListTableModel taskListTableModel)
	{
		super();
		Objects.requireNonNull(taskListTableModel);

		ActionListener al = new TaskPanelActionListener(this);

		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		// Combobox for Sorting
		// construct the JComboBox
		comboMap = new LinkedHashMap<String, Comparator<Task>>();
		comboMap.put("Manual", Task.manualComparator);
		comboMap.put("Name", Task.nameComparator);
		comboMap.put("Due Date", Task.dueDateComparator);
		comboMap.put("Priority", Task.priorityComparator);
		JComboBox<String> sortList = new JComboBox<String>(comboMap.keySet()
				.toArray(new String[comboMap.size()]));
		sortList.setActionCommand("dropdownsort");
		sortList.addActionListener(al);

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
		upButton = createIconedButton("resources/up.png", "Move Up", al);
		downButton = createIconedButton("resources/down.png", "Move Down", al);

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

		newTaskButton.addActionListener(al);
		editTaskButton.addActionListener(al);
		deleteTaskButton.addActionListener(al);

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
		
		/*
		// Filtering panel
		JPanel filterPanel = new JPanel(new FlowLayout());
		JTextField filterField = new JTextField();
		filterField.setPreferredSize(new Dimension(100, 20));
		
		filterPanel.add(filterField);

		add(filterPanel, BorderLayout.CENTER);
		*/
		
		// Actual table
		table = new JTable(taskListTableModel);

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
		add(scroll, BorderLayout.SOUTH);

	}

	private JButton createIconedButton(String iconPath, String buttonName,
			ActionListener al)
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
		button.setEnabled(true);
		button.addActionListener(al);
		return button;
	}

	/**
	 * Fires an event to redraw the table.
	 */
	public void refresh()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
				"refresh()", "Refreshing TaskListTableModel" + this.table);

		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	public void setBumpButtonsVisibility(boolean visibility)
	{
		upButton.setEnabled(visibility);
		downButton.setEnabled(visibility);
	}
	
	public Task getSelectedTask() {
		int row = table.getSelectedRow();
		if (row >= 0 && row < table.getRowCount())
		{
			return Timeflecks.getSharedApplication().getFilteringManager().getFilteredTaskList().get(row);
		}
		else
		{
			// This happens if there are no tasks selected

			// TODO Grey out the Edit Task Button if there are no tasks
			// selected

			GlobalLogger.getLogger().logp(Level.WARNING,
					"TaskListTablePanel", "getSelectedTask()",
					"Selected row is out of bounds for the current table.");
			return null;
		}
	}

	public JTable getTable()
	{
		return table;
	}

	public LinkedHashMap<String, Comparator<Task>> getComboMap()
	{
		return comboMap;
	}

}
