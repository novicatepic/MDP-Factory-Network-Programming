package org.unibl.etf.mdp.user;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.unibl.etf.mdp.model.User;
import org.unibl.etf.mdp.properties.PropertiesService;

import com.google.gson.Gson;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.awt.event.ActionEvent;

public class UsersForm extends JFrame {

	private JPanel contentPane;
	JFrame frame = new JFrame("Requests table");
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(UsersForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(UsersForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	public UsersForm() {
        JButton suspendButton = new JButton("SUSPEND/UNSUSPEND");
        suspendButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			File sourceFile = new File(StartForm.USERS_PATH);
                	int[] selectedRows = table.getSelectedRows();
                	Integer[] selRows = new Integer[selectedRows.length];
                	for(int i=0; i<selRows.length; i++) {
                		selRows[i]=selectedRows[i];
                	}
                	List<Integer> positions = (List<Integer>) Arrays.asList(selRows);
                	for(int i=0; i<users.size(); i++) {
                		if(positions.contains(i)) {
                			users.get(i).setSuspended(!users.get(i).isSuspended());
                		}
                	}
                	sourceFile.delete();
                	sourceFile.createNewFile();
                	Gson gson = new Gson();
                	PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(StartForm.USERS_PATH)), true);
                	for(User u : users) {
                		pw.println(gson.toJson(u));
                	}
                	
        			pw.close();
        		} catch(Exception ex) {
        			Logger.getLogger(UsersForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
        			ex.printStackTrace();
        		}       		
        	}
        });
        frame.getContentPane().setLayout(null);
        suspendButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        suspendButton.setBounds(69, 328, 373, 100);
        frame.getContentPane().add(suspendButton);
        
        JButton deleteButton = new JButton("DELETE");
        deleteButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			File sourceFile = new File(StartForm.USERS_PATH);
                	int[] selectedRows = table.getSelectedRows();
                	Integer[] selRows = new Integer[selectedRows.length];
                	for(int i=0; i<selRows.length; i++) {
                		selRows[i]=selectedRows[i];
                		model.removeRow(selectedRows[i]);
                	}
                	List<Integer> positions = (List<Integer>) Arrays.asList(selRows);
                	ArrayList<User> newUsers = new ArrayList<>();
                	for(int i=0; i<users.size(); i++) {
                		if(!positions.contains(i)) {
                			newUsers.add(users.get(i));
                		}
                	}
                	Gson gson = new Gson();
                	sourceFile.delete();
                	sourceFile.createNewFile();
                	PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(StartForm.USERS_PATH)), true);
                	for(User nu : newUsers) {
                		pw.println(gson.toJson(nu));
                	}
                	
        			pw.close();
        		} catch(Exception ex) {
        			Logger.getLogger(UsersForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
        			ex.printStackTrace();
        		}
        		
        	}
        });
        deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        deleteButton.setBounds(519, 328, 373, 100);
        frame.getContentPane().add(deleteButton);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setVisible(true);
	}
	JTable table;
	private ArrayList<User> users = new ArrayList<>();
	private DefaultTableModel model;
	public void populateData(ArrayList<User> users) {
		this.users = users;
		Object[] columnHeaders = {"Company", "Address", "Phone", "User name", "Password", "Suspended"};
		System.out.println("USERSSIZE="+users.size());
		Object[][] data = new Object[users.size()][6];
		for(int i=0; i<users.size();i++) {
			for(int j=0; j<6; j++) {
				if(j==0) {
					data[i][j]=users.get(i).getCompanyName();
				} else if(j==1) {
					data[i][j]=users.get(i).getAddress();
				} else if(j==2) {
					data[i][j]=users.get(i).getContactPhone();
				} else if(j==3) {
					data[i][j]=users.get(i).getUserName();
				} else if(j==4) {
					data[i][j]=users.get(i).getPassword();
				} else if(j==5) {
					if(users.get(i).isSuspended()) {
						data[i][j]=String.valueOf("YES");
					} else {
						System.out.println("NO");
						data[i][j]=String.valueOf("NO");
					}
					
				}
			}
		}
		model = new DefaultTableModel(data, columnHeaders);
		//System.out.println(users);
		table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 0, 976, 276);
        frame.getContentPane().add(scrollPane);
	}
}
