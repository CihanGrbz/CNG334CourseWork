package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

	// Variables needed
	ServerSocket ss;
	Socket socket;
	DataInputStream din;
	DataOutputStream dout;
	String word;
	int result;
	///File file;
	boolean running = true;
	
	ArrayList<FileHandler> files = new ArrayList<FileHandler>();

	public static void main(String args[]) {
		new Server();
	}

	public Server() {
		try {
			// Initialize the server socket to port 7878 and open a socket for client
			// connections
			// and initialize input/output streams for client communication
			ss = new ServerSocket(1234);
			socket = ss.accept();
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());

			listenForClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listenForClient() {
		while (running) {

			try {
				// While there is no input from the Client, do nothing.
				// Check only every 10ms to prevent too much CPU draining in busy waiting
				while (din.available() == 0) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
						// In case of an exception, break out of loop
						break;
					}
				}

				// Read input from client
				word = din.readUTF();

				// Start file handler threads and get result
				result = startSearch();

				// Send back result to the client
				dout.writeInt(result);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public int startSearch() {
		// TODO given a directory name, get a list of all text files within the directory and send it to search handler
		// TODO use SearchHandler in order to start searching and managing the FileHandler
		
		// Instead, for now the server will manually let the FileHandler run some pseudo-threads		
		
		int result = 0;
		
		// Generate a random amount of threads to imitate a pseudo amount of text files in a directory
		int randomFiles = (int) (Math.random()*51 + 15);
		System.out.print("Number of files found: ");
		System.out.println(randomFiles);
		
		for(int i = 0; i < randomFiles; i++) {
			FileHandler f = new FileHandler();
			files.add(f); 
			f.countWords();
		}
		
		// TODO wait for all threads to finish before counting
		
		
		for (FileHandler fileH : files) {
			result = result + fileH.getCount();
		}
		
		return result;
	}

}
