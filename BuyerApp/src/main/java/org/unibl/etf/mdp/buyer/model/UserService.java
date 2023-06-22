package org.unibl.etf.mdp.buyer.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.buyer.RegisterUserForm;
import org.unibl.etf.mdp.properties.PropertiesService;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;

public class UserService {

	private static ArrayList<User> users = new ArrayList<>();
	

	public static final String PATH = "." + File.separator + "MDP-Project" + File.separator + "RegistrationRequests" + File.separator;
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(UserService.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(UserService.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	public static boolean createUser(User u) {
		String fileName = u.getUserName();
		Gson gson = new Gson();
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(PATH+fileName)), true);
			pw.println(gson.toJson(u));
			pw.close();
		} catch(Exception e) {
			Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
			return false;
		}
		synchronized (users) {
			users.add(u);
		}
		return true;
	}
	
}
