package org.unibl.etf.mdp.distributor2;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.buyer.model.Product;
import org.unibl.etf.mdp.distributor.ChooseWhoToBuyFromForm;
import org.unibl.etf.mdp.model.Distributor;
import org.unibl.etf.mdp.product.ProductService;
import org.unibl.etf.mdp.properties.PropertiesService;

import com.google.gson.Gson;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

public class ProductForm extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField amountField;
	private JTextField priceField;
	public static final String PATH=".."+File.separator+"Distributors"+File.separator;
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(ProductForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(ProductForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	public ProductForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 501, 352);
		setResizable(false);
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
		
		JLabel lblContactPhone = new JLabel("Price");
		lblContactPhone.setHorizontalAlignment(SwingConstants.CENTER);
		lblContactPhone.setBounds(47, 169, 118, 24);
		contentPane.add(lblContactPhone);
		
		priceField = new JTextField();
		priceField.setColumns(10);
		priceField.setBounds(207, 163, 167, 30);
		contentPane.add(priceField);
		
		JLabel lblNewLabel_1 = new JLabel("PRODUCT FORM");
		lblNewLabel_1.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 10, 413, 39);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("ADD PRODUCT");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Let distributor create a new product
					//Check if options are correct
					String name = nameField.getText();
					Integer amount = Integer.valueOf(amountField.getText());
					Double price = Double.valueOf(priceField.getText());
					
					if(amount<0 || price<0) {
						throw new Exception("Amount or price lower than zero!");
					}
					
					if(ProductService.containsProduct(name)) {
						throw new Exception("Product already exists!");
					}
					
					if(name != null) {
						if(name.contains("/")) {
							throw new Exception("Invalid / sign in product name!");
						}
					}
					
					Product newProduct = new Product(name, amount, price);
					StartForm.distributor.addProduct(newProduct);
					Gson gson = new Gson();
					PrintWriter pw = new PrintWriter(new FileWriter(new File(PATH+StartForm.distributor.getName()+".txt"), true), true);
					pw.println(gson.toJson(newProduct));
					pw.close();			
					dispose();
				} catch(Exception ex) {
					dispose();
					Logger.getLogger(ProductForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
					ex.printStackTrace();
				}				
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton.setBounds(63, 235, 311, 59);
		contentPane.add(btnNewButton);
	}
}
