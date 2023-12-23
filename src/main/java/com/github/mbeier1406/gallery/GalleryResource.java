package com.github.mbeier1406.gallery;

import static jakarta.ws.rs.core.Response.Status.OK;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Ermöglicht das Hochladen von Photos per REST.
 */
@Path("upload")
public class GalleryResource {

	/**
	 * Pfad, unter dem die Fotos gespeichert werden aus
	 * {@code /FotoGallerie/src/main/resources/META-INF/microprofile-config.properties}
	 */
	@Inject
	@ConfigProperty(name = "photoPath")
	String photoPath;

	/**
	 * Liefert den Pfad, unter dem Die Fotos abgelegt werden.
	 * @return den Dateipfad
	 */
	@Path("photo")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadPhoto() {
		return photoPath;
	}

	/**
	 * Lädt ein Foto per REST PUT hoch.
	 * @param fileName Dateiname
	 * @param fileContent das Foto
	 * @return den HTTP-Status
	 */
	@Path("upload")
	@PUT
	public Response upload(@PathParam("fileName") String name, byte[] fileContent) {
		// TODO Implementierung
		return Response.status(OK).build();
	}

}
