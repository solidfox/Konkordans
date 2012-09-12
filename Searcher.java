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
	
	Path wordIndexPath;
	Path instanceIndexPath;
	Path korpusPath;
	Map<String, Long> trieRoot;
	
	public Searcher(
			String wordIndexPath, 
			String instanceIndexPath,
			String korpusPath) {
		this.wordIndexPath 			= Paths.get(wordIndexPath);
		this.instanceIndexPath 		= Paths.get(instanceIndexPath);
		this.korpusPath				= Paths.get(korpusPath);
		
		this.trieRoot = this.buildTrieRoot();
		
		System.out.println(trieRoot.size());
	}
	
	public String findWord(String word) throws IOException {
		// Get the truplet pointer
		long trupletPointer = this.trieRoot.get(this.getTruplet(word));
		// Find the word in the wordIndex
		long instanceIndexPointer = this.findInstanceIndexPointer(trupletPointer, word);
		// TODO handle if word didn't exist.
		// Fetch instance pointers for word
		long[] instancePointers = readInstancePointers(instanceIndexPointer);
		
		String result = this.buildResult(instancePointers, word.length());
		
		return result;
	}

	private Map<String, Long> buildTrieRoot() {
		
		HashMap<String, Long> trieRoot = new HashMap<String, Long>();
		IndexReader wordIndexReader = new IndexReader(wordIndexPath); 
		
		String word = "";
		long position = 0;
		boolean eof = false;
		while (!eof) {
			try {
				position = wordIndexReader.getFilePointer();
				word = wordIndexReader.readUTF();
				// Throw away instanceIndex address
				wordIndexReader.readLong(); 
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
		
		return trieRoot;
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
	
	/**
	 * Returns -1 if no word was found.
	 * @param wordIndexPointer
	 * @param word
	 * @return
	 */
	private long findInstanceIndexPointer(long wordIndexPointer, String word) {
		IndexReader wordIndexReader = new IndexReader(wordIndexPath);
		try {
			wordIndexReader.skip(wordIndexPointer);
			// Read in first word
			String currentWord = wordIndexReader.readUTF();
			long currentPointer = wordIndexReader.readLong();
			while (!currentWord.equals(word)) {
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
			IndexReader instanceIndex = new IndexReader(instanceIndexPath);
			
			instanceIndex.skip(instanceIndexPointer);
			
			int instanceCount = instanceIndex.readInt();
			
			long[] instancePointers = new long[instanceCount];
			for (int i = 0; i < instanceCount; i++) {
				instancePointers[i] = instanceIndex.readLong();
			}
			
			return instancePointers;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new IllegalStateException("Reached illegal state while reading instance pointers.");
	}
	
	private String buildResult(long[] instancePointers, int wordLength) throws IOException {
		IndexReader korpus = new IndexReader(Paths.get("korpus"));
		StringBuilder out = new StringBuilder(instancePointers.length * 50);
		int desiredNumberOfResults = 25;
		
		Arrays.sort(instancePointers);
		
		int numberOfResults = instancePointers.length;
		out.append("There are " + numberOfResults + " instances of the word.\n");
		
		numberOfResults = (numberOfResults > desiredNumberOfResults ? desiredNumberOfResults : numberOfResults);
		
		long lastPointer = 0;
		for (int i = 0; i < numberOfResults; i++) {
			long pointer = instancePointers[i];
			long bytesToSkip = pointer - 30 - lastPointer;
			// TODO what if we've already skipped the previous bytes?
			// TODO that is, if the word exists in close succession
			// Make sure we don't try to skip backwards
			bytesToSkip = (bytesToSkip >= 0 ? bytesToSkip : 0);
			korpus.skip(bytesToSkip);
			String context = korpus.readChars(60 + wordLength);
			context = context.replace("\n", " ");
			out.append(context);
			out.append("\n");
			lastPointer = korpus.getFilePointer();
		}
		return out.toString();
	}

	public static void main(String[] args) {
		Searcher searcher = new Searcher("wordIndex", "instanceIndex", "korpus");
		Stopwatch time = new Stopwatch();
		time.start();
		try {
			System.out.println(searcher.findWord("kvinnor"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		time.stop();
		System.out.println("Search done in " + time.milliseconds() + " milliseconds.");
	}
}
