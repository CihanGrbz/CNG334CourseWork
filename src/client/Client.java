//test for branch
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	// Variables needed
	Socket socket;
	DataInputStream din;
	DataOutputStream dout;
	String word;
	// File file;
	int result;

	boolean running = true;

	public static void main(String args[]) {
		new Client();
	}

	public Client() {
		try {
			// Initialize socket on local host, port 7878
			// and initialize input/output streams for server communication
			socket = new Socket("localhost", 1234);
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());

			// Listen for client input
			listenForInput();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void listenForInput() {
		Scanner sc = new Scanner(System.in);
		while (running) {
			// Wait until client enters a word to search for
			// Check only every half second to prevent too much CPU draining in busy waiting

			while (!sc.hasNextLine()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			word = sc.nextLine();

			// TODO open file to read and send as argument
			// In it's current state, the client will only send a word to search for
			// In the second part of the assignment, proper modifications will be done to open up a directory and read files

			try {
				// Write the word to the output stream so the server can read it
				dout.writeUTF(word);

				// While there is no response from the Server, do nothing.
				// Check only every 10ms to prevent too much CPU draining in busy waiting
				while (din.available() == 0) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
						// In case of exception, break out of loop
						break;
					}
				}

				// Read response from server
				result = din.readInt();

				// Present result to the user
				System.out.print("The total wordcount is: ");
				System.out.println(result);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		
		try {
			// Close everything
			socket.close();
			dout.close();
			din.close();
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
