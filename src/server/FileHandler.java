package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

public class FileHandler extends Thread {

	private Thread t;
	// private SearchHandler sh;
	private String fileName;
	private String word;
	private int count;

	public FileHandler() {
	}
	
	// TODO send SearchHandler while creating threads
	public FileHandler(String f, String w /* ,SearchHandler s*/) {
		this.fileName = f;
		this.word = w.toLowerCase(); // For non-case-sensitive search, make it all lower case
		this.count = 0;
		// this.sh = s; // Keep track of the SearchHandler
	}
	
	public int getCount() {
		return this.count;
	}

	public void countWords() {
		if (t == null) {
			t = new Thread(this);
            t.start();
		}		
	}

	public void run() {
		// Given a filename, open a FileReader and immediately a BufferedReader
		try {
			File file = new File(this.fileName);
			FileReader freader = new FileReader(file);
			BufferedReader reader = new BufferedReader(freader);			
			
			String line;
			String[] words = null;		
			
			// Read each line from the text file until the end of the file
			while ((line = reader.readLine()) != null) {
				// Split the line by spaces, into words
				words = line.split(" ");
				// Compare each word to the original and increment counter
				for (String word:words) {
					if (word.toLowerCase().contains(this.word)) {
						// TODO synchronize increment
						this.count++;
						// sh.count++;
					}
				}
				
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("Thread found: ");
		System.out.println(count);
	}
}
