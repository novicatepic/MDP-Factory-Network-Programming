package org.unibl.etf.mdp.rest;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.unibl.etf.mdp.product.Product;
import org.unibl.etf.mdp.product.ProductService;

@Path("/products")
public class RestProduct {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		ArrayList<Product> products = ProductService.readProducts();
		return Response.ok().entity(products).build();
	}
}
