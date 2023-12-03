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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 */
@ManagedBean
@ViewScoped
public class GalleryBean implements Serializable {  
    
    private List<Photo> photos = new ArrayList<>();
    private static final int BUFFER_SIZE = 6124;

    /**
     * Creates a new instance of MyGalleryBean
     */
    public GalleryBean() {      
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Path path = Paths.get(((ServletContext) externalContext.getContext())
                .getRealPath(File.separator + "resources" + File.separator + "photos"));
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            for (Path file : ds) {
                Photo photo = new Photo(file.getFileName().toString(), false);
                photos.add(photo);
            }
        } catch (IOException ex) {
            Logger.getLogger(GalleryBean.class.getName()).log(Level.SEVERE, null, ex);
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
                inputStream = event.getFile().getInputStream();
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
                Logger.getLogger(GalleryBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File " + event.getFile().getFileName() + " cannot be uploaded!", null));
                Logger.getLogger(GalleryBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File does not exist!", null));
            Logger.getLogger(GalleryBean.class.getName()).log(Level.SEVERE, "File does not exist!", "File does not exist!");
        }
        
    }

    private UploadedFile file;
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    public void upload() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        if (file != null) {
            Path path = Paths.get(((ServletContext) externalContext.getContext())
                    .getRealPath(File.separator + "resources" + File.separator + "photos" + File.separator));
            FileOutputStream fileOutputStream;
            InputStream inputStream;
            try {
                String fn = file.getFileName();
                fileOutputStream = new FileOutputStream(path.toString() + File.separator + fn);

                byte[] buffer = new byte[BUFFER_SIZE];

                int bulk;
                inputStream = file.getInputStream();
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
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File " + file.getFileName() + " cannot be found!", null));
                Logger.getLogger(GalleryBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File " + file.getFileName() + " cannot be uploaded!", null));
                Logger.getLogger(GalleryBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}
