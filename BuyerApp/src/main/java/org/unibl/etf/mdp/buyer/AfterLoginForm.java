package org.unibl.etf.mdp.buyer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AfterLoginForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AfterLoginForm frame = new AfterLoginForm();
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
	public AfterLoginForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				
			}
		});
		checkProductsButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		checkProductsButton.setBounds(41, 71, 334, 58);
		contentPane.add(checkProductsButton);
		
		JButton createOrderButton = new JButton("CREATE AN ORDER");
		createOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterUserForm ruf = new RegisterUserForm();
				ruf.setVisible(true);
			}
		});
		createOrderButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		createOrderButton.setBounds(41, 166, 334, 58);
		contentPane.add(createOrderButton);
	}

}
