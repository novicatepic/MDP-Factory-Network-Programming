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

import org.unibl.etf.mdp.model.Operator;

import com.google.gson.Gson;

public class ServerThread extends Thread {
	private static final String FACTORY_USERS_PATH = ".." + File.separator + "Users" + File.separator + "factory_users.json";
	private static final String ORDER_INFO_PATH = "." + File.separator + "OrderInfo" + File.separator;
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private String username;

	public ServerThread(Socket socket) {
		super();
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			String option = in.readLine();
			if("LOGIN".equals(option)) {
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
					out.println("OK");
					out.println(foundOp);
				} else {
					out.println("NOT_OK");
				}
			} else if("GEN_INFO".equals(option)){
				PrintWriter pw2 = new PrintWriter(new FileWriter(new File("."+File.separator+"OrderInfo"+File.separator+"file_"+new Date().getTime()+".txt"), true), true);
				pw2.println(in.readLine());
				pw2.close();
			}
			
			in.close();
			out.close();
			socket.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}		
	}

	
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
