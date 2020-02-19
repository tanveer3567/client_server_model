/** NAME: TANVEER AHMED SHAIK
    STUDENT ID: 1001704423 
**/

package com.assignment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import javax.swing.JTextArea;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class has the methods responsible for a client request
 */
public class ServerThread implements Runnable {

	private Socket socket;
	private Scanner scan = null;
	public JTextArea textArea = null;
	public JTextArea textArea_1 = null;
	public boolean isRequest = false;
	public List<UserDetails> userDetails;
	public boolean isConnected;

	public ServerThread(Socket socket, JTextArea textArea, JTextArea textArea_1, int id, List<UserDetails> userDetails)
			throws IOException {
		this.socket = socket;
		this.scan = new Scanner(new InputStreamReader(socket.getInputStream()));
		this.textArea = textArea;
		this.userDetails = userDetails;
		this.textArea_1 = textArea_1;
	}

	/**
	 * This handles all the incoming messages to the server.
	 */
	@Override
	public void run() {
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
			PrintWriter wr = new PrintWriter(outputStream);
			ArrayList<String> httpMessageList = null;
			while (scan.hasNext()) {
				String input = scan.nextLine();
				Message message = jsonParser(input);
				if (input != null && !input.isEmpty()) {
					if (input.toUpperCase().contains("POST") || input.toUpperCase().contains("GET")) {
						httpMessageList = new ArrayList<String>();
						textArea.append("\n" + input);
						httpMessageList.add("HTTP/1.1 200 OK");
					} else {
						if (input.substring(0, 11).equalsIgnoreCase("User-Agent:")) {
							httpMessageList.add("Date: " + DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC)
									.format(Instant.now()));
						} else if (input.substring(0, 5).equalsIgnoreCase("Host:")) {
							httpMessageList.add("Server: Local/1.0 (Win64)");
						} else {
							httpMessageList.add(input);
							if (input.substring(0, 7).equalsIgnoreCase("Accept:")) {
								httpMessageList.remove(httpMessageList.size() - 1);
							}
							textArea.append("\n" + input);
							if (Objects.nonNull(message)) {
								if (message.getMessage().contains("REQUEST_TO_CONNECT")) {
									try {
										userDetails.add(new UserDetails(message.getClient(), socket.getOutputStream(),
												socket.getPort()));
										textArea_1.append(message.getClient() + ": " + "CONNECTED\n");
										isConnected = true;
									} catch (IOException e) {
										e.printStackTrace();
									}
									httpSuccessResponseEncoder(wr, textArea, true);
								} else {
									if (isConnected) {
										if (message.getMessage().contains("REQUEST_TO_DISCONNECT")) {
											deleteUser(message.getClient());
											httpSuccessResponseEncoder(wr, textArea, false);
											textArea_1.append(message.getClient() + ": " + "DISCONNECTED\n");
											isConnected = false;
											break;
										} else {
											UserDetails destination = checkDestination(message);
											if (Objects.nonNull(destination)) {
												BufferedWriter bufferedWriter = new BufferedWriter(
														new OutputStreamWriter(destination.getOutput()));
												textArea.append("\nSending message to recipient .....\n");
												httpMessageList.forEach(httpMessage -> {
													try {
														textArea.append(httpMessage + "\n");
														bufferedWriter.write(httpMessage);
														bufferedWriter.newLine();
														bufferedWriter.flush();
													} catch (IOException e) {
														e.printStackTrace();
													}
												});
												textArea.append("Message successfully delivered to recipient .....");
												httpSuccessResponseEncoder(wr, textArea, true);
											} else {
												httpNotFoundResponse(wr, textArea);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e1) {
		}
	}

	/**
	 * This method will structure 200 Ok Success Http response and append it to
	 * Server GUI.
	 * 
	 * @param wr
	 * @param textArea
	 * @param check
	 */
	private void httpSuccessResponseEncoder(PrintWriter wr, JTextArea textArea, boolean check) {
		String str1 = "\nHTTP/1.1 200 OK\n";
		String str2 = "Date: " + DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC).format(Instant.now())
				+ "\n";
		String str21 = "Server: Local/1.0 (Win64)\n";
		String str3 = "Content-Length: 0\n";
		String str4 = "Content-Type: text/plain\n";
		String str5 = "Connection: Keep-Alive\n";
		String str6 = "Connection: Closed\n";

		wr.println(str1);
		textArea.append(str1);
		wr.println(str2);
		textArea.append(str2);
		wr.println(str21);
		textArea.append(str21);
		wr.println(str3);
		textArea.append(str3);
		wr.println(str4);
		textArea.append(str4);
		if (check) {
			wr.println(str5);
			textArea.append(str5);
		} else {
			wr.println(str6);
			textArea.append(str6);
		}
		wr.flush();
	}

	/**
	 * This method will structure 404 Not Found Http response and append it to
	 * Server GUI.
	 * 
	 * @param wr
	 * @param textArea
	 */
	private void httpNotFoundResponse(PrintWriter wr, JTextArea textArea) {

		String str1 = "\nHTTP/1.1 404 Not Found\n";
		String str2 = "Date: " + DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC).format(Instant.now())
				+ "\n";
		String str21 = "Server: Local/1.0 (Win64)\n";
		String str3 = "Content-Length: 0\n";
		String str4 = "Content-Type: text/plain\n";
		String str5 = "Connection: Keep-Alive\n";

		wr.println(str1);
		textArea.append(str1);
		wr.println(str2);
		textArea.append(str2);
		wr.println(str21);
		textArea.append(str21);
		wr.println(str3);
		textArea.append(str3);
		wr.println(str4);
		textArea.append(str4);
		wr.println(str5);
		textArea.append(str5);
		wr.flush();
	}

	/**
	 * This method will destination of a clients message
	 * 
	 * @param message
	 * @return
	 */
	private UserDetails checkDestination(Message message) {

		for (UserDetails userDetail : userDetails) {
			if (userDetail.getUserName().equalsIgnoreCase(message.getDestination())) {
				return userDetail;
			}
		}
		return null;
	}

	/**
	 * As the incoming request is http and message format is json. This method will
	 * parse the json message.
	 * 
	 * @param message
	 * @return
	 */
	public Message jsonParser(String message) {

		Message msgObject = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			msgObject = mapper.readValue(message, Message.class);
		} catch (Exception e) {
			return msgObject;
		}
		return msgObject;
	}

	/**
	 * This method will be used to delete the user details after a user has
	 * disconnected.
	 * 
	 * @param user
	 */
	public void deleteUser(String user) {
		for (int i = 0; i < userDetails.size(); i++) {
			if (userDetails.get(i).getUserName().equalsIgnoreCase(user)) {
				userDetails.remove(i);
			}
		}
	}

}
