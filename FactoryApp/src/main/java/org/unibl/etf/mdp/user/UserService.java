package org.unibl.etf.mdp.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.model.User;
import org.unibl.etf.mdp.properties.PropertiesService;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;

public class UserService {

	private static ArrayList<User> users = new ArrayList<>();
	public static final String PATH = "." + File.separator + "MDP-Project" + File.separator + "RegistrationRequests" + File.separator;
	
	//When user makes a request, REST is called and it writes new request to a file
	//REST default place for files is Desktop
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
	
	//REST calls this method to check if user with username and password exists
	public static User checkCredentials(User user) {
		User returnUser = null;
		try {		
			ArrayList<User> readUsers = readExistsingUsers();
			for(User u : readUsers) {
				if(u.getUserName().equals(user.getUserName()) && u.getPassword().equals(user.getPassword())) {
					returnUser = u;
					return returnUser;
				}
			}
			return null;
		} catch(Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	//Read all of the users from the JSON file
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
