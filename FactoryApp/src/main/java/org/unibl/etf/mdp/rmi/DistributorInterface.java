package org.unibl.etf.mdp.rmi;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.mdp.buyer.model.Product;

public interface DistributorInterface extends Remote {
	//Get all the products from the distributor
	public List<Product> getDistributorProducts(String name) throws RemoteException;
	//After we got the products from the distributor, write updated products back
	public void writeUpdatedProducts(List<Product> products) throws RemoteException, IOException;
	
	public File[] listFiles() throws RemoteException;
}
