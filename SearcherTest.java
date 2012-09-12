import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.SortedMap;

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
		
		Tokenizer token = new Tokenizer("test/testFile");
		IndexBuilder builder = new IndexBuilder();
		HugeSortedWordIndex index = builder.fillInSortedMap(token);
		SortedMap<String,Collection<Long>> map = index.getMap();
		// builder.printToFiles(map);
	}
	
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
