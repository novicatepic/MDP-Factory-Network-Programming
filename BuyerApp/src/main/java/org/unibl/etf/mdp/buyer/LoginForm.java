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
import org.unibl.etf.mdp.properties.PropertiesService;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {

	private JPanel contentPane;
	private JTextField userNameField;
	private JTextField passwordField;

	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(LoginForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(LoginForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	{
		URI_BASE=PropertiesService.getElement("LOGIN_REST");
	}
	
	private static String URI_BASE;
	
	public LoginForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 501, 305);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		userNameField = new JTextField();
		userNameField.setBounds(207, 59, 167, 30);
		contentPane.add(userNameField);
		userNameField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("User Name");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(47, 61, 118, 24);
		contentPane.add(lblNewLabel);
		
		JLabel lblAddress = new JLabel("Password");
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setBounds(47, 111, 118, 24);
		contentPane.add(lblAddress);
		
		passwordField = new JTextField();
		passwordField.setColumns(10);
		passwordField.setBounds(207, 109, 167, 30);
		contentPane.add(passwordField);
		
		JLabel lblNewLabel_1 = new JLabel("LOGIN FORM");
		lblNewLabel_1.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 10, 413, 39);
		contentPane.add(lblNewLabel_1);
		
		JButton loginButton = new JButton("LOGIN");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String userName = userNameField.getText();
					String password = passwordField.getText();
					User user = new User(userName, password);
					Client client = ClientBuilder.newClient();
					WebTarget target = client.target(URI_BASE);
					Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(user, MediaType.APPLICATION_JSON));
					if(response.getStatus()==201) {
						AfterLoginForm alf = new AfterLoginForm();
						User user2 = response.readEntity(User.class);
						System.out.println("USR2="+user2.getAddress());
						alf.setUser(user2);
						alf.setVisible(true);
					} else {
						throw new Exception("Non-existing account or suspended!");
					}
					response.close();
					client.close();
				} catch(Exception ex) {
					Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
					ex.printStackTrace();
				}				
			}
		});
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		loginButton.setBounds(63, 175, 311, 59);
		contentPane.add(loginButton);
	}
}
