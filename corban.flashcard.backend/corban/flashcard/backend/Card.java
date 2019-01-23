package corban.flashcard.backend;

/**
 * This is a Card with a word, definition, hint, knowledge score and a try number.
 * @author CA
 */
public class Card implements Comparable<Card>{
	
	private String word;
	private String def;
	private String hint;
	private int cardStat;
	private int numTimesTryed;
	
	/**
	 * This creates a Card.
	 * @param word the word
	 * @param def the definition for the word
	 * @param hint the hint for the word
	 * @param cardStat the card knowledge value
	 * @param numTimesTryed the number of times that the card has been tested
	 */
	public Card(String word, String def, String hint, int numTimesTryed, int cardStat){
		this.word = word;
		this.def = def;
		this.setHint(hint);
		this.cardStat = cardStat;
		this.numTimesTryed = numTimesTryed;
	}
	
	/**
	 * This creates a Card with the card knowledge value and number of tries at 0 by default.
	 * @param word the word
	 * @param def the definition for the word
	 * @param hint the hint for the word
	 */
	public Card(String word, String def, String hint){
		this(word, def, hint, 0, 0);
	}
	
	/**
	 * This creates a Card with the word, definition and hint an empty string and <br>
	 * the card knowledge value and number of tries at 0 by default.
	 */
	public Card(){
		this("", "", "");
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @return the def
	 */
	public String getDef() {
		return def;
	}
	
	/**
	 * @return the hint
	 */
	public String getHint() {
		return hint;
	}

	/**
	 * @return the cardStat
	 */
	public int getCardStat() {
		return cardStat;
	}

	/**
	 * @return the numTimesTryed
	 */
	public int getNumTimesTryed() {
		return numTimesTryed;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @param def the def to set
	 */
	public void setDef(String def) {
		this.def = def;
	}
	
	/**
	 * @param hint the hint to set
	 */
	public void setHint(String hint) {
		this.hint = hint;
	}

	/**
	 * @param cardStat the cardStat to set
	 */
	public void setCardStat(int cardStat) {
		this.cardStat = cardStat;
	}

	/**
	 * @param numTimesTryed the numTimesTryed to set
	 */
	public void setNumTimesTryed(int numTimesTryed) {
		this.numTimesTryed = numTimesTryed;
	}

	@Override
	public int compareTo(Card card) {
		return this.cardStat - card.cardStat;
	}
}
