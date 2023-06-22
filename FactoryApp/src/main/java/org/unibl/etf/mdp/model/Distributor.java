package org.unibl.etf.mdp.model;

import java.util.ArrayList;
import java.util.Objects;
import org.unibl.etf.mdp.buyer.model.Product;

//Each distributor contains a name and a list of products
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

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Distributor other = (Distributor) obj;
		return Objects.equals(name, other.name);
	}
	
	
}
