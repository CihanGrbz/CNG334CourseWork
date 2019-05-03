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

	// Variables needed for the client
	Socket socket;
	DataInputStream din;
	DataOutputStream dout;
	
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
			// Wait until user enters a word to search for
			// Check only every half second to prevent too much CPU draining in busy waiting
			while (!sc.hasNextLine()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// The user will enter his input in this format: "path/../directory   word   maxthread" as a single string
			String word = sc.nextLine();

			try {
				// Write the user's input to the output stream so the server can read it
				// Let Server worry about separating the 3 inputs
				dout.writeUTF(word);
				dout.flush();
				

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
				int result = din.readInt();

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
