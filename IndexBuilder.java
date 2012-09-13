import java.util.*;
import java.util.Map.Entry;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class IndexBuilder {

	private HugeSortedWordIndex wordIndex;
	
	public static void main(String[] args) {
		IndexBuilder builder = new IndexBuilder("korpus");
		builder.writeIndexes("trieRoot", "wordIndex", "instanceIndex");
	}
	
	public IndexBuilder(String fileToIndex) {
		Tokenizer tokenizer = new Tokenizer(fileToIndex);
		wordIndex = buildIndex(tokenizer);
	}
	
	public void writeIndexes(String trieRootPath, String wordIndexPath, String instanceIndexPath) {
		this.writeIndexToFiles(trieRootPath, wordIndexPath, instanceIndexPath);
	}
	
	private HugeSortedWordIndex buildIndex(Tokenizer token){
		
		HugeSortedWordIndex index = new HugeSortedWordIndex();
		
		System.out.println("Indexing distinct words.");
		
		long fileLength = token.length();
		 
		/*
		 * Så länge det finns en ny token:
		 * -Kolla om ordet har förekommit
		 * -(Ordet har förekommit)-->hitta ordet, och lägg in en till byteposition i arraylisten.
		 * -(Ordet har inte förekommit)--->Skapa en ny lista där bytepositionerna kommer lagras,
		 * och lägg till ordet med den motsvarande bytepositionen
		 */
		int percent = 0;
		while (token.hasNext()) {
			// TODO invariant?
			token.next();
			
			String word = token.getWord().toLowerCase();
			long position = token.getBytePosition();
			
			index.add(word, position);
			
			if (position > percent * fileLength / 100) {
				System.out.println(percent + "%");
				percent += 10;
			}
		}
		// Add last word.
		index.add(token.getWord(), token.getBytePosition());
		System.out.println("100%");
		return index;
	}

	/**
	 * För varje unika element, kommer den räkna occurance som börjar på 0
	 * Därefter skapar den en ny lista(för att hålla enklare koll på den med hjälp av en variabel)
	 * I den varje sån lista---> Skriv ut occurance av det ordet, och sedan bytepositionen(integern)  
	 */
	private void writeIndexToFiles(String trieRoot, String wordIndex, String instanceIndex) {
		SortedMap<String,Collection<Long>> map = this.wordIndex.getMap();
		
		
		System.out.println("Entering printToFiles");
		
		Path instanceIndexPath = Paths.get(instanceIndex);
		Path wordIndexPath = Paths.get(wordIndex);
		Path trieRootPath = Paths.get(trieRoot);
		
		this.ensureFresh(instanceIndexPath);
		this.ensureFresh(wordIndexPath);
		this.ensureFresh(trieRootPath);
		
		IndexWriter instanceIndexFile = new IndexWriter(instanceIndexPath);
		IndexWriter wordIndexFile = new IndexWriter(wordIndexPath);
		RandomAccessFile trieRootFile = null;
		try {
			trieRootFile = new RandomAccessFile(trieRoot, "rw");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		First for-loop steps through every word, writing out their instance count,
		the nestled one then writes out all the bytepositions for each word. 
		 */
		
		
		String lastTruplet = "";
		for (Entry<String, Collection<Long>> entry : map.entrySet())
		{
			// TODO invariant?
			String word = entry.getKey();
			long wordIndexPointer = wordIndexFile.getFilePointer();
			long instanceIndexPointer = instanceIndexFile.getFilePointer();
			

			Collection<Long> korpusPointers = entry.getValue();
			
			
			try{
				PerfectHashTruplet truplet = new PerfectHashTruplet(word);
				if (!truplet.toString().equals(lastTruplet)) {
					lastTruplet = truplet.toString();
					// Write to trieRoot
					int trupletHash = truplet.hashCode(); //get hash code
					
					if (trieRootFile.length() < trupletHash*8) {
						trieRootFile.setLength(trupletHash*8);
					}
					
					trieRootFile.seek(trupletHash*8);
					
					trieRootFile.writeLong(wordIndexPointer); //print wordPointer in rootIndex
				}
				
				
				// Write word index
				
				wordIndexFile.writeUTF(word);
				wordIndexFile.writeLong(instanceIndexPointer);
				
				// Write to instanceIndex
				instanceIndexFile.writeInt(korpusPointers.size());
				for (Long bytepos : korpusPointers) {
					// TODO invariant?
					instanceIndexFile.writeLong(bytepos);
				}
				
			}catch (IOException e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}
		try {
			instanceIndexFile.close();
			wordIndexFile.close();
			trieRootFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done.");
	}
	
	private void ensureFresh(Path path) {
		try {
			Files.deleteIfExists(path);
			Files.createFile(path);
		} catch (IOException e) {
			// TODO auto generated
			e.printStackTrace();
		}
		
	}
}
