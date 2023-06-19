package org.unibl.etf.mdp.user;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class StartForm extends JFrame {

	public static final String REQUESTS_PATH = ".." + File.separator + "RegistrationRequests" + File.separator;
	public static final String USERS_PATH = ".." + File.separator + "Users" + File.separator + "users.json";
	private static ArrayList<User> blogs = new ArrayList<>();
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartForm frame = new StartForm();
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
	public StartForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 456, 477);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("WELCOME TO FACTORY");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(31, 10, 375, 42);
		contentPane.add(lblNewLabel);
		
		JButton usersButton = new JButton("CHECK USERS");
		usersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		usersButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		usersButton.setBounds(41, 71, 334, 58);
		contentPane.add(usersButton);
		
		JButton btnRegister = new JButton("PRODUCTS");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnRegister.setBounds(41, 254, 334, 58);
		contentPane.add(btnRegister);
		
		JButton requestsButton = new JButton("CHECK REQUESTS");
		requestsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File sourceDir = new File(REQUESTS_PATH);
					File[] files = sourceDir.listFiles();
					Gson gson = new Gson();
					ArrayList<User> existingUsers = readExistsingUsers();
					ArrayList<User> requestUsers = new ArrayList<>();
					for(File file : files) {
						BufferedReader br = new BufferedReader(new FileReader(file));
						String line="", jsonObject="";
						while((line = br.readLine()) != null) 
							jsonObject += line;
						User user = gson.fromJson(jsonObject, User.class);
						//System.out.println(user);
						requestUsers.add(user);
						
						/*for(User usr : existingUsers) {
							if(usr.getUserName() == user.getUserName()) {
								br.close();
								throw new Exception("Same user name!");
							}
						}
						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(USERS_PATH)));
						pw.println(gson.toJson(user));
						System.out.println("UPISAO KORISNIKA U FAJL");*/
					}
					RequestsForm rf = new RequestsForm();
					rf.populateData(requestUsers);
					rf.setVisible(true);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		requestsButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		requestsButton.setBounds(41, 160, 334, 58);
		contentPane.add(requestsButton);
		
		JButton promotionTextButton = new JButton("WRITE PROMOTION TEXT");
		promotionTextButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		promotionTextButton.setBounds(41, 345, 334, 58);
		contentPane.add(promotionTextButton);
	}
	
	private ArrayList<User> readExistsingUsers() throws Exception {
		Gson gson = new Gson();
		File f = new File(USERS_PATH);
		f.createNewFile();
		ArrayList<User> users = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line="", jsonObject="";
		while((line = br.readLine()) != null) {
			jsonObject += line;
			users.add(gson.fromJson(jsonObject, User.class));
		}
		return users;
	}

}
