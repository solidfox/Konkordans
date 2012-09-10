import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class Tokenizer {
	
	private RandomAccessFile file;
	private String currentWord;
	private String nextWord;
	private long bytePosition;
	private boolean eof = false;
	
	/**
	 * Create a new Tokenizer for the file at the given path.
	 * @param fileToTokenize
	 */
	public Tokenizer(String fileToTokenize) {
		try {
			this.file = new RandomAccessFile (fileToTokenize, "r");
		} catch (FileNotFoundException e) {
			System.err.println(fileToTokenize + " was not found.");
		}
		this.next();
		this.next();
	}
	
	/**
	 * Returns true if there exists more tokens.
	 * @return true if there exists more tokens.
	 */
	public boolean hasNext() {
		if (!eof) {
			return true;
		} else {
			if (nextWord == null) {
				return false;
			}
		}
		throw new IllegalStateException("Tokenizer.hasNext() reached an illegal state.");
	}
	
	/**
	 * Jumps to the next token.
	 */
	public void next() {
		this.currentWord = this.nextWord;
		this.nextWord = this.readWord();
	}
	
	/**
	 * Returns the word of the current token.
	 * @return the word of the current token.
	 */
	public String getWord() {
		return this.currentWord;
	}
	
	/**
	 * 
	 */
	public long getBytePosition() {
		return this.bytePosition;
	}
	
	private String readWord() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(); 
		byte currentChar = readByte();
		// Remove irrelevant bytes
		while (junk(currentChar)) {
			currentChar = readByte();
		}
		try {
			this.bytePosition = file.getFilePointer() - 1;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Read word
		while(!junk(currentChar)) {
			bytes.write(currentChar);
			currentChar = readByte();
		}
		
		byte[] byteArray = bytes.toByteArray();
		
		// Check that we didn't reach end of file before reading a word
		if (byteArray.length == 0) {
			return null;
		}
		
		// Return the word
		try {
			return new String(byteArray, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Bloody ISO-8859-1 is not supported!!!");
		}
		throw new IllegalStateException("readWord could not convert from ISO-8859-1");
	}
	
	// Read one byte from the file
	private byte readByte() {
		byte b = 0;
		try {
			b = file.readByte();
		} catch (IOException e) {
			this.reachedEOF();
		}
		return b;
	}
	
	/**
	 * Check if the inserted byte is any of the following:
	 * a number
	 * a lower case a-z
	 * an upper case A-Z
	 * a special character like ÅÄÖ.
	 * Source http://www.ic.unicamp.br/~stolfi/EXPORT/www/ISO-8859-1-Encoding.html
	 * @param b the byte to check
	 * @return true if the byte is not relevant. false if it is relevant.
	 */
	private boolean junk(byte b) {
		if (b >= 0x20 && b <= 0x2F ||
			b >= 0x3A && b <= 0x40 ||
			b >= 0x5B && b <= 0x60 ||
			b >= 0x7B && b <= 0xBF ||
			b == 0xD7 || b == 0xF7) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method should be called when we hit end of file.
	 */
	private void reachedEOF() {
		this.eof = true;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tokenizer t = new Tokenizer("korpus");
		while (t.hasNext()) {
			System.out.println(t.getWord() + " " + t.getBytePosition());
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t.next();
		}
		
	}

}
