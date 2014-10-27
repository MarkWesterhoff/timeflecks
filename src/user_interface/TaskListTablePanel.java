package user_interface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import logging.GlobalLogger;
import core.Task;
import core.Timeflecks;

public class TaskListTablePanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final int MIN_COMPLETED_COLUMN_WIDTH = 60;
	private static final int PREFERRED_COMPLETED_COLUMN_WIDTH = 40;
	private static final int MIN_NAME_COLUMN_WIDTH = 30;
	private static final int PREFERRED_NAME_COLUMN_WIDTH = 200;

	private final JTable table;

	private LinkedHashMap<String, Comparator<Task>> comboMap;
	private JButton newButton;
	private JButton editButton;
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
		JButton newTaskButton = new JButton("New Task");
		JButton editTaskButton = new JButton("Edit Task");

		newTaskButton.setActionCommand("New Task");
		editTaskButton.setActionCommand("Edit Task");

		newTaskButton.addActionListener(this);
		editTaskButton.addActionListener(this);

		JPanel newButtonPanel = new JPanel();

		// layout.setVgap(8);

		newButtonPanel.setLayout(layout);

		newButtonPanel.add(newTaskButton);
		newButtonPanel.add(editTaskButton);

		topPanel.add(newButtonPanel, BorderLayout.WEST);

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
				"refresh()", "Refreshing TaskListTableModel");
		Timeflecks.getSharedApplication().getTaskList().sort();
		table.invalidate();
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
			}
		}
		else if (e.getActionCommand().equals("New Task"))
		{
			// TODO
			GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
					"actionPerformed(ActionEvent)",
					"New task button pressed. Bringing up NewTaskPanel.");
		}
		else if (e.getActionCommand().equals("Edit Task"))
		{
			// TODO
			GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
					"actionPerformed(ActionEvent)",
					"Edit task button pressed. Bringing up EditTaskPanel.");
		}
		else
		{
			GlobalLogger.getLogger().logp(Level.WARNING, "TaskListTablePanel",
					"actionPerformed()",
					"Action command " + e.getActionCommand() + " not found");
		}
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
				new TaskListTableModel(Timeflecks.getSharedApplication()
						.getTaskList()));

		frame.getContentPane().add(tltp, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
