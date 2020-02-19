/** NAME: TANVEER AHMED SHAIK
    STUDENT ID: 1001704423 
**/

package com.assignment;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

/**
 * This will listen for any incoming messages to the socket.
 */
public class MessageServer {

	public ServerSocket serverSocket;
	private Socket clientSocket;
	private List<UserDetails> userDetails;

	public MessageServer(ServerSocket serverSocket) throws IOException {
		this.serverSocket = serverSocket;
		this.userDetails = new ArrayList<UserDetails>();
	}

	/**
	 * This method is responsible for incoming connections.
	 * 
	 * @param textArea
	 * @throws IOException
	 */
	public void listen(JTextArea textArea, JTextArea textArea_1) throws IOException {
		int i = 1;
		Thread t = null;
		while (true) {
			if (!serverSocket.isClosed()) {
				clientSocket = this.serverSocket.accept();
				ServerThread serverThread = new ServerThread(clientSocket, textArea, textArea_1, i, userDetails);
				t = new Thread(serverThread, "User " + i);
				t.start();
				i++;
			} else {
				clientSocket.close();
				clientSocket = null;
				t.interrupt();
				break;
			}
		}
	}

}
