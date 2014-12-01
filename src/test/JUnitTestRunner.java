package test;

import java.util.ArrayList;
import java.util.logging.Level;

import logging.GlobalLogger;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import core.Timeflecks;
import core.test.*;
import database.test.*;
import user_interface.test.*;
import utility.ArrayUtility;
/**
 * Class to run all JUnit tests from the project.
 * 
 * @author Andrew
 */
public class JUnitTestRunner
{
	public static void main(String[] args)
	{
		// Only care about WARNING log messages and worse
		//GlobalLogger.getLogger().setLevel(Level.WARNING);
		
		// ArrayList to hold all classes to be tested
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		
		// Core tests
		classes.add(new DatabaseSerializableTest().getClass());
		//classes.add(new EventTest().getClass());
		//classes.add(new FilteredTaskListTest().getClass());
		//classes.add(new RecurrenceTests().getClass());
		//classes.add(new TaskListTest().getClass());
		//classes.add(new TaskTest().getClass());
		
		// Database tests
		//classes.add(new SQLiteConnectorTest().getClass());
		
		// User interface tests
		//classes.add(new CalendarPanelTest().getClass());
		//classes.add(new MainWindowTest().getClass());
		//classes.add(new MenuBarTest().getClass());
		//classes.add(new NewTaskPanelTest().getClass());
		//classes.add(new TaskComponentTest().getClass());
		
		// Run all tests
		JUnitCore core = new JUnitCore();
		Result result = core.run(ArrayUtility.arrayListToArray(classes));

		System.out.println("There were "
				+ Integer.toString(result.getFailureCount()) + " failures.");
		
		for (Failure failure : result.getFailures())
		{
			System.out.println("Test failed: " + failure.getDescription());
			System.out.println("\tThrew exception: "
					+ failure.getException().toString());
			System.out.println();
		}
	}
}
