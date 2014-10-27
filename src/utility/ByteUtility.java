package utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class ByteUtility {
	/**
	 * Returns an array of bytes representing the serialized object.
	 * 
	 * @param obj the object to serialize
	 * @return the array of bytes representing the serialized object
	 * @throws IOException when there is a problem serializing the object
	 */
	public static byte[] getBytes(Object obj) throws IOException {
	    Objects.requireNonNull(obj);
	    
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(bos);
	    oos.writeObject(obj);
	    oos.flush();
	    oos.close();
	    bos.close();
	    byte[] data = bos.toByteArray();
	    return data;
	}
	
	/**
	 * Returns an Object that represents the deserialized byte array.
	 * 
	 * @param bytes the serialized byte array
	 * @return the deserialized Object
	 * @throws ClassNotFoundException if there is a problem deserializing the object
	 * @throws IOException if there is a problem with the stream of bytes representing
	 * 						the serialized object
	 */
	public static Object getObject(byte[] bytes) throws ClassNotFoundException, IOException {
		Objects.requireNonNull(bytes);
		
		ObjectInputStream inStream = null;
		if(bytes != null) {
			inStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
		}

		return inStream.readObject();
	}
}
