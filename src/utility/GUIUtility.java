package utility;

import java.awt.*;
import java.util.Objects;

/**
 * Utility class for Graphics.
 * 
 */
public class GUIUtility
{
	/**
	 * Draws the specified string on the specified graphics wrapped to the
	 * specified width.
	 * 
	 * @param g
	 *            The graphics on which to draw the string
	 * @param s
	 *            The string to draw
	 * @param x
	 *            The x coordinate to start the top left part of the string at
	 * @param y
	 *            The y coordinate to start the top left part of the string at
	 * @param width
	 *            The width of the string to be drawn / wrapped
	 */
	public static void drawString(Graphics g, String s, int x, int y, int width)
	{
		Objects.requireNonNull(g);
		Objects.requireNonNull(s);

		FontMetrics fm = g.getFontMetrics();

		int lineHeight = fm.getHeight();

		int curX = x;
		int curY = y;

		String[] words = s.split(" ");

		for (String word : words)
		{
			// Find out the width of the word.
			int wordWidth = fm.stringWidth(word + " ");

			// If text exceeds the width, then move to next line.
			if (curX + wordWidth >= x + width)
			{
				curY += lineHeight;
				curX = x;
			}

			g.drawString(word, curX, curY);

			// Move over to the right for next word.
			curX += wordWidth;
		}
	}
}