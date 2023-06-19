package org.unibl.etf.mdp.buyer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JButton;

public class ProductsTableForm extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProductsTableForm frame = new ProductsTableForm();
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
	public ProductsTableForm() {
		JFrame frmProductsTable = new JFrame("2x2 JTable");
		frmProductsTable.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmProductsTable.setTitle("Products table");
		
        Object[][] data = {
            {"Row 1, Col 1", "Row 1, Col 2"},
            {"Row 2, Col 1", "Row 2, Col 2"}
        };

        String[] columnHeaders = {"Column 1", "Column 2"};
        frmProductsTable.getContentPane().setLayout(null);

        JTable table = new JTable(data, columnHeaders);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0, 986, 290);

        frmProductsTable.getContentPane().add(scrollPane);
        frmProductsTable.setSize(1000, 500);
        frmProductsTable.setResizable(false);
        frmProductsTable.setVisible(true);
		
	}
}
