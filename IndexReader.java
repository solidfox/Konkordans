import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.io.output.CountingOutputStream;

/**
 * Handles reading of datatypes int, long and UTF strings to file while keeping track of 
 * the current position in the file.
 * @author Daniel Schlaug
 * @version 2012-09-11
 */
public class IndexReader {
	
	private DataInputStream dataStream;
	private CountingInputStream countingStream;	
	/**
	 * Create a new IndexReader with the given indexFile.
	 * @param indexFile the file to read from.
	 */
	public IndexReader(Path indexFile) {
		
		try {
			InputStream os = Files.newInputStream(indexFile, StandardOpenOption.READ);
			countingStream = new CountingInputStream(os);
			dataStream = new DataInputStream(countingStream);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Returns the current byte offset (position) in the file.
	 * @return the current byte offset (position) in the file.
	 */
	
	public long getFilePointer() {
		return countingStream.getByteCount();
	}


	/**
	 * Read an integer to the file.
	 * @param data the integer to read
	 * @throws IOException ...yup.
	 */
	public int readInt() throws IOException {
		return dataStream.readInt();		
	}


	/**
	 * Read a string as unicode to the file. Starts of with a short defining the length of the string.
	 * @param str the string to read.
	 * @throws IOException ...yes.
	 */
	public String readUTF() throws IOException {
		return dataStream.readUTF();
	}


	/**
	 * Read a long to the file.
	 * @param data the long to read.
	 * @throws IOException ...mmm.
	 */
	public long readLong() throws IOException {
		return dataStream.readLong();
	}


	/**
	 * Flushes any buffers and closes the file.
	 * @throws IOException ...as always.
	 */
	public void close() throws IOException {
		dataStream.close();
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	}

}
