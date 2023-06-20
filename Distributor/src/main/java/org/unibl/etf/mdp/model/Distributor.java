package org.unibl.etf.mdp.model;

import java.util.ArrayList;

public class Distributor {

	private String name;	
	private ArrayList<Product> products = new ArrayList<>();
	
	public Distributor() {
		super();
	}

	public Distributor(String name, ArrayList<Product> products) {
		super();
		this.name = name;
		this.products = products;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}	
	
	public void addProduct(Product product) {
		this.products.add(product);
	}
}
