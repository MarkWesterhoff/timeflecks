package utility;

import java.io.File;
import java.util.Objects;

public class FileUtility {
	/**
	 * Returns the extension of the input File, or "" if no extension.
	 * 
	 * @param file the File to get the extension of
	 * @return the file's extension
	 */
	public static String getFileExtension(File file) {
	    Objects.requireNonNull(file);
		
		String name = file.getName();
	    int lastIndexOf = name.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return ""; // empty extension
	    }
	    return name.substring(lastIndexOf);
	}
}
