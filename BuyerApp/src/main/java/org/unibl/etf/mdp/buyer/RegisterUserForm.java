package org.unibl.etf.mdp.buyer;

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

import org.unibl.etf.mdp.buyer.model.User;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterUserForm extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField addressField;
	private JTextField phoneField;
	private JTextField userNameField;
	private JTextField passwordField;
	private JTextField passwordAgainField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterUserForm frame = new RegisterUserForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public static final String URI_BASE = "http://localhost:8080/BuyerProject/api/register/";
	
	public RegisterUserForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 501, 489);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nameField = new JTextField();
		nameField.setBounds(207, 59, 167, 30);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Company name");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(47, 61, 118, 24);
		contentPane.add(lblNewLabel);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setBounds(47, 111, 118, 24);
		contentPane.add(lblAddress);
		
		addressField = new JTextField();
		addressField.setColumns(10);
		addressField.setBounds(207, 109, 167, 30);
		contentPane.add(addressField);
		
		JLabel lblContactPhone = new JLabel("Contact phone");
		lblContactPhone.setHorizontalAlignment(SwingConstants.CENTER);
		lblContactPhone.setBounds(47, 169, 118, 24);
		contentPane.add(lblContactPhone);
		
		phoneField = new JTextField();
		phoneField.setColumns(10);
		phoneField.setBounds(207, 163, 167, 30);
		contentPane.add(phoneField);
		
		JLabel lblUserName = new JLabel("User name");
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(47, 222, 118, 24);
		contentPane.add(lblUserName);
		
		userNameField = new JTextField();
		userNameField.setColumns(10);
		userNameField.setBounds(207, 220, 167, 30);
		contentPane.add(userNameField);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(47, 274, 118, 24);
		contentPane.add(lblPassword);
		
		JLabel lblPasswordAgain = new JLabel("Password again");
		lblPasswordAgain.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasswordAgain.setBounds(47, 324, 118, 24);
		contentPane.add(lblPasswordAgain);
		
		passwordField = new JTextField();
		passwordField.setColumns(10);
		passwordField.setBounds(207, 272, 167, 30);
		contentPane.add(passwordField);
		
		passwordAgainField = new JTextField();
		passwordAgainField.setColumns(10);
		passwordAgainField.setBounds(207, 318, 167, 30);
		contentPane.add(passwordAgainField);
		
		JLabel lblNewLabel_1 = new JLabel("REGISTER FORM");
		lblNewLabel_1.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 10, 413, 39);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("REGISTER");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String companyName = nameField.getText();
					String address = addressField.getText();
					String phone = phoneField.getText();
					String pwField = passwordField.getText();
					String pwField2 = passwordAgainField.getText();
					String userName = userNameField.getText();
					if(!pwField.equals(pwField2)) {
						throw new Exception("Passwords don't match!");
					}
					User user = new User(companyName, address, phone, userName, pwField);
					Client client = ClientBuilder.newClient();
					WebTarget target = client.target(URI_BASE);
					Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(user, MediaType.APPLICATION_JSON));
				} catch(Exception ex) {
					ex.printStackTrace();
				}				
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton.setBounds(68, 383, 311, 59);
		contentPane.add(btnNewButton);
	}
}
