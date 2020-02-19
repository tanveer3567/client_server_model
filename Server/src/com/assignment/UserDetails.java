/** NAME: TANVEER AHMED SHAIK
    STUDENT ID: 1001704423 
**/

package com.assignment;

import java.io.OutputStream;

/**
 * User pojo. This will be used to store the details of all the clients who are
 * connected to the server.
 *
 */
public class UserDetails {

	private String userName;
	private OutputStream output;
	private int port;

	public UserDetails(String userName, OutputStream output, int port) {
		this.userName = userName;
		this.output = output;
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public OutputStream getOutput() {
		return output;
	}

	public void setOutput(OutputStream output) {
		this.output = output;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
