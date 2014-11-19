package utility;

import java.util.ArrayList;
import java.util.Objects;

public class StringUtility {
	
	/**
	 * Checks if the string only contains alphanumeric characters.
	 * 
	 * @param str the string to check
	 * @return true if string is alphanumeric, false otherwise
	 */
	public static boolean isAlphaNumeric(String str) {
		Objects.requireNonNull(str);
		
		String pattern = "^[a-zA-Z0-9]+$";
		if(str.matches(pattern)) {
			return true;
		}
		return false;
	}

	/**
	 * Throws an IllegalArgumentException if the String is null or empty.
	 * 
	 * @param str
	 *            the String to check
	 */
	public void requiresNotNullOrEmpty(String str)
	{
		if (str == null )
		{
			throw new NullPointerException(str);
		}
		
		if (str.equals(""))
		{
			throw new IllegalArgumentException(str);
		}
	}

	public static String join(ArrayList<String> strings, char joinChar) {
		StringBuilder sb = new StringBuilder();
		String joinString = "";
		for(String s : strings) {
			sb.append(joinString);
			sb.append(s);
			joinString = String.valueOf(joinChar);
		}
		return sb.toString();
	}
}
