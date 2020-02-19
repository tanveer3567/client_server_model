/** NAME: TANVEER AHMED SHAIK
    STUDENT ID: 1001704423 
**/

package com.assignment;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The class is responsible for Client 3 operations.
 * 
 * @author Tanveer
 *
 */
public class Client3 extends JFrame {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private static Client3 frame = null;
	private JPanel contentPane;
	private Socket socket;
	private DataOutputStream output;
	private OutputStreamWriter outputStreamWriter;
	private BufferedWriter bufferedWriter;
	private boolean isConnected = false;
	private JButton btnNewButton = new JButton("Connect to Server");
	private JButton btnNewButton_1 = new JButton("Disconnect from Server");
	private static int logicalTime = -1;
	private static Random random = new Random();
	static Thread t0 = null;
	static Thread t1 = null;
	static Thread t2 = null;
	private static JLabel label = null;

	/**
	 * Launch the application.
	 */
	public static Runnable r = () -> {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (frame == null) {
						frame = new Client3("User 3", -1);
						frame.setVisible(true);
						closingListner();
					}
				} catch (Exception e) {
				}
			}
		});
	};

	/**
	 * Parses the incoming body of http request as its a json.
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
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	private Client3(String id, int time) throws UnknownHostException, IOException {

		logicalTime = time;

		String[] users = { "User 1", "User 2" };

		btnNewButton.setEnabled(true);
		btnNewButton_1.setEnabled(false);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 646, 607);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setFont(new Font("Calibri", Font.PLAIN, 20));

		Runnable listener = () -> {
			Scanner scan;
			try {
				scan = new Scanner(socket.getInputStream());
				while (scan.hasNext()) {
					String input = scan.nextLine();
					Message message = jsonParser(input);
					if (Objects.nonNull(message)) {
						textArea_1.append(message.getClient() + " : " + message.getMessage() + "\n");
						int remoteTime = Integer.parseInt(message.getMessage());
						if (remoteTime >= logicalTime) {
							int tempTime = logicalTime;
							logicalTime = remoteTime + 1;
							textArea_1.append("Adjustment necessary: local time adjusted from " + tempTime + " to "
									+ logicalTime + "\n");
						} else {
							textArea_1.append("No adjustment necessary. Because local time is "+ logicalTime+"\n");
						}
					} else if (input.contains("HTTP/1.1 200 OK")) {
						textArea_1.append("Server: Success\n");
					} else if (input.contains("HTTP/1.1 404 Not Found")) {
						textArea_1.append("Server: Message not send as the receiver is unavialable\n");
					}
				}
			} catch (IOException e1) {
			}
		};

		Runnable sendMessage = () -> {
			try {
				while (true) {
					Thread.sleep((random.nextInt(11) + 1) * 1000);
					Message message = new Message("User 3", users[random.nextInt(2)], Integer.toString(logicalTime));
					HttpEncoder.encode(bufferedWriter, message);
					textArea_1.append(
							message.getClient() + "->" + message.getDestination() + ": " + message.getMessage() + "\n");
				}
			} catch (IOException | InterruptedException e1) {
			}
		};

		Runnable counter = () -> {
			try {
				while (true) {
					Thread.sleep(1000);
					logicalTime += 1;
					label.setText(String.valueOf(logicalTime));
				}
			} catch (InterruptedException e1) {
			}
		};

		if (logicalTime < 0) {
			logicalTime = random.nextInt(51) + 1;
			t0 = new Thread(counter);
			t0.start();
		}

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Message message = new Message("User 3", null, "REQUEST_TO_CONNECT");
				try {
					socket = new Socket("localhost", 8080);
					output = new DataOutputStream(socket.getOutputStream());
					outputStreamWriter = new OutputStreamWriter(output);
					bufferedWriter = new BufferedWriter(outputStreamWriter);
					HttpEncoder.encode(bufferedWriter, message);
					t1 = new Thread(sendMessage);
					t1.start();
					t2 = new Thread(listener);
					t2.start();
					isConnected = true;
					btnNewButton.setEnabled(false);
					btnNewButton_1.setEnabled(true);
				} catch (UnknownHostException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					isConnected = false;
					btnNewButton.setEnabled(true);
					btnNewButton_1.setEnabled(false);
					textArea_1.append("Server unavialable\n");
				}
			}
		});
		btnNewButton.setBounds(29, 73, 248, 60);
		contentPane.add(btnNewButton);

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Message message = new Message("User 3", null, "REQUEST_TO_DISCONNECT");
				try {
					HttpEncoder.encode(bufferedWriter, message);
					t1.interrupt();
					t2.interrupt();
					isConnected = false;
					btnNewButton.setEnabled(true);
					btnNewButton_1.setEnabled(false);
				} catch (IOException e1) {
					isConnected = false;
					btnNewButton.setEnabled(true);
					btnNewButton_1.setEnabled(false);
					textArea_1.append("Server unavialable\n");
				}
			}
		});
		btnNewButton_1.setBounds(336, 73, 252, 60);
		contentPane.add(btnNewButton_1);

		textArea_1.setBackground(new Color(173, 216, 230));
		textArea_1.setEditable(false);
		textArea_1.setBounds(29, 149, 454, 197);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(29, 149, 559, 331);
		scrollPane.setViewportView(textArea_1);
		contentPane.add(scrollPane);

		JLabel lblUser = new JLabel("User 3");
		lblUser.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblUser.setBounds(265, 16, 79, 41);
		contentPane.add(lblUser);

		JLabel lblLocalTime = new JLabel("local time :");
		lblLocalTime.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLocalTime.setBounds(424, 23, 100, 34);
		contentPane.add(lblLocalTime);

		label = new JLabel();
		label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label.setBounds(522, 23, 45, 34);
		contentPane.add(label);

		JButton btnClose = new JButton("close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (frame.isConnected) {
					Message message = new Message("User 1", null, "REQUEST_TO_DISCONNECT");
					try {
						HttpEncoder.encode(frame.bufferedWriter, message);
						logicalTime = -1;
						t0.interrupt();
						t1.interrupt();
						t2.interrupt();
						Thread.currentThread().interrupt();
						frame.dispose();
						frame = null;
					} catch (IOException ex) {
					}
				} else {
					frame.dispose();
					frame = null;
				}
			}
		});
		btnClose.setBounds(265, 506, 115, 29);
		contentPane.add(btnClose);
	}

	/**
	 * Checks whether close button of the UI is clicked. If clicked and if it is
	 * connected to server it disconnects connection with server and closes.
	 */
	public static void closingListner() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if (frame.isConnected) {
					Message message = new Message("User 3", null, "REQUEST_TO_DISCONNECT");
					try {
						HttpEncoder.encode(frame.bufferedWriter, message);
						frame = null;
						logicalTime = -1;
						t0.interrupt();
						t1.interrupt();
						t2.interrupt();
						Thread.currentThread().interrupt();
					} catch (IOException e1) {
					}
				} else {
					frame = null;
				}
			}
		});
	}

	/**
	 * Checks whether client is about to crash. Before it crashes if it is connected
	 * to server it disconnects connection with server and closes.
	 */
	public static void crashClosingListner() {

		if (frame != null) {
			if (frame.isConnected) {
				Message message = new Message("User 3", null, "REQUEST_TO_DISCONNECT");
				try {
					HttpEncoder.encode(frame.bufferedWriter, message);
					logicalTime = -1;
					t0.interrupt();
					t1.interrupt();
					t2.interrupt();
					Thread.currentThread().interrupt();
				} catch (IOException e1) {
				}
				frame = null;
			} else {
				frame = null;
			}
		}
	}
}
