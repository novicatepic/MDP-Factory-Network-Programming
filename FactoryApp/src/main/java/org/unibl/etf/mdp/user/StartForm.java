package org.unibl.etf.mdp.user;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.product.ProductService;

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
				try {
					ArrayList<User> existingUsers = readExistsingUsers();
					UsersForm uf = new UsersForm();
					uf.populateData(existingUsers);
				} catch(Exception ex) {
					ex.printStackTrace();
				}				
			}
		});
		usersButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		usersButton.setBounds(41, 71, 334, 58);
		contentPane.add(usersButton);
		
		JButton btnRegister = new JButton("PRODUCTS");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProductsForm pf = new ProductsForm();
				pf.populateData(ProductService.readProducts());
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
					ArrayList<User> requestUsers = new ArrayList<>();
					for(File file : files) {
						BufferedReader br = new BufferedReader(new FileReader(file));
						String line="", jsonObject="";
						while((line = br.readLine()) != null) 
							jsonObject += line;
						User user = gson.fromJson(jsonObject, User.class);
						requestUsers.add(user);
						br.close();
					}
					RequestsForm rf = new RequestsForm();
					rf.populateData(requestUsers);
					//rf.setVisible(true);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		requestsButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		requestsButton.setBounds(41, 160, 334, 58);
		contentPane.add(requestsButton);
		
		JButton promotionTextButton = new JButton("WRITE PROMOTION TEXT");
		promotionTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PromotionTextForm ptf = new PromotionTextForm();
				ptf.setVisible(true);
			}
		});
		promotionTextButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		promotionTextButton.setBounds(41, 345, 334, 58);
		contentPane.add(promotionTextButton);
	}
	
	public static ArrayList<User> readExistsingUsers() throws Exception {
		Gson gson = new Gson();
		File f = new File(USERS_PATH);
		f.createNewFile();
		ArrayList<User> users = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line="", content="";
		while((line = br.readLine()) != null) {
			content += line;
		}
		if(!"".equals(content)) {
			String[] split = content.split("}");
			for(int i=0; i<split.length; i++) {		
				split[i]+="}";
				users.add(gson.fromJson(split[i], User.class));
			}
		}		
		br.close();
		return users;
	}

}
