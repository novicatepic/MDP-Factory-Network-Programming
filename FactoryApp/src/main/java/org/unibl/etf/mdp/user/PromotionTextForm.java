package org.unibl.etf.mdp.user;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.unibl.etf.mdp.properties.PropertiesService;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

public class PromotionTextForm extends JFrame {
	
	{
		MULTICAST_PORT = Integer.valueOf(PropertiesService.getElement("MULTICAST_PORT")); 
		MULTICAST_HOST = PropertiesService.getElement("HOST"); 
	}
	
	private static int MULTICAST_PORT;
	private static String MULTICAST_HOST;
	private JPanel contentPane;
	private JTextField messageField;
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(PromotionTextForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(PromotionTextForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	public PromotionTextForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 501, 489);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		messageField = new JTextField();
		messageField.setBounds(31, 113, 386, 248);
		contentPane.add(messageField);
		messageField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Input text");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(47, 61, 355, 42);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("TEXT FORM");
		lblNewLabel_1.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 10, 413, 39);
		contentPane.add(lblNewLabel_1);
		
		JButton sendTextButton = new JButton("SEND TEXT");
		sendTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MulticastSocket socket = new MulticastSocket();
					InetAddress address = InetAddress.getByName(MULTICAST_HOST);
					String message = messageField.getText();
					byte[] buf = new byte[message.getBytes().length];
					buf = message.getBytes();
					DatagramPacket packet = new DatagramPacket(buf, buf.length, address, MULTICAST_PORT);
					socket.send(packet);
					socket.close();
				} catch(Exception ex) {
					Logger.getLogger(PromotionTextForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
					ex.printStackTrace();
				}				
			}
		});
		sendTextButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		sendTextButton.setBounds(68, 383, 311, 59);
		contentPane.add(sendTextButton);
	}
}
