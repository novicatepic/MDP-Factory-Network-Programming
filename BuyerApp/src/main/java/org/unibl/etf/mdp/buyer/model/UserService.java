package org.unibl.etf.mdp.buyer.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
		
		/*for(User pr : users) {
			for(String sifra : pr.getSifre()) {
				for(String sifra2 : p.getSifre()) {
					if(sifra.equals(sifra2)) {
						System.out.println("VRATIO FALSE!");
						return false;
					}
				}
			}
		}*/
		
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
	
}
