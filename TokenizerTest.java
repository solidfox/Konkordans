import static org.junit.Assert.*;

import java.util.Collection;
import java.util.SortedMap;

import org.junit.Test;


public class TokenizerTest {

	// Testa för korta filer och korta ord
	// Testa för filer utan enter i slutet
	
	@Test
	public void test() {
		Tokenizer token = new Tokenizer("korpus-small");
		String[] list = {"våren", "då", "organisationen", "kvinnofrihet", "utsåg", "en", "arbetsgrupp", "att", "utforma", "förslag", "till", "aktioner", "som", "skulle", "motverka", "förtryck", "och", "diskriminering", "av", "kvinnor"};
		for (String word : list) {
			assertEquals("Word " + word + " was not returned by tokenizer in list.",
					word, token.getWord().toLowerCase());
			token.next();
		}
	}

}
