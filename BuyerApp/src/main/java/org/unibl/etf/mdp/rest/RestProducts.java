package org.unibl.etf.mdp.rest;

import java.io.FileWriter;
import java.io.PrintWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.unibl.etf.mdp.buyer.model.User;
import org.unibl.etf.mdp.buyer.model.UserService;

@Path("/userproducts")
public class RestProducts {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		return null;
	}
}
