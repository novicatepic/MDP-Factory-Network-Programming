package org.unibl.etf.mdp.operator.server;

import java.io.IOException;
import java.net.ServerSocket;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.unibl.etf.mdp.properties.PropertiesService;

public class SecureServer {

	{
		PORT=Integer.valueOf(PropertiesService.getElement("PORT_8443"));
		KEY_STORE_PASSWORD=PropertiesService.getElement("KEYSTORE_PASSWORD");
	}
	
	private static int PORT;
	private static final String KEY_STORE_PATH = "./keystore.jks";
	private static String KEY_STORE_PASSWORD;
	
	public static void main(String[] args) throws IOException {		

		System.setProperty("javax.net.ssl.keyStore", KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);

		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		ServerSocket ss = ssf.createServerSocket(PORT);
		System.out.println("Server started");
		while (true) {
			SSLSocket s = (SSLSocket) ss.accept();
			System.out.println("Client accepted!!!");
			new ServerThread(s).start();
		}

	}
	
}
