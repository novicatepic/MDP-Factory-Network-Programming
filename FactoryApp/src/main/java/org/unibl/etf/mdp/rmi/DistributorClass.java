package org.unibl.etf.mdp.rmi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.buyer.model.Product;
import org.unibl.etf.mdp.distributor.ChooseWhoToBuyFromForm;
import org.unibl.etf.mdp.distributor.StartDistributorForm;
import org.unibl.etf.mdp.distributor2.ProductForm;
import org.unibl.etf.mdp.properties.PropertiesService;

import com.google.gson.Gson;

public class DistributorClass implements DistributorInterface {
	public static final String PATH = "resources";
	public DistributorClass() throws RemoteException {}
	private String fileName;
	
	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(DistributorClass.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(DistributorClass.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Product> getDistributorProducts(String name) throws RemoteException {
		ArrayList<Product> products = new ArrayList<>();
		try {
			Gson gson = new Gson();
			File f = new File(StartDistributorForm.DISTRIBUTORS_PATH+ProductForm.PATH+name);
			fileName=ProductForm.PATH+name;
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line="", content="";
			while((line = br.readLine()) != null) {
				content += line;
			}
			if(!"".equals(content)) {
				String[] split = content.split("}");
				for(int i=0; i<split.length; i++) {		
					split[i]+="}";
					products.add(gson.fromJson(split[i], Product.class));
				}
			}		
			br.close();
		} catch(Exception ex) {
			Logger.getLogger(DistributorClass.class.getName()).log(Level.SEVERE, ex.fillInStackTrace().toString());
			ex.printStackTrace();
			return null;
		}
		return products;
	}
	

	@Override
	public void writeUpdatedProducts(List<Product> products) throws RemoteException, IOException {
		File f = new File(fileName);
		f.delete();
		Gson gson = new Gson();
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)),true);
		for(Product p : products) {
			pw.println(gson.toJson(p));
		}
		pw.close();
	}
	
	public static void main(String[] args) throws Exception {
		//Classic code
		System.setProperty("java.security.policy", PATH+File.separator+"server_policyfile.txt");
		if(System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			DistributorClass server = new DistributorClass();
			DistributorInterface stub = (DistributorInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("Distributor", stub);
		} catch(Exception e) {
			Logger.getLogger(DistributorClass.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
}
