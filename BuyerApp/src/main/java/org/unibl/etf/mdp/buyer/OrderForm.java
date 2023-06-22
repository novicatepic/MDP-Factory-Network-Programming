package org.unibl.etf.mdp.buyer;
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

import org.unibl.etf.mdp.buyer.model.Product;
import org.unibl.etf.mdp.buyer.model.User;
import org.unibl.etf.mdp.properties.PropertiesService;

import com.google.gson.Gson;

import javax.el.ArrayELResolver;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class OrderForm extends JFrame {

	private JPanel contentPane;
	
	JFrame frame = new JFrame("Requests table");
	public OrderForm() {
        frame.getContentPane().setLayout(null);
        
        JButton createOrderButton = new JButton("CREATE ORDER");
        createOrderButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//Get which product(s) client selected and pass them to another form
        		//So that the user can select amount for each product
        		int[] selectedItems = table.getSelectedRows();
        		Integer[] selectedItemsInteger = new Integer[selectedItems.length];
        		for(int i=0; i<selectedItems.length; i++) {
        			selectedItemsInteger[i] = selectedItems[i];
        		}
        		List<Integer> selectedItemsList = Arrays.asList(selectedItemsInteger);
        		ArrayList<Product> productsToPass = new ArrayList<>();
        		for(int i=0; i<products.size(); i++) {
        			if(selectedItemsList.contains(i)) {
        				productsToPass.add(products.get(i));
        			}
        		}
        		CreateOrderForm cof = new CreateOrderForm();
        		cof.populateData(productsToPass);
        		cof.setUser(user);
        		cof.setVisible(true);
        	}
        });
        createOrderButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        createOrderButton.setBounds(519, 328, 373, 100);
        frame.getContentPane().add(createOrderButton);
        
        JLabel lblNewLabel = new JLabel("SELECT PRODUCT(S) IN TABLE");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(49, 328, 272, 100);
        frame.getContentPane().add(lblNewLabel);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setVisible(true);
	}
	JTable table;
	private List<Product> products = new ArrayList<>();
	private DefaultTableModel model;
	//Populate table with products, easier to work with JTable
	public void populateData(List<Product> ps) {
		this.products = ps;
		Object[] columnHeaders = {"Name", "Amount", "Price"};
		//System.out.println("USERSSIZE="+products.size());
		Object[][] data = new Object[products.size()][3];
		for(int i=0; i<products.size();i++) {
			for(int j=0; j<3; j++) {
				if(j==0) {
					data[i][j]=products.get(i).getName();
				} else if(j==1) {
					data[i][j]=products.get(i).getAmount();
				} else if(j==2) {
					data[i][j]=products.get(i).getPrice();
				} 
			}
		}
		model = new DefaultTableModel(data, columnHeaders);
		table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 0, 976, 276);
        frame.getContentPane().add(scrollPane);
	}
	
	private User user;
	public void setUser(User u) {
		user = u;
	}
}
