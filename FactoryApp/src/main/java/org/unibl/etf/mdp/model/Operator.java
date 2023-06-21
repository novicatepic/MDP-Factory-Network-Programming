package org.unibl.etf.mdp.model;

import java.util.Objects;

public class Operator {

	private String userName;

	public Operator() {
		super();
	}

	public Operator(String userName) {
		super();
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Operator other = (Operator) obj;
		return Objects.equals(userName, other.userName);
	}
}
