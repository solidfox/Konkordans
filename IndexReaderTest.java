import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;


public class IndexReaderTest {

	@Test
	public void testOfIndexReader() {
		
		IndexReader inReader = new IndexReader(Paths.get("wordIndex"));
		
		String[] strarr = {"apple", "blue", "green", "zebra"};
		try {
			for(int x=0; x<strarr.length;x++){
			
			assertEquals(strarr[x], inReader.readUTF());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
