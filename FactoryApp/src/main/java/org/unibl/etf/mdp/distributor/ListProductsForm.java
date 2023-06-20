package org.unibl.etf.mdp.distributor;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.unibl.etf.mdp.product.Product;
import org.unibl.etf.mdp.product.ProductService;

import com.google.gson.Gson;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class ListProductsForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListProductsForm frame = new ListProductsForm();
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
            				products.get(i).setAmount(products.get(i).getAmount()-enteredAmount);
            				ProductService.addProduct(products.get(i));
            			}
            		}
            		File f = new File(file);
            		f.delete();
            		Gson gson = new Gson();
            		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            		for(Product p : products) {
            			pw.println(gson.toJson(p));
            		}
            		pw.close();
        		} catch(Exception ex) {
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
