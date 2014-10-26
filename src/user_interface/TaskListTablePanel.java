package user_interface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import logging.GlobalLogger;

import core.Task;
import core.TaskList;

public class TaskListTablePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final JTable table;
	
	private HashMap<String,Comparator<Task>> comboMap;
	private JButton upButton;
	private JButton downButton;
	
	private transient Logger logger;
	
	
	public TaskListTablePanel(TableModel tableModel) {
		super();
		
		logger = GlobalLogger.getLogger();

		upButton = createIconedButton("resources/up.png","bump up");
		downButton = createIconedButton("resources/down.png","bump down");

		
		// construct the JComboBox
		comboMap = new HashMap<String, Comparator<Task>>();
		comboMap.put("Name", Task.nameComparator);
		comboMap.put("Due date", Task.dueDateComparator);
		comboMap.put("Manual", Task.manualComparator);
		comboMap.put("Priority", Task.priorityComparator);
		JComboBox<String> sortList = new JComboBox<String>(comboMap.keySet().toArray(new String[comboMap.size()]));
		sortList.setActionCommand("dropdownsort");
		sortList.addActionListener(this);
		add(sortList);
		
		table = new JTable(tableModel);
		JScrollPane scroll = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		add(scroll);
		add(upButton);
		add(downButton);
	}
	
	private JButton createIconedButton(String iconPath, String buttonName) {
		ImageIcon buttonIcon = new ImageIcon(iconPath);
		JButton button;
		if(buttonIcon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
			button = new JButton(buttonIcon);
			logger.logp(Level.INFO, "TaskListTabelPanel", 
					"createIconedButton()", 
					buttonName + " icon successfully loaded");
		} else {
			button = new JButton(buttonName);
			logger.logp(Level.WARNING, "TaskListTabelPanel", 
					"TaskListTablePanel()", 
					buttonName + " icon could not be loaded");
		}
		button.setActionCommand(buttonName);
		button.setEnabled(false);
		button.addActionListener(this);
		return button;
	}
	
	public void refresh() {
		TaskList.getInstance().sort();
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}
	
	//handle changing of SortList
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand() == "dropdownsort") {
			
			JComboBox<String> box = (JComboBox<String>)(e.getSource());
			String switchOn = (String)box.getSelectedItem();
			TaskList.getInstance().setTaskComparator(comboMap.get(switchOn));
			refresh();
			if(switchOn == "Manual") {
				upButton.setEnabled(true);
				downButton.setEnabled(true);
			} else {
				upButton.setEnabled(false);
				downButton.setEnabled(false);
			}
			
		} else if(e.getActionCommand() == "bump up") {
			
			int row = table.getSelectedRow();
			if(row > 0) {
				TaskList.getInstance().getTasks().get(row).setOrdering(row-1);
				TaskList.getInstance().getTasks().get(row-1).setOrdering(row);
				refresh();
				logger.logp(Level.INFO, "TaskListTablePanel", 
						"actionPerformed()", 
						"Task in row " + row + " bumped up.");
			}
			
		} else if(e.getActionCommand() == "bump down") {
			
			int row = table.getSelectedRow();
			if(row > -1 && row < table.getRowCount() - 1) {
				TaskList.getInstance().getTasks().get(row).setOrdering(row+1);
				TaskList.getInstance().getTasks().get(row+1).setOrdering(row);
				refresh();
				logger.logp(Level.INFO, "TaskListTablePanel", 
						"actionPerformed()", 
						"Task in row " + row + " bumped down.");
			}
		} else {
			logger.logp(Level.WARNING, "TaskListTablePanel", 
						"actionPerformed()",
						"Action command " + e.getActionCommand() + 
						"not found");
		}
	}
	
	public static void main(String[] args) {
		TaskList tl = TaskList.getInstance();

		JFrame frame = new JFrame();
		TaskListTablePanel tltp = new TaskListTablePanel(new TaskListTableModel(tl));
		
		frame.getContentPane().add(tltp, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}


}
