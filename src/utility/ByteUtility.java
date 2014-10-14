package utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ByteUtility {
	/**
	 * Returns an array of bytes representing the serialized object.
	 * 
	 * @param obj the object to serialize
	 * @return the array of bytes representing the serialized object
	 * @throws IOException when there is a problem serializing the object
	 */
	public static byte[] getBytes(Object obj) throws IOException {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(bos);
	    oos.writeObject(obj);
	    oos.flush();
	    oos.close();
	    bos.close();
	    byte[] data = bos.toByteArray();
	    return data;
	}
}
