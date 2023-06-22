package org.unibl.etf.mdp.distributor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.properties.PropertiesService;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

public class StartDistributorForm extends JFrame {

	private JPanel contentPane;

	//Didn't put these into config file because of File.separator, this seems more precise
	public static final String RESOURCE_PATH="resources";
	public static final String DISTRIBUTORS_PATH=".."+File.separator+"Distributors"+File.separator;
	public static final String FACTORY_DISTRIBUTORS_PATH=".."+File.separator+"FactoryDistributors"+File.separator;
	public static final ArrayList<String> distributors = new ArrayList<>();
	
	//Add distributors that Factory connected with
	//Factory has it's own folder in which files with the names of Distributors are containted
	//Every time application starts, these files are loaded
	//The idea is that factory doesn't know anything about distributors
	public void populateDistributors() {
		try {
			File f = new File(FACTORY_DISTRIBUTORS_PATH);
			File[] files = f.listFiles();
			for(File fi : files) {
				if(fi!=null && !distributors.contains(fi.getName()))
					distributors.add(fi.getName());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(StartDistributorForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(StartDistributorForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	public StartDistributorForm() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 452, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("WELCOME");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(31, 10, 375, 42);
		contentPane.add(lblNewLabel);
		
		JButton connectionButton = new JButton("CONNECT WITH DISTRIBUTOR");
		connectionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					populateDistributors();
					ChooseDistributorForm cdf = new ChooseDistributorForm();
					ArrayList<String> trimmedFiles = new ArrayList<>();
					//I used distributors path, but should use factory distributors path, old implementation 
					/*File f1 = new File(DISTRIBUTORS_PATH);
					File[] files = f1.listFiles();
					for(File f : files) {
						System.out.println(f.getName());
						if(!distributors.contains(f.getName().substring(0, f.getName().length()-4))) {
							
							trimmedFiles.add(f.getName().substring(0, f.getName().length()-4));
						}						
					}*/
					
					//In this implementation, Factory doesn't know anything about distributors
					File f2 = new File(FACTORY_DISTRIBUTORS_PATH);
					File[] files2 = f2.listFiles();
					for(File f : files2) {
						//System.out.println(f.getName());
						if(!distributors.contains(f.getName())) {
							trimmedFiles.add(f.getName());
						}						
					}
					cdf.populateData(trimmedFiles);
					cdf.setVisible(true);
				} catch(Exception ex) {
					Logger.getLogger(StartDistributorForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
					ex.printStackTrace();
				}
				
			}
		});
		connectionButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		connectionButton.setBounds(41, 71, 334, 58);
		contentPane.add(connectionButton);
		
		JButton productsButton = new JButton("CHECK PRODUCTS");
		productsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Show form for checking distributors product
					populateDistributors();
					ChooseWhoToBuyFromForm c = new ChooseWhoToBuyFromForm();
					c.populateData(distributors);
					c.setVisible(true);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		productsButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		productsButton.setBounds(41, 166, 334, 58);
		contentPane.add(productsButton);
	}

}
