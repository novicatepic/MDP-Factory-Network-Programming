package org.unibl.etf.mdp.buyer.model;

import java.util.ArrayList;
import java.util.List;

//Same as BuyerProject order, it has to be in the same module because of XMLEncoder
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

	public String printProductsAndAmounts() {
		String result="";
		result+="ORDER: ";
		//System.out.println("ORDER: ");
		for(int i=0; i<products.size(); i++) {
			result+="Name: " + products.get(i).getName() + " Amount: " + amounts.get(i) + " ";
			/*System.out.print("Name: " + products.get(i).getName() + " Amount: " + amounts.get(i));
			System.out.println();*/
		}
		return result;
	}
	
	@Override
	public String toString() {
		/*return "Order [userName=" + userName + ", address=" + address + ", products=" + products.toArray() + ", amounts="
				+ amounts.toArray() + "]";*/
		return printProductsAndAmounts();
	}
	
	
	
}
