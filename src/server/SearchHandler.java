package server;

public class SearchHandler {
	
	// Variables needed for SearchHandler
	private String word;	// The word it's searching for
	private String directory;	// The directory it's looking in
	private int maxThreads;	// Number of max concurrently running threads
	
	public int wordCount = 0;	// Total word counts, to be increased by the threads
	
	public SearchHandler(String d, String w, int m) {
		this.directory = d;
		this.word = w;
		this.maxThreads = m;
	}
	
	// The function that the threads will call in a critical section
	public void incrementWord() {
		this.wordCount++;
	}
		
	public int startSearch() {
		// TODO given a directory name, get a list of all text files within the directory and send it to search handler
		
		
		// TODO go through directory and for every file found start a thread
		for(int i = 0; i < 1; i++) {
			// TODO make wait if number of threads exceed maxthread
			FileHandler f = new FileHandler(this.directory, this.word, this);
			f.countWords();
		}
		
		// TODO wait for all threads to finish before counting
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return this.wordCount;
	}

}
