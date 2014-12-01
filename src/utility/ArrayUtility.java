package utility;

import java.util.ArrayList;

public class ArrayUtility
{
	/**
	 * Converts the ArrayList of Class objects into an array of Class objects.
	 * 
	 * @param classes the ArrayList to convert
	 * @return an array containing the classes from the ArrayList
	 */
	public static Class<?>[] arrayListToArray(ArrayList<Class<?>> classes)
	{
		Class<?>[] classArray = new Class<?>[classes.size()];
		for (int i = 0; i < classes.size(); ++i)
		{
			classArray[i] = classes.get(i);
		}
		return classArray;
	}
}
