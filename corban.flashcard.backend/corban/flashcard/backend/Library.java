package corban.flashcard.backend;

import java.util.ArrayList;

/**
 * A Library holds a set of Books
 * @author CA
 */
public class Library {
	
	private String name;
	private String des;
	private ArrayList<Book> books;
	
	/**
	 * Creates a Library.
	 * @param name the name of the Library
	 * @param des the description of the Library
	 * @param books the books in the Library
	 */
	public Library(String name, String des, ArrayList<Book> books){
		this.name = name;
		this.des = des;
		this.books = books;
	}
	
	
	/**
	 * Creates an empty Library.
	 * @param name the name of the Library
	 * @param des the description of the Library
	 */
	public Library(String name, String des){
		this(name, des, new ArrayList<Book>());
	}
	
	/**
	 * Creates an empty Library with a blank name and description.
	 */
	public Library(){
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
	 * @return the books
	 */
	public ArrayList<Book> getBooks() {
		return books;
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
	 * @param books the books to set
	 */
	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}
}
