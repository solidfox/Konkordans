import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

public class IndexReaderTest {

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

}
