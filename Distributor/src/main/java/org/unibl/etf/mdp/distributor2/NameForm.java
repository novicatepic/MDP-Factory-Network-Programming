package org.unibl.etf.mdp.distributor2;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NameForm extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NameForm frame = new NameForm();
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
	public NameForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 451, 257);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("NAME FORM");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(33, 25, 336, 40);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("INPUT NAME");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(44, 99, 112, 45);
		contentPane.add(lblNewLabel_1);
		
		nameField = new JTextField();
		nameField.setBounds(209, 99, 188, 45);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JButton submitButton = new JButton("SUBMIT");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartForm.distributor.setName(nameField.getText());
			}
		});
		submitButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		submitButton.setBounds(124, 170, 169, 40);
		contentPane.add(submitButton);
	}
}
