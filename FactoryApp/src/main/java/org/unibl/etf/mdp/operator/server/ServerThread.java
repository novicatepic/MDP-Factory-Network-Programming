package org.unibl.etf.mdp.operator.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.unibl.etf.mdp.model.Operator;

import com.google.gson.Gson;

public class ServerThread extends Thread {
	private static final String FACTORY_USERS_PATH = ".." + File.separator + "Users" + File.separator + "factory_users.json";
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
			ArrayList<Operator> operators = readExistsingOperators();
			String userName = in.readLine();
			boolean found = false;
			for(Operator o : operators) {
				if(o.getUserName().equals(userName)) {
					found = true;					
				}
			}
			if(found) {
				out.println("OK");
			} else {
				out.println("NOT_OK");
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
