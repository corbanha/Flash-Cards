package corban.flashcard.backend;

import java.util.ArrayList;

/**
 * This is a Stack, it holds and manages Card objects.
 * @author CA
 */

public class Book {
	
	private String name;
	private String des;
	private ArrayList<Card> book;
	
	/**
	 * Creates a Stack.
	 * @param name the name of the book
	 * @param des the description of the Stack
	 * @param book the book of Card objects to set
	 */
	public Book(String name, String des, ArrayList<Card> book){
		this.name = name;
		this.des = des;
		this.book = book;
	}
	
	/**
	 * Creates a Stack with a book of 0 cards.
	 * @param name the name of the book
	 * @param des the description of the book
	 */
	public Book(String name, String des){
		this(name, des, new ArrayList<Card>());
	}
	
	/**
	 * Creates a book with blank name and description and an empty book.
	 */
	public Book(){
		this("", "");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the des
	 */
	public String getDes() {
		return des;
	}

	/**
	 * @return the book
	 */
	public ArrayList<Card> getStack() {
		return book;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param des the des to set
	 */
	public void setDes(String des) {
		this.des = des;
	}

	/**
	 * @param book the book to set
	 */
	public void setStack(ArrayList<Card> book) {
		this.book = book;
	}
	
	/**
	 * Returns the number of cards in the book with the given card stat.
	 * @param cardStat the card stat to test
	 * @return the number of cards with the given card stat
	 */
	public int getNumCardsWithCardStat(int cardStat){
		int num = 0;
		for(int i = 0; i < book.size(); i++){
			if(book.get(i).getCardStat() == cardStat) num++;
		}
		return num;
	}
}
