package org.unibl.etf.mdp.operator;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.model.Operator;
import org.unibl.etf.mdp.user.User;

import com.google.gson.Gson;

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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class OrderStartForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
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
	
	private static final String HOST="127.0.0.1";
	private static final int PORT = 8443;
	private static final String KEY_STORE_PATH = "."+File.separator+"keystore.jks";
	private static final String KEY_STORE_PASSWORD = "sigurnost";
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
					//ArrayList<Operator> operators = readExistsingOperators();
					System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
					System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
					SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
					SSLSocket s = (SSLSocket) sf.createSocket(HOST, PORT);
					BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
					System.out.println("UN="+userNameField.getText());
					out.println(userNameField.getText());
					String response = in.readLine();
					if("OK".equals(response)) {
						OrderTakeAndGo otg = new OrderTakeAndGo();
						otg.setVisible(true);
					} else {
						throw new Exception("Invalid operator credentials!");
					}
					in.close();
					out.close();
					s.close();
					/*for(Operator o : operators) {
						System.out.println(o.getUserName());
					}*/
				} catch(Exception ex) {
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
