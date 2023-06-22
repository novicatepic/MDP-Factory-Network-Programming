package org.unibl.etf.mdp.product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.buyer.model.Product;
import org.unibl.etf.mdp.distributor.ChooseWhoToBuyFromForm;
import org.unibl.etf.mdp.model.User;
import org.unibl.etf.mdp.properties.PropertiesService;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ProductService {

	public static ArrayList<Product> products = new ArrayList<>();
	public static String instanceName = "Product";
	private static JedisPool pool = new JedisPool("localhost");
	private static Jedis jedis = pool.getResource();
	
	public static void writeProducts() {
		try {
			jedis.set(instanceName, "OK");
			for (Product p : products) {
				jedis.lpush(instanceName + ":students:strings", p.toString());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	public static void addProduct(Product p) {
		products = readProducts();
		if(products.contains(p)) {
			int index = products.indexOf(p);
			products.get(index).setAmount(products.get(index).getAmount()+p.getAmount());
			String product;
			jedis.set(instanceName, "OK");
			while((product=jedis.rpop(instanceName + ":students:strings"))!=null) {	}
			for (Product pr : products) {
				jedis.lpush(instanceName + ":students:strings", pr.toString());
			}
		} else {
			products.add(p);
			String product;
			jedis.set(instanceName, "OK");
			while((product=jedis.rpop(instanceName + ":students:strings"))!=null) {	}
			for (Product pr : products) {
				jedis.lpush(instanceName + ":students:strings", pr.toString());
			}
		}
	}
	
	public static ArrayList<Product> readProducts() {
		ArrayList<Product> result = new ArrayList<>();
		try {
			String product;
			jedis.set(instanceName, "OK");
			while((product=jedis.rpop(instanceName + ":students:strings"))!=null) {				
				String[] split = product.split("/");
				result.add(new Product(split[1], Integer.parseInt(split[3]), Double.parseDouble(split[5])));
			}
			for (Product p : result) {
				jedis.lpush(instanceName + ":students:strings", p.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean deleteProduct(String name) {
		products=readProducts();
		Product pr = new Product(name);
		if(!products.contains(pr)) {
			return false;
		}
		jedis.del(instanceName);
		jedis.flushAll();
		products.remove(pr);
		jedis.set(instanceName, "OK");
		System.out.println("REMOVED~~~");
		for (Product p : products) {
			jedis.lpush(instanceName + ":students:strings", p.toString());
		}
		return true;
	}
	
	public static boolean updateProduct(Product product) {
		products=readProducts();
		if(!products.contains(product)) {
			return false;
		}
		jedis.del(instanceName);
		jedis.flushAll();
		for(int i=0; i<products.size(); i++) {
			if(products.get(i).equals(product)) {
				System.out.println("Setting");
				products.get(i).setAmount(product.getAmount());
				products.get(i).setName(product.getName());
				products.get(i).setPrice(product.getPrice());
			}
		}
		jedis.set(instanceName, "OK");
		for (Product p : products) {
			jedis.lpush(instanceName + ":students:strings", p.toString());
		}
		return true;
	}
	
	public static final String PATH=".."+File.separator+"Distributors"+File.separator;
	public static ArrayList<String> productNames() throws IOException {
		ArrayList<String> names = new ArrayList();
		File f = new File(PATH);
		File[] files = f.listFiles();
		Gson gson = new Gson();
		for(File file : files) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line="", content="";
			while((line = br.readLine()) != null) {
				content += line;
			}
			if(!"".equals(content)) {
				String[] split = content.split("}");
				for(int i=0; i<split.length; i++) {		
					split[i]+="}";
					Product p = gson.fromJson(split[i], Product.class);
					names.add(p.getName());
				}
			}		
			br.close();
		}
		ArrayList<Product> products = readProducts();
		for(Product p : products) {
			if(!names.contains(p.getName()))
				names.add(p.getName());
		}
		return names;
	}
	
	public static boolean containsProduct(String name) throws Exception {
		ArrayList<String> names = productNames();
		return names.contains(name);
	}
	
	//Test
	public static void main(String[] args) throws Exception {
		//writeProducts();
		/*for(Product p : products) {
			System.out.println(p);
		}
		deleteProduct("Product0");
		ArrayList<Product> pr = readProducts();
		for(Product p : pr) {
			System.out.println(p);
		}*/
		ArrayList<String> names = productNames();
		for(String name : names) {
			System.out.println(name);
		}
	}
}
