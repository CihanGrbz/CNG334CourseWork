package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler extends Thread {

	// Variables that a FileHandler needs to keep track of
	private Thread t;	// Its active thread
	private SearchHandler sh;	// The SearchHandler it belongs to
	private File file;	// The file it is searching in
	private String word;	// The word it is searching for

	
	public FileHandler(File f, String w ,SearchHandler s) {
		this.file = f;
		this.word = w.toLowerCase(); // For non-case-sensitive search, make it all lower case
		this.sh = s; // Keep track of the SearchHandler that send out this thread
	}

	public void countWords() {
		// When initializing the thread, check first if a thread is open. If not, open a new one and run
		if (t == null) {
			t = new Thread(this);
            t.start();
		}		
	}

	public void run() {
		// Given a filename, open a FileReader and immediately a BufferedReader
		try {
			FileReader freader = new FileReader(this.file);
			BufferedReader reader = new BufferedReader(freader);			
			
			String line;
			String[] words = null;		
			
			// Read each line from the text file until the end of the file
			while ((line = reader.readLine()) != null) {
				// Split the line by spaces, into words
				words = line.split(" ");
				
				// Compare each word to the original and increment counter if it contains the word				
				for (String word:words) {
					if (word.toLowerCase().contains(this.word)) {
						// TODO PART 3 synchronize increment, critical section
						sh.incrementWord();
					}
				}
				
				/* The function "contains(String)" is used intentionally, as it fixes two issues very easily:
				 * 1: 	We decided that, when searching for a word like 'tree', we also want to include words that include this word, for example 'Treehouse'
				 * 		Instead of comparing directly, which would give a false negative, using "contains" gives a very convenient solution to this problem
				 * 
				 * 2:	When searching line by line and then splitting words by spaces, the words we're looking at includes also things such as periods, commas etc.
				 * 		If we search for 'hello' in a sentence like "Hello, my name is..", the splitting by spaces method will result the 'Hello,' substring, giving us a false negative when directly comparing equality
				 * 		This could be fixed in many ways; we found that this very conveniently also solves this problem * 
				 * */
			}
			
			// When file has finished, close reader and let thread terminate itself
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
