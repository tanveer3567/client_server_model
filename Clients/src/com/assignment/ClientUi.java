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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * This class is responsible for creating the ui of the client creator UI of the
 * application
 * 
 * @author Tanveer
 *
 */
public class ClientUi extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ClientUi frame;
	private JPanel contentPane;
	private static Thread t1 = null;
	private static Thread t2 = null;
	private static Thread t3 = null;
	private static JButton btnCreate;
	private static JButton button;
	private static JButton button_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ClientUi();
					frame.setVisible(true);
					closingListner();
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientUi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 587, 401);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnCreate = new JButton("create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t1 = new Thread(Client1.r);
				t1.start();
			}
		});
		btnCreate.setBounds(211, 81, 115, 29);
		contentPane.add(btnCreate);

		JLabel lblClientCreator = new JLabel("Client creator");
		lblClientCreator.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblClientCreator.setBounds(152, 16, 236, 32);
		contentPane.add(lblClientCreator);

		JLabel lblClickOnCreate = new JLabel(
				"Click on create to create a new client. Maximum of 3 clients can be created at any point of time.");
		lblClickOnCreate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblClickOnCreate.setBounds(15, 260, 535, 41);
		contentPane.add(lblClickOnCreate);

		JLabel lblUserClient = new JLabel("User 1 client :");
		lblUserClient.setBounds(57, 85, 139, 20);
		contentPane.add(lblUserClient);

		JLabel lblUserClient_1 = new JLabel("User 2 client :");
		lblUserClient_1.setBounds(57, 138, 139, 20);
		contentPane.add(lblUserClient_1);

		JLabel lblUserClient_2 = new JLabel("User 3 client :");
		lblUserClient_2.setBounds(57, 193, 139, 20);
		contentPane.add(lblUserClient_2);

		button = new JButton("create");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t2 = new Thread(Client2.r);
				t2.start();
			}
		});
		button.setBounds(211, 134, 115, 29);
		contentPane.add(button);

		button_1 = new JButton("create");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t3 = new Thread(Client3.r);
				t3.start();
			}
		});
		button_1.setBounds(211, 189, 115, 29);
		contentPane.add(button_1);

		JLabel lblNoteAClient = new JLabel(
				"Note: A client once created cannot be recreated until existing client is destroyed.");
		lblNoteAClient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNoteAClient.setBounds(15, 300, 535, 29);
		contentPane.add(lblNoteAClient);

		JButton btnNewButton = new JButton("SHUTDOWN");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (t1 != null) {
					Client1.crashClosingListner();
				}
				if (t2 != null) {
					Client2.crashClosingListner();
				}
				if (t3 != null) {
					Client3.crashClosingListner();
				}
				System.exit(0);
			}
		});
		btnNewButton.setForeground(Color.RED);
		btnNewButton.setBounds(392, 121, 139, 55);
		contentPane.add(btnNewButton);
	}

	/**
	 * Checks whether client creator is about to crash. Before it crashes it sends
	 * commands to clients to disconnect with server if connected before client
	 * creator crashes.
	 */
	public static void closingListner() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if (t1 != null) {
					Client1.crashClosingListner();
				}
				if (t2 != null) {
					Client2.crashClosingListner();
				}
				if (t3 != null) {
					Client3.crashClosingListner();
				}
			}
		});
	}
}
