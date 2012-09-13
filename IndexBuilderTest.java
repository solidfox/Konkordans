

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.Test;

public class IndexBuilderTest {
	
	@Test
	public void test() {
		IndexBuilder builder = new IndexBuilder("test/korpus-small");
		builder.writeIndexes("test/trieRoot", "test/smallWordIndex", "test/smallInstanceIndex");
		String[] list = {"våren", "då", "organisationen", "kvinnofrihet", "utsåg", "en", "arbetsgrupp", "att", "utforma", "förslag", "till", "aktioner", "som", "skulle", "motverka", "förtryck", "och", "diskriminering", "av", "kvinnor"};
		Arrays.sort(list);
		IndexReader wordIndex = new IndexReader(Paths.get("test/smallWordIndex"));
		IndexReader trieRoot = new IndexReader(Paths.get("test/trieRoot"));
		
		for (String key : list) {
			try {
				String readWord = wordIndex.readUTF();
				wordIndex.readLong();
				assertEquals("Word " + key + " was not in written index.", key, readWord);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				fail("IOException");
			}
		}

		PerfectHashTruplet truplet = new PerfectHashTruplet("utf");
		try {
			System.out.println(truplet.hashCode()*8);
			trieRoot.skip(((truplet.hashCode())*8));
			assertEquals(13, trieRoot.readLong());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	@Test
//	public void ööö() {
//		IndexBuilder builder = new IndexBuilder("test/ööö");
//		builder.writeIndexes("test/öööTrieRoot", "test/smallWordIndex", "test/smallInstanceIndex");
////		
////		IndexReader trieRoot = new IndexReader(Paths.get("test/trieRoot"));
////		
////		for (String key : list) {
////			try {
////				String readWord = wordIndex.readUTF();
////				wordIndex.readLong();
////				assertEquals("Word " + key + " was not in written index.", key, readWord);
////				
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				fail("IOException");
////			}
////		}
////
////		PerfectHashTruplet truplet = new PerfectHashTruplet("arb");
////		try {
////			System.out.println(Integer.toHexString(truplet.hashCode()*8));
////			trieRoot.skip(((truplet.hashCode())*8));
////			assertEquals(13, trieRoot.readLong());
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//	}
}
