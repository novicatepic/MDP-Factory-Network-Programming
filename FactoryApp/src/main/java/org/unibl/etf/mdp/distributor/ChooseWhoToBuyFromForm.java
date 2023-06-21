package org.unibl.etf.mdp.distributor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.model.User;
import org.unibl.etf.mdp.rmi.DistributorInterface;

import com.google.gson.Gson;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class ChooseWhoToBuyFromForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChooseWhoToBuyFromForm frame = new ChooseWhoToBuyFromForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	private JComboBox disBox = new JComboBox();
	public static final String PATH = "resources";
	public ChooseWhoToBuyFromForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 500, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("BUY FORM");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(73, 23, 316, 36);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Choose distributor");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(38, 108, 135, 42);
		contentPane.add(lblNewLabel_1);
		disBox.setBounds(228, 108, 203, 34);
		contentPane.add(disBox);
		JButton submitButton = new JButton("SUBMIT");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File f = new File(StartDistributorForm.DISTRIBUTORS_PATH);
					File[] files = f.listFiles();
					String selectedItem = (String)disBox.getSelectedItem();
					String fileName="";
					for(File fi : files) {
						if(fi.getName().equals(selectedItem+".txt")) {
							fileName=fi.getName();
							break;
						}
					}
					if(!"".equals(fileName)) {
						ListProductsForm lif = new ListProductsForm();
						System.setProperty("java.security.policy", PATH+File.separator+"client_policyfile.txt");
						if(System.getSecurityManager() == null) {
							System.setSecurityManager(new SecurityManager());
						}
						String name="Distributor";
						Registry registry = LocateRegistry.getRegistry(1099);
						DistributorInterface dif = (DistributorInterface) registry.lookup(name);
						//System.out.println(fileName);
						lif.populateData(dif.getDistributorProducts(StartDistributorForm.DISTRIBUTORS_PATH+fileName));
						//lif.populateData(dif.getDistributorProducts(fileName));
						lif.setFile(StartDistributorForm.DISTRIBUTORS_PATH+fileName);
						//lif.setVisible(true);
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}			
			}
		});
		submitButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		submitButton.setBounds(36, 207, 158, 50);
		contentPane.add(submitButton);
		
		
	}
	
	public void populateData(ArrayList<String> files) {
		for(String f : files) {
			disBox.addItem(f);
		}
	}
}
