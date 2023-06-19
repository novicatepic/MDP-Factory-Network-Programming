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

@Path("/register")
public class RestRegistration {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		return null;
		/*try {
			PrintWriter pw = new PrintWriter(new FileWriter("./a.txt"));
			pw.println("cc");
			pw.close();
			System.out.println("Written");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(ProizvodServis.getProizvodi()).build();*/
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProizvod(User u) {
		if(UserService.createUser(u)) {
			return Response.status(201).build();
		} else {
			return Response.status(500).build();
		}
	}
	
}
