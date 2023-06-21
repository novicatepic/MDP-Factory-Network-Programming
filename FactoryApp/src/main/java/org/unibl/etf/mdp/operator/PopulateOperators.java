package org.unibl.etf.mdp.operator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.unibl.etf.mdp.model.Operator;

import com.google.gson.Gson;

public class PopulateOperators {

	public static final String PATH=".."+File.separator+"Users"+File.separator+"factory_users.json";
	
	public static void main(String[] args) {
		try {
			Gson gson = new Gson();
			ArrayList<Operator> operators = new ArrayList<>();
			for(int i=0; i<5; i++) {
				operators.add(new Operator("Operator"+i));
			}
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(PATH, true)), true);
			for(Operator o : operators) {
				pw.println(gson.toJson(o));
			}
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
