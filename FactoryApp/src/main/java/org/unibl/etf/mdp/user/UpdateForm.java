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
import org.unibl.etf.mdp.product.ProductService;
import org.unibl.etf.mdp.properties.PropertiesService;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UpdateForm extends JFrame {

	private JPanel contentPane;
	private JTextField amountField;
	private JTextField valueField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateForm frame = new CreateForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	{
		URI_BASE = PropertiesService.getElement("REGISTER_REST");
	}
	
	public static String URI_BASE;
	
	public UpdateForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 501, 325);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAddress = new JLabel("Amount");
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setBounds(47, 74, 118, 24);
		contentPane.add(lblAddress);
		
		amountField = new JTextField();
		amountField.setColumns(10);
		amountField.setBounds(207, 72, 167, 30);
		contentPane.add(amountField);
		
		JLabel lblContactPhone = new JLabel("Value");
		lblContactPhone.setHorizontalAlignment(SwingConstants.CENTER);
		lblContactPhone.setBounds(47, 144, 118, 24);
		contentPane.add(lblContactPhone);
		
		valueField = new JTextField();
		valueField.setColumns(10);
		valueField.setBounds(207, 142, 167, 30);
		contentPane.add(valueField);
		
		JLabel lblNewLabel_1 = new JLabel("UPDATE FORM");
		lblNewLabel_1.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 10, 413, 39);
		contentPane.add(lblNewLabel_1);
		JButton updateButton = new JButton("UPDATE");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Integer amount = Integer.valueOf(amountField.getText());
					Double value = Double.valueOf(valueField.getText());
					Product product2 = new Product(product.getName(), amount, value);
					ProductService.updateProduct(product2);
				} catch(Exception ex) {
					ex.printStackTrace();
				}				
			}
		});
		updateButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		updateButton.setBounds(72, 202, 311, 59);
		contentPane.add(updateButton);
	}
	
	private Product product;
	public void populateData(Product p) {
		this.product = p;
		amountField.setText(String.valueOf(p.getAmount()));
		valueField.setText(String.valueOf(p.getPrice()));
	}
	
}
