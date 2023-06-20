package org.unibl.etf.mdp.distributor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.model.Distributor;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StartForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	
	public static Distributor distributor = new Distributor();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartForm frame = new StartForm();
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
	public StartForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 452, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("WELCOME");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(31, 10, 375, 42);
		contentPane.add(lblNewLabel);
		
		JButton nameButton = new JButton("ADD NAME");
		nameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NameForm nf = new NameForm();
				nf.setVisible(true);
			}
		});
		nameButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nameButton.setBounds(41, 71, 334, 58);
		contentPane.add(nameButton);
		
		JButton productButton = new JButton("ADD PRODUCT");
		productButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProductForm pf = new ProductForm();
				pf.setVisible(true);
			}
		});
		productButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		productButton.setBounds(41, 166, 334, 58);
		contentPane.add(productButton);
	}

}
