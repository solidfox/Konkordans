import java.util.*;
import java.util.Map.Entry;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class IndexBuilder {

	public static void main(String[] args) {
		Tokenizer token = new Tokenizer("small");
		SortedMap<String,Collection<Long>> map = fillInSortedMap(token);
		printToFiles(map);
	}

	private static SortedMap<String, Collection<Long>> fillInSortedMap(Tokenizer token){
		
		System.out.println("Indexing distinct words.");
		
		SortedMap<String,Collection<Long>> map = new TreeMap<String,Collection<Long>>();
		long fileLength = token.length();
		 
		/*
		 * Så länge det finns en ny token:
		 * -Kolla om ordet har förekommit
		 * -(Ordet har förekommit)-->hitta ordet, och lägg in en till byteposition i arraylisten.
		 * -(Ordet har inte förekommit)--->Skapa en ny lista där bytepositionerna kommer lagras,
		 * och lägg till ordet med den motsvarande bytepositionen
		 */
		int percent = 0;
		while(token.hasNext())
		{
			String word = token.getWord();
			word = word.toLowerCase();
			long position = token.getBytePosition();
			if(map.containsKey(word)){
				map.get(word).add(position);
			}
			else{
				Collection<Long> list = new ArrayList<Long>();
				list.add(position);
				map.put(word, list);
			}
			if (position > percent * fileLength / 100) {
				System.out.println(percent + "%");
				percent += 10;
			}
			
			token.next();
		}
		System.out.println("100%");
		return map;
	}

	/**
	 * För varje unika element, kommer den räkna occurance som börjar på 0
	 * Därefter skapar den en ny lista(för att hålla enklare koll på den med hjälp av en variabel)
	 * I den varje sån lista---> Skriv ut occurance av det ordet, och sedan bytepositionen(integern)  
	 */
	private static SortedMap<String, Collection<Long>> 
		printToFiles(SortedMap<String,Collection<Long>> map) {
		
		System.out.println("Entering printToFiles");
		
		Path instanceIndexPath = Paths.get("instanceIndex");
		Path wordIndexPath = Paths.get("wordIndex");
		
		IndexWriter instanceIndex = new IndexWriter(instanceIndexPath);
		IndexWriter wordIndex = new IndexWriter(wordIndexPath);
		
		/*
		First for-loop gets the amount of bytepositions we will get, and the nestled 
		one iterates the elements and prints them out 
		 */
		for (Entry<String, Collection<Long>> entry : map.entrySet())
		{
			String word = entry.getKey();
			long instancePointer = instanceIndex.getFilePointer();

			Collection<Long> korpusPointers = entry.getValue();
			
			// Write to instanceIndex
			try{
				instanceIndex.writeInt(korpusPointers.size());
				for (Long bytepos : korpusPointers) {
					instanceIndex.writeLong(bytepos);
				}
			}catch (Exception e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
			
			// Write word index 
			try{
				wordIndex.writeUTF(word);
				wordIndex.writeLong(instancePointer);
			}catch (IOException e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}
		try {
			instanceIndex.close();
			wordIndex.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

}
