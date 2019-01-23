import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import corban.flashcard.backend.Book;
import corban.flashcard.backend.Card;
import corban.flashcard.backend.FileManager;
import corban.flashcard.backend.Library;
import corban.flashcard.backend.Timer;


public class FlashCardsTerminal {
	
	ArrayList<Library> index = new ArrayList<Library>();
	Scanner scanner = new Scanner(System.in);
	
	@SuppressWarnings("deprecation")
	public FlashCardsTerminal(){
		//System.out.println("Hello and welcome to Corban's Flash Card Program ver 0.0.2.4av3.65g\nThis is still a beta.");
		
		System.out.println("Loading Libraries...");
		
		index = FileManager.loadIndex();
		standardizeCaps();
		findDuplicates();
		
		long timeSinceLastForget = System.currentTimeMillis() - FileManager.getTimeStampOfLastForget();
		if(timeSinceLastForget >= 172800000){ 
			//if it's been longer than two days....
			int numToDum = (int) (timeSinceLastForget / 172800000);			
			System.out.println("You've forgotten some things... You lost " + numToDum + " experience.\n");

			for(int i = 0; i < index.size(); i++){
				for(int j = 0; j < index.get(i).getBooks().size(); j++){
					for(int k = 0; k < index.get(i).getBooks().get(j).getStack().size(); k++){
						if(index.get(i).getBooks().get(j).getStack().get(k).getCardStat() != -1)	index.get(i).getBooks().get(j).getStack().get(k).setCardStat(index.get(i).getBooks().get(j).getStack().get(k).getCardStat() - numToDum);
						if(index.get(i).getBooks().get(j).getStack().get(k).getCardStat() < -2) index.get(i).getBooks().get(j).getStack().get(k).setCardStat(-2);
					}
				}
			}
			FileManager.setTimeStampOfLastForget(FileManager.getTimeStampOfLastForget() + numToDum * 172800000);
		}else{
			System.out.println("Time until next forget: " + Timer.millToStringSentence(172800000 - timeSinceLastForget) + "\n");
		}
		
		
		for(int i = 0; i < index.size(); i++){
			for(int j = 0; j < index.get(i).getBooks().size(); j++){
				int numWordsToStudy = 0;
				for(int k = 0; k < index.get(i).getBooks().get(j).getStack().size(); k++){
					if(index.get(i).getBooks().get(j).getStack().get(k).getCardStat() < 10) numWordsToStudy++;
				}
				
				System.out.println(String.format("%-15s %-35s %4d/%-4d   %4.2f", index.get(i).getName(), index.get(i).getBooks().get(j).getName(),
						(index.get(i).getBooks().get(j).getStack().size() - numWordsToStudy), index.get(i).getBooks().get(j).getStack().size(),
						100 * (index.get(i).getBooks().get(j).getStack().size() - numWordsToStudy) / (double) index.get(i).getBooks().get(j).getStack().size()) + 
						"%");
			}
		}
			
		int score = 0;
		
		for(int i = 0; i < index.size(); i++){
			for(int j = 0; j < index.get(i).getBooks().size(); j++){
				for(int k = 0; k < index.get(i).getBooks().get(j).getStack().size(); k++){
					score += index.get(i).getBooks().get(j).getStack().get(k).getCardStat();
					score += index.get(i).getBooks().get(j).getStack().get(k).getNumTimesTryed();
				}
			}
		}
	
		System.out.printf("You currently have %,d Flash Card Points!\n", score);
		
		
		Date saveDate = new Date(FileManager.getTimeStampOfSave());
		
		//System.out.println(saveDate);
		
		System.out.print("\nDate of previous save: ");
		switch(saveDate.getDay()){
			case 0:
				System.out.print("Sun "); break;
			case 1:
				System.out.print("Mon "); break;
			case 2:
				System.out.print("Tue "); break;
			case 3:
				System.out.print("Wed "); break;
			case 4:
				System.out.print("Thur "); break;
			case 5:
				System.out.print("Fri "); break;
			case 6:
				System.out.print("Sat "); break;
		}
		
		int hours = saveDate.getHours();
		if(hours >= 12) hours -= 12;
		//hours++;
		System.out.print((saveDate.getMonth() + 1) + "/" + saveDate.getDate() + "/" + (saveDate.getYear() - 100) + " " + 
		String.format("%02d:%02d:%02d", hours, (saveDate.getMinutes()), saveDate.getSeconds()));
		
		if(saveDate.getHours() >= 12){
			System.out.println(" pm");
		}else{
			System.out.println(" am");
		}
		
		
		
		/*for(int i = 0; i < index.size(); i++){
			System.out.println(index.get(i).getName());
			for(int j = 0; j < index.get(i).getBooks().size(); j++){
				System.out.println("     " + index.get(i).getBooks().get(j).getName());
				for(int k = 0; k < index.get(i).getBooks().get(j).getStack().size(); k++){
					System.out.println("          " + index.get(i).getBooks().get(j).getStack().get(k).getWord());
				}
			}
		}*/
		
		
		
		while(true){
			System.out.println("\nWhat would you like to do?");
			System.out.println("1 Exit\n2 Review Cards\n3 Add Cards");
			
			String next = scanner.next();
			
			if(next.equals("2")){
				reviewCards();
			}else if(next.equals("3")){
				addCards();
			}else{
				break;
			}
		}
		
		boolean savedWell = FileManager.saveIndex(index);
		if(savedWell){
			System.out.println("\nSuccessfully Saved Files!");
		}else{
			System.out.println("\nFiles Not Saved Successfully");
		}
		
		System.exit(0);
	}
	
	public void reviewCards(){
		for(int i = 0; i < index.size(); i++){
			System.out.println(index.get(i).getName());
			for(int j = 0; j < index.get(i).getBooks().size(); j++){
				System.out.println("     " + index.get(i).getBooks().get(j).getName());
				Collections.sort(index.get(i).getBooks().get(j).getStack());
				System.out.println("         Num Cards     Card Stat");
				for(int k = -5; k < 35; k++){
					if(index.get(i).getBooks().get(j).getNumCardsWithCardStat(k) != 0){
						System.out.println("         " + String.format("%-4d          %-4d", index.get(i).getBooks().get(j).getNumCardsWithCardStat(k), k));
					}
				}
			}
		}
		
		System.out.println("\nWhat would you like to do?\n1 Back\n2 Review a Book\n3 Review a Library\n4 Review Multiple Books\n" + 
				"5 Review Multiple Libraries\n6 Review Multiple Books in Multiple Libraries");
		
		String next = scanner.next();
		switch(next){
		case "2":
			reviewBook(); break;
		case "3":
			reviewLibrary(); break;
		case "4":
			reviewMultBooks(); break;
		case "5":
			reviewMultLibraries(); break;
		case "6":
			reviewMultBooksMultLibraries(); break;
		}
	}

	private void reviewBook() {
		
		ArrayList<Card> cardsToReview = new ArrayList<Card>();
		
		System.out.println("\nPlease select the Library that the book is in:\n");		
		for(int i = 0; i < index.size(); i++){
			System.out.println("" + (i + 1) + " " + index.get(i).getName());
		}
		int library = scanner.nextInt() - 1;
		
		System.out.println("\nPlease enter the Book that you would like to review:\n");
		for(int i = 0; i < index.get(library).getBooks().size(); i++){
			System.out.println("" + (i + 1) + " " + index.get(library).getBooks().get(i).getName());
		}
		int book = scanner.nextInt() - 1;
		
		
		boolean gotGoodAnswer = false;
		String userAnswer;
		do{
			System.out.println("\nHow would you like the words sorted?\n1 By least experience\n2 By most experience\n3 Alphabetically\n4 Random");
			userAnswer = scanner.next();
			if(userAnswer.equals("1")){
				//least experience
				Collections.shuffle(index.get(library).getBooks().get(book).getStack());
				Collections.sort(index.get(library).getBooks().get(book).getStack());
				gotGoodAnswer = true;
			}else if(userAnswer.equals("2")){
				//sort cards by most experience
				gotGoodAnswer = true;
			}else if(userAnswer.equals("3")){
				//sort cards by abc
				gotGoodAnswer = true;
			}else if(userAnswer.equals("4")){
				//sort randomly
				Collections.shuffle(index.get(library).getBooks().get(book).getStack());
				gotGoodAnswer = true;
			}else{
				System.out.println("I dont understand.");
			}
		}while(!gotGoodAnswer);
		
		
		boolean gotNum = false;
		int num = 0;
		do{
			System.out.println("\nHow many cards would you like to test? Type \"all\" for the entire Book.");
			String userNum = scanner.next();
			
			if(userNum.toLowerCase().equals("all")){
				num = index.get(library).getBooks().get(book).getStack().size();
				cardsToReview = index.get(library).getBooks().get(book).getStack();
				break;
			}
			
			try{
				num = Integer.parseInt(userNum);
				
				if(num <= 0){
					System.out.println("\nThat is too small of a number.");
					continue;
				}else if(num > index.get(library).getBooks().get(book).getStack().size()){
					System.out.println("\nThat number is too large.");
					continue;
				}else{

					int numCardsKnown = 0;
					for(int i = 0; i < index.get(library).getBooks().get(book).getStack().size(); i++){
						if(index.get(library).getBooks().get(book).getStack().get(i).getCardStat() >= 10){
							numCardsKnown++;
						}
					}
					System.out.println("Num cards known: " + numCardsKnown);
					
					if(num > index.get(library).getBooks().get(book).getStack().size() - numCardsKnown){
						for(int i = 0; i < num; i++){
							cardsToReview.add(index.get(library).getBooks().get(book).getStack().get(i));
						}
					}else{
						for(int i = index.get(library).getBooks().get(book).getStack().size() - numCardsKnown - 1; 
								i >= index.get(library).getBooks().get(book).getStack().size() - numCardsKnown - num; i--){
							cardsToReview.add(0, index.get(library).getBooks().get(book).getStack().get(i));
						}
					}
					
					gotNum = true;
				}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("\nThat wouldn't work.");
				continue;
			}
			
		}while(!gotNum);
		
		System.out.println("\nPlease choose your card preference:\n1 Show Word, Test Definition\n2 Show Definition, Test Word");
		String view = scanner.next();
		
		long startTime = System.currentTimeMillis();
		
		if(view.equals("2")){
			for(int i = 0; i < cardsToReview.size(); i++){
				if(i != 0 && i % 20 == 0){
					boolean savedWell = FileManager.saveIndex(index);
					if(savedWell){
						System.out.println("\nSuccessfully Saved Files!");
					}else{
						System.out.println("\nFiles Not Saved Successfully");
					}
				}
				System.out.println("\n\nCard " + (i + 1) + "/" + cardsToReview.size() + 
						", from Book " + index.get(library).getBooks().get(book).getName());
				System.out.println(cardsToReview.get(i).getDef());
				System.out.println("\n1 Get Word\n2 Get Hint\n3 Next Word\n4 Previous Word\n5 Restart Book\n6 Delete Word\n7 Back");
				String answer = scanner.next();
				if(answer.equals("1") || answer.toLowerCase().equals("z")){
					System.out.println("\n" + cardsToReview.get(i).getWord());
					cardsToReview.get(i).setNumTimesTryed(cardsToReview.get(i).getNumTimesTryed() + 1);
					
					System.out.println("Did you get it right? (1/y)/(2/n)");
					String userGetRight = scanner.next();
					if(userGetRight.equals("1") || userGetRight.equals("y")){
						cardsToReview.get(i).setCardStat((cardsToReview.get(i).getCardStat() + 1));
					}else{
						cardsToReview.get(i).setCardStat((cardsToReview.get(i).getCardStat() / 2));
					}
					continue;
				}else if(answer.equals("2") || answer.toLowerCase().equals("x")){
					System.out.println("\n" + cardsToReview.get(i).getHint());
					i--; //Bad Convention!
				}else if(answer.equals("3") || answer.toLowerCase().equals("c")){
					continue;
				}else if(answer.equals("4") || answer.toLowerCase().equals("v")){
					i -= 2; //Bad Convention!
					if(i <= -2) i = -1;
				}else if(answer.equals("5") || answer.toLowerCase().equals("b")){
					i = -1; //Bad Convention!
				}else if(answer.equals("6") || answer.toLowerCase().equals("n")){
					cardsToReview.remove(i);
					num--;
					i -= 1; // bad convention
				}else if(answer.equals("7") || answer.toLowerCase().equals("m")){
					break;
				}else{
					i--; //Bad convention!
				}
			}
		}else{
			long timePerCard = 5000;
			for(int i = 0; i < num; i++){
				if(i != 0 && i % 20 == 0){
					boolean savedWell = FileManager.saveIndex(index);
					if(savedWell){
						System.out.println("\nSuccessfully Saved Files!");
					}else{
						System.out.println("\nFiles Not Saved Successfully");
					}
				}
				
				System.out.println("\n\nCard " + (i + 1) + "/" + num + ", from Book " + index.get(library).getBooks().get(book).getName());
				System.out.println(Timer.millToString(timePerCard * (num - i)).substring(3) + "\n\n");
				
				
				timePerCard = (System.currentTimeMillis() - startTime) / (i + 1);
				//System.out.println(timePerCard);
				System.out.println(cardsToReview.get(i).getWord());
				System.out.println("\n1 Get Definition\n2 Get Hint\n3 Next Word\n4 Previous Word\n5 Restart Book\n6 Delete Word\n7 Back");
				String answer = scanner.next();
				if(answer.equals("1") || answer.toLowerCase().equals("z")){
					if(cardsToReview.get(i).getDef().contains("$")){
						System.out.println();
						for(int l = 0; l < cardsToReview.get(i).getDef().length(); l++){
							if(cardsToReview.get(i).getDef().substring(l, l + 1).equals("$")){
								System.out.println();
							}else{
								System.out.print(cardsToReview.get(i).getDef().substring(l, l + 1));
							}
						}
						System.out.println();
					}else{
						System.out.println("\n" + cardsToReview.get(i).getDef());
					}
					cardsToReview.get(i).setNumTimesTryed(cardsToReview.get(i).getNumTimesTryed() + 1);
					
					System.out.println("Did you get it right? (1/y)/(2/n)");
					String userGetRight = scanner.next();
					if(userGetRight.equals("1") || userGetRight.equals("y")){
						cardsToReview.get(i).setCardStat((cardsToReview.get(i).getCardStat() + 1));
					}else{
						cardsToReview.get(i).setCardStat((cardsToReview.get(i).getCardStat() / 2));
					}
					
					continue;
				}else if(answer.equals("2") || answer.toLowerCase().equals("x")){
					System.out.println("\n" + cardsToReview.get(i).getHint());
					i--; //Bad Convention!
				}else if(answer.equals("3") || answer.toLowerCase().equals("c")){
					continue;
				}else if(answer.equals("4") || answer.toLowerCase().equals("v")){
					i -= 2; //Bad Convention!
					if(i <= -2) i = -1;
				}else if(answer.equals("5") || answer.toLowerCase().equals("b")){
					i = -1; //Bad Convention!
				}else if(answer.equals("6") || answer.toLowerCase().equals("n")){
					cardsToReview.remove(i);
					num--;
					i -= 1; // bad convention
				}else if(answer.equals("7") || answer.toLowerCase().equals("m")){
					break;
				}else{
					i--; //Bad convention!
				}
			}
		}
	}
	
	private void reviewLibrary() {
		System.out.println("This Option is not yet supported.");
	}
	
	private void reviewMultBooks() {
		System.out.println("This Option is not yet supported.");
	}

	private void reviewMultLibraries() {
		System.out.println("This Option is not yet supported.");
	}

	private void reviewMultBooksMultLibraries() {
		System.out.println("This Option is not yet supported.");
	}

	public void addCards(){
		System.out.println("\nWhat would you like to do?\n1 Back\n2 Add a Library\n3 Add a Book to an existing Library\n" +
				"4 Add a Card to an existing Book in an existing Library\n5 Edit a Library\n6 Edit a Book\n7 Edit a Card");
		String next = scanner.next();
		switch(next){
			case "2":
				addLibrary(); break;
			case "3":
				addBook(); break;
			case "4":
				addCard(); break;
			case "5":
				editLibrary(); break;
			case "6":
				editBook(); break;
			case "7":
				editCard(); break;
		}
	}
	
	public void addLibrary(){
		System.out.print("What is the name of your new Library?: ");
		String name = scanner.next();
		//System.out.println("\nPlease describe your new Library on the line below:");
		//String description = scanner.nextLine();
		index.add(new Library(name, ""));
	}
	
	public void addBook(){
		if(index.size() > 0){
			for(int i = 0; i < index.size(); i++){
				System.out.println("" + (i + 1) + " " + index.get(i).getName());
			}
			System.out.print("\nPlease choose the Library to add the word:");
			int library = scanner.nextInt() - 1;
			
			System.out.print("What is the name of your new Book?");
			String name = scanner.next();
			//System.out.println("\nPlease describe your new Book on the line below:");
			//String description = scanner.nextLine();
			
			index.get(library).getBooks().add(new Book(name, ""));
		}else{
			System.out.println("Please add a Library First.");
		}
	}
	
	public void addCard(){
		if(index.size() > 0){
			for(int i = 0; i < index.size(); i++){
				System.out.println("" + (i + 1) + " " + index.get(i).getName());
			}
			System.out.print("\nPlease choose the Library to add the word:");
			int library = scanner.nextInt() - 1;
			
			if(index.get(library).getBooks().size() > 0){
				for(int i = 0; i < index.get(library).getBooks().size(); i++){
					System.out.println("" + (i + 1) + " " + index.get(library).getBooks().get(i).getName());
				}
				System.out.print("\nPlease choose the Book to add the word:");
				int book = scanner.nextInt() - 1;
				
				System.out.print("What is the word?:");
				String word = scanner.next();
				System.out.println("\nWhat is the Definition?:");
				String def = scanner.next();
				System.out.println("\nWhat is the hint for this Word?:");
				String hint = scanner.next();
				
				index.get(library).getBooks().get(book).getStack().add(new Card(word, def, hint));
			}else{
				System.out.println("Please add a Book to this Library First.");
			}
		}else{
			System.out.println("Please add a Library First.");
		}
	}
	
	public void editLibrary(){
		System.out.println("This Option is not yet supported.");
	}
	
	public void editBook(){
		System.out.println("This Option is not yet supported.");
	}
	
	public void editCard(){
		System.out.println("This Option is not yet supported.");
	}
	
	public void standardizeCaps(){
		for(int i = 0; i < index.size(); i++){
			for(int j = 0; j < index.get(i).getBooks().size(); j++){
				for(int k = 0; k < index.get(i).getBooks().get(j).getStack().size(); k++){
					String word = index.get(i).getBooks().get(j).getStack().get(k).getWord();
					word.toLowerCase();
					word = word.substring(0, 1).toUpperCase() + word.substring(1); //make the first letter capital
					index.get(i).getBooks().get(j).getStack().get(k).setWord(word);					
				}
			}
		}
	}
	
	public void findDuplicates(){
		System.out.println("Looking for Duplicates...");
		for(int i = 0; i < index.size(); i++){
			//System.out.println(index.get(i).getName());
			for(int j = 0; j < index.get(i).getBooks().size(); j++){
				//System.out.println(index.get(i).getBooks().get(j).getName());
				for(int k = 0; k < index.get(i).getBooks().get(j).getStack().size() - 1; k++){
					for(int l = k + 1; l < index.get(i).getBooks().get(j).getStack().size(); l++){
						if(index.get(i).getBooks().get(j).getStack().get(k).getWord().equals(index.get(i).getBooks().get(j).getStack().get(l).getWord())){
							System.out.println(String.format("%-25s%-20s%-20s", index.get(i).getBooks().get(j).getStack().get(k).getWord(), index.get(i).getBooks().get(j).getName(), index.get(i).getName()));
						}
					}
				}
			}
		}
		System.out.println();
	}
	
	public double getPercentSpellingCheck(String correctWord, String testWord){
		correctWord.trim();
		testWord.trim();
		if(correctWord.equals(testWord)) return 1;
		
		ArrayList<String> twPieces = new ArrayList<String>();
		for(int i = 1; i < testWord.length(); i++){ //length of piece
			for(int j = 0; j < testWord.length() - i + 1; j++){ //cut pieces out of string
				twPieces.add(testWord.substring(j, j + i));
				
			}
		}
		twPieces.add(testWord);
		
		ArrayList<String> cwPieces = new ArrayList<String>();
		for(int i = 1; i < correctWord.length(); i++){
			for(int j = 0; j < correctWord.length() - i + 1; j++){
				cwPieces.add(correctWord.substring(j, j + i));
				
			}
		}
		cwPieces.add(correctWord);
		
		double orgSize;
		if(twPieces.size() > cwPieces.size()){
			orgSize = twPieces.size();
			
			for(int i = 0; i < twPieces.size(); i++){
				if(i == twPieces.size()) break;
				if(!cwPieces.contains(twPieces.get(i))){
					twPieces.remove(i);
					i--;
				}
			}
			
			System.out.println(twPieces.size() + " / " + orgSize);
			return twPieces.size() / orgSize;
		}else{
			orgSize = cwPieces.size();
			
			for(int i = 0; i < cwPieces.size(); i++){
				if(i == cwPieces.size()) break;
				if(!twPieces.contains(cwPieces.get(i))){
					cwPieces.remove(i);
					i--;
				}
			}
			
			System.out.println(cwPieces.size() + " / " + orgSize);
			return cwPieces.size() / orgSize;
		}
	}
	
	private String sortFor10FastFingers(String str){
		String finishedString = "";
		Scanner strScanner = new Scanner("");
		int waitTime = 800;
		
		try {
			strScanner = new Scanner(new File("C:\\FlashCards\\10FastFingersTesting.txt"));
			PrintWriter writer = new PrintWriter(new File("C:\\Users\\Corban\\Desktop\\10fastfingers.ahk"));
			
			writer.println("Sleep 3500");
			
			int wordNum = 0;
			int time = 0;
			
			while(strScanner.hasNext()){
				String word = strScanner.next();
				//System.out.println(word);
				if(word.startsWith("class") && word.length() > 17){
					if(wordNum == 0 && word.length() > 25){
						finishedString += "\nSend " + word.substring(18, word.length() - 7) + "{Space}\nSleep " + waitTime;
						writer.println("Send " + word.substring(18, word.length() - 7) + "{Space}");
					}else{
						finishedString += "\nSend " + word.substring(9, word.length() - 7) + "{Space}\nSleep " + waitTime;
						writer.println("Send " + word.substring(9, word.length() - 7) + "{Space}");
					}
					
					writer.println("Sleep " + waitTime);
					
					wordNum++;
					time += waitTime;
					if(time >= 55000) break;
				}
				//if(wordNum > 5) break;
			}
			
			writer.println("#NoEnv");
			writer.println("SendMode Input");
			writer.println("SetWorkingDir %A_ScriptDir%");
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		strScanner.close();
		return "";
	}
}
