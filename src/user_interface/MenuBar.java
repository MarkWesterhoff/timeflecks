package user_interface;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.*;

import logging.GlobalLogger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.Timeflecks;

public class MenuBar extends JMenuBar
{

	private JMenu filemenu, editmenu, helpmenu;
	private JMenuItem menuItem;

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new MenuBar with the items for the Timeflecks application
	 */
	public MenuBar()
	{
		super();
		MenuBarController mb = new MenuBarController(this);
		createAndAddFileMenu(mb);

		// TODO Add back in the edit menu if we want one
		createAndAddEditMenu(mb);

		// createAndAddHelpMenu(mb);

	}

	/**
	 * Creates the file menu for the menu bar
	 */
	private void createAndAddFileMenu(MenuBarController mb)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "Creating file menu");

		// Create the file menu
		filemenu = new JMenu("File");
		filemenu.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_F));

		getAccessibleContext().setAccessibleDescription("File menu");

		add(filemenu);

		// Open menu item
		menuItem = new JMenuItem("Open", KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Open file");
		menuItem.addActionListener(mb);

		filemenu.add(menuItem);

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "Added open menu");

		// Add a separator, like many applications have
		filemenu.addSeparator();

		// Save menu item

		menuItem = new JMenuItem("Save", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save file");
		menuItem.addActionListener(mb);

		filemenu.add(menuItem);

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "Added save menu");

		// Save as... menu item

		menuItem = new JMenuItem("Save As...");
		menuItem.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_A));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
		menuItem.addActionListener(mb);

		filemenu.add(menuItem);

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "Added save as... menu");

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddFileMenu", "File menu generation complete");
	}

	/**
	 * Creates the edit menu for the menu bar
	 */
	private void createAndAddEditMenu(MenuBarController mb)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddEditMenu", "Creating edit menu");

		// Create the edit menu
		editmenu = new JMenu("Edit");
		editmenu.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_E));

		getAccessibleContext().setAccessibleDescription("Edit menu");

		add(editmenu);

		// Edit menu items
		menuItem = new JMenuItem("New Task...", KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Create new task");
		menuItem.addActionListener(mb);

		editmenu.add(menuItem);

		menuItem = new JMenuItem("Edit Task...", KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Edit task properties");
		menuItem.addActionListener(mb);

		editmenu.add(menuItem);

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddEditMenu", "Added edit menu");

	}

	/**
	 * Creates the help menu for the menu bar
	 */
	private void createAndAddHelpMenu(MenuBarController mb)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddHelpMenu", "Creating help menu");

		// Create the file menu
		helpmenu = new JMenu("Help");
		helpmenu.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_H));

		getAccessibleContext().setAccessibleDescription("Help menu");

		add(helpmenu);

		// Open menu item
		menuItem = new JMenuItem("No Help For You!", KeyEvent.VK_H);

		// No keyboard shortcut for this
		// menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_,
		// InputEvent.CTRL_DOWN_MASK));

		menuItem.getAccessibleContext().setAccessibleDescription(
				"No help available");
		menuItem.addActionListener(mb);

		helpmenu.add(menuItem);

		GlobalLogger.getLogger().logp(Level.INFO, "MenuBar",
				"createAndAddHelpMenu", "Added help menu");

	}
}