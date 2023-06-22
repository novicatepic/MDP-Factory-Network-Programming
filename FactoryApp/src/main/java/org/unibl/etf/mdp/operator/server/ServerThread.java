package org.unibl.etf.mdp.operator.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.unibl.etf.mdp.distributor.ChooseWhoToBuyFromForm;
import org.unibl.etf.mdp.model.Operator;
import org.unibl.etf.mdp.properties.PropertiesService;
import com.google.gson.Gson;

public class ServerThread extends Thread {
	private static final String FACTORY_USERS_PATH = ".." + File.separator + "Users" + File.separator + "factory_users.json";
	private static final String ORDER_INFO_PATH = "." + File.separator + "OrderInfo" + File.separator;
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private String username;
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(ServerThread.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(ServerThread.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}

	{
		OK_MESSAGE=PropertiesService.getElement("PROTOCOL_OK_MESSAGE");
		NOT_OK_MESSAGE=PropertiesService.getElement("PROTOCOL_NOT_OK_MESSAGE");
		LOGIN_MESSAGE=PropertiesService.getElement("PROTOCOL_LOGIN_MESSAGE");
		GEN_INFO_MESSAGE=PropertiesService.getElement("PROTOCOL_GEN_INFO_MESSAGE");
	}
	
	private static String OK_MESSAGE;
	private static String NOT_OK_MESSAGE;
	private static String LOGIN_MESSAGE;
	private static String GEN_INFO_MESSAGE;
	
	public ServerThread(Socket socket) {
		super();
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		} catch (IOException e) {
			Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			String option = in.readLine();
			//Separate login from writting to a file
			//Follow the protocol
			if(LOGIN_MESSAGE.equals(option)) {
				ArrayList<Operator> operators = readExistsingOperators();
				String userName = in.readLine();
				boolean found = false;
				Operator foundOp = new Operator();
				for(Operator o : operators) {
					if(o.getUserName().equals(userName)) {
						foundOp = o;
						found = true;					
					}
				}
				if(found) {
					out.println(OK_MESSAGE);
					out.println(foundOp);
				} else {
					out.println(NOT_OK_MESSAGE);
				}
			} else if(GEN_INFO_MESSAGE.equals(option)){
				PrintWriter pw2 = new PrintWriter(new FileWriter(new File("."+File.separator+"OrderInfo"+File.separator+"file_"+new Date().getTime()+".txt"), true), true);
				String message = in.readLine();
				System.out.println("MSSG = " + message);
				pw2.println(message);
				pw2.close();
			}
			
			in.close();
			out.close();
			socket.close();
		} catch(Exception ex) {
			Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
			ex.printStackTrace();
		}		
	}

	//Read operators so we can check if operator login with user name was valid or not
	public static ArrayList<Operator> readExistsingOperators() throws Exception {
		Gson gson = new Gson();
		File f = new File(FACTORY_USERS_PATH);
		//f.createNewFile();
		ArrayList<Operator> users = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line="", content="";
		while((line = br.readLine()) != null) {
			content += line;
		}
		if(!"".equals(content)) {
			String[] split = content.split("}");
			for(int i=0; i<split.length; i++) {		
				split[i]+="}";
				users.add(gson.fromJson(split[i], Operator.class));
			}
		}		
		br.close();
		return users;
	}
	
	
}
