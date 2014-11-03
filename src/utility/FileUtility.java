package utility;

import java.io.File;
import java.util.Objects;

import core.Timeflecks;

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
	
	/**
	 * Checks against the existence of the file (same file is ok).
	 * 
	 * @param newFile
	 *            The file to check
	 * @return True if there is a problem. False if there is no problem
	 */
	public static boolean fileExistsAndIsNotSame(File newFile)
	{
		if (newFile.exists())
		{
			// The file exists
			if (Timeflecks.getSharedApplication().getCurrentFile()
					.equals(newFile))
			{
				return false;
			}
			else
			{
				// If it exists and is not the same, then we have an error, and
				// need
				// to prompt the user.
				return true;
			}

		}
		else
		{
			return false;
		}
	}
}
