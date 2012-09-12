import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class IndexReaderTest {
	
	Path testIndex = Paths.get("test/testIndex");
	
	public IndexReaderTest() {
		try {
			Files.deleteIfExists(testIndex);
			Files.createFile(testIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testOfIndexReader() {

		IndexReader inReader = new IndexReader(Paths.get("wordIndex"));
		String[] arr = {"apple","blue", "green", "zebra"};
		try {
			String x;
			for(String word: arr){
				x = inReader.readUTF();
				assertEquals(x,word, x);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void skip() {
		IndexWriter iw = new IndexWriter(testIndex);
		IndexReader ir = new IndexReader(testIndex);
		long pointer = 0;
		try {
			iw.writeUTF("First string");
			iw.writeLong(1337);
			pointer = iw.getFilePointer();
			String sought = "Second string"; 
			iw.writeUTF(sought);
			ir.skip(pointer);
			String found = ir.readUTF();
			assertEquals(sought, found);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
