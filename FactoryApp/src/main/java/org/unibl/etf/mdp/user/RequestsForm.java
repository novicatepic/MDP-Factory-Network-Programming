package org.unibl.etf.mdp.user;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.awt.event.ActionEvent;

public class RequestsForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RequestsForm frame = new RequestsForm();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	JFrame frame = new JFrame("Requests table");
	public RequestsForm() {
        JButton rejectButt = new JButton("REJECT");
        rejectButt.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int[] selectedRows = table.getSelectedRows();
                if (selectedRows.length > 0) {
                	File sourceDir = new File(StartForm.REQUESTS_PATH);
                	File[] files = sourceDir.listFiles();
                	ArrayList<Integer> listFiles = new ArrayList<>();
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        model.removeRow(selectedRows[i]);
                        listFiles.add(i);
                        boolean status = files[selectedRows[i]].delete();
                    }
                }
        	}
        });
        frame.getContentPane().setLayout(null);
        rejectButt.setFont(new Font("Tahoma", Font.PLAIN, 20));
        rejectButt.setBounds(69, 328, 373, 100);
        frame.getContentPane().add(rejectButt);
        
        JButton acceptButt = new JButton("ACCEPT");
        acceptButt.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			Gson gson = new Gson();
            		ArrayList<User> existingUsers = StartForm.readExistsingUsers();
            		ArrayList<User> selectedUsers = new ArrayList<>();
            		File sourceDir = new File(StartForm.REQUESTS_PATH);
                	File[] files = sourceDir.listFiles();
            		int[] selectedRows = table.getSelectedRows();
            		for(int row:selectedRows) {
            			String company = (String)table.getValueAt(row, 0);
            			String address = (String)table.getValueAt(row, 1);
            			String phone = (String)table.getValueAt(row, 2);
            			String userName = (String)table.getValueAt(row, 3);
            			String password = (String)table.getValueAt(row, 4);
            			selectedUsers.add(new User(company, address, phone, userName, password));
            			boolean status = files[row].delete();
            		}
            		
            		for(User su : selectedUsers) {
            			boolean duplicate = false;
            			for(User eu : existingUsers) {
            				if(su.getAddress().equals(eu.getAddress()) || su.getUserName().equals(eu.getUserName())) {
            					duplicate = true;
            				}
            			}
            			if(duplicate == false) {  
            				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(StartForm.USERS_PATH, true)), true);
            				pw.println(gson.toJson(su));
            				pw.println();
            				pw.close();
            			}
            		}
        		} catch(Exception ex) {
        			ex.printStackTrace();
        		}       		
        	}
        });
        acceptButt.setFont(new Font("Tahoma", Font.PLAIN, 20));
        acceptButt.setBounds(519, 328, 373, 100);
        frame.getContentPane().add(acceptButt);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setVisible(true);
	}
	JTable table;
	private ArrayList<User> users = new ArrayList<>();
	private DefaultTableModel model;
	public void populateData(ArrayList<User> users) {
		this.users = users;
		Object[] columnHeaders = {"Company", "Address", "Phone", "User name", "Password"};
		System.out.println("USERSSIZE="+users.size());
		Object[][] data = new Object[users.size()][5];
		for(int i=0; i<users.size();i++) {
			for(int j=0; j<5; j++) {
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
				}
			}
		}
		model = new DefaultTableModel(data, columnHeaders);
		table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 0, 976, 276);
        frame.getContentPane().add(scrollPane);
	}
}
