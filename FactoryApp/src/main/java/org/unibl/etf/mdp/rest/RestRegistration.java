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

import org.unibl.etf.mdp.model.User;

@Path("/register")
public class RestRegistration {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProizvod(User u) {
		if(org.unibl.etf.mdp.user.UserService.createUser(u)) {
			return Response.status(201).build();
		} else {
			return Response.status(500).build();
		}
	}
	
}
