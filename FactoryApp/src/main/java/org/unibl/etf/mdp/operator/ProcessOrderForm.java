package org.unibl.etf.mdp.operator;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.unibl.etf.mdp.buyer.model.Order;
import org.unibl.etf.mdp.buyer.model.Product;
import org.unibl.etf.mdp.distributor.ChooseWhoToBuyFromForm;
import org.unibl.etf.mdp.mq.ConnectionFactoryUtil;
import org.unibl.etf.mdp.product.ProductService;
import org.unibl.etf.mdp.properties.PropertiesService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

public class ProcessOrderForm extends JFrame {

	private JPanel contentPane;

	{
		HOST=PropertiesService.getElement("LOCAL_HOST");
		PORT=Integer.valueOf(PropertiesService.getElement("PORT_8443"));
		KEY_STORE_PASSWORD=PropertiesService.getElement("KEYSTORE_PASSWORD");
		GEN_INFO=PropertiesService.getElement("PROTOCOL_GEN_INFO_MESSAGE");
		QUEUE=PropertiesService.getElement("QUEUE_NAME");
	}
	
	private static String QUEUE;
	private static String GEN_INFO;
	private static final String KEY_STORE_PATH = "."+File.separator+"keystore.jks";
	private static String KEY_STORE_PASSWORD;
	private static String HOST;
	private static int PORT;
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(ProcessOrderForm.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(ProcessOrderForm.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	public ProcessOrderForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1006, 764);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton acceptButton = new JButton("ACCEPT");
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//When we accept an order, we need to establish secure connection, so server side can write message to a file
					System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
					System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
					SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
					SSLSocket s = (SSLSocket) sf.createSocket(HOST, PORT);
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
					out.println(GEN_INFO);				
					ArrayList<Product> prodService = ProductService.readProducts();
					//If there were two or more orders to the same product and one was accepted
					//Next one would be invalid if we got to the point where amount was lower than zero
					//So we throw an exception
					for(int i=0; i<prodService.size(); i++) {
						if(order.getProducts().contains(prodService.get(i))) {
							int pos = order.getProducts().indexOf(prodService.get(i));
							if(prodService.get(i).getAmount()<order.getAmounts().get(pos)) {
								throw new Exception("Amount not possible at the current moment!");
							}
						}
					}
					String mesToSend = "ACCEPTED ORDER " + order + " FROM OPERATOR " + operator;
					System.out.println("MSSG FROM ORDER="+mesToSend);
					//Send to the server side
					out.println(mesToSend);
					for(int i=0; i<order.getProducts().size(); i++) {
						order.getProducts().get(i).setAmount(order.getProducts().get(i).getAmount()-Integer.valueOf(order.getAmounts().get(i)));
						ProductService.updateProduct(order.getProducts().get(i));
					}
					//Send mail
					boolean status = sendMail(order.getAddress(), "Order notification success", mesToSend);
					out.close();
					s.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}				
			}
		});
		acceptButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		acceptButton.setBounds(58, 651, 218, 66);
		contentPane.add(acceptButton);
		
		JButton rejectButton = new JButton("REJECT");
		rejectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Establish secure socket
					System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
					System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
					SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
					SSLSocket s = (SSLSocket) sf.createSocket(HOST, PORT);
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
					String mesToSend = "REJECTED ORDER " + order + " FROM OPERATOR " + operator;
					System.out.println("MSSG FROM ORDER="+mesToSend);
					out.println(GEN_INFO);
					out.println(mesToSend);
					boolean status = sendMail(order.getAddress(), "Order notification failure", mesToSend);
					out.close();
					s.close();
				} catch(Exception ex) {
					Logger.getLogger(ProcessOrderForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
					ex.printStackTrace();
				}				
			}
		});
		rejectButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		rejectButton.setBounds(523, 651, 218, 66);
		contentPane.add(rejectButton);
		
		userNameLabel = new JLabel("");
		userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		userNameLabel.setBounds(182, 551, 559, 39);
		contentPane.add(userNameLabel);
		
		addressLabel = new JLabel("");
		addressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		addressLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addressLabel.setBounds(182, 602, 559, 39);
		contentPane.add(addressLabel);
		
		JLabel lblNewLabel = new JLabel("USER NAME");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(60, 551, 84, 39);
		contentPane.add(lblNewLabel);
		
		JLabel lblProductInfo = new JLabel("PRODUCT INFO");
		lblProductInfo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblProductInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblProductInfo.setBounds(211, 10, 285, 45);
		contentPane.add(lblProductInfo);
		
		JLabel lblMail = new JLabel("MAIL");
		lblMail.setHorizontalAlignment(SwingConstants.CENTER);
		lblMail.setBounds(60, 600, 84, 39);
		contentPane.add(lblMail);
		
		panel = new JPanel();
		panel.setBounds(171, 58, 800, 211);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Wanted amounts");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(703, 279, 94, 31);
		contentPane.add(lblNewLabel_1);
		
		amountsPanel = new JPanel();
		amountsPanel.setBounds(753, 323, 218, 230);
		contentPane.add(amountsPanel);
	}
	private JLabel userNameLabel;
	private JLabel addressLabel;
	private JPanel panel;
	private JPanel amountsPanel;
	private String xml="";
	private String operator="";
	public void setOperator(String op) {
		operator=op;
	}

	//Different MQ implementation
	//This way I can get orders one by one, not all in one try
	public void processOrder() throws Exception {
		Connection connection = ConnectionFactoryUtil.createConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE, false, false, false, null);		
		GetResponse response = channel.basicGet(QUEUE, true);
		if (response != null) {
		    String message = new String(response.getBody(), "UTF-8");
		    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./message.xml", true)), true);
			pw.println(message);
			pw.close();
		}
		channel.close();
		connection.close();
	}
	
	private Order order;
	public void setXML() {
		try {
			//Get the string
			processOrder();
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File("./internetschema.xsd"));
			Validator validator = schema.newValidator();
			
			//Read XML content
			BufferedReader br = new BufferedReader(new FileReader(new File("./message.xml")));
			String str = "";
			while((str = br.readLine()) != null) {
				xml += str;
			}
			br.close();

			//Validate it with validator based on schema
			validator.validate(new StreamSource(new File("./message.xml")));
			//Decode order based on XML decoder
			InputStream xmlFile = new FileInputStream("./message.xml");
			XMLDecoder decoder = new XMLDecoder(xmlFile);
			Object decodedObject = decoder.readObject();
			if(decodedObject instanceof Order) {
				order = (Order)decodedObject;
				userNameLabel.setText(order.getUserName());
				addressLabel.setText(order.getAddress());
				//Populate grid layout with info
				panel.setLayout(new GridLayout(order.getProducts().size()+1, 3));
				for(int i=0; i<order.getProducts().size()+1; i++) {
					for(int j=0; j<3; j++) {
						if(i==0) {
							if(j==0) {
								panel.add(new JLabel("Name"));
							} else if(j==1) {
								panel.add(new JLabel("Amount"));
							} else {
								panel.add(new JLabel("Value"));
							}
						} else {
							if(j==0) {
								panel.add(new JLabel(order.getProducts().get(i-1).getName()));
							} else if(j==1) {
								panel.add(new JLabel(String.valueOf(order.getProducts().get(i-1).getAmount())));
							} else {
								panel.add(new JLabel(String.valueOf(order.getProducts().get(i-1).getPrice())));
							}
						}
					}
					
				}
				amountsPanel.setLayout(new GridLayout(order.getAmounts().size(), 1));
				for(int i=0; i<order.getAmounts().size(); i++) {
					amountsPanel.add(new JLabel(String.valueOf(order.getAmounts().get(i))));
				}
			}
			decoder.close();
			
			File f = new File("./message.xml");
			f.delete();
			xml="";			
		} catch(Exception ex) {
			Logger.getLogger(ProcessOrderForm.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
			ex.printStackTrace();
		}		
	}
	
	//Copied implementation
	//Load gmail properties from property file
	//Password for the account is not the classic password, we use 2-step authentication password for applications
	String username="", password="";
	private Properties loadMailConfig() throws FileNotFoundException, IOException {
		Properties serverprop = new Properties();
		serverprop.load(new FileInputStream(new File("./server.properties")));
		String mailProvider = serverprop.getProperty("mail_provider");

		System.out.println("We use " + mailProvider);
		
		Properties mailProp = new Properties();
		mailProp.load(new FileInputStream(new File("./mail" + File.separator + mailProvider + ".properties")));

		username = mailProp.getProperty("username");
		password = mailProp.getProperty("password");
		return mailProp;
	}

	private boolean sendMail(String to, String title, String body) throws FileNotFoundException, IOException {
		//Load parameters
		Properties props = loadMailConfig();
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		});

		//Make a message and send it
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(title);
			message.setText(body);
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			Logger.getLogger(ProcessOrderForm.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
			return false;
		}
	}
}
