package org.unibl.etf.mdp.user;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
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
		/*table = new JTable(3, 3);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 0, 976, 276);
        frame.getContentPane().add(scrollPane);*/
        JButton btnNewButton = new JButton("EXECUTE");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.out.println(table.getSelectedRow());
        	}
        });
        frame.getContentPane().setLayout(null);
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnNewButton.setBounds(27, 299, 881, 139);
        frame.getContentPane().add(btnNewButton);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setVisible(true);
	}
	JTable table;
	public void populateData(ArrayList<User> users) {
		String[] columnHeaders = {"Company", "Address", "Phone", "User name", "Password"};
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
		//System.out.println(users);
		table = new JTable(data, columnHeaders);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 0, 976, 276);
        frame.getContentPane().add(scrollPane);
	}

}
