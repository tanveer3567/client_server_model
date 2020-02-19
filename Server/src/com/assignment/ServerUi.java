/** NAME: TANVEER AHMED SHAIK
    STUDENT ID: 1001704423
**/

package com.assignment;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

/**
 * This is a server UI Jframe.
 */
public class ServerUi extends JFrame {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public String message = "server started\n";
	private JTextArea textArea;
	private JTextArea textArea_1;
	private Scanner scan = null;
	private Thread t1 = null;
	private Thread t2 = null;
	private ServerSocket server = null;
	private MessageServer messageServer = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerUi frame = new ServerUi();
					frame.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * create a new server socket at 8080
	 */
	public Runnable serverThread = () -> {
		try {
			server = new ServerSocket(8080);
			messageServer = new MessageServer(server);
			messageServer.listen(textArea, textArea_1);
		} catch (IOException ex) {
		}
	};

	/**
	 * The will write messages to the UI of the server.
	 */
	public Runnable serverMessagesThread = () -> {
		while (true) {
			textArea.setText(scan.nextLine());
		}
	};

	/**
	 * Create the frame for server ui.
	 */
	public ServerUi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 996, 639);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// This button will start the server at port 8080
		JButton btnStartServer = new JButton("start server");
		// This button will stop the server at port 8080
		JButton btnStopServer = new JButton("stop server");
		btnStartServer.setBounds(62, 508, 200, 29);
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t1 = new Thread(serverThread);
				t1.start();
				textArea.append(message);
				scan = new Scanner(System.in);
				t2 = new Thread(serverMessagesThread);
				t2.start();
				btnStartServer.setEnabled(false);
				btnStopServer.setEnabled(true);
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnStartServer);

		btnStopServer.setEnabled(false);
		btnStopServer.setBounds(350, 508, 200, 29);
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					server.close();
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				textArea.append("server stopped\n");
				btnStartServer.setEnabled(true);
				btnStopServer.setEnabled(false);
			}
		});
		contentPane.add(btnStopServer);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(45, 40, 580, 443);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("Arial Black", Font.PLAIN, 16));
		textArea.setBounds(39, 42, 577, 427);
		textArea.setEditable(false);

		textArea_1 = new JTextArea();
		textArea_1.setBounds(640, 40, 283, 443);
		textArea_1.setFont(new Font("Arial Black", Font.PLAIN, 16));
		textArea_1.setBounds(39, 42, 577, 427);
		textArea_1.setLineWrap(true);
		textArea_1.setWrapStyleWord(true);
		textArea_1.setEditable(false);
		contentPane.add(textArea_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportView(textArea_1);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(640, 40, 283, 443);
		contentPane.add(scrollPane_1);

	}
}
