package org.unibl.etf.mdp.operator;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.unibl.etf.mdp.model.Order;
import org.unibl.etf.mdp.mq.ConnectionFactoryUtil;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ProcessOrderForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProcessOrderForm frame = new ProcessOrderForm();
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
	public ProcessOrderForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 532);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton acceptButton = new JButton("ACCEPT");
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setXML();
				} catch(Exception ex) {
					ex.printStackTrace();
				}				
			}
		});
		acceptButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		acceptButton.setBounds(71, 419, 218, 66);
		contentPane.add(acceptButton);
		
		JButton rejectButton = new JButton("REJECT");
		rejectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		rejectButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		rejectButton.setBounds(502, 419, 218, 66);
		contentPane.add(rejectButton);
		
		JLabel productInfoLabel = new JLabel("");
		productInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		productInfoLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		productInfoLabel.setBounds(52, 72, 689, 89);
		contentPane.add(productInfoLabel);
		
		JLabel userNameLabel = new JLabel("");
		userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		userNameLabel.setBounds(194, 186, 538, 71);
		contentPane.add(userNameLabel);
		
		JLabel addressLabel = new JLabel("");
		addressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		addressLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addressLabel.setBounds(194, 277, 538, 71);
		contentPane.add(addressLabel);
		
		JLabel lblNewLabel = new JLabel("USER NAME");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(26, 186, 84, 71);
		contentPane.add(lblNewLabel);
		
		JLabel lblProductInfo = new JLabel("PRODUCT INFO");
		lblProductInfo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblProductInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblProductInfo.setBounds(211, 10, 285, 45);
		contentPane.add(lblProductInfo);
		
		JLabel lblMail = new JLabel("MAIL");
		lblMail.setHorizontalAlignment(SwingConstants.CENTER);
		lblMail.setBounds(26, 277, 84, 71);
		contentPane.add(lblMail);
	}
	
	private String xml="";
	

	public void processOrder() throws Exception {
		Connection connection = ConnectionFactoryUtil.createConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare("order", false, false, false, null);
		//channel.basicQos(1);	
		
		//String message = "";
		//final String[] messages = new String[1]; 
		/*Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("Message received: " + message);
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./message.xml", true)), true);
				pw.println(message);
				pw.close();
				channel.basicAck(envelope.getDeliveryTag(), false);
				return;
			}
		};*/
		
		//channel.basicConsume("order", false, consumer);
		
		GetResponse response = channel.basicGet("order", true);
		if (response != null) {
		    String message = new String(response.getBody(), "UTF-8");
		    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./message.xml", true)), true);
			pw.println(message);
			pw.close();
		}
		channel.close();
		connection.close();
	}
	
	private Order order = new Order();
	public void setXML() {
		try {
			processOrder();
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File("./internetschema.xsd"));
			Validator validator = schema.newValidator();
			
			BufferedReader br = new BufferedReader(new FileReader(new File("./message.xml")));
			String str = "";
			while((str = br.readLine()) != null) {
				xml += str;
			}
			br.close();
			System.out.println("READ XML = " + xml);
			validator.validate(new StreamSource(new File("./message.xml")));
			
			File xmlFile = new File("path/to/xml/file.xml");
            // Create a JAXBContext for the class
            JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
            // Create an Unmarshaller
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // Unmarshal the XML file to create an object
            Order myObject = (Order) unmarshaller.unmarshal(xmlFile);
            System.out.println("USER-NAME="+myObject.getUserName());
			
			File f = new File("./message.xml");
			f.delete();
			System.out.println("END");
			xml="";
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}		
	}
}
