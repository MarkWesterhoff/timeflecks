package user_interface;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;

public class NewTaskPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	public NewTaskPanel()
	{
		super();

		this.setLayout(new BorderLayout());

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

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout()); // Going to be flexible with
													// this layout
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		
		Insets labelInsets = new Insets(4, 12, 0, 8);
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

		JTextField taskNameField = new JTextField(30);
		taskNameLabel.setLabelFor(taskNameField); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(taskNameField, gc);

		JLabel taskDescriptionLabel = new JLabel("Description");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDescriptionLabel, gc);

		JTextField taskDescriptionField = new JTextField(30);
		taskDescriptionLabel.setLabelFor(taskDescriptionField); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(taskDescriptionField, gc);

		// Possibly going to use JDatePicker from here:
		// http://www.codejava.net/java-se/swing/how-to-use-jdatepicker-to-display-calendar-component
		// This could give us our open source library

		JLabel taskStartDateLabel = new JLabel("Start Date");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskStartDateLabel, gc);

		JTextField taskStartDateField = new JTextField(30);
		taskStartDateLabel.setLabelFor(taskStartDateField); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(taskStartDateField, gc);

		// Due Date Support
		JLabel taskDueDateLabel = new JLabel("Due Date");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDueDateLabel, gc);

		JTextField taskDueDateField = new JTextField(30);
		taskDueDateLabel.setLabelFor(taskDueDateField); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(taskDueDateField, gc);

		JLabel taskDurationLabel = new JLabel("Duration");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskDurationLabel, gc);
		
		// The duration picker - CURRENTLY SHOWS A TIME FOR A DATE - Do we want to use a JSpinner for our date/time picker? 
		
		// Initialize to 12pm
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24); // 24 == 12 PM == 00:00:00
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Create a date model for the spinner
        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(calendar.getTime());
        
        // Create the spinner and the date editor
        JSpinner durationSpinner = new JSpinner(model);
        JSpinner.DateEditor durationPicker = new JSpinner.DateEditor(durationSpinner, "hh:mm:ss a");
        durationSpinner.setEditor(durationPicker);
		
        // Don't allow invalid typing in the field
		DateFormatter formatter = (DateFormatter)durationPicker.getTextField().getFormatter();
		formatter.setAllowsInvalid(false); // this makes what you want
		formatter.setOverwriteMode(true);
	
		taskDurationLabel.setLabelFor(durationPicker); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(durationPicker, gc);

		// Priority Support?

		JLabel taskPriorityLabel = new JLabel("Priority");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(taskPriorityLabel, gc);

		JComboBox<String> taskPriorityComboBox = new JComboBox<String>();
		taskPriorityComboBox.addItem("Not Set");
		taskPriorityComboBox.addItem("High");
		taskPriorityComboBox.addItem("Medium");
		taskPriorityComboBox.addItem("Low");

		taskPriorityLabel.setLabelFor(taskPriorityComboBox); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(taskPriorityComboBox, gc);

		// Tags support goes here

		this.add(centerPanel, BorderLayout.CENTER);

		// Save and cancel button

		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");

		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Save button pressed.");

				System.out.println("Saving task to TaskList...");

				System.out
						.println("Saving this task from TaskList to data file...");

				System.out.println("Saved.\n\n");

				// TODO: Pull each of the values from each of the inputs, check
				// those inputs, create a new task, add it to the TaskList, then
				// trigger a save of that new task to disk.
			}
		});

		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Cancel button pressed.");

				System.exit(0);
			}
		});

		JPanel subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout());
		subpanel.setBorder(new EmptyBorder(10, 12, 10, 12));

		subpanel.add(saveButton, BorderLayout.EAST);
		subpanel.add(cancelButton, BorderLayout.WEST);

		this.add(subpanel, BorderLayout.SOUTH);

	}

	
	
	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{

				try
				{
					for (LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels())
					{
						if ("Nimbus".equals(info.getName()))
						{
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				}
				catch (Exception e)
				{
					// If Nimbus is not available, you can set the GUI to
					// another look and feel.

					// TODO: Remove this
					System.err
							.println("ERROR: Nimbus not found. Using default look and feel");
				}

				try
				{
					JFrame newFrame = new JFrame("Timeflecks - Add New Task");
					Container c = newFrame.getContentPane();

					NewTaskPanel p = new NewTaskPanel();

					c.add(p, BorderLayout.CENTER);

					newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					newFrame.pack();

					newFrame.setSize(374, 400);
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
