import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.output.CountingOutputStream;

/**
 * Handles writing of datatypes int, long and UTF strings to file while keeping track of 
 * the current position in the file.
 * @author Daniel Schlaug
 * @version 2012-09-11
 */
public class IndexWriter {
	
	private Path indexFile;
	private DataOutputStream dataStream;
	private CountingOutputStream countingStream;
	
	/**
	 * Create a new IndexWriter with the given indexFile.
	 * @param indexFile the file to write to.
	 */
	public IndexWriter(Path indexFile) {
		this.indexFile = indexFile;
		try {
			OutputStream os = Files.newOutputStream(indexFile, StandardOpenOption.WRITE);
			countingStream = new CountingOutputStream(os);
			dataStream = new DataOutputStream(countingStream);
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
	 * Write an integer to the file.
	 * @param data the integer to write
	 * @throws IOException ...yup.
	 */
	public void writeInt(int data) throws IOException {
		dataStream.writeInt(data);		
	}


	/**
	 * Write a string as unicode to the file. Starts of with a short defining the length of the string.
	 * @param str the string to write.
	 * @throws IOException ...yes.
	 */
	public void writeUTF(String str) throws IOException {
		dataStream.writeUTF(str);
	}


	/**
	 * Write a long to the file.
	 * @param data the long to write.
	 * @throws IOException ...mmm.
	 */
	public void writeLong(long data) throws IOException {
		dataStream.writeLong(data);
	}


	/**
	 * Flushes any buffers and closes the file.
	 * @throws IOException ...as always.
	 */
	public void close() throws IOException {
		dataStream.flush();
		dataStream.close();
	}

	/**
	 * Restores the file to empty.
	 */
	public void resetFile() {
		try {
			Files.deleteIfExists(indexFile);
			Files.createFile(indexFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	}

}
