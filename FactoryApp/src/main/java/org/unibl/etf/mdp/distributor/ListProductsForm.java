package org.unibl.etf.mdp.distributor;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.unibl.etf.mdp.buyer.model.Product;
import org.unibl.etf.mdp.product.ProductService;
import org.unibl.etf.mdp.properties.PropertiesService;
import org.unibl.etf.mdp.rmi.DistributorInterface;

import com.google.gson.Gson;

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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class ListProductsForm extends JFrame {

	private JPanel contentPane;

	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(ListProductsForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(ListProductsForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	private String file;
	public void setFile(String f) {
		file = f;
	}
	
	JFrame frame = new JFrame("Requests table");
	public ListProductsForm() {
        frame.getContentPane().setLayout(null);
        
        JButton buyButton = new JButton("ACCEPT");
        buyButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			int enteredAmount = Integer.parseInt(amountField.getText());
            		int selectedItem = table.getSelectedRow();
            		for(int i=0; i<products.size(); i++) {
            			if(selectedItem==i) {
            				if(products.get(i).getAmount() < enteredAmount) {
            					throw new Exception("Amount must be lower than real amount!");
            				}
            				//System.out.println(products.get(i).getName());
            				boolean contains = ProductService.products.contains(products.get(i).getName());
            				if(contains) {
            					System.out.println("CONTAINS");
            					Product temp = null;
            					ArrayList<Product> prods = ProductService.readProducts();
            					for(Product p : prods) {
            						if(p.getName().equals(products.get(i).getName())) {
            							temp = p;
            						}
            					}
            					if(temp != null) {
            						temp.setAmount(temp.getAmount()+enteredAmount);
            						System.out.println(temp.getName());
            						System.out.println(temp.getAmount());
            						ProductService.updateProduct(temp);
            					}
            				} else {
            					System.out.println("DOESN'T CONTAIN");
            					Product temp = new Product(products.get(i).getName(), enteredAmount, products.get(i).getPrice());
            					ProductService.addProduct(temp);
            				}   
            				products.get(i).setAmount(products.get(i).getAmount()-enteredAmount); 
            			}
            		}
            		String name="Distributor";
					Registry registry = LocateRegistry.getRegistry(1099);
					DistributorInterface dif = (DistributorInterface) registry.lookup(name);
					dif.writeUpdatedProducts(products);
        		} catch(Exception ex) {
        			Logger.getLogger(ChooseWhoToBuyFromForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
        			ex.printStackTrace();
        		}       		
        	}
        });
        buyButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        buyButton.setBounds(519, 328, 373, 100);
        frame.getContentPane().add(buyButton);
        
        JLabel lblNewLabel = new JLabel("ENTER AMOUNT");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(31, 338, 173, 90);
        frame.getContentPane().add(lblNewLabel);
        
        amountField = new JTextField();
        amountField.setBounds(214, 356, 182, 57);
        frame.getContentPane().add(amountField);
        amountField.setColumns(10);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setVisible(true);
	}
	JTable table;
	private List<Product> products = new ArrayList<>();
	private DefaultTableModel model;
	private JTextField amountField;
	public void populateData(List<Product> ps) {
		this.products = ps;
		Object[] columnHeaders = {"Name", "Amount", "Price"};
		System.out.println("USERSSIZE="+products.size());
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
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 0, 976, 276);
        frame.getContentPane().add(scrollPane);
	}
}
