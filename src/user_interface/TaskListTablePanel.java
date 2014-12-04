package user_interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import logging.GlobalLogger;
import core.Task;
import core.Timeflecks;
import core.TimeflecksEvent;
import core.TimeflecksEventResponder;
import dnd.TaskListTableTransferHandler;

public class TaskListTablePanel extends JPanel implements
		TimeflecksEventResponder
{
	private static final long serialVersionUID = 1L;

	private static final int MIN_COMPLETED_COLUMN_WIDTH = 80;
	private static final int PREFERRED_COMPLETED_COLUMN_WIDTH = 80;
	private static final int MIN_NAME_COLUMN_WIDTH = 400;
	private static final int PREFERRED_NAME_COLUMN_WIDTH = 400;

	private final JTable table;

	private LinkedHashMap<String, Comparator<Task>> comboMap;
	private JButton newTaskButton;
	private JButton editTaskButton;
	private JButton deleteTaskButton;
	private JButton upButton;
	private JButton downButton;
	private JList<String> tagSelector;
	private JButton clearTagButton;
	private Vector<String> tagSelectionChoices;
	private JTextField searchField;

	private int tasksBeingEdited;

	public TaskListTablePanel(TaskListTableModel taskListTableModel)
	{
		super();
		Objects.requireNonNull(taskListTableModel);

		tasksBeingEdited = 0;

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
		JComboBox<String> sortList =
				new JComboBox<String>(comboMap.keySet().toArray(
						new String[comboMap.size()]));
		sortList.setActionCommand("dropdownsort");
		sortList.addActionListener(al);

		JLabel sortLabel = new JLabel("Sort By: ");

		JPanel sortSet = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setHgap(0);
		layout.setVgap(0);
		sortSet.setLayout(layout);

		sortSet.add(sortLabel);
		sortSet.add(sortList);

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

		JLabel spacer2 = new JLabel("  ");
		newButtonPanel.add(spacer2);

		topPanel.add(newButtonPanel, BorderLayout.WEST);

		// For now, we just add a spacer
		// topPanel.setPreferredSize(new Dimension(600, 50));

		add(topPanel, BorderLayout.NORTH);

		// Tag panel components
		JLabel tagLabel = new JLabel("Filter by Tag:");

		// Multiple tag selector list
		tagSelectionChoices = new Vector<String>();
		tagSelector = new JList<String>(tagSelectionChoices);
		tagSelector
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tagSelector.addListSelectionListener(new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				GlobalLogger.getLogger().logp(Level.INFO,
						this.getClass().getName(), "valueChanged",
						"List selection has changed. Checking tags");

				JList<String> tagSelect = (JList<String>) e.getSource();
				for (int i = e.getFirstIndex(); i <= e.getLastIndex(); ++i)
				{
					String tag = tagSelectionChoices.get(i);
					if (tagSelect.isSelectedIndex(i))
					{
						Timeflecks.getSharedApplication().getFilteringManager()
								.getTagCollection().addTag(tag);
					}
					else
					{
						Timeflecks.getSharedApplication().getFilteringManager()
								.getTagCollection().removeTag(tag);
					}
				}

				// Post notification to repopulate the task list that's
				// filtered by tag
				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);
			}
		});

		refreshTagSelector();

		JScrollPane filterScrollPane = new JScrollPane(tagSelector);
		filterScrollPane.setPreferredSize(new Dimension(150, 60));

		// Clear tags button
		clearTagButton = new JButton("Clear tags");
		clearTagButton.setActionCommand("Clear tag selection");

		// Use anonymous action listener instead of TaskPanelActionListener
		// because we need access to the tagSelector.
		clearTagButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getActionCommand().equals("Clear tag selection"))
				{
					GlobalLogger.getLogger().logp(Level.INFO,
							this.getClass().getName(),
							"actionPerformed(ActionEvent)",
							"Clear tag button pressed. Clearing tag selection");
					tagSelector.clearSelection();
				}
			}
		});

		// FlowLayout JPanel for tag components
		JPanel tagPanel = new JPanel(new FlowLayout());
		tagPanel.add(tagLabel);
		tagPanel.add(filterScrollPane);
		tagPanel.add(clearTagButton);

		// Search text field
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(100, 20));
		searchField.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
						"actionPerformed(ActionEvent)",
						"Search button pressed.");

				String searchText = searchField.getText();

				// Either clearing the search or searching by the string,
				// depending on if empty or not
				String logMessage =
						(searchText.equals("")) ? "Clearing search."
								: "Searching by text \"" + searchText + "\"";
				GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
						"actionPerformed(ActionEvent)", logMessage);

				Timeflecks.getSharedApplication().getFilteringManager()
						.setSearchText(searchText);

				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);

			}

			@Override
			public void keyPressed(KeyEvent e)
			{
			}
		});

		// Search Label
		JLabel searchLabel = new JLabel("Search Tasks:");

		// Search Panel
		JPanel filterPanel = new JPanel(new FlowLayout());
		filterPanel.add(searchLabel);
		filterPanel.add(searchField);
		filterPanel.add(tagPanel);

		add(filterPanel, BorderLayout.SOUTH);

		// Actual table
		table = new JTable(taskListTableModel);
		table.setDragEnabled(true);
		table.setTransferHandler(new TaskListTableTransferHandler());
		table.setDropMode(DropMode.USE_SELECTION);

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

		// Register for Timeflecks events
		final TaskListTablePanel thisTaskListTablePanel = this;
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				Timeflecks.getSharedApplication().registerForTimeflecksEvents(
						thisTaskListTablePanel);

			}
		});
	}

	public JTextField getSearchField()
	{
		return searchField;
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
				"refresh()", "Refreshing TaskListTablePanel");

		// Enable or disable edit and delete buttons based on number of Tasks
		// currently being edited.
		updateEditAndDeleteButtons();

		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	/**
	 * Re-creates the list of tags that is used by the tag selector.
	 */
	public void refreshTagSelector()
	{
		// Find the tags that were selected before the refresh
		HashSet<String> previouslySelected = new HashSet<String>();
		for (int index : tagSelector.getSelectedIndices())
		{
			if (tagSelector.isSelectedIndex(index))
			{
				previouslySelected.add(tagSelector.getModel().getElementAt(
						index));
			}
		}

		tagSelector.clearSelection();

		// Refresh the tags in the selection list
		// Get tags and sort alphabetically
		Collection<String> tags =
				Timeflecks.getSharedApplication().getTaskList().getAllTags();
		tagSelectionChoices = new Vector<String>(tags);
		Collections.sort(tagSelectionChoices);
		tagSelector.setListData(tagSelectionChoices);
		tagSelector.clearSelection();

		// Select the tags that were selected before
		for (int i = 0; i < tagSelectionChoices.size(); ++i)
		{
			if (previouslySelected.contains(tagSelectionChoices.get(i)))
			{
				tagSelector.addSelectionInterval(i, i);
			}
		}
	}

	public void setBumpButtonsVisibility(boolean visibility)
	{
		upButton.setEnabled(visibility);
		downButton.setEnabled(visibility);
	}

	/**
	 * Gets the Tasks that have been selected based on the table rows that have
	 * been selected.
	 * 
	 * @return a list of Tasks that have been selected
	 */
	public List<Task> getSelectedTasks()
	{
		List<Task> tasks = new ArrayList<Task>();

		for (int row : this.table.getSelectedRows())
		{
			if (row >= 0 && row < table.getRowCount())
			{
				tasks.add(Timeflecks.getSharedApplication()
						.getFilteringManager().getFilteredTaskList().get(row));
			}
			else
			{
				GlobalLogger.getLogger().logp(
						Level.WARNING,
						"TaskListTablePanel",
						"getSelectedTask()",
						"Selected row " + Integer.toString(row)
								+ " is out of bounds for the current table.");
			}
		}

		return tasks;
	}

	public JTable getTable()
	{
		return table;
	}

	public LinkedHashMap<String, Comparator<Task>> getComboMap()
	{
		return comboMap;
	}

	/**
	 * Disables the Edit Task and Delete Task buttons if a task is currently
	 * being edited.
	 */
	private void updateEditAndDeleteButtons()
	{
		if (tasksBeingEdited == 0)
		{
			GlobalLogger.getLogger().logp(Level.INFO,
					this.getClass().getName(), "updateEditAndDeleteButtons()",
					"Setting Edit and Delete buttons to enabled");

			editTaskButton.setEnabled(true);
			deleteTaskButton.setEnabled(true);
		}
		else if (tasksBeingEdited > 0)
		{
			GlobalLogger.getLogger().logp(Level.INFO,
					this.getClass().getName(), "updateEditAndDeleteButtons()",
					"Setting Edit and Delete buttons to disabled");

			editTaskButton.setEnabled(false);
			deleteTaskButton.setEnabled(false);
		}
		else
		{
			GlobalLogger.getLogger().logp(
					Level.WARNING,
					this.getClass().getName(),
					"updateEditAndDeleteButtons()",
					"Invalid value for tasksBeingEdited: "
							+ Integer.toString(tasksBeingEdited));
		}
	}

	@Override
	public void eventPosted(TimeflecksEvent t)
	{
		// Changing possible tags means we must recreate the tag selector
		if (t.equals(TimeflecksEvent.CHANGED_POSSIBLE_TAGS))
		{
			GlobalLogger.getLogger().logp(Level.INFO,
					this.getClass().getName(), "eventPosted(TimeflecksEvent)",
					"Recreating tag selector");
			refreshTagSelector();
		}
		if (t.equals(TimeflecksEvent.CREATED_EDIT_PANEL))
		{
			tasksBeingEdited++;

			GlobalLogger.getLogger().logp(
					Level.INFO,
					this.getClass().getName(),
					"eventPosted(TimeflecksEvent)",
					"One more Task is being edited ("
							+ Integer.toString(tasksBeingEdited)
							+ " total Tasks)");

			updateEditAndDeleteButtons();
		}
		if (t.equals(TimeflecksEvent.DISMISSED_EDIT_PANEL))
		{
			tasksBeingEdited--;

			GlobalLogger.getLogger().logp(
					Level.INFO,
					this.getClass().getName(),
					"eventPosted(TimeflecksEvent)",
					"One less Task is being edited ("
							+ Integer.toString(tasksBeingEdited)
							+ " total Tasks)");

			updateEditAndDeleteButtons();
		}
		else
		{
			// Ignore other events
		}
	}
}
