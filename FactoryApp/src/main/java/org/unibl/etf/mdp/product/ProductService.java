package org.unibl.etf.mdp.product;

import java.util.ArrayList;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ProductService {

	private static ArrayList<Product> products = new ArrayList<>();
	public static String instanceName = "Product";
	
	static {
		for(int i=0;i<7;i++) {
			products.add(new Product("Product"+i, i*4+1, i*15+22.4));
		}
	}
	
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
		Product pr = new Product(name);
		if(!products.contains(pr)) {
			return false;
		}
		jedis.del(instanceName);
		products.remove(pr);
		jedis.set(instanceName, "OK");
		for (Product p : products) {
			jedis.lpush(instanceName + ":students:strings", p.toString());
		}
		return true;
	}
	
	public static boolean updateProduct() {
		return false;
	}
	
	//Test
	public static void main(String[] args) {
		//writeProducts();
		//deleteProduct("Product0");
		ArrayList<Product> pr = readProducts();
		for(Product p : pr) {
			System.out.println(p);
		}
	}
}
