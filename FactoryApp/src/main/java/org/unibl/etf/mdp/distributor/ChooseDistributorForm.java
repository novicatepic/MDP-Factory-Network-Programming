package org.unibl.etf.mdp.distributor;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.unibl.etf.mdp.properties.PropertiesService;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
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
import javax.swing.JComboBox;

public class ChooseDistributorForm extends JFrame {

	private JPanel contentPane;	
	private JComboBox disBox = new JComboBox();
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(ChooseDistributorForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(ChooseDistributorForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	public ChooseDistributorForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 500, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("CHOOSE DISTRIBUTOR FORM");
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
					//Connect with a distributor (write a new file)
					String str = (String)disBox.getSelectedItem();
					StartDistributorForm.distributors.add(str);
					File f = new File(StartDistributorForm.FACTORY_DISTRIBUTORS_PATH+str);
					f.createNewFile();
				} catch(Exception ex) {
					Logger.getLogger(ChooseDistributorForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
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
