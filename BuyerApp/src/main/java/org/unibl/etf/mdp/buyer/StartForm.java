package org.unibl.etf.mdp.buyer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.properties.PropertiesService;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.awt.event.ActionEvent;


public class StartForm extends JFrame {

	private static int MULTICAST_PORT;
	private static String HOST;
	private JPanel contentPane;
	private static JLabel messageLabel = new JLabel("");
	
	{
		MULTICAST_PORT=Integer.valueOf(PropertiesService.getElement("MULTICAST_PORT"));
		HOST=PropertiesService.getElement("HOST");
		//System.out.println(HOST + " " + MULTICAST_PORT);
	}
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartForm frame = new StartForm();
					frame.setVisible(true);
					//MulticastSocket socket = null;					
					new Thread(new Runnable() {						
						@Override
						public void run() {
							try {
								MulticastSocket socket =  new MulticastSocket(MULTICAST_PORT);
								byte[] buffer = new byte[256];
								InetAddress address = InetAddress.getByName(HOST);
								socket.joinGroup(address);
								while(true) {
									DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
									socket.receive(packet);
									String received = new String(packet.getData(), 0, packet.getLength());
									System.out.println(received); 
									messageLabel.setText(received);	
								}									
							} catch(IOException ex) {
								ex.printStackTrace();
							}										
						}
					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 677, 484);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("WELCOME");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(31, 10, 375, 42);
		contentPane.add(lblNewLabel);
		
		JButton loginButton = new JButton("LOGIN");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginForm lf = new LoginForm();
				lf.setVisible(true);
			}
		});
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		loginButton.setBounds(41, 71, 334, 58);
		contentPane.add(loginButton);
		
		JButton btnRegister = new JButton("REGISTER");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterUserForm ruf = new RegisterUserForm();
				ruf.setVisible(true);
			}
		});
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnRegister.setBounds(41, 166, 334, 58);
		contentPane.add(btnRegister);
		
		
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setBounds(41, 263, 580, 156);
		contentPane.add(messageLabel);
	}

}
