package user_interface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import core.Task;
import core.TaskList;

public class TaskListTablePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final JTable table;
	
	private HashMap<String,Comparator<Task>> comboMap;

	public TaskListTablePanel(TableModel tableModel) {
		super();
		
		// construct the JComboBox
		comboMap = new HashMap<String, Comparator<Task>>();
		comboMap.put("Name", Task.nameComparator);
		comboMap.put("Due date", Task.dueDateComparator);
		comboMap.put("Manual", Task.manualComparator);
		comboMap.put("Priority", Task.priorityComparator);
		JComboBox<String> sortList = new JComboBox<String>(comboMap.keySet().toArray(new String[comboMap.size()]));
		sortList.addActionListener(this);
		add(sortList);
		
		table = new JTable(tableModel);
		JScrollPane scroll = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		add(scroll);
	}
	
	
	public void refresh() {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
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

	//handle changing of SortList
	public void actionPerformed(ActionEvent e)
	{
		JComboBox<String> box = (JComboBox<String>)(e.getSource());
		String switchOn = (String)box.getSelectedItem();
		TaskList.getInstance().sortTasks(comboMap.get(switchOn));
		refresh();
	}
}
