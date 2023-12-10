package com.github.mbeier1406.gallery;

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

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

/**
 *
 */
@ManagedBean
@ViewScoped
public class GalleryBean implements Serializable {  

	private static final long serialVersionUID = 3893732715971441682L;
	private List<Photo> photos = new ArrayList<>();
    private static final int BUFFER_SIZE = 6124;

    public static final Logger LOGGER = LogManager.getLogger(GalleryBean.class);

    public GalleryBean() throws IOException {      

        Path path = Paths
        		.get(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext())
                .getRealPath(File.separator + "resources" + File.separator + "photos"));
        try ( CloseableThreadContext.Instance ctx = CloseableThreadContext.put("path", path.toString());
        		DirectoryStream<Path> ds = Files.newDirectoryStream(path) ) {
            for (Path file : ds) {
                Photo photo = new Photo(file.getFileName().toString(), false);
                photos.add(photo);
                LOGGER.debug("photo="+photo);
            }
        } catch (IOException ex) {
            LOGGER.error("path="+path, ex);
        }
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void handleFileUpload(FileUploadEvent event) {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        
        if (event.getFile() != null) {
        Path path = Paths.get(((ServletContext) externalContext.getContext())
                    .getRealPath(File.separator + "resources" + File.separator + "photos" + File.separator));
            FileOutputStream fileOutputStream;
            InputStream inputStream;
            try {
                String fn = event.getFile().getFileName();
                fileOutputStream = new FileOutputStream(path.toString() + File.separator + fn);

                byte[] buffer = new byte[BUFFER_SIZE];

                int bulk;
                inputStream = event.getFile().getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }

                fileOutputStream.close();
                inputStream.close();

                Photo new_photo = new Photo(fn, false);
                photos.add(new_photo);

                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Files were successfully uploaded !", null));
            } catch (FileNotFoundException ex) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File " + event.getFile().getFileName() + " cannot be found!", null));
                LOGGER.error("path="+path, ex);
            } catch (IOException ex) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File " + event.getFile().getFileName() + " cannot be uploaded!", null));
                LOGGER.error("path="+path, ex);
            }
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File does not exist!", null));
            LOGGER.error("event="+event+": Datei nicht vorhanden!");
        }
        
    }
    
}
