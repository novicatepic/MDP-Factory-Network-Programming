package org.unibl.etf.mdp.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.unibl.etf.mdp.model.Product;

public interface DistributorInterface extends Remote {

	public ArrayList<Product> getDistributorProducts(String name) throws RemoteException;
	
}
