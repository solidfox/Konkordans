import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Tokenizer {
	
	private RandomAccessFile file;
	
	public Tokenizer(String fileToTokenize) {
		try {
			this.file = new RandomAccessFile (fileToTokenize, "r");
		} catch (FileNotFoundException e) {
			System.err.println(fileToTokenize + " was not found.");
		}
	}
	
	/**
	 * Returns true if there exists more tokens.
	 * @return true if there exists more tokens.
	 */
	public boolean hasNext() {
		
	}
	
	/**
	 * Jumps to the next token.
	 */
	public void next() {
		
	}
	
	/**
	 * Returns the word of the current token.
	 * @return the word of the current token.
	 */
	public String getWord() {
		
	}
	
	/**
	 * 
	 */
	public String getBytePosition() {
		
	}
	
	private String readWord() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(); 
		while(true) {
			byte currentChar = 0;
			try {
				currentChar = file.readByte();
			} catch (IOException e) {
				this.reachedEOF();
			}
			if (junk(currentChar)) {break;}
			bytes.write(currentChar);
		}
		byte[] byteArray = bytes.toByteArray();
		try {
			return new String(byteArray, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Bloody ISO-8859-1 is not supported!!!");
		}
		return "";
	}
	
	
	
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
	
	private void reachedEOF() {
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
