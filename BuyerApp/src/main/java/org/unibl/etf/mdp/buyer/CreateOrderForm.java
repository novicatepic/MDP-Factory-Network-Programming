package org.unibl.etf.mdp.buyer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.buyer.model.Order;
import org.unibl.etf.mdp.buyer.model.Product;
import org.unibl.etf.mdp.buyer.model.User;
import org.unibl.etf.mdp.mq.ConnectionFactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.awt.GridLayout;
import java.awt.TextArea;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class CreateOrderForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateOrderForm frame = new CreateOrderForm();
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
	private JPanel panel;
	public CreateOrderForm() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 1031, 558);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(10, 10, 997, 419);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton submitButton = new JButton("SUBMIT ORDER");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("test.xml")));
					ArrayList<Integer> amounts = new ArrayList<>();
					for(int i=0; i<products.size(); i++) {
						amounts.add((Integer)comboBoxes[i].getSelectedItem());
					}
					Order order = new Order(user.getUserName(), user.getAddress(), products, amounts);
					encoder.writeObject(order);
					encoder.close();
					String xmlString = new String(Files.readAllBytes(Paths.get("test.xml")));
					File f = new File("test.xml");
					f.delete();
					Connection connection = ConnectionFactoryUtil.createConnection();
					Channel channel = connection.createChannel();
					System.out.println(xmlString);
					channel.queueDeclare("order", false, false, false, null);
					channel.basicPublish("", "order", null, xmlString.getBytes());
					channel.close();
					connection.close();
					
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		submitButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		submitButton.setBounds(10, 449, 997, 62);
		contentPane.add(submitButton);
	}
	
	List<Product> products = new ArrayList<>();
	JComboBox[] comboBoxes;
	public void populateData(List<Product> prs) {
		products = prs;
		panel.setLayout(new GridLayout(products.size(), 2));
		comboBoxes = new JComboBox[products.size()];
		for(int i=0; i<comboBoxes.length; i++) {
			comboBoxes[i] = new JComboBox();
		}
		for(int i=0; i<products.size(); i++) {
			for(int j=0; j<2; j++) {
				if(j==0) {
					panel.add(new JLabel(products.get(i).getName()));
				} else {
					for(int k=1; k<=products.get(i).getAmount(); k++) {
						System.out.println("K="+k);
						comboBoxes[i].addItem(k);
					}
					panel.add(comboBoxes[i]);
					
				}
			}
		}
	}
	private User user;
	public void setUser(User u) {
		user = u;
		System.out.println("CREATE ORDER FORM USER="+user.getAddress());
	}
}
