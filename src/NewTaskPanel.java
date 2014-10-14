import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.border.EmptyBorder;

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

		JLabel taskNameLabel = new JLabel("Name");
		taskNameLabel.setHorizontalAlignment(JLabel.LEFT);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		gc.insets = new Insets(4, 12, 0, 8);

		centerPanel.add(taskNameLabel, gc);

		gc.ipadx = 0;
		gc.ipady = 0;

		JTextField taskNameField = new JTextField(30);
		taskNameLabel.setLabelFor(taskNameField); // Accessibility

		gc.gridx = 0;
		gc.gridy = 1;
		gc.insets = new Insets(0, 8, 0, 8);

		centerPanel.add(taskNameField, gc);

		JLabel taskDescriptionLabel = new JLabel("Description");

		gc.gridx = 0;
		gc.gridy = 2;
		gc.insets = new Insets(4, 12, 0, 8);

		centerPanel.add(taskDescriptionLabel, gc);

		JTextField taskDescriptionField = new JTextField(30);
		taskDescriptionLabel.setLabelFor(taskDescriptionField); // Accessibility

		gc.gridx = 0;
		gc.gridy = 3;
		gc.insets = new Insets(0, 8, 0, 8);

		centerPanel.add(taskDescriptionField, gc);

		// Possibly going to use JDatePicker from here:
		// http://www.codejava.net/java-se/swing/how-to-use-jdatepicker-to-display-calendar-component
		// This could give us our open source library

		JLabel taskStartDateLabel = new JLabel("Start Date");

		gc.gridx = 0;
		gc.gridy = 4;
		gc.insets = new Insets(4, 12, 0, 8);

		centerPanel.add(taskStartDateLabel, gc);

		JTextField taskStartDateField = new JTextField(30);
		taskStartDateLabel.setLabelFor(taskStartDateField); // Accessibility

		gc.gridx = 0;
		gc.gridy = 5;
		gc.insets = new Insets(0, 8, 0, 8);

		centerPanel.add(taskStartDateField, gc);

		// Due Date Support
		JLabel taskDueDateLabel = new JLabel("Due Date");

		gc.gridx = 0;
		gc.gridy = 6;
		gc.insets = new Insets(4, 12, 0, 8);

		centerPanel.add(taskDueDateLabel, gc);

		JTextField taskDueDateField = new JTextField(30);
		taskDueDateLabel.setLabelFor(taskDueDateField); // Accessibility

		gc.gridx = 0;
		gc.gridy = 7;
		gc.insets = new Insets(0, 8, 0, 8);

		centerPanel.add(taskDueDateField, gc);

		JLabel taskDurationLabel = new JLabel("Duration");

		gc.gridx = 0;
		gc.gridy = 8;
		gc.insets = new Insets(4, 12, 0, 8);

		centerPanel.add(taskDurationLabel, gc);

		JComboBox<String> taskDurationComboBox = new JComboBox<String>();
		taskDurationComboBox.addItem("Not Set");
		for (int i = 1; i <= 300; i++)
		{
			if (i == 1)
			{
				taskDurationComboBox.addItem(new String(i + " Minute"));
			}
			else
			{
				taskDurationComboBox.addItem(new String(i + " Minutes"));
			}
		}

		taskDurationLabel.setLabelFor(taskDurationComboBox); // Accessibility

		gc.gridx = 0;
		gc.gridy = 9;
		gc.insets = new Insets(0, 8, 0, 8);

		centerPanel.add(taskDurationComboBox, gc);

		// Priority Support?

		JLabel taskPriorityLabel = new JLabel("Priority");

		gc.gridx = 0;
		gc.gridy = 10;
		gc.insets = new Insets(4, 12, 0, 8);

		centerPanel.add(taskPriorityLabel, gc);

		JComboBox<String> taskPriorityComboBox = new JComboBox<String>();
		taskPriorityComboBox.addItem("Not Set");
		taskPriorityComboBox.addItem("High");
		taskPriorityComboBox.addItem("Medium");
		taskPriorityComboBox.addItem("Low");

		taskPriorityLabel.setLabelFor(taskPriorityComboBox); // Accessibility

		gc.gridx = 0;
		gc.gridy = 11;
		gc.insets = new Insets(0, 8, 0, 8);

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
