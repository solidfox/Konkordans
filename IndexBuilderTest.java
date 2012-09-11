

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

import org.junit.Test;

public class IndexBuilderTest {

	@Test
	public void fillInSortedMap() {
		Tokenizer token = new Tokenizer("korpus-small");
		IndexBuilder builder = new IndexBuilder();
		SortedMap<String,Collection<Long>> map = builder.fillInSortedMap(token).getMap();
		String[] list = {"våren", "då", "organisationen", "kvinnofrihet", "utsåg", "en", "arbetsgrupp", "att", "utforma", "förslag", "till", "aktioner", "som", "skulle", "motverka", "förtryck", "och", "diskriminering", "av", "kvinnor"};
		for (String key : list) {
			assertTrue("Word " + key + " was not in list.", map.containsKey(key));
		}
		assertEquals("Word count was wrong.", 20, map.size());
		List kvinnofrihet = map.get("kvinnofrihet");
		assertTrue("Kvinnofrihet was found on position " + map.get("kvinnofrihet").contains(25));
	}

}
