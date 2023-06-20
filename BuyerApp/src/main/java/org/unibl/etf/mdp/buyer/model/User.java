package org.unibl.etf.mdp.buyer.model;

import java.io.Serializable;

public class User implements Serializable {

	private String companyName;
	private String address;
	private String contactPhone;
	private String userName;
	private String password;
	private boolean suspended=false;
	
	public User() {
		super();
	}

	public User(String companyName, String address, String contactPhone, String userName, String password) {
		super();
		this.companyName = companyName;
		this.address = address;
		this.contactPhone = contactPhone;
		this.userName = userName;
		this.password = password;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
}
