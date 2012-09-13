
public class PerfectHashTruplet {

	private String data;
	
	public PerfectHashTruplet(String data) {
		this.data = getTruplet(data);
	}
	
	/*
	 * This method is supposed to receive a truplet - word consisting of 3 letters, and code it
	 * according to our hash function: x*30^0 + y*30^1 + z*30^2, where x,y,z are 
	 * the letters of our truplet
	 * The following formula genereates a UNIQUE code for EACH truplet ranging
	 *  from all possible 30^3 combinations between aaa to ööö
	 */
	@Override
	public int hashCode(){
		String word = data;
		int out =0;
		
		if(word.length()>=1){
			out+=charToInt(word.charAt(0));
		}
		if(word.length()>=2){
			out+=charToInt(word.charAt(1))*30;
		}
		if(word.length()==3){
			out+=charToInt(word.charAt(2))*900;
		}
		if(word.length() == 0 || word.length() > 3){
			throw new IllegalArgumentException("Hash function only defined for 1-3 length strings.");
		}
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PerfectHashTruplet) {
			PerfectHashTruplet truplet = (PerfectHashTruplet) obj;
			if (this.toString().equals(truplet.toString())) return true;
		}
		return false;
	}
	
	private int charToInt(char c) {
		switch (c) {
		case 'å':
			return 28;
		case 'ä':
			return 29;
		case 'ö':
			return 30;
		default:
			return c - 96;
		}
	}
	
	/**
	 * Returns the first three letters in a given string.
	 * Or, if the string is shorter than 3 letters, returns the string unmodified.
	 * @param word the word to get the truplet for.
	 * @return the first three letters in a given string. 
	 * Or, if the string is shorter than 3 letters, returns the string unmodified.
	 */
	private String getTruplet(final String word) {
		int trupletEnd = (word.length() < 3 ? word.length() : 3);
		final String truplet = word.substring(0, trupletEnd);
		return truplet;
	}
	
	@Override
	public String toString() {
		return data;
	}
}
