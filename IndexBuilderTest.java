

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.Test;

public class IndexBuilderTest {
	
	@Test
	public void test() {
		IndexBuilder builder = new IndexBuilder("test/korpus-small");
		builder.writeIndexes("test/smallWordIndex", "test/smallInstanceIndex");
		String[] list = {"våren", "då", "organisationen", "kvinnofrihet", "utsåg", "en", "arbetsgrupp", "att", "utforma", "förslag", "till", "aktioner", "som", "skulle", "motverka", "förtryck", "och", "diskriminering", "av", "kvinnor"};
		Arrays.sort(list);
		IndexReader reader = new IndexReader(Paths.get("test/smallWordIndex"));
		for (String key : list) {
			try {
				String readWord = reader.readUTF();
				reader.readLong();
				assertEquals("Word " + key + " was not in written index.", key, readWord);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				fail("IOException");
			}
		}
	}
}
