package com.github.mbeier1406.gallery;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;

/**
 * Die Bean der Anwendung speichert eine Liste der vorhandenen Photos, liest
 * zum Start die vorhandencein und bietet die Möglichkeit, weitere über eine
 * Upload-Funktion hinzuzufügen. Das Verzeichnis ist {@code /FotoGallerie/src/main/webapp/resources/photos}.
 * Die nachträglich geladenen Photos sind also nicht Bestandteil des Deployments und werden bei
 * jedem Update gelöscht (keine Persistierung).
 * @author mbeier
 */
@Named
@ViewScoped
public class GalleryBean implements Serializable {  

    public static final Logger LOGGER = LogManager.getLogger(GalleryBean.class);

	private static final long serialVersionUID = 3893732715971441682L;
	private static List<Photo> listOfPhotos = new ArrayList<>();

	/**
     * Liest die im Deployment vorhandenen Photos in {@linkplain #listOfPhotos} ein.
     * @throws IOException Falls die im Ressourcenordner vorhandenen Photos nicht geladen werden können
     * @see #getPhotoPath(ExternalContext)
     */
    public GalleryBean() throws IOException {      
    	listOfPhotos.clear();
        Path path = getPhotoPath(FacesContext.getCurrentInstance().getExternalContext());
        try ( CloseableThreadContext.Instance ctx = put("path", path.toString());
        	  DirectoryStream<Path> ds = Files.newDirectoryStream(path) ) {
        	ds.forEach(file -> {
                Photo photo = new Photo(file.getFileName().toString(), false);
                listOfPhotos.add(photo);
                LOGGER.debug("photo="+photo);
        	});
        } catch (IOException ex) {
            LOGGER.error("path="+path, ex);
        }
    }

    /**
     * Liefert die Liste der eingeesenen Photos.
     * @return die Liste
     */
    public List<Photo> getPhotos() {
        return listOfPhotos;
    }

    /**
     * Lädt eine Datei hoch in den Ressourcenordner der Anwendung und speichert ein
     * enstprechendes {@linkplain Photo}-Objekt in {@linkplain #listOfPhotos}.
     * @param event enthält die Daten zur hochzuladenden Datei
     * @see #getPhotoPath(ExternalContext)
     */
    public void handleFileUpload(FileUploadEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (event.getFile() != null) {
        	Path path = getPhotoPath(facesContext.getExternalContext());
            String fn = event.getFile().getFileName();
            try ( CloseableThreadContext.Instance ctx = put("path", path.toString()).put("fn", fn);
            	  FileOutputStream fileOutputStream = new FileOutputStream(path.toString() + File.separator + fn);
            	  InputStream inputStream = event.getFile().getInputStream() ) {
                byte[] buffer = new byte[6124];
                int bulk;
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }
                Photo newPhoto = new Photo(fn, false);
                listOfPhotos.add(newPhoto);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dateien hochgeladen!", null));
            } catch (FileNotFoundException ex) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File " + event.getFile().getFileName() + " nicht gefunden!", null));
                LOGGER.error("path={}; event={}", path, event, ex);
            } catch (IOException ex) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File " + event.getFile().getFileName() + " nicht hochgeladen!", null));
                LOGGER.error("path={}; event={}", path, event, ex);
            }
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Datei existiert nicht!", null));
            LOGGER.error("event={}", event+": Datei nicht vorhanden!");
        }
    }

    /**
     * Liefert zum externen Kontext das Verezichnis, in dem sich die Photos befinden.
     * @param externalContext der externe Kontext der Anwendung ({@linkplain FacesContext#getExternalContext()})
     * @return den Pfad im Ressourcenordner
     */
    public Path getPhotoPath(final ExternalContext externalContext) {
        return Paths.get(
        		((ServletContext) externalContext.getContext())
                .getRealPath(File.separator + "resources" + File.separator + "photos" + File.separator));
    }

    /**
     * Fügt der Liste der Fotos ein neues hinzu.
     * @param newPhoto das neue Foto
     */
    public static void addPhoto(final Photo newPhoto) {
    	listOfPhotos.add(newPhoto);
    }

}
