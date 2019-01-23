package corban.flashcard.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {
	
	private static String loadFilePrefix = "C:\\FlashCards";
	private static long saveTimeStamp = 0;
	private static long timeStampOfLastForget = 0;
	
	public static ArrayList<Library> loadIndex(){
		ArrayList<Library> index = new ArrayList<Library>();
		
		try {
			if(new File(loadFilePrefix + "\\Libraries.txt").exists()){
				Scanner libraryScanner = new Scanner(new File(loadFilePrefix + "\\Libraries.txt"));
				saveTimeStamp = libraryScanner.nextLong();
				libraryScanner.nextLine(); //this consumes the new line character that the nextLong() doesn't
				timeStampOfLastForget = libraryScanner.nextLong();
				libraryScanner.nextLine();
				
				while(libraryScanner.hasNextLine()){
					String name = libraryScanner.nextLine();
					String description = libraryScanner.nextLine();
					index.add(new Library(name, description)); // here we create the empty libraries.
				}
				
				libraryScanner.close();
				
				//get the books for each library
				for(int i = 0; i < index.size(); i++){
					Scanner scanner = new Scanner(new File(loadFilePrefix + "\\" + index.get(i).getName() + ".txt"));
					ArrayList<Book> books = new ArrayList<Book>();
					while(scanner.hasNextLine()){
						String name = scanner.nextLine();
						String description = scanner.nextLine();
						books.add(new Book(name, description));
					}
					scanner.close();
					index.get(i).setBooks(books);
				}
				
				//now we get the cards for each book of each library
				for(int i = 0; i < index.size(); i++){
					for(int j = 0; j < index.get(i).getBooks().size(); j++){
						Scanner scanner = new Scanner(new File(loadFilePrefix + "\\" + index.get(i).getName() + "_" + index.get(i).getBooks().get(j).getName() + ".txt"));
						ArrayList<Card> cards = new ArrayList<Card>();
						//int line = 0;
						while(scanner.hasNextLine()){
							//line += 5;
							String name = scanner.nextLine();
							String definition = scanner.nextLine();
							String hint = scanner.nextLine();
							String numTimesTryed = scanner.nextLine();
							String stats = scanner.nextLine();
							//System.out.println(index.get(i).getBooks().get(j).getName() + " " + line);
							cards.add(new Card(name, definition, hint, Integer.parseInt(numTimesTryed), Integer.parseInt(stats)));
						}
						scanner.close();
						index.get(i).getBooks().get(j).setStack(cards);
						
					}
				}
				
				return index;
			}else{
				//The main file doesn't exist
				System.out.println("Couldn't get the Files!!!");
				return new ArrayList<Library>();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<Library>();
		}
	}
	
	public static boolean saveIndex(ArrayList<Library> index){
		try {
			if(!(new File(loadFilePrefix)).exists()){
				new File(loadFilePrefix).mkdir();
			}
			
			PrintWriter librariesOut = new PrintWriter(loadFilePrefix + "\\Libraries.txt");
			
			librariesOut.println(System.currentTimeMillis());
			librariesOut.println(timeStampOfLastForget);
			
			for(int i = 0; i < index.size(); i++){
				librariesOut.println(index.get(i).getName()); //Prints a list of the Libraries
				librariesOut.println(index.get(i).getDes()); //With their description
			}
			
			librariesOut.close();
			
			//For each library, we will create a file that shows all of the books
			for(int i = 0; i < index.size(); i++){
				PrintWriter writer = new PrintWriter(loadFilePrefix + "\\" + index.get(i).getName() + ".txt"); //Make a file for each library
				for(int j = 0; j < index.get(i).getBooks().size(); j++){ //Writes the books
					writer.println(index.get(i).getBooks().get(j).getName()); // writes the book name and Descriptions
					writer.println(index.get(i).getBooks().get(j).getDes());
				}
				writer.close();
			}
			
			//for each book in each library we will create a file to house all of the Cards
			for(int i = 0; i < index.size(); i++){
				for(int j = 0; j < index.get(i).getBooks().size(); j++){
					PrintWriter writer = new PrintWriter(loadFilePrefix + "\\" + index.get(i).getName() + "_" + index.get(i).getBooks().get(j).getName() + ".txt");
					for(int k = 0; k < index.get(i).getBooks().get(j).getStack().size(); k++){
						writer.println(index.get(i).getBooks().get(j).getStack().get(k).getWord());
						writer.println(index.get(i).getBooks().get(j).getStack().get(k).getDef());
						writer.println(index.get(i).getBooks().get(j).getStack().get(k).getHint());
						writer.println(index.get(i).getBooks().get(j).getStack().get(k).getNumTimesTryed());
						writer.println(index.get(i).getBooks().get(j).getStack().get(k).getCardStat());
					}
					writer.close();
				}
			}
			
			return true;
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns the time in Milliseconds of the last save
	 * @return the time in milliseconds
	 */
	public static long getTimeStampOfSave(){
		if(saveTimeStamp == 0) loadIndex();
		return saveTimeStamp;
	}
	
	public static long getTimeStampOfLastForget(){
		if(timeStampOfLastForget == 0) loadIndex();
		return timeStampOfLastForget;
	}
	
	public static boolean setTimeStampOfLastForget(long timeStamp){
		if(timeStamp > timeStampOfLastForget){
			timeStampOfLastForget = timeStamp;
			return true;
		}
		return false;
	}
}
