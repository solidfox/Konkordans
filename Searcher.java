import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Quite full of bugs due to my brain working on overtime but I think you'll get the point.
 * Now sleep.
 * @author Daniel Schlaug
 *
 */
public class Searcher {
	
	Path wordIndexFile;
	Path instanceIndexFilePath;
	Map<String, Long> trieRoot;
	
	public Searcher() {
		wordIndexFile = Paths.get("wordIndex");
		instanceIndexFilePath = Paths.get("instanceIndex");
		trieRoot = new HashMap<String, Long>();
		
		IndexReader wordIndexReader = new IndexReader(wordIndexFile); 
		String word = "";
		long position = 0;
		
		boolean eof = false;
		while (!eof) {
			try {
				position = wordIndexReader.getFilePointer();
				word = wordIndexReader.readUTF();
				wordIndexReader.readLong(); // Throw away instanceIndex address
			} catch (EOFException e) {
				eof = true;
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String truplet = this.getTruplet(word);
			
			if (!trieRoot.containsKey(truplet)) {
				trieRoot.put(truplet, position);
			}
		}
		System.out.println(trieRoot.size());
	}
	
	/**
	 * Returns the first three letters in a given string.
	 * Or, if the string is shorter than 3 letters, returns the string unmodified.
	 * @param word the word to get the truplet for.
	 * @return the first three letters in a given string. 
	 * Or, if the string is shorter than 3 letters, returns the string unmodified.
	 */
	private String getTruplet(String word) {
		int trupletEnd = (word.length() < 3 ? word.length() : 3);
		String truplet = word.substring(0, trupletEnd);
		return truplet;
	}
	
	public String findWord(String word) throws IOException {
		// Open the korpus file
		IndexReader korpus = new IndexReader(Paths.get("korpus"));
		// Get the truplet pointer
		long trupletPointer = this.trieRoot.get(this.getTruplet(word));
		// Find the word in the wordIndex
		long instanceIndexPointer = this.findInstanceIndexPointer(trupletPointer, word);
		// Fetch instance pointers for word
		long[] instancePointers = readInstancePointers(instanceIndexPointer);
		Arrays.sort(instancePointers);
		StringBuilder out = new StringBuilder(instancePointers.length * 50);
		long lastPointer = 0;
		for (long pointer : instancePointers) {
			long bytesToSkip = pointer - 20 - lastPointer;
			// Make sure we don't try to skip backwards
			bytesToSkip = (bytesToSkip >= 0 ? bytesToSkip : 0);
			korpus.skip(bytesToSkip);
			String context = korpus.readChars(40 + word.length());
			out.append(context);
			out.append("\n");
			lastPointer = pointer;
		}
		return out.toString();
	}
	
	/**
	 * Returns -1 if no word was found.
	 * @param wordIndexPointer
	 * @param word
	 * @return
	 */
	private long findInstanceIndexPointer(long wordIndexPointer, String word) {
		IndexReader wordIndexReader = new IndexReader(wordIndexFile);
		try {
			wordIndexReader.skip(wordIndexPointer);
			// Read in first word
			String currentWord = wordIndexReader.readUTF();
			long currentPointer = wordIndexReader.readLong();
			while (!currentWord.equals(word)) {
				System.out.println(currentWord);
				// Make sure we don't search to far
				if (!currentWord.startsWith(this.getTruplet(word))) {
					return -1;
				}
				currentWord = wordIndexReader.readUTF();
				currentPointer = wordIndexReader.readLong();
			}
			return currentPointer;
		} catch (IOException e) {
			return -1;
		}
	}
	
	private long[] readInstancePointers(long instanceIndexPointer) {
		try {
			IndexReader wordIndex = new IndexReader(instanceIndexFilePath);
			wordIndex.skip(instanceIndexPointer);
			int instanceCount = wordIndex.readInt();
			long[] instancePointers = new long[instanceCount];
			for (int i = 0; i < instanceCount; i++) {
				instancePointers[i] = wordIndex.readLong();
			}
			return instancePointers;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new IllegalStateException("Reached illegal state while reading instance pointers.");
	}
	
	public static void main(String[] args) {
		Searcher searcher = new Searcher();
		try {
			System.out.println(searcher.findWord("vasaparken"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
}
