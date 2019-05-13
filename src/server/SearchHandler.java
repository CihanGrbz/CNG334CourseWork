package server;

import java.io.File;
import java.io.FilenameFilter;

public class SearchHandler {
	
	// Variables needed for SearchHandler
	private String word;	// The word it's searching for
	private String directory;	// The directory it's looking in
	private int maxThreads;	// Number of max concurrently running threads
	
	public int wordCount = 0;	// Total word counts, to be increased by the threads
	
	public SearchHandler(String d, String w, int m) {
		this.directory = d;
		this.word = w;
		this.maxThreads = m + 1; 	// As the parent thread is included in the total thread counts, we need to increment it by one
									// Assuming the user specifies 10 as the max thread, we assume he means 10 files being concurrently searched. 10 file threads + parent thread = 11
	}
	
	// The function that the threads will call in a critical section
	public void incrementWord() {
		synchronized (this) {
			this.wordCount++;
		}
	}
	
	// This method will open the directory, and return an array of files that end with .txt
	File[] filterDirectoryForTxt() {
		File dir = new File(this.directory);
		
		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".txt");
			}
		});
	}
		
	public int startSearch() {
		
		// Go through directory and get every .txt file
		File[] files = filterDirectoryForTxt();
		
		// Display number of files found
		System.out.println("Found " + files.length + " files");
		
		// Go through text files and for every file start a thread
		for(File file: files) {
			// Make wait if number of threads are equal to or exceed the maxthread
			while(Thread.activeCount() - 1 >= this.maxThreads) {
				System.out.println("Max Threads reached! Waiting for threads to finish to start new... Active Threads: " + (Thread.activeCount() - 1));
				
				// Check only every 250ms to prevent too much CPU draining in busy waiting
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// Open a FileHandler for each file, sending the file it's looking at, word it's looking for and reference to the SearchHandler that created it
			FileHandler fh = new FileHandler(file, this.word, this);
			// And run the thread
			fh.countWords();
		}
		
		// Wait for all threads to finish before returning value
		while(Thread.activeCount() > 1) {
			System.out.println("Waiting for threads to finish... Active threads: " + Thread.activeCount());
			
			// Check only every 250ms to prevent too much CPU draining in busy waiting
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return this.wordCount;
	}

}
