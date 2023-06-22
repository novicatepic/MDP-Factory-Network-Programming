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

@Path("/login")
public class RestLogin {

	//Return 201 if there are user credentials, else 404
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProizvod(User u) {
		User usr = org.unibl.etf.mdp.user.UserService.checkCredentials(u);
		if(usr != null && !usr.isSuspended()) {
			//User found, 200 status code as a response
			return Response.status(200).entity(usr).build();
		} else if(usr.isSuspended()){
			//Fobidden (no access) if the user is suspended at the given moment
			return Response.status(403).build();
		} else {
			//User not found
			return Response.status(404).build();
		}
	}
}
