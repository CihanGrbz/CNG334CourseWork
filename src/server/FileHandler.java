package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class FileHandler extends Thread {

	private Thread t;
	private String name;

	boolean finished =  false;

	String word;
	int maxThreads;
	File files;

	int count = 0;

	public FileHandler() {

	}

	public FileHandler(String n) {
		this.name = n;
	}
	
	public int getCount() {
		return count;
	}

	public void countWords() {
		// TODO go through a given file and count matching words
		if (t == null) {
			t = new Thread(this);
            t.start();
		}		
	}

	public void run() {
		// For now, a for-loop will pseudo-count up to 150 words		
		int pseudoWordCount = (int) (Math.random() * 151) + 10;

		for (int i = 0; i < pseudoWordCount; i++) {
			count++;
		}
		
		System.out.println(count);
		finished = true;
	}
}
