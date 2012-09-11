import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;


public class HugeSortedWordIndex {

	SortedMap<String,Collection<Long>> map;
	
	public HugeSortedWordIndex() {
		map = new TreeMap<String,Collection<Long>>();
	}
	
	public void add(String word, long position) {
		if(map.containsKey(word)){
			map.get(word).add(position);
		}
		else{
			Collection<Long> list = new ArrayList<Long>();
			list.add(position);
			map.put(word, list);
		}
	}
	
	public SortedMap<String,Collection<Long>> getMap() {
		return this.map;
	}
}
