import static org.junit.Assert.*;

import java.util.Collection;
import java.util.SortedMap;

import org.junit.Test;


public class TokenizerTest {

	// Testa för korta filer och korta ord
	// Testa för filer utan enter i slutet
	
	@Test
	public void korpusSmall() {
		Tokenizer token = new Tokenizer("test/korpus-small");
		String[] list = {"våren", "då", "organisationen", "kvinnofrihet", "utsåg", "en", "arbetsgrupp", "att", "utforma", "förslag", "till", "aktioner", "som", "skulle", "motverka", "förtryck", "och", "diskriminering", "av", "kvinnor"};
		for (String word : list) {
			assertEquals("Word " + word + " was not returned by tokenizer in list.",
					word, token.getWord().toLowerCase());
			token.next();
		}
	}
	
	@Test
	public void oneWord() {
		Tokenizer token = new Tokenizer("test/oneword");
		String[] list = {"våren"};
		for (String word : list) {
			assertEquals("Word " + word + " was not returned by tokenizer in list.",
					word, token.getWord().toLowerCase());
			token.next();
		}
	}
	
	@Test
	public void oneWordNewline() {
		Tokenizer token = new Tokenizer("test/onewordnewline");
		String[] list = {"våren"};
		for (String word : list) {
			assertEquals("Word " + word + " was not returned by tokenizer in list.",
					word, token.getWord().toLowerCase());
			token.next();
		}
	}
	
	public void fewWordsNewline() {
		Tokenizer token = new Tokenizer("test/fewwordsnewline");
		String[] list = {"våren", "kvinnor", "bar"};
		for (String word : list) {
			assertEquals("Word " + word + " was not returned by tokenizer in list.",
					word, token.getWord().toLowerCase());
			token.next();
		}
	}

}
