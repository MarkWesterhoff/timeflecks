package core;

import java.util.ArrayList;
import java.util.logging.Level;

import logging.GlobalLogger;

/**
 * Receives events and sends them to TimeflecksEventResponders.
 * 
 */
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
		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"addListener(TimeflecksEventResponder)",
				"Adding listener " + toAdd);
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
		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"removeListener(TimeflecksEventResponder)",
				"Removing listener " + toRemove);
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
		GlobalLogger.getLogger().logp(Level.INFO, this.getClass().getName(),
				"postEvent(TimeflecksEvent)",
				"Posting event " + e + " to all listeners.");

		// Notify everyone who is listening
		for (TimeflecksEventResponder r : listeners)
		{
			r.eventPosted(e);
		}
	}
}
