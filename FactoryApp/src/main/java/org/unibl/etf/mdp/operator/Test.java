package org.unibl.etf.mdp.operator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./message.xml", true)), true);
		pw.println("ab");
		pw.close();
	}

}
