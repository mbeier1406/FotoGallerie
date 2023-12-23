package com.github.mbeier1406.gallery;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

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
	 * @return den Datipfad
	 */
	@Path("photo")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadPhoto() {
		return photoPath;
	}

}
