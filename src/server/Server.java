package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	// Variables needed for the Server
	ServerSocket ss;
	Socket socket;
	DataInputStream din;
	DataOutputStream dout;
	
	boolean running = true;	

	public static void main(String args[]) {
		new Server();
	}

	public Server() {
		try {
			// Initialize the server socket to port 1234 and open a socket for client
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

				// Read input from the client, and split them by space into 3; directory - word - maxthreadcount
				String[] clientInput = din.readUTF().split(" ");
				
				// Splitting the inputs accordingly
				String directory = clientInput[0];
				String word = clientInput[1];
				int maxT = Integer.parseInt(clientInput[2]);
				
				// Create a SearchHandler with the arguments
				SearchHandler searcher = new SearchHandler(directory, word, maxT);
				
				// Start file handler threads and get result
				int result = searcher.startSearch();
				
				// Send back result to the client
				dout.writeInt(result);
				dout.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
