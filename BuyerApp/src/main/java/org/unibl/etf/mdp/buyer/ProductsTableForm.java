package org.unibl.etf.mdp.buyer;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.unibl.etf.mdp.buyer.model.Product;

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

public class ProductsTableForm extends JFrame {

	private JPanel contentPane;

	JFrame frame = new JFrame("Requests table");
	public ProductsTableForm() {
        frame.getContentPane().setLayout(null);
        frame.setSize(1000, 409);
        frame.setResizable(false);
        frame.setVisible(true);
	}
	JTable table;
	private List<Product> products = new ArrayList<>();
	private DefaultTableModel model;
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
}
