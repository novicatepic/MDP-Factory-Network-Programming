package org.unibl.etf.mdp.user;

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

import org.unibl.etf.mdp.buyer.model.Product;
import org.unibl.etf.mdp.distributor.ChooseWhoToBuyFromForm;
import org.unibl.etf.mdp.product.ProductService;
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

public class CreateForm extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField amountField;
	private JTextField valueField;

	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(CreateForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(CreateForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}

	{
		URI_BASE = PropertiesService.getElement("REGISTER_REST");
	}
	
	private static String URI_BASE;
	
	public CreateForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 501, 345);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nameField = new JTextField();
		nameField.setBounds(207, 59, 167, 30);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Product name");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(47, 61, 118, 24);
		contentPane.add(lblNewLabel);
		
		JLabel lblAddress = new JLabel("Amount");
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setBounds(47, 111, 118, 24);
		contentPane.add(lblAddress);
		
		amountField = new JTextField();
		amountField.setColumns(10);
		amountField.setBounds(207, 109, 167, 30);
		contentPane.add(amountField);
		
		JLabel lblContactPhone = new JLabel("Value");
		lblContactPhone.setHorizontalAlignment(SwingConstants.CENTER);
		lblContactPhone.setBounds(47, 169, 118, 24);
		contentPane.add(lblContactPhone);
		
		valueField = new JTextField();
		valueField.setColumns(10);
		valueField.setBounds(207, 163, 167, 30);
		contentPane.add(valueField);
		
		JLabel lblNewLabel_1 = new JLabel("PRODUCT FORM");
		lblNewLabel_1.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 10, 413, 39);
		contentPane.add(lblNewLabel_1);
		
		JButton createProductButton = new JButton("CREATE");
		createProductButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String prName = nameField.getText();
					Integer amount = Integer.valueOf(amountField.getText());
					Double value = Double.valueOf(valueField.getText());
					Product product = new Product(prName, amount, value);
					if(ProductService.containsProduct(prName)) {
						throw new Exception("Couldn't add product, already exists!");
					}
					ProductService.addProduct(product);
				} catch(Exception ex) {
					Logger.getLogger(CreateForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
					ex.printStackTrace();
				}				
			}
		});
		createProductButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		createProductButton.setBounds(73, 226, 311, 59);
		contentPane.add(createProductButton);
	}
}
