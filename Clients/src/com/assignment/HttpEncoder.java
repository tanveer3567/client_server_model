/** NAME: TANVEER AHMED SHAIK
    STUDENT ID: 1001704423 
**/

package com.assignment;

import java.io.BufferedWriter;
import java.io.IOException;

import org.json.JSONObject;

/**
 * This class is responsible for Http encoding and json parsing.
 * 
 * @author Tanveer
 *
 */
public class HttpEncoder {

	/**
	 * Encodes the raw message from user to Http Message
	 * 
	 * @param bufferedWriter
	 * @param message
	 * @throws IOException
	 */
	public static void encode(BufferedWriter bufferedWriter, Message message) throws IOException {

		String jsonMessage = convertMessageToJsonString(message);
		bufferedWriter.write("POST / HTTP/1.1\r\n");
		bufferedWriter.write("User-Agent: " + message.getClient() + "\r\n");
		bufferedWriter.write("Host: localhost:8080\r\n");
		bufferedWriter.write("Connection: Keep-Alive\r\n");
		bufferedWriter.write("Accept: text/plain, application/json\n");
		bufferedWriter.write("Content-Type: application/json\r\n");
		bufferedWriter.write("Content-Length: " + jsonMessage.length() + "\r\n");
		bufferedWriter.write(jsonMessage);
		bufferedWriter.newLine();
		bufferedWriter.flush();
	}

	/**
	 * As the response from server is in http format and message format is json.
	 * This method will parse the json message.
	 * 
	 * @param message
	 * @return
	 */
	public static String convertMessageToJsonString(Message message) {
		JSONObject jsonObject = new JSONObject(message);
		return jsonObject.toString();
	}

}
