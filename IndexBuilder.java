import java.util.*;
import java.util.Map.Entry;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.Object;


public class IndexBuilder {

	public static void main(String[] args) {
		SortedMap<String,Collection<Integer>> map = new TreeMap<String,Collection<Integer>>();
		Tokenizer token = new Tokenizer("korpus");
		map = fillInSortedMap(token,map);
		printToFiles(map);
	}

	private static SortedMap fillInSortedMap(Tokenizer token, SortedMap<String,Collection<Integer>> map){

		/*
		 * Så länge det finns en ny token:
		 * -Kolla om ordet har förekommit
		 * -(Ordet har förekommit)-->hitta ordet, och lägg in en till byteposition i arraylisten.
		 * -(Ordet har inte förekommit)--->Skapa en ny lista där bytepositionerna kommer lagras,
		 * och lägg till ordet med den motsvarande bytepositionen
		 */
		while(token.hasNext())
		{
			if(map.containsKey(token.getWord())){
				map.get(token.getWord()).add(token.getBytePosition());
			}
			else{
				Collection list = new ArrayList();
				map.put(token.getWord(), list.add(token.getBytePosition()));
			}
			System.out.println("Adding:" + "Key: " + token.getWord() +" Value: " + token.getBytePosition());
			token.next();
		}
		return map;
	}

	/*
	 * För varje unika element, kommer den räkna occurance som börjar på 0
	 * Därefter skapar den en ny lista(för att hålla enklare koll på den med hjälp av en variabel)
	 * I den varje sån lista---> Skriv ut occurance av det ordet, och sedan bytepositionen(integern)  
	 */
	private static SortedMap printToFiles(SortedMap<String,Collection<Integer>> map) throws IOException{
		int occurance;
		RandomAccessFile instanceIndex = new RandomAccessFile("instanceIndex", "rw");
		RandomAccessFile wordIndex = new RandomAccessFile("wordIndex", "rw");
		long pointer = 0;
		/*
		First for-loop gets the amount of bytepositions we will get, and the nestled 
		one iterates the elements and prints them out 
		 */
		for (Entry<String, Collection<Integer>> entry : map.entrySet())
		{
			Collection<Integer> listan = entry.getValue();
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
				for (Integer bytepos : listan) {
					instanceIndex.writeInt(bytepos);
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
			}catch (Exception e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}




			System.out.println(entry.getKey() + " Occurance: " + occurance);	
		}		
		instanceIndex.close();
		wordIndex.close();
		return map;



	}

}
