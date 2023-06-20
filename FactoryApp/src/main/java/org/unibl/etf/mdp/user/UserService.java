package org.unibl.etf.mdp.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;

public class UserService {

	private static ArrayList<User> users = new ArrayList<>();
	

	public static final String PATH = "." + File.separator + "MDP-Project" + File.separator + "RegistrationRequests" + File.separator;
	
	static {
		users.add(new User("a", "a", "a", "a", "a"));
	}
	
	public static boolean createUser(User u) {
		String fileName = u.getUserName();
		Gson gson = new Gson();
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(PATH+fileName)), true);
			pw.println(gson.toJson(u));
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		synchronized (users) {
			users.add(u);
		}
		return true;
	}
	
	public static boolean checkCredentials(User user) {
		try {
			ArrayList<User> readUsers = readExistsingUsers();
			for(User u : readUsers) {
				System.out.println(u);
				if(u.getUserName().equals(user.getUserName()) && u.getPassword().equals(user.getPassword())) {
					return true;
				}
			}
			return false;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	private static final String USERS_PATH = "." + File.separator + "MDP-Project" + File.separator + "Users" + File.separator + "users.json";
	private static ArrayList<User> readExistsingUsers() throws Exception {
		Gson gson = new Gson();
		File f = new File(USERS_PATH);
		f.createNewFile();
		ArrayList<User> users = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line="", content="";
		while((line = br.readLine()) != null) {
			content += line;
		}
		System.out.println("CONTENT = " + content);
		if(!"".equals(content)) {
			String[] split = content.split("}");
			for(int i=0; i<split.length; i++) {		
				split[i]+="}";
				users.add(gson.fromJson(split[i], User.class));
			}
		}		
		br.close();
		return users;
	}
	
}
