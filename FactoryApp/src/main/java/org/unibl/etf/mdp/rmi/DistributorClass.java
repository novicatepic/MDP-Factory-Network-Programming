package org.unibl.etf.mdp.rmi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.mdp.distributor2.ProductForm;
import org.unibl.etf.mdp.product.Product;

import com.google.gson.Gson;

public class DistributorClass implements DistributorInterface {
	public static final String PATH = "resources";
	public DistributorClass() throws RemoteException {}

	@Override
	public List<Product> getDistributorProducts(String name) throws RemoteException {
		ArrayList<Product> products = new ArrayList<>();
		try {
			Gson gson = new Gson();
			File f = new File(ProductForm.PATH+name);
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
					products.add(gson.fromJson(split[i], Product.class));
				}
			}		
			br.close();
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return products;
	}
	
	public static void main(String[] args) throws Exception {
		/*ArrayList<Product> prs = new DistributorClass().getDistributorProducts("D1");
		for(Product p : prs) {
			System.out.println(p);
		}*/
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
			e.printStackTrace();
		}
	}
	
	
}
