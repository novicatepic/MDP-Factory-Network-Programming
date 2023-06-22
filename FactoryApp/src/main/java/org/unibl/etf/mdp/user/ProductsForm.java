package org.unibl.etf.mdp.user;
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

import org.unibl.etf.mdp.buyer.model.Product;
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

public class ProductsForm extends JFrame {

	private JPanel contentPane;
	JFrame frame = new JFrame("Requests table");
	
	public ProductsForm() {
        JButton createProduct = new JButton("CREATE");
        createProduct.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CreateForm cf = new CreateForm();
        		cf.setVisible(true);
        		frame.dispose();
        	}
        });
        frame.getContentPane().setLayout(null);
        createProduct.setFont(new Font("Tahoma", Font.PLAIN, 20));
        createProduct.setBounds(69, 328, 112, 100);
        frame.getContentPane().add(createProduct);
        
        JButton updateButton = new JButton("UPDATE");
        updateButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int selectedNum = table.getSelectedRow();
        		Product p = products.get(selectedNum);
        		UpdateForm uf = new UpdateForm();
        		uf.populateData(p);
        		uf.setVisible(true);
        		frame.dispose();
        	}
        });
        updateButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        updateButton.setBounds(228, 328, 112, 100);
        frame.getContentPane().add(updateButton);
        
        JButton deleteButton = new JButton("DELETE");
        deleteButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//Delete a product and immediately update table
        		//Also, update Redis!
        		int selectedRow = table.getSelectedRow();
        		model.removeRow(selectedRow);
        		String name="";
        		for(int i=0; i<products.size(); i++) {
        			if(selectedRow == i) {
        				name = products.get(i).getName();
        				products.remove(i);
        			}
        		}
        		if(!"".equals(name)) {
        			ProductService.deleteProduct(name);
        		}       		
        	}
        });
        deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        deleteButton.setBounds(386, 328, 112, 100);
        frame.getContentPane().add(deleteButton);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setVisible(true);
	}
	JTable table;
	private List<Product> products = new ArrayList<>();
	private DefaultTableModel model;
	
	//Populate table with products
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
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 0, 976, 276);
        frame.getContentPane().add(scrollPane);
	}
}
