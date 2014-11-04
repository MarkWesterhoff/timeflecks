package core;

import java.util.ArrayList;
import java.util.logging.Level;

import logging.GlobalLogger;

public class TimeflecksEventManager
{
	ArrayList<TimeflecksEventResponder> listeners = new ArrayList<TimeflecksEventResponder>();

	/**
	 * Add a listener to the list of people who are registered for this
	 * notification
	 * 
	 * @param toAdd
	 *            The listener to add
	 */
	public void addListener(TimeflecksEventResponder toAdd)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "TimeflecksEventManager",
				"addListener", "Adding listener " + toAdd);
		listeners.add(toAdd);
	}

	/**
	 * Remove a listener from the list of people who are registered for this
	 * notification
	 * 
	 * @param toRemove
	 *            The listener to remove
	 */
	public void removeListener(TimeflecksEventResponder toRemove)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "TimeflecksEventManager",
				"removeListener", "Removing listener " + toRemove);
	}

	/**
	 * Send a notification to each registered listener that the specified
	 * TimeflecksEvent has occurred. Note that it is up to each listener to
	 * decide what to do with each possible event, if they choose to do anything
	 * at all.
	 * 
	 * @param e
	 *            THe TimeflecksEvent to post to everyone
	 */
	public void postEvent(TimeflecksEvent e)
	{
		GlobalLogger.getLogger().logp(Level.INFO, "TimeflecksEventManager",
				"postEvent", "Posting event " + e + " to all listeners.");

		// Notify everyone who is listening
		for (TimeflecksEventResponder r : listeners)
			r.eventPosted(e);
	}
}
