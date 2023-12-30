package com.github.mbeier1406.gallery;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static jakarta.ws.rs.core.Response.Status.OK;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
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

	public static final Logger LOGGER = LogManager.getLogger(GalleryResource.class);

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
	@Path("path")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadPath() {
		return photoPath;
	}

	/**
	 * Lädt ein Foto per REST PUT hoch.
	 * @param fileName Dateiname
	 * @param fileContent das Foto
	 * @return den HTTP-Status
	 */
	@Path("file/{fileName}")
	@PUT
	public Response upload(@PathParam("fileName") String fileName, byte[] fileContent) {
		jakarta.ws.rs.core.Response.Status httpStatus;
		try ( CloseableThreadContext.Instance ctx = CloseableThreadContext.put("photoPath", photoPath) ) {
			LOGGER.info("Name: {}; Bytes: {}", fileName, fileContent.length);
			java.nio.file.Path path = Paths.get(galleryBean.getPhotoPath(photoPath).toString(), fileName);
			LOGGER.info("path={}", path);
			Files.write(path, fileContent, CREATE_NEW);
			httpStatus = OK;
		}
		catch ( Exception e ) {
			LOGGER.error("Path: {}; Name: {}; Bytes: {}", photoPath, fileName, fileContent.length, e);			
			httpStatus = INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpStatus).build();
	}

}
