import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.output.CountingOutputStream;


public class IndexWriter {
	
	private DataOutputStream dataStream;
	private CountingOutputStream countingStream;
	
	public IndexWriter(Path indexFile) {
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



	public long getFilePointer() {
		return countingStream.getByteCount();
	}



	public void writeInt(int data) throws IOException {
		dataStream.writeInt(data);		
	}



	public void writeUTF(String str) throws IOException {
		dataStream.writeUTF(str);
	}



	public void writeLong(long data) throws IOException {
		dataStream.writeLong(data);
	}



	public void close() throws IOException {
		dataStream.flush();
		dataStream.close();
	}

}
