package com.github.mbeier1406.gallery;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("rest")
public class RestResource {

	@Inject
	@ConfigProperty(name = "photoPath")
	String photoPath;

	@Path("upload")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String rest() {
		return "";
	}

}
