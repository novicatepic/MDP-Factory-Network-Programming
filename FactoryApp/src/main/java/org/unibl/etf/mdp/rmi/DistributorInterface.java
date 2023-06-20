package org.unibl.etf.mdp.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.mdp.product.Product;

public interface DistributorInterface extends Remote {

	public List<Product> getDistributorProducts(String name) throws RemoteException;
	
}
