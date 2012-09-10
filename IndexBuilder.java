import java.util.*;
import java.util.Map.Entry;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.Object;


public class IndexBuilder {

	public static void main(String[] args) {
		Tokenizer token = new Tokenizer("korpus");
		SortedMap<String,Collection<Long>> map = fillInSortedMap(token);
		printToFiles(map);
	}

	private static SortedMap<String, Collection<Long>> fillInSortedMap(Tokenizer token){
		
		 SortedMap<String,Collection<Long>> map = new TreeMap<String,Collection<Long>>();
		
		/*
		 * Så länge det finns en ny token:
		 * -Kolla om ordet har förekommit
		 * -(Ordet har förekommit)-->hitta ordet, och lägg in en till byteposition i arraylisten.
		 * -(Ordet har inte förekommit)--->Skapa en ny lista där bytepositionerna kommer lagras,
		 * och lägg till ordet med den motsvarande bytepositionen
		 */
		while(token.hasNext())
		{
			String word = token.getWord();
			long position = token.getBytePosition();
			if(map.containsKey(word)){
				map.get(word).add(position);
			}
			else{
				Collection<Long> list = new ArrayList<Long>();
				list.add(position);
				map.put(word, list);
			}
			token.next();
		}
		return map;
	}

	/*
	 * För varje unika element, kommer den räkna occurance som börjar på 0
	 * Därefter skapar den en ny lista(för att hålla enklare koll på den med hjälp av en variabel)
	 * I den varje sån lista---> Skriv ut occurance av det ordet, och sedan bytepositionen(integern)  
	 */
	private static SortedMap<String, Collection<Long>> printToFiles(SortedMap<String,Collection<Long>> map) {
		int occurance;
		RandomAccessFile instanceIndex=null;
		RandomAccessFile wordIndex=null;
		try {
			instanceIndex = new RandomAccessFile("instanceIndex", "rw");
			wordIndex = new RandomAccessFile("wordIndex", "rw");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long pointer = 0;
		/*
		First for-loop gets the amount of bytepositions we will get, and the nestled 
		one iterates the elements and prints them out 
		 */
		for (Entry<String, Collection<Long>> entry : map.entrySet())
		{
			Collection<Long> listan = entry.getValue();
			occurance = listan.size();
			/*
			 * 
			 * Write to InstanceIndex
			 * 
			 * 1)Save pointer
			 * 2)Write occurance
			 * 3)write the bytepositions
			 */

			try{
				pointer = instanceIndex.getFilePointer();
				instanceIndex.writeInt(occurance);
				for (Long bytepos : listan) {
					instanceIndex.writeLong(bytepos);
				}	
			}catch (Exception e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}



			/*
			 * 
			 * Write to WordIndex
			 * 1)Write word
			 * 2)Write byteposition
			 */

			try{

				wordIndex.writeUTF(entry.getKey());
				wordIndex.writeLong(pointer);
			}catch (IOException e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}




			System.out.println(entry.getKey() + " Occurance: " + occurance);	
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
