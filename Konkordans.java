import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;


public class Konkordans {
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Konkordans konkordans = new Konkordans();
//		Map<Integer,Long> rootIndex = new TreeMap<Integer,Long>();
//		rootIndex = konkordans.addAssociationHashCodePos(rootIndex, wordIndex);
		System.out.println(konkordans.hashCode("ö"));
	}
	

	
	
	/*
	 * Associate hashcode with a position with a word.
	 * This method takes in an empty rootIndex and begins filling it up
	 * using the values from wordIndexWords, which contains only unique words and their byte positions
	 * that are pointers to another file - instanceIndex.
	 * Anyhow, this one associates a unique hash code with a unique word. 
	 * The hash code is described below and hashes after the first 3 letters. 
	 *
	 */
	private Map<Integer, Long> addAssociationHashCodePos(Map<Integer, Long> rootIndex, SortedMap<String, Long> wordIndex) {
		for (Entry<String, Long> entry : wordIndex.entrySet())
		{
			String wordToHash = entry.getKey();
			long positionInWordIndex = entry.getValue();
			rootIndex.put(hashCode(wordToHash), positionInWordIndex);
	}
		
		return rootIndex;
	}

	
	/*
	 * This method is supposed to receive a truplet - word consisting of 3 letters, and code it
	 * according to our hash function: x*30^0 + y*30^1 + z*30^2, where x,y,z are 
	 * the letters of our truplet
	 * The following formula genereates a UNIQUE code for EACH truplet ranging
	 *  from all possible 30^3 combinations between aaa to ööö
	 */
	
	int hashCode(String word){
		int out =0;
		
		if(word.length()>=1){
			out+=word.charAt(0)-96;
		}
		if(word.length()>=2){
			out+=(word.charAt(1)-96)*30;
		}
		if(word.length()==3){
			out+=(word.charAt(2)-96)*900;
		}
		if(word.length() == 0 || word.length() > 3){
			throw new IllegalArgumentException("Hash function only defined for 1-3 length strings.");
		}
		return out;

	}
	

	
	
	
	

}

