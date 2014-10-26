package user_interface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import logging.GlobalLogger;
import core.Task;
import core.TaskList;
import core.Timeflecks;

public class TaskListTablePanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final JTable table;

	private LinkedHashMap<String, Comparator<Task>> comboMap;
	private JButton upButton;
	private JButton downButton;
	
	public TaskListTablePanel(TableModel tableModel)
	{
		super();

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
		;

		JLabel sortLabel = new JLabel("Sort By: ");

		JPanel sortSet = new JPanel();
		sortSet.setLayout(new BorderLayout()); // Could just as easily be
												// FlowLayout, same below

		sortSet.add(sortLabel, BorderLayout.WEST);
		sortSet.add(sortList, BorderLayout.EAST);

		topPanel.add(sortSet, BorderLayout.WEST);

		// Up and down bump buttons
		upButton = createIconedButton("resources/up.png", "Move Up");
		downButton = createIconedButton("resources/down.png", "Move Down");

		JPanel buttonSet = new JPanel();
		buttonSet.setLayout(new BorderLayout());

		// Goes with buttons
		buttonSet.add(upButton, BorderLayout.WEST);
		buttonSet.add(downButton, BorderLayout.EAST);

		topPanel.add(buttonSet, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);

		// Actual table
		table = new JTable(tableModel);
		JScrollPane scroll = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
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
					"createIconedButton()", buttonName
							+ " icon successfully loaded");
		}
		else
		{
			button = new JButton(buttonName);
			GlobalLogger.getLogger().logp(Level.WARNING, "TaskListTabelPanel",
					"TaskListTablePanel()", buttonName
							+ " icon could not be loaded");
		}
		button.setActionCommand(buttonName);
		button.setEnabled(false);
		button.addActionListener(this);
		return button;
	}

	public void refresh()
	{
		Timeflecks.getSharedApplication().getTaskList().sort();
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	// handle changing of SortList
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() == "dropdownsort")
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
		else if (e.getActionCommand() == "Move Up")
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
						"actionPerformed()", "Task in row " + row
								+ " bumped up.");
			}
			table.getSelectionModel().setSelectionInterval(row - 1, row - 1);
		}
		else if (e.getActionCommand() == "Move Down")
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
						"actionPerformed()", "Task in row " + row
								+ " bumped down.");
				table.getSelectionModel().setSelectionInterval(row + 1, row + 1);
			}
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
		TaskList tl = Timeflecks.getSharedApplication().getTaskList();
		JFrame frame = new JFrame();
		TaskListTablePanel tltp = new TaskListTablePanel(
				new TaskListTableModel(tl));

		frame.getContentPane().add(tltp, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
