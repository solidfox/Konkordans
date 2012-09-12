import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;


public class SearcherTest {

	Path testFile = Paths.get("test/testFile");
	
	public SearcherTest() {
		try {
			Files.deleteIfExists(testFile);
			Files.createFile(testFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IndexBuilder builder = new IndexBuilder("test/testFile");
		builder.writeIndexes("test/wordIndex", "test/instanceIndex");
	}
	
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
