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

public class NewTaskPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private Logger logger;

	public NewTaskPanel()
	{
		super();
		
		logger = GlobalLogger.getLogger();

		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(350, 395));
		
		logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel", 
				"Beginning interface setup");

		// Title Label

		JLabel titleLabel = new JLabel("New Task");
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

		Insets labelInsets = new Insets(8, 8, 0, 8); // First number could be 4, 12
		Insets fieldInsets = new Insets(0, 8, 0, 8);

		// TODO Put ++ instead of manual stuff

		JLabel taskNameLabel = new JLabel("Name");
		taskNameLabel.setHorizontalAlignment(JLabel.LEFT);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		gc.insets = labelInsets;

		centerPanel.add(taskNameLabel, gc);

		gc.ipadx = 0;
		gc.ipady = 0;

		final JTextField taskNameField = new JTextField(30);
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

		JLabel taskStartDateLabel = new JLabel("Start Date");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskStartDateLabel, gc);
		
		final JDateChooser startDateChooser = new JDateChooser(null, "MM/dd/yyyy hh:mm:ss a");
		
		startDateChooser.setMinimumSize(new Dimension(175,startDateChooser.getMinimumSize().height));
		startDateChooser.setPreferredSize(new Dimension(330, startDateChooser.getPreferredSize().height));
		
		taskStartDateLabel.setLabelFor(startDateChooser); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(startDateChooser, gc);

		// Due Date Support
		JLabel taskDueDateLabel = new JLabel("Due Date");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDueDateLabel, gc);

		final JDateChooser dueDateChooser = new JDateChooser(null, "MM/dd/yyyy hh:mm:ss a");
		
		dueDateChooser.setMinimumSize(new Dimension(175,dueDateChooser.getMinimumSize().height));
		dueDateChooser.setPreferredSize(new Dimension(330, dueDateChooser.getPreferredSize().height));
		
		taskStartDateLabel.setLabelFor(dueDateChooser); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(dueDateChooser, gc);
		
		logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel", 
				"Added date choosers");

		JLabel taskDurationLabel = new JLabel("Duration");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDurationLabel, gc);

		// The duration picker, hours, minutes, and seconds
		
		JLabel hours = new JLabel("hrs");
		JLabel minutes = new JLabel("mins");
		JLabel seconds = new JLabel("secs");
		
		final SpinnerModel hourModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		final SpinnerModel minuteModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		final SpinnerModel secondModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		
		JSpinner hourSpinner = new JSpinner(hourModel);
		JSpinner minuteSpinner = new JSpinner(minuteModel);
		JSpinner secondSpinner = new JSpinner(secondModel);
		
		hourSpinner.setMinimumSize(new Dimension(25,hourSpinner.getMinimumSize().height));
		hourSpinner.setPreferredSize(new Dimension(75, hourSpinner.getPreferredSize().height));
		
		minuteSpinner.setMinimumSize(new Dimension(25,minuteSpinner.getMinimumSize().height));
		minuteSpinner.setPreferredSize(new Dimension(75, minuteSpinner.getPreferredSize().height));
		
		secondSpinner.setMinimumSize(new Dimension(25,secondSpinner.getMinimumSize().height));
		secondSpinner.setPreferredSize(new Dimension(75, secondSpinner.getPreferredSize().height));
		
		JPanel durationPanel = new JPanel();
		durationPanel.setLayout(new FlowLayout());
		
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

		JLabel taskPriorityLabel = new JLabel("Priority");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskPriorityLabel, gc);

		final JComboBox<String> taskPriorityComboBox = new JComboBox<String>();
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
		JLabel taskDescriptionLabel = new JLabel("Description");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDescriptionLabel, gc);

		final JTextArea taskDescriptionArea = new JTextArea(4,30);
		taskDescriptionArea.setLineWrap(true);
		taskDescriptionArea.setEditable(true);
		
		JScrollPane scrollPane = new JScrollPane (taskDescriptionArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		taskDescriptionLabel.setLabelFor(scrollPane); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(scrollPane, gc);
		
		logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel", 
				"Added text area for description");

		// Tags support goes here

		this.add(centerPanel, BorderLayout.CENTER);

		// Save and cancel button

		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");

		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (taskNameField.getText().length() == 0)
				{
					logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel", 
							"Save button pressed. Missing required name.");
					
					JOptionPane.showMessageDialog(centerPanel,
						    "You must specify a name for this task.",
						    "Name Required",
						    JOptionPane.WARNING_MESSAGE);
				}
				else 
				{
					logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel", 
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
					duration += (Integer)secondModel.getValue() * 1000;
					duration += (Integer)minuteModel.getValue() * 60 * 1000;
					duration += (Integer)hourModel.getValue() * 60 * 60 * 1000;
											
//						long seconds = timeInMilliSeconds / 1000;
//						long minutes = seconds / 60;
//						long hours = minutes / 60;
//						long days = hours / 24;
//						String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;

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
					
					TaskList instance = TaskList.getInstance();
					
					instance.addTask(task);
					logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel", 
							"Added task to TaskList.");
					
					try
					{
						task.saveToDatabase();
						logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel", 
								"Saved task to database.");
					}
					catch (SQLException e1)
					{
						logger.logp(Level.WARNING, "NewTaskPanel", "NewTaskPanel", 
								"SQLException caught when saving task to database.\nSQL State:\n" + e1.getSQLState() + "\nMessage:\n" + e1.getMessage());
						//e1.printStackTrace();
					}
					catch (IOException e1)
					{
						logger.logp(Level.WARNING, "NewTaskPanel", "NewTaskPanel", 
								"IOException caught when saving task to database.\nMessage:\n" + e1.getLocalizedMessage());
						//e1.printStackTrace();
					}
					
					dismissPane();
				}
			}
		});

		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				logger.logp(Level.INFO, "NewTaskPanel", "NewTaskPanel", 
						"Cancel button pressed. Dismissing Panel.");

				dismissPane();
			}
		});

		JPanel subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout());
		subpanel.setBorder(new EmptyBorder(10, 12, 10, 12));

		subpanel.add(saveButton, BorderLayout.EAST);
		subpanel.add(cancelButton, BorderLayout.WEST);

		this.add(subpanel, BorderLayout.SOUTH);

	}
	
	public void dismissPane() {
		JComponent parent = (JComponent) getParent();
        
        if (parent != null) {
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
				
				// Non-Nimbus works better for much of the layout here. Unfortunately. 

//				// Set the look and feel to Nimbus
//				try
//				{
//					for (LookAndFeelInfo info : UIManager
//							.getInstalledLookAndFeels())
//					{
//						if ("Nimbus".equals(info.getName()))
//						{
//							UIManager.setLookAndFeel(info.getClassName());
//							break;
//						}
//					}
//				}
//				catch (Exception e)
//				{
//					// If Nimbus is not available, you can set the GUI to
//					// another look and feel.
//
//					// TODO: Remove this
//					System.err
//							.println("ERROR: Nimbus not found. Using default look and feel");
//				}

				try
				{
					JFrame newFrame = new JFrame("Timeflecks - Add New Task");
					Container c = newFrame.getContentPane();

					NewTaskPanel p = new NewTaskPanel();

					c.add(p, BorderLayout.CENTER);

					newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					newFrame.pack();

					//newFrame.setSize(374, 450);
					newFrame.setAutoRequestFocus(true);
					newFrame.setResizable(false);

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
