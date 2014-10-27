package utility;

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
}
