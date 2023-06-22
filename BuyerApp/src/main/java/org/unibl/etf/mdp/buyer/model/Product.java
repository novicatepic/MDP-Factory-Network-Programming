package org.unibl.etf.mdp.buyer.model;

import java.util.Objects;

//Each product contains name, amount and price attribute
public class Product {

	private String name;
	private int amount;
	private double price;
		
	public Product() {
		super();
	}
	
	public Product(String name) {
		super();
		this.name = name;
	}

	public Product(String name, int amount, double price) {
		super();
		this.name = name;
		this.amount = amount;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "name=/" + name + "/, amount=/" + amount + "/, price=/" + price+"/";
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
		Product other = (Product) obj;
		return Objects.equals(name, other.name);
	}
	
	
	
}
