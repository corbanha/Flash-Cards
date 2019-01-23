package corban.flashcard.backend;

import java.util.ArrayList;

public class BackEnd {
	
	private ArrayList<Library> index;
	
	public BackEnd(){
		index = FileManager.loadIndex();
	}

	/**
	 * @return the index
	 */
	public ArrayList<Library> getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(ArrayList<Library> index) {
		this.index = index;
	}
}
