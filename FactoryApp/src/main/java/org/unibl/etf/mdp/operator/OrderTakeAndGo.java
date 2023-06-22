package org.unibl.etf.mdp.operator;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.mdp.distributor.ChooseWhoToBuyFromForm;
import org.unibl.etf.mdp.model.Operator;
import org.unibl.etf.mdp.model.User;
import org.unibl.etf.mdp.mq.ConnectionFactoryUtil;
import org.unibl.etf.mdp.properties.PropertiesService;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class OrderTakeAndGo extends JFrame {

	private JPanel contentPane;

	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(OrderTakeAndGo.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(OrderTakeAndGo.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	public OrderTakeAndGo() {	
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 452, 197);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("PROCESS ORDER");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(31, 10, 375, 42);
		contentPane.add(lblNewLabel);
		
		JButton processButton = new JButton("PROCESS");
		processButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Process an order
					//Set an operator so it is possible to write in a file and to write an email containing operator who processed the order
					ProcessOrderForm pof = new ProcessOrderForm();
					pof.setOperator(operator);
					//Real processing is called with this method
					pof.setXML();
					pof.setVisible(true);
				} catch(Exception ex) {
					Logger.getLogger(OrderTakeAndGo.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
					ex.printStackTrace();
				}
				
			}
		});
		processButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		processButton.setBounds(44, 86, 334, 58);
		contentPane.add(processButton);
	}
	
	private String operator;
	public void setOperator(String op) {
		this.operator = op;
	}

}
