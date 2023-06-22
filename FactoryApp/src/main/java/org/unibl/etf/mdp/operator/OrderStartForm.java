package org.unibl.etf.mdp.operator;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.properties.PropertiesService;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class OrderStartForm extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrderStartForm frame = new OrderStartForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(OrderStartForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(OrderStartForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	//Read required parameters
	{
		HOST=PropertiesService.getElement("LOCAL_HOST");
		PORT=Integer.valueOf(PropertiesService.getElement("PORT_8443"));
		KEY_STORE_PASSWORD=PropertiesService.getElement("KEYSTORE_PASSWORD");
		LOGIN_MESSAGE=PropertiesService.getElement("PROTOCOL_LOGIN_MESSAGE");
		OK_MESSAGE=PropertiesService.getElement("PROTOCOL_OK_MESSAGE");
	}
	
	private static String LOGIN_MESSAGE;
	private static String OK_MESSAGE;
	private static String HOST;
	private static int PORT;
	private static final String KEY_STORE_PATH = "."+File.separator+"keystore.jks";
	private static String KEY_STORE_PASSWORD;
	private JTextField userNameField;
	
	public OrderStartForm() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 452, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("LOGIN");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(31, 10, 375, 42);
		contentPane.add(lblNewLabel);
		
		JButton loginButton = new JButton("LOGIN");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Logging in requires secure communication, which is established 
					//A protocol is being followed
					System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
					System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
					SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
					SSLSocket s = (SSLSocket) sf.createSocket(HOST, PORT);
					BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
					out.println(LOGIN_MESSAGE);
					out.println(userNameField.getText());
					String response = in.readLine();
					String operator = in.readLine();
					if(OK_MESSAGE.equals(response)) {
						OrderTakeAndGo otg = new OrderTakeAndGo();
						otg.setOperator(operator);
						otg.setVisible(true);
					} else {
						throw new Exception("Invalid operator credentials!");
					}
					in.close();
					out.close();
					s.close();
				} catch(Exception ex) {
					Logger.getLogger(OrderStartForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
					ex.printStackTrace();
				}
				
			}
		});
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		loginButton.setBounds(44, 180, 334, 58);
		contentPane.add(loginButton);
		
		JLabel lblNewLabel_1 = new JLabel("User name");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(31, 62, 103, 61);
		contentPane.add(lblNewLabel_1);
		
		userNameField = new JTextField();
		userNameField.setBounds(181, 73, 211, 50);
		contentPane.add(userNameField);
		userNameField.setColumns(10);
	}

}
