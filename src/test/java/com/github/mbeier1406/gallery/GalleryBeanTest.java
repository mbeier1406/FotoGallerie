package com.github.mbeier1406.gallery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.nio.file.Path;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Tests für die Klasse {@linkplain GalleryBean}.
 * @author mbeier
 */
public class GalleryBeanTest {

    public static final Logger LOGGER = LogManager.getLogger(GalleryBean.class);

    /** Der Pfad, der von {@linkplain #testeGetPhotoPath()} geliefert ist {@value} */
    public static final String PATH = ".";

    @Rule
    public MockitoRule mckitoRule = MockitoJUnit.rule();

    @Mock
    public ServletContext servletContext;

    @Mock
    public ExternalContext externalContext;

    @Spy
    @InjectMocks
    public GalleryBean galleryBean;

    @BeforeClass
    public static void initClass() {
    	FacesContextMocker.mockFacesContext();    	
    }

    @Before
    public void init() {
    	when(servletContext.getRealPath(Mockito.any())).thenReturn(PATH);
    	when(externalContext.getContext()).thenReturn(servletContext);
    }

    @Test
    public void testeGetPhotoPath() {
    	Path photoPath = galleryBean.getPhotoPath(externalContext);
    	LOGGER.info("photoPath={}", photoPath);
    	assertThat(photoPath, equalTo(Path.of(PATH)));
    }

}
