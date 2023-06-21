package org.unibl.etf.mdp.model;

import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.mdp.product.Product;

public class Order {

	private String userName;
	private String address;
	private List<Product> products = new ArrayList<>();
	private ArrayList<Integer> amounts = new ArrayList<>();
		
	public Order() {
		super();
	}
	
	public Order(String userName, String address, List<Product> products, ArrayList<Integer> amounts) {
		super();
		this.userName = userName;
		this.address = address;
		this.products = products;
		this.amounts = amounts;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	public ArrayList<Integer> getAmounts() {
		return amounts;
	}
	public void setAmounts(ArrayList<Integer> amounts) {
		this.amounts = amounts;
	}
	
	
	
}
