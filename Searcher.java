import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	
	private String getTruplet(String word) {
		int trupletEnd = (word.length() < 3 ? word.length() - 1 : 2);
		String truplet = word.substring(0, trupletEnd);
		return truplet;
	}
	
	public String findWord(String word) throws IOException {
		IndexReader korpus = new IndexReader(Paths.get("korpus"));
		long wordIndexPointer = this.trieRoot.get(this.getTruplet(word));
		long[] instancePointers = getInstancePointers(wordIndexPointer);
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
	
	private long[] getInstancePointers(long wordIndexPointer) {
		try {
			RandomAccessFile wordIndex = new RandomAccessFile("instanceIndex", "r");
			wordIndex.seek(wordIndexPointer);
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
