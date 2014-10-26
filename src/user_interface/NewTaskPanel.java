package user_interface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logging.GlobalLogger;

import com.toedter.calendar.JDateChooser;

import core.Task;
import core.TaskList;

public class NewTaskPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private Logger logger;

	private JLabel titleLabel, taskNameLabel, taskStartDateLabel,
			taskDueDateLabel, taskDurationLabel, taskPriorityLabel,
			taskDescriptionLabel;
	private JTextField taskNameField;
	private JDateChooser startDateChooser, dueDateChooser;
	private SpinnerModel hourModel, minuteModel, secondModel;
	private JComboBox<String> taskPriorityComboBox;
	private JTextArea taskDescriptionArea;

	private JButton saveButton, cancelButton;

	public NewTaskPanel()
	{
		super();

		logger = GlobalLogger.getLogger();

		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(350, 395));

		logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel",
				"Beginning interface setup");

		// Title Label

		titleLabel = new JLabel("New Task");
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setBorder(new EmptyBorder(10, 4, 4, 4));

		Font font = titleLabel.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		titleLabel.setFont(boldFont);

		this.add(titleLabel, BorderLayout.NORTH);

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

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(taskNameField, gc);

		logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel",
				"Added name field");

		// Using JCalendar from here:
		// http://toedter.com/jcalendar/
		// (LGPL, http://www.gnu.org/licenses/lgpl.html)
		//
		// This is our open source library

		taskStartDateLabel = new JLabel("Start Date");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskStartDateLabel, gc);

		startDateChooser = new JDateChooser(null, "MM/dd/yyyy hh:mm:ss a");

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

		dueDateChooser = new JDateChooser(null, "MM/dd/yyyy hh:mm:ss a");

		dueDateChooser.setMinimumSize(new Dimension(175, dueDateChooser
				.getMinimumSize().height));
		dueDateChooser.setPreferredSize(new Dimension(330, dueDateChooser
				.getPreferredSize().height));

		taskStartDateLabel.setLabelFor(dueDateChooser); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(dueDateChooser, gc);

		logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel",
				"Added date choosers");

		// Duration support

		taskDurationLabel = new JLabel("Duration");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDurationLabel, gc);

		// The duration picker, hours, minutes, and seconds

		JLabel hours = new JLabel(" hrs  ");
		JLabel minutes = new JLabel(" mins  ");
		JLabel seconds = new JLabel(" secs");

		hourModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		minuteModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		secondModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);

		JSpinner hourSpinner = new JSpinner(hourModel);
		JSpinner minuteSpinner = new JSpinner(minuteModel);
		JSpinner secondSpinner = new JSpinner(secondModel);

		hourSpinner.setMinimumSize(new Dimension(25, hourSpinner
				.getMinimumSize().height));
		hourSpinner.setPreferredSize(new Dimension(75, hourSpinner
				.getPreferredSize().height));

		minuteSpinner.setMinimumSize(new Dimension(25, minuteSpinner
				.getMinimumSize().height));
		minuteSpinner.setPreferredSize(new Dimension(75, minuteSpinner
				.getPreferredSize().height));

		secondSpinner.setMinimumSize(new Dimension(25, secondSpinner
				.getMinimumSize().height));
		secondSpinner.setPreferredSize(new Dimension(75, secondSpinner
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
		durationPanel.add(secondSpinner);
		durationPanel.add(seconds);

		taskDurationLabel.setLabelFor(durationPanel);

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(durationPanel, gc);

		logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel",
				"Added duration pickers");

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

		taskPriorityLabel.setLabelFor(taskPriorityComboBox); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(taskPriorityComboBox, gc);

		logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel",
				"Added priority drop down");

		// Description
		taskDescriptionLabel = new JLabel("Description");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDescriptionLabel, gc);

		taskDescriptionArea = new JTextArea(4, 30);
		taskDescriptionArea.setLineWrap(true);
		taskDescriptionArea.setEditable(true);

		JScrollPane scrollPane = new JScrollPane(taskDescriptionArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		taskDescriptionLabel.setLabelFor(scrollPane); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(scrollPane, gc);

		logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel",
				"Added text area for description");

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

		this.add(subpanel, BorderLayout.SOUTH);

	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Save"))
		{
			if (taskNameField.getText().length() == 0)
			{
				logger.logp(Level.INFO, "NewTaskPanel", "actionPerformed",
						"Save button pressed. Missing required name.");

				JOptionPane.showMessageDialog(this,
						"You must specify a name for this task.",
						"Name Required", JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				logger.logp(Level.INFO, "NewTaskPanel", "actionPerformed",
						"Save button pressed. Saving task.");

				Task task = new Task(taskNameField.getText());

				if (startDateChooser.getDate() != null)
				{
					task.setStartTime(startDateChooser.getDate());
				}

				if (dueDateChooser.getDate() != null)
				{
					task.setDueDate(dueDateChooser.getDate());
				}

				long duration = 0;
				duration += (Integer) secondModel.getValue() * 1000;
				duration += (Integer) minuteModel.getValue() * 60 * 1000;
				duration += (Integer) hourModel.getValue() * 60 * 60 * 1000;

				// long seconds = timeInMilliSeconds / 1000;
				// long minutes = seconds / 60;
				// long hours = minutes / 60;
				// long days = hours / 24;
				// String time = days + ":" + hours % 24 + ":" + minutes %
				// 60 + ":" + seconds % 60;

				if (duration != 0)
				{
					task.setDuration(duration);
				}

				if (taskPriorityComboBox.getSelectedIndex() != 0)
				{
					if (taskPriorityComboBox.getSelectedIndex() == 1)
					{
						task.setPriority(Task.HIGH_PRIORITY);
					}
					else if (taskPriorityComboBox.getSelectedIndex() == 2)
					{
						task.setPriority(Task.MEDIUM_PRIORITY);
					}
					else if (taskPriorityComboBox.getSelectedIndex() == 3)
					{
						task.setPriority(Task.LOW_PRIORITY);
					}
				}

				if (taskDescriptionArea.getText().length() != 0)
				{
					task.setDescription(taskDescriptionArea.getText());
				}

				// Actually create the task and add it to the list
				TaskList instance = TaskList.getInstance();

				instance.addTask(task);
				logger.logp(Level.INFO, "NewTaskPanel", "actionPerformed",
						"Added task to TaskList.\n" + task);

				try
				{
					task.saveToDatabase();
					logger.logp(Level.INFO, "NewTaskPanel", "actionPerformed",
							"Saved task to database.");
				}
				catch (SQLException a)
				{
					logger.logp(
							Level.WARNING,
							"NewTaskPanel",
							"actionPerformed",
							"SQLException caught when saving task to database.\nSQL State:\n"
									+ a.getSQLState() + "\nMessage:\n"
									+ a.getMessage());

					JOptionPane
							.showMessageDialog(
									this,
									"Database Error. (1302)\nYour task was not saved. Please try again, or check your database file.",
									"Database Error", JOptionPane.ERROR_MESSAGE);

					// TODO Auto-generated catch block
					a.printStackTrace();
				}
				catch (IOException a)
				{
					logger.logp(Level.WARNING, "NewTaskPanel",
							"actionPerformed",
							"IOException caught when saving task to database.\nMessage:\n"
									+ a.getLocalizedMessage());

					// Trouble serializing objects
					JOptionPane
							.showMessageDialog(
									this,
									"Object Serialization Error. (1303)\nYour task was not saved. Please try again, or check your database file.",
									"Database Error", JOptionPane.ERROR_MESSAGE);
				}

				dismissPane();
			}
		}
		else if (e.getActionCommand().equals("Cancel"))
		{
			logger.logp(Level.INFO, "NewTaskPanel", "actionPerformed",
					"Cancel button pressed. Dismissing Panel.");

			if (hasEnteredText())
			{
				logger.logp(Level.INFO, "NewTaskPanel", "actionPerformed",
						"Task modified. Prompting user.");

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
					logger.logp(Level.INFO, "NewTaskPanel", "actionPerformed",
							"User elected to discard the task. Dismissing Panel.");

					dismissPane();
				}
				else
				{
					// We simply return the user to editing
					logger.logp(Level.INFO, "NewTaskPanel", "actionPerformed",
							"User selected to continue editing the task.");
				}
			}
			else
			{
				dismissPane();
			}
		}
		else
		{

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
			;
		}

		if (dueDateChooser.getDate() != null)
		{
			returnBool = true;
		}

		long duration = 0;
		duration += (Integer) secondModel.getValue() * 1000;
		duration += (Integer) minuteModel.getValue() * 60 * 1000;
		duration += (Integer) hourModel.getValue() * 60 * 60 * 1000;

		if (duration != 0)
		{
			returnBool = true;
		}

		if (taskPriorityComboBox.getSelectedIndex() != 0)
		{
			returnBool = true;
		}

		if (taskDescriptionArea.getText().length() != 0)
		{
			returnBool = true;
		}

		return returnBool;
	}

	public void dismissPane()
	{
		JComponent parent = (JComponent) getParent();

		if (parent != null)
		{
			Container toplevel = parent.getTopLevelAncestor();
			parent.remove(this);
			toplevel.validate();
			toplevel.repaint();
		}
	}

	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{

				// Non-Nimbus works better for much of the layout here.
				// Unfortunately.

				// // Set the look and feel to Nimbus
				// try
				// {
				// for (LookAndFeelInfo info : UIManager
				// .getInstalledLookAndFeels())
				// {
				// if ("Nimbus".equals(info.getName()))
				// {
				// UIManager.setLookAndFeel(info.getClassName());
				// break;
				// }
				// }
				// }
				// catch (Exception e)
				// {
				// // If Nimbus is not available, you can set the GUI to
				// // another look and feel.
				//
				// // TODO: Remove this
				// System.err
				// .println("ERROR: Nimbus not found. Using default look and feel");
				// }

				try
				{
					JFrame newFrame = new JFrame("Timeflecks - Add New Task");
					Container c = newFrame.getContentPane();

					NewTaskPanel p = new NewTaskPanel();

					c.add(p, BorderLayout.CENTER);

					newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					newFrame.pack();

					// newFrame.setSize(374, 450);
					newFrame.setAutoRequestFocus(true);
					newFrame.setResizable(true);

					newFrame.setVisible(true);

				}
				catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Unable to " + e);
				}
			}
		});
	}

}
