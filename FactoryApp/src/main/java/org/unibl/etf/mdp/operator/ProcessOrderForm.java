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
	}
	
	private static final String KEY_STORE_PATH = "."+File.separator+"keystore.jks";
	private static String KEY_STORE_PASSWORD;
	private static String HOST;
	private static int PORT;
	
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
					System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
					System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
					SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
					SSLSocket s = (SSLSocket) sf.createSocket(HOST, PORT);
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
					out.println("GEN_INFO");
					String mesToSend = "PRIHVACENA NARUDZBA " + order + " OD OPERATORA " + operator;
					out.println(mesToSend);
					ArrayList<Product> prodService = ProductService.readProducts();
					for(int i=0; i<prodService.size(); i++) {
						if(order.getProducts().contains(prodService.get(i))) {
							int pos = order.getProducts().indexOf(prodService.get(i));
							if(prodService.get(i).getAmount()<order.getAmounts().get(pos)) {
								throw new Exception("Amount not possible at the current moment!");
							}
						}
					}
					for(int i=0; i<order.getProducts().size(); i++) {
						order.getProducts().get(i).setAmount(order.getProducts().get(i).getAmount()-Integer.valueOf(order.getAmounts().get(i)));
						ProductService.updateProduct(order.getProducts().get(i));
					}
					
					boolean status = sendMail(order.getAddress(), "Obavjestenje o narudzbi", mesToSend);
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
					System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
					System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
					SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
					SSLSocket s = (SSLSocket) sf.createSocket(HOST, PORT);
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
					String mesToSend = "ODBIJENA NARUDZBA " + order + " OD OPERATORA " + operator;
					out.println("GEN_INFO");
					out.println("ODBIJENA NARUDZBA " + order + " OD OPERATORA " + operator);
					boolean status = sendMail(order.getAddress(), "Obavjestenje o narudzbi", mesToSend);
					out.close();
					s.close();
				} catch(Exception ex) {
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
	
	public static void sendEmail(Session session, String toEmail, String subject, String body){
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

	      msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      System.out.println("Message is ready");
    	  Transport.send(msg);  

	      System.out.println("EMail Sent Successfully!!");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}

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
	
	private Order order;
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
			
			InputStream xmlFile = new FileInputStream("./message.xml");
			XMLDecoder decoder = new XMLDecoder(xmlFile);
			Object decodedObject = decoder.readObject();
			if(decodedObject instanceof Order) {
				order = (Order)decodedObject;
				userNameLabel.setText(order.getUserName());
				addressLabel.setText(order.getAddress());
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
			System.out.println("END");
			xml="";
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	String username="", password="";
	private Properties loadMailConfig() throws FileNotFoundException, IOException {
		Properties serverprop = new Properties();
		serverprop.load(new FileInputStream(new File("./server.properties")));
		String mailProvider = serverprop.getProperty("mail_provider");

		System.out.println("Koristi se " + mailProvider);
		
		Properties mailProp = new Properties();
		mailProp.load(new FileInputStream(new File("./mail" + File.separator + mailProvider + ".properties")));

		username = mailProp.getProperty("username");
		password = mailProp.getProperty("password");
		return mailProp;
	}

	private boolean sendMail(String to, String title, String body) throws FileNotFoundException, IOException {

		Properties props = loadMailConfig();
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(title);
			message.setText(body);
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*private static boolean mailFunc() {
		String  d_email = "novicamdp@mail.com",
	            d_uname = "Novica",
	            d_password = "novicamdp",
	            d_host = "smtp.gmail.com",
	            d_port  = "465",
	            m_to = "tepicnovic@gmail.com",
	            m_subject = "Indoors Readable File: ",
	            m_text = "This message is from Indoor Positioning App. Required file(s) are attached.";
	    Properties props = new Properties();
	    props.put("mail.smtp.user", d_email);
	    props.put("mail.smtp.host", d_host);
	    props.put("mail.smtp.port", d_port);
	    props.put("mail.smtp.starttls.enable","true");
	    props.put("mail.smtp.debug", "true");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.socketFactory.port", d_port);
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.socketFactory.fallback", "false");

	    //SMTPAuthenticator auth = new SMTPAuthenticator();
	    Session session = Session.getInstance(props, auth);
	    session.setDebug(true);

	    MimeMessage msg = new MimeMessage(session);
	    try {
	        msg.setSubject(m_subject);
	        msg.setFrom(new InternetAddress(d_email));
	        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(m_to));

	Transport transport = session.getTransport("smtps");
	            transport.connect(d_host, Integer.valueOf(d_port), d_uname, d_password);
	            transport.sendMessage(msg, msg.getAllRecipients());
	            transport.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        } 
	}*/
}
