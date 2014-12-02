package user_interface;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logging.GlobalLogger;

import com.toedter.calendar.JDateChooser;

import core.*;

public class NewTaskPanel extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private Task taskToEdit;

	private JLabel titleLabel, taskNameLabel, taskStartDateLabel,
			taskDueDateLabel, taskDurationLabel, taskPriorityLabel,
			taskDescriptionLabel, tagListLabel;
	private JTextField taskNameField, tagListField;
	private JDateChooser startDateChooser, dueDateChooser;
	private SpinnerModel hourModel, minuteModel;
	private JComboBox<String> taskPriorityComboBox;
	private JTextArea taskDescriptionArea;
	private JLabel recurrenceLabel;
	private JDateChooser recurrenceEndDateChooser;
	private JComboBox<String> repeatComboBox;

	private JButton saveButton, cancelButton;

	public NewTaskPanel()
	{
		this(null);
	}

	public NewTaskPanel(Task toEdit)
	{
		super();

		if (toEdit != null)
		{
			taskToEdit = toEdit;
		}

		this.getContentPane().setLayout(new BorderLayout());

		// Set the size of the panel. Note one is setting content pane and one
		// is setting frame.

		if (taskToEdit != null)
		{
			// Smaller pane
			// OS specific sizing is necessary because UI elements are different
			// sizes based upon platform.
			if (Timeflecks.isMac())
			{
				this.getContentPane().setPreferredSize(new Dimension(370, 445));
				this.setMinimumSize(new Dimension(400, 490));
			} else
			{
				this.getContentPane().setPreferredSize(new Dimension(350, 405));
				this.setMinimumSize(new Dimension(380, 450));
			}
		} else
		{
			// OS specific sizing is necessary because UI elements are different
			// sizes based upon platform.
			if (Timeflecks.isMac())
			{
				this.getContentPane().setPreferredSize(new Dimension(380, 500));
				this.setMinimumSize(new Dimension(406, 532));
			} else
			{
				this.getContentPane().setPreferredSize(new Dimension(350, 450));
				this.setMinimumSize(new Dimension(380, 495));
			}
		}

		GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
				"NewTaskPanel", "Beginning interface setup");

		// Title Label

		// addTitleLabel();

		// Center will be the forms, in a panel with FlowLayout (default, but we
		// set it for clarity)

		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout()); // Going to be flexible with
													// this layout
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.LAST_LINE_START;

		Insets labelInsets = new Insets(8, 8, 0, 8); // First number could be 4,
														// 12
		Insets fieldInsets = new Insets(0, 8, 0, 8);

		// TODO Put ++ instead of manual stuff

		taskNameLabel = new JLabel("Name");
		taskNameLabel.setHorizontalAlignment(JLabel.LEFT);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		gc.insets = labelInsets;

		centerPanel.add(taskNameLabel, gc);

		gc.ipadx = 0;
		gc.ipady = 0;

		taskNameField = new JTextField(30);
		taskNameLabel.setLabelFor(taskNameField); // Accessibility

		if (taskToEdit != null)
		{
			taskNameField.setText(taskToEdit.getName());
		}

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(taskNameField, gc);

		GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
				"NewTaskPanel", "Added name field");

		// Using JCalendar from here:
		// http://toedter.com/jcalendar/
		// (LGPL, http://www.gnu.org/licenses/lgpl.html)
		//
		// This is our open source library

		taskStartDateLabel = new JLabel("Start Date");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskStartDateLabel, gc);

		if (taskToEdit != null)
		{
			startDateChooser = new JDateChooser(taskToEdit.getStartTime(),
					"MM/dd/yyyy hh:mm a");
		} else
		{
			startDateChooser = new JDateChooser(null, "MM/dd/yyyy hh:mm a");
		}

		startDateChooser.setMinimumSize(new Dimension(175, startDateChooser
				.getMinimumSize().height));
		startDateChooser.setPreferredSize(new Dimension(330, startDateChooser
				.getPreferredSize().height));

		taskStartDateLabel.setLabelFor(startDateChooser); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(startDateChooser, gc);

		// Due Date Support
		taskDueDateLabel = new JLabel("Due Date");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDueDateLabel, gc);

		if (taskToEdit != null)
		{
			dueDateChooser = new JDateChooser(taskToEdit.getDueDate(),
					"MM/dd/yyyy hh:mm a");
		} else
		{
			dueDateChooser = new JDateChooser(null, "MM/dd/yyyy hh:mm a");
		}

		// HERE

		dueDateChooser.setMinimumSize(new Dimension(175, dueDateChooser
				.getMinimumSize().height));
		dueDateChooser.setPreferredSize(new Dimension(330, dueDateChooser
				.getPreferredSize().height));

		taskDueDateLabel.setLabelFor(dueDateChooser); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(dueDateChooser, gc);

		GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
				"NewTaskPanel", "Added date choosers");

		// Duration support

		taskDurationLabel = new JLabel("Duration");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDurationLabel, gc);

		// The duration picker, hours, minutes, and seconds

		JLabel hours = new JLabel(" hrs  ");
		JLabel minutes = new JLabel(" mins  ");

		if (taskToEdit != null)
		{
			long duration = taskToEdit.getDuration();
			int durationMins = (int) ((duration / 1000 / 60) % 60);
			int durationHours = (int) ((duration / 1000 / 60 / 60) % 24);

			// NOTE: Days go right off the end

			hourModel = new SpinnerNumberModel(durationHours, 0, 23, 1);
			minuteModel = new SpinnerNumberModel(durationMins, 0, 59, 1);
		} else
		{
			hourModel = new SpinnerNumberModel(0, 0, 23, 1);
			minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
		}

		JSpinner hourSpinner = new JSpinner(hourModel);
		JSpinner minuteSpinner = new JSpinner(minuteModel);

		hourSpinner.setMinimumSize(new Dimension(25, hourSpinner
				.getMinimumSize().height));
		hourSpinner.setPreferredSize(new Dimension(120, hourSpinner
				.getPreferredSize().height));

		minuteSpinner.setMinimumSize(new Dimension(25, minuteSpinner
				.getMinimumSize().height));
		minuteSpinner.setPreferredSize(new Dimension(120, minuteSpinner
				.getPreferredSize().height));

		JPanel durationPanel = new JPanel();
		FlowLayout panelLayout = new FlowLayout();
		panelLayout.setHgap(0);
		panelLayout.setVgap(0);
		durationPanel.setLayout(panelLayout);

		durationPanel.add(hourSpinner);
		durationPanel.add(hours);
		durationPanel.add(minuteSpinner);
		durationPanel.add(minutes);

		taskDurationLabel.setLabelFor(durationPanel);

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(durationPanel, gc);

		GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
				"NewTaskPanel", "Added duration pickers");

		// Priority Support

		taskPriorityLabel = new JLabel("Priority");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskPriorityLabel, gc);

		taskPriorityComboBox = new JComboBox<String>();
		taskPriorityComboBox.addItem("Not Set");
		taskPriorityComboBox.addItem("High");
		taskPriorityComboBox.addItem("Medium");
		taskPriorityComboBox.addItem("Low");

		if (taskToEdit != null)
		{
			if (taskToEdit.getPriority() == Priority.HIGH_PRIORITY)
			{
				taskPriorityComboBox.setSelectedItem("High");
			} else if (taskToEdit.getPriority() == Priority.MEDIUM_PRIORITY)
			{
				taskPriorityComboBox.setSelectedItem("Medium");
			} else if (taskToEdit.getPriority() == Priority.LOW_PRIORITY)
			{
				taskPriorityComboBox.setSelectedItem("Low");
			} else if (taskToEdit.getPriority() == Priority.NO_PRIORITY_SELECTED)
			{
				taskPriorityComboBox.setSelectedItem("Not Set");
			}
		}

		taskPriorityLabel.setLabelFor(taskPriorityComboBox); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(taskPriorityComboBox, gc);

		GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
				"NewTaskPanel", "Added priority drop down");

		// TAGS -- start with semicolon separation
		tagListLabel = new JLabel("Tags");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(tagListLabel, gc);

		tagListField = new JTextField(30);
		tagListLabel.setLabelFor(tagListField); // Accessibility

		if (taskToEdit != null)
		{
			tagListField.setText(taskToEdit.getTagsAsString());
		}

		gc.gridy++;
		gc.insets = fieldInsets;
		centerPanel.add(tagListField, gc);

		// Recurrence support

		if (taskToEdit == null)
		{
			recurrenceLabel = new JLabel("Repeat");

			gc.gridy++;
			gc.insets = labelInsets;

			centerPanel.add(recurrenceLabel, gc);

			repeatComboBox = new JComboBox<String>();
			repeatComboBox.addItem("Don't Repeat");
			repeatComboBox.addItem("Daily Until");
			repeatComboBox.addItem("Weekly Until");
			repeatComboBox.addItem("Every Weekday Until");
			repeatComboBox.addItem("Monthly Until");
			repeatComboBox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					recurrenceEndDateChooser.setVisible(repeatComboBox
							.getSelectedIndex() > 0);
				}
			});

			recurrenceLabel.setLabelFor(repeatComboBox); // Accessibility

			GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
					"NewTaskPanel", "Added recurrence drop down");

			JPanel recurrencePanel = new JPanel();
			recurrencePanel.setLayout(panelLayout);

			recurrencePanel.add(repeatComboBox);
			recurrencePanel.add(new JLabel("  "));

			recurrenceEndDateChooser = new JDateChooser(null, "MM/dd/yyyy");
			recurrenceEndDateChooser.setVisible(false);
			recurrenceEndDateChooser.setMinimumSize(new Dimension(120,
					startDateChooser.getMinimumSize().height));
			recurrenceEndDateChooser.setPreferredSize(new Dimension(120,
					startDateChooser.getPreferredSize().height));

			recurrenceLabel.setLabelFor(recurrenceEndDateChooser); // Accessibility

			recurrencePanel.add(recurrenceEndDateChooser);

			gc.gridy++;
			gc.insets = fieldInsets;

			centerPanel.add(recurrencePanel, gc);
		}

		// Description
		taskDescriptionLabel = new JLabel("Description");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDescriptionLabel, gc);

		taskDescriptionArea = new JTextArea(4, 30);
		taskDescriptionArea.setLineWrap(true);
		taskDescriptionArea.setEditable(true);

		if (taskToEdit != null)
		{
			taskDescriptionArea.setText(taskToEdit.getDescription());
		}

		JScrollPane scrollPane = new JScrollPane(taskDescriptionArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		taskDescriptionLabel.setLabelFor(scrollPane); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(scrollPane, gc);

		GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
				"NewTaskPanel", "Added text area for description");

		// Tags support goes here

		this.add(centerPanel, BorderLayout.CENTER);

		// Save and cancel button

		saveButton = new JButton("Save");
		cancelButton = new JButton("Cancel");

		saveButton.setActionCommand("Save");
		cancelButton.setActionCommand("Cancel");

		saveButton.addActionListener(this);

		cancelButton.addActionListener(this);

		JPanel subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout());
		subpanel.setBorder(new EmptyBorder(10, 12, 10, 12));

		subpanel.add(saveButton, BorderLayout.EAST);
		subpanel.add(cancelButton, BorderLayout.WEST);

		this.getContentPane().add(subpanel, BorderLayout.SOUTH);

	}

	@SuppressWarnings("unused")
	private void addTitleLabel()
	{
		if (taskToEdit == null)
		{
			titleLabel = new JLabel("New Task");
		} else
		{
			titleLabel = new JLabel("Edit Task");
		}

		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setBorder(new EmptyBorder(10, 4, 4, 4));

		Font font = titleLabel.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		titleLabel.setFont(boldFont);

		this.getContentPane().add(titleLabel, BorderLayout.NORTH);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Save"))
		{
			if (taskNameField.getText().length() == 0)
			{
				GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
						"actionPerformed",
						"Save button pressed. Missing required name.");

				JOptionPane.showMessageDialog(this,
						"You must specify a name for this task.",
						"Name Required", JOptionPane.WARNING_MESSAGE);
			} else
			{
				GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
						"actionPerformed", "Save button pressed. Saving task.");

				Task task;
				if (taskToEdit != null)
				{
					task = taskToEdit;
					task.setName(taskNameField.getText());
				} else
				{
					task = new Task(taskNameField.getText());
				}

				// We stop checking for null here so that we can remove dates.

				task.setStartTime(startDateChooser.getDate());

				task.setDueDate(dueDateChooser.getDate());

				long duration = 0;
				duration += (Integer) minuteModel.getValue() * 60 * 1000;
				duration += (Integer) hourModel.getValue() * 60 * 60 * 1000;

				// long seconds = timeInMilliSeconds / 1000;
				// long minutes = seconds / 60;
				// long hours = minutes / 60;
				// long days = hours / 24;
				// String time = days + ":" + hours % 24 + ":" + minutes %
				// 60 + ":" + seconds % 60;

				// Allow you to zero out the duration if you're editing a task
				// that already exists
				if (duration != 0 || taskToEdit != null)
				{
					task.setDuration(duration);
				}

				if (taskPriorityComboBox.getSelectedIndex() != 0)
				{
					if (taskPriorityComboBox.getSelectedIndex() == 1)
					{
						task.setPriority(Priority.HIGH_PRIORITY);
					} else if (taskPriorityComboBox.getSelectedIndex() == 2)
					{
						task.setPriority(Priority.MEDIUM_PRIORITY);
					} else if (taskPriorityComboBox.getSelectedIndex() == 3)
					{
						task.setPriority(Priority.LOW_PRIORITY);
					}
				} else
				{
					task.setPriority(Priority.NO_PRIORITY_SELECTED);
				}

				// If there are not tags present, this will give an empty
				// ArrayList<String>, which is what we want to set anyways.
				ArrayList<String> tags = new ArrayList<String>(
						Arrays.asList(tagListField.getText().split(",")));

				// Parse the list of tags
				for (int i = 0; i < tags.size();)
				{
					String tag = tags.get(i);

					// Remove leading and trailing whitespace
					tag = tag.trim();

					// Only add if not blank
					if (tag.equals(""))
					{
						tags.remove(i);
					} else
					{
						tags.set(i, tag);
						i++;
					}
				}

				task.setTags(tags);

				task.setDescription(taskDescriptionArea.getText());

				// Now the task is all set up, let's add however many tasks we
				// need to.

				ArrayList<Task> tasks = new ArrayList<Task>();

				if (repeatComboBox != null
						&& repeatComboBox.getSelectedIndex() != 0)
				{
					Date endDate = recurrenceEndDateChooser.getDate();
					if (endDate != null)
					{
						// Set the time on the date to 11:59:59 PM so that it
						// will
						// recur until the end of that date set.
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(endDate);

						calendar.set(Calendar.HOUR_OF_DAY, 23);
						calendar.set(Calendar.MINUTE, 59);
						calendar.set(Calendar.SECOND, 59);

						endDate = calendar.getTime();

						Recurrence r;

						try
						{
							if (repeatComboBox.getSelectedIndex() == 1)
							{
								r = new DailyRecurrence(task, endDate);
								tasks = r.getTasks();
							} else if (repeatComboBox.getSelectedIndex() == 2)
							{
								r = new WeeklyRecurrence(task, endDate);
								tasks = r.getTasks();
							} else if (repeatComboBox.getSelectedIndex() == 3)
							{
								r = new WeekDayRecurrence(task, endDate);
								tasks = r.getTasks();
							} else if (repeatComboBox.getSelectedIndex() == 4)
							{
								r = new MonthlyRecurrence(task, endDate);
								tasks = r.getTasks();
							}
						} catch (IllegalArgumentException e1)
						{
							// If the task did not have a due date, then we need
							// to put the user back to be editing.

							GlobalLogger
									.getLogger()
									.logp(Level.WARNING,
											"NewTaskPanel",
											"actionPerformed",
											"IllegalArgumentException for repeating task. Likely no due/start date set. Prompting user.\nException Message: "
													+ e1.getMessage());

							JOptionPane
									.showMessageDialog(
											this,
											"You must specify either a start date or a due date for a repeating task.",
											"Start or Due Date Required",
											JOptionPane.WARNING_MESSAGE);

							// Now we just return the user to editing
							return;
						}

						// Tasks are all created. Now we just need to add them
						// to the list

					} else
					{
						// We require an end date for the recurrence, so we
						// will have an error here and alert the user

						GlobalLogger
								.getLogger()
								.logp(Level.WARNING, "NewTaskPanel",
										"actionPerformed",
										"No end date specified for recurrence. Prompting user.");

						JOptionPane
								.showMessageDialog(
										this,
										"You must specify an end date for a repeating task.",
										"End Date Required",
										JOptionPane.WARNING_MESSAGE);

						// Now we just return the user to editing
						return;
					}
				} else
				{
					// We add the task to the list of tasks, since we are just
					// going to save that later.
					tasks.add(task);
				}

				// Regardless of the repeating selection, we just add everything
				// in the list.

				// Actually create the task and add it to the list
				if (taskToEdit == null)
				{
					for (Task t : tasks)
					{
						// If it already existed, we don't want to add it
						Timeflecks.getSharedApplication().getTaskList()
								.addTask(t);

						GlobalLogger.getLogger().logp(Level.INFO,
								"NewTaskPanel", "actionPerformed",
								"Added task to TaskList.\n" + t);
					}
				}

				try
				{
					for (Task t : tasks)
					{
						// Update the task.
						t.saveToDatabase();

						GlobalLogger.getLogger().logp(Level.INFO,
								"NewTaskPanel", "actionPerformed",
								"Saved task to database.\n" + t);
					}
				} catch (Exception ex)
				{
					ExceptionHandler.handleDatabaseSaveException(ex, this,
							"ActionPerformed", "1302");
				}

				Timeflecks.getSharedApplication().postNotification(
						TimeflecksEvent.CHANGED_POSSIBLE_TAGS);

				// We're done with this pane, let's get rid of it now
				dismissPane();
			}
		} else if (e.getActionCommand().equals("Cancel"))
		{
			tryToClose();
		}
	}

	public void tryToClose()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel", "tryToClose",
				"Cancel button pressed. Dismissing Panel.");

		if (hasEnteredText())
		{
			GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
					"tryToClose", "Task modified. Prompting user.");

			Object[] options = { "Discard Task", "Cancel" };
			int reply = JOptionPane
					.showOptionDialog(
							this,
							"You have edited this task. Do you want to save your changes?",
							"Task Changed", JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[1]);

			if (reply == JOptionPane.YES_OPTION)
			{
				// The user selected to discard the task
				GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
						"tryToClose",
						"User elected to discard the task. Dismissing Panel.");

				dismissPane();
			} else
			{
				// We simply return the user to editing
				GlobalLogger.getLogger().logp(Level.INFO, "NewTaskPanel",
						"tryToClose",
						"User selected to continue editing the task.");
			}
		} else
		{
			dismissPane();
		}

	}

	public boolean hasEnteredText()
	{
		boolean returnBool = false;
		if (taskNameField.getText() != null
				&& taskNameField.getText().length() > 0)
		{
			returnBool = true;
		}

		if (startDateChooser.getDate() != null)
		{
			returnBool = true;
		}

		if (dueDateChooser.getDate() != null)
		{
			returnBool = true;
		}

		long duration = 0;
		duration += (Integer) minuteModel.getValue() * 60 * 1000;
		duration += (Integer) hourModel.getValue() * 60 * 60 * 1000;

		if (duration != 0)
		{
			returnBool = true;
		}

		if (taskPriorityComboBox.getSelectedIndex() != 0)
		{
			returnBool = true;
			// If we are editing, this could be null
		}

		if (repeatComboBox != null)
		{
			if (repeatComboBox.getSelectedIndex() != 0)
			{
				returnBool = true;
			}
		}

		// Note that we don't need to check the date, because that won't show up
		// unless repeatComboBox has a different selection

		if (taskDescriptionArea.getText().length() != 0)
		{
			returnBool = true;
		}

		// We don't want to prompt if they are editing a task
		if (taskToEdit != null)
		{
			returnBool = false;
		}

		return returnBool;
	}

	public void dismissPane()
	{
		if (taskToEdit != null)
		{
			// Post notification that a Task is no longer being edited
			Timeflecks.getSharedApplication().postNotification(
					TimeflecksEvent.DISMISSED_EDIT_PANEL);
		}

		// After it is done, we need to refresh everything
		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.INVALIDATED_FILTERED_TASK_LIST);

		this.setVisible(false);
		this.dispose();
	}

	public void displayFrame()
	{
		if (taskToEdit == null)
		{
			this.setTitle("Timeflecks - New Task");
		} else
		{
			this.setTitle("Timeflecks - Edit Task");

			// Post notification that a Task is being edited
			Timeflecks.getSharedApplication().postNotification(
					TimeflecksEvent.CREATED_EDIT_PANEL);
		}
		this.addWindowListener(new WindowAdapter()
		{

			@Override
			public void windowClosing(WindowEvent e)
			{
				tryToClose();
			}
		});

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		this.pack();

		this.setAutoRequestFocus(true);
		this.setResizable(true);

		this.setVisible(true);
	}

}
