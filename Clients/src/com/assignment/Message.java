/** NAME: TANVEER AHMED SHAIK
    STUDENT ID: 1001704423 
**/

package com.assignment;

/**
 * Message POJO. This pojo holds each message from the clients in a Message
 * object.
 */
public class Message {

	private String client;
	private String destination;
	private String message;

	public Message() {
	}

	public Message(String client, String destination, String message) {
		this.client = client;
		this.destination = destination;
		this.message = message;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}