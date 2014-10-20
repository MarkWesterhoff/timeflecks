package user_interface;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import core.TaskList;

public class TaskListTablePanel extends JPanel{
	private static final long serialVersionUID = 1L;

	public TaskListTablePanel(TableModel tableModel) {
		super();
		
		final JTable table = new JTable(tableModel);
		JScrollPane scroll = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		add(scroll);
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
