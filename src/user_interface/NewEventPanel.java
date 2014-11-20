package user_interface;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logging.GlobalLogger;

import com.toedter.calendar.JDateChooser;

import core.*;

public class NewEventPanel extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private core.Event eventToEdit;

	private JLabel titleLabel, eventNameLabel, eventStartDateLabel,
			eventEndDateLabel, eventDescriptionLabel;
	private JTextField eventNameField;
	private JDateChooser startDateChooser, endDateChooser;
	private JTextArea eventDescriptionArea;

	private JButton saveButton, cancelButton, deleteButton;

	public NewEventPanel()
	{
		this(null);
	}

	public NewEventPanel(core.Event toEdit)
	{
		super();

		if (toEdit != null)
		{
			eventToEdit = toEdit;
		}

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().setPreferredSize(new Dimension(350, 280));

		GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
				"NewEventPanel", "Beginning interface setup");

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

		eventNameLabel = new JLabel("Name");
		eventNameLabel.setHorizontalAlignment(JLabel.LEFT);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		gc.insets = labelInsets;

		centerPanel.add(eventNameLabel, gc);

		gc.ipadx = 0;
		gc.ipady = 0;

		eventNameField = new JTextField(30);
		eventNameLabel.setLabelFor(eventNameField); // Accessibility

		if (eventToEdit != null)
		{
			eventNameField.setText(eventToEdit.getName());
		}

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(eventNameField, gc);

		GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
				"NewEventPanel", "Added name field");

		// Using JCalendar from here:
		// http://toedter.com/jcalendar/
		// (LGPL, http://www.gnu.org/licenses/lgpl.html)
		//
		// This is our open source library

		eventStartDateLabel = new JLabel("Start Date");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(eventStartDateLabel, gc);

		if (eventToEdit != null)
		{
			startDateChooser = new JDateChooser(eventToEdit.getStartTime(),
					"MM/dd/yyyy hh:mm a");
		}
		else
		{
			startDateChooser = new JDateChooser(null, "MM/dd/yyyy hh:mm a");
		}

		startDateChooser.setMinimumSize(new Dimension(175, startDateChooser
				.getMinimumSize().height));
		startDateChooser.setPreferredSize(new Dimension(330, startDateChooser
				.getPreferredSize().height));

		eventStartDateLabel.setLabelFor(startDateChooser); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(startDateChooser, gc);

		eventEndDateLabel = new JLabel("End Date");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(eventEndDateLabel, gc);

		if (eventToEdit != null)
		{
			endDateChooser = new JDateChooser(eventToEdit.getEndTime(),
					"MM/dd/yyyy hh:mm a");
		}
		else
		{
			endDateChooser = new JDateChooser(null, "MM/dd/yyyy hh:mm a");
		}

		endDateChooser.setMinimumSize(new Dimension(175, endDateChooser
				.getMinimumSize().height));
		endDateChooser.setPreferredSize(new Dimension(330, endDateChooser
				.getPreferredSize().height));

		eventStartDateLabel.setLabelFor(endDateChooser); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(endDateChooser, gc);

		GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
				"NewEventPanel", "Added end date choosers");

		// Description
		eventDescriptionLabel = new JLabel("Description");

		gc.gridy++;
		gc.insets = labelInsets;

		centerPanel.add(eventDescriptionLabel, gc);

		eventDescriptionArea = new JTextArea(4, 30);
		eventDescriptionArea.setLineWrap(true);
		eventDescriptionArea.setEditable(true);

		if (eventToEdit != null)
		{
			eventDescriptionArea.setText(eventToEdit.getDescription());
		}

		JScrollPane scrollPane = new JScrollPane(eventDescriptionArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		eventDescriptionLabel.setLabelFor(scrollPane); // Accessibility

		gc.gridy++;
		gc.insets = fieldInsets;

		centerPanel.add(scrollPane, gc);

		GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
				"NewEventPanel", "Added text area for description");

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
		((BorderLayout) subpanel.getLayout()).setHgap(50);

		subpanel.add(saveButton, BorderLayout.EAST);
		subpanel.add(cancelButton, BorderLayout.WEST);

		if(eventToEdit != null) {
			deleteButton = new JButton("Delete");
			deleteButton.setActionCommand("Delete");
			deleteButton.addActionListener(this);
			subpanel.add(deleteButton, BorderLayout.CENTER);
		}
		
		this.getContentPane().add(subpanel, BorderLayout.SOUTH);

	}

	@SuppressWarnings("unused")
	private void addTitleLabel()
	{
		if (eventToEdit == null)
		{
			titleLabel = new JLabel("New Event");
		}
		else
		{
			titleLabel = new JLabel("Edit Event");
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
			if (eventNameField.getText().length() == 0)
			{
				GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
						"actionPerformed",
						"Save button pressed. Missing required name.");

				JOptionPane.showMessageDialog(this,
						"You must specify a name for this event.",
						"Name Required", JOptionPane.WARNING_MESSAGE);
			}
			else if (startDateChooser.getDate() == null
					|| endDateChooser.getDate() == null)
			{
				GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
						"actionPerformed",
						"Save button pressed. Missing required time.");

				JOptionPane
						.showMessageDialog(
								this,
								"You must specify a start and end time for this event.",
								"Name Required", JOptionPane.WARNING_MESSAGE);
			}
			else if (startDateChooser.getDate().compareTo(
					endDateChooser.getDate()) > 0)
			{
				GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
						"actionPerformed",
						"Save button pressed. End Time before start time");

				JOptionPane.showMessageDialog(this,
						"The end time entered must be before the start time.",
						"Name Required", JOptionPane.WARNING_MESSAGE);
			}
			// order
			else
			{
				GlobalLogger.getLogger()
						.logp(Level.INFO, "NewEventPanel", "actionPerformed",
								"Save button pressed. Saving event.");

				core.Event event;
				if (eventToEdit != null)
				{
					event = eventToEdit;
					event.setName(eventNameField.getText());
					event.setStartTime(startDateChooser.getDate());
					event.setEndTime(endDateChooser.getDate());
				}
				else
				{
					event = new core.Event(eventNameField.getText(),
							startDateChooser.getDate(),
							endDateChooser.getDate());
				}

				if (eventDescriptionArea.getText().length() != 0)
				{
					event.setDescription(eventDescriptionArea.getText());
				}

				// Actually create the event and add it to the list
				if (eventToEdit == null)
				{
					// If it already existed, we don't want to add it
					Timeflecks.getSharedApplication().getTaskList()
							.addEvent(event);

					GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
							"actionPerformed",
							"Added event to EventList.\n" + event);
				}

				try
				{
					// Update the event.
					event.saveToDatabase();

					GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
							"actionPerformed", "Saved event to database.");
				}
				catch (Exception ex)
				{
					ExceptionHandler.handleDatabaseSaveException(ex, this,
							"ActionPerformed", "1302");
				}

				dismissPane();
			}
		}
		else if (e.getActionCommand().equals("Cancel"))
		{
			tryToClose();
		}
		else if (e.getActionCommand().equals("Delete"))
		{
			Object[] options = { "Delete Event", "Cancel" };

			int reply = JOptionPane.showOptionDialog(this,
					"Are you sure you wish to delete the event \"" + eventToEdit.getName()
							+ "\"?", "Confirm Delete",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[1]);

			if (reply == JOptionPane.YES_OPTION)
			{
				// The user selected to delete the Task
				GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
						"actionPerformed",
						"User elected to delete Event " + eventToEdit.getName());

				boolean removed = Timeflecks.getSharedApplication()
						.getTaskList().getEvents().remove(eventToEdit);

				if (!removed)
				{
					GlobalLogger
							.getLogger()
							.logp(Level.WARNING, "NewEventPanel",
									"actionPerformed(ActionEvent)",
									"Selected Event for deletion does not exist in application's EventList.");
				}

				try
				{
					Timeflecks.getSharedApplication().getDBConnector()
							.delete(eventToEdit.getId());
				}
				catch (Exception ex)
				{
					ExceptionHandler.handleDatabaseDeleteException(ex,
							"TaskPanelActionListener",
							"actionPerformed(ActionEvent)", "1102");
				}
			}
			else
			{
				// User selected to not delete task
				GlobalLogger.getLogger().logp(Level.INFO, "TaskListTablePanel",
						"actionPerformed(ActionEvent)",
						"User cancelled Task deletion.");
			}
			Timeflecks.getSharedApplication().postNotification(
					TimeflecksEvent.EVERYTHING_NEEDS_REFRESH); //FIX THIS
			dismissPane();
		}
	}

	public void tryToClose()
	{
		GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
				"actionPerformed", "Cancel button pressed. Dismissing Panel.");

		if (hasEnteredText())
		{
			GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
					"actionPerformed", "Event modified. Prompting user.");

			Object[] options = { "Discard Event", "Cancel" };
			int reply = JOptionPane
					.showOptionDialog(
							this,
							"You have edited this event. Do you want to save your changes?",
							"Event Changed", JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[1]);

			if (reply == JOptionPane.YES_OPTION)
			{
				// The user selected to discard the event
				GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
						"actionPerformed",
						"User elected to discard the event. Dismissing Panel.");

				dismissPane();
			}
			else
			{
				// We simply return the user to editing
				GlobalLogger.getLogger().logp(Level.INFO, "NewEventPanel",
						"actionPerformed",
						"User selected to continue editing the event.");
			}
		}
		else
		{
			dismissPane();
		}
	}

	public boolean hasEnteredText()
	{

		boolean returnBool = false;
		if (eventNameField.getText() != null
				&& eventNameField.getText().length() > 0)
		{
			returnBool = true;
		}

		if (startDateChooser.getDate() != null)
		{
			returnBool = true;
		}

		if (endDateChooser.getDate() != null)
		{
			returnBool = true;
		}

		if (eventDescriptionArea.getText().length() != 0)
		{
			returnBool = true;
		}

		// We don't want to prompt if they are editing a event
		if (eventToEdit != null)
		{
			returnBool = false;
		}

		return returnBool;
	}

	public void dismissPane()
	{
		// this.processWindowEvent(new WindowEvent(this,
		// WindowEvent.WINDOW_CLOSING));

		// After it is done, we need to refresh everything
		// Dismissing a newEventPanel causes a refresh
		Timeflecks.getSharedApplication().postNotification(
				TimeflecksEvent.GENERAL_REFRESH);

		this.setVisible(false);
		this.dispose();
	}

	public void displayFrame()
	{
		if (eventToEdit == null)
		{
			this.setTitle("Timeflecks - New Event");
		}
		else
		{
			this.setTitle("Timeflecks - Edit Event");
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
		this.setMinimumSize(new Dimension(380, 310));

		this.setVisible(true);
	}

}
