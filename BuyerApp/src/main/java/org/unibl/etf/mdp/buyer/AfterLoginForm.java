package org.unibl.etf.mdp.buyer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.unibl.etf.mdp.buyer.model.Product;
import org.unibl.etf.mdp.buyer.model.User;
import org.unibl.etf.mdp.properties.PropertiesService;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

public class AfterLoginForm extends JFrame {

	private JPanel contentPane;
	
	{
		URI_BASE=PropertiesService.getElement("PRODUCTS_REST");
	}
	
	private static String URI_BASE;
	
	/**
	 * Create the frame.
	 */
	public AfterLoginForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 452, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("CHOOSE AN OPTION");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(31, 10, 375, 42);
		contentPane.add(lblNewLabel);
		
		JButton checkProductsButton = new JButton("CHECK PRODUCTS");
		checkProductsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target(URI_BASE);
				Response response = target.request(MediaType.APPLICATION_JSON).get();				
				List<Product> products = response.readEntity(new GenericType<List<Product>>() {});			
				ProductsTableForm ptf = new ProductsTableForm();
				ptf.populateData(products);				
				response.close();
				client.close();
			}
		});
		checkProductsButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		checkProductsButton.setBounds(41, 71, 334, 58);
		contentPane.add(checkProductsButton);
		
		JButton createOrderButton = new JButton("CREATE AN ORDER");
		createOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target(URI_BASE);
				Response response = target.request(MediaType.APPLICATION_JSON).get();				
				List<Product> products = response.readEntity(new GenericType<List<Product>>() {});	
				OrderForm of = new OrderForm();
				of.setUser(user);
				of.populateData(products);
				//of.setVisible(true);
				response.close();
				client.close();
			}
		});
		createOrderButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		createOrderButton.setBounds(41, 166, 334, 58);
		contentPane.add(createOrderButton);
	}

	private User user;
	public void setUser(User u) {
		user = u;
	}
}
