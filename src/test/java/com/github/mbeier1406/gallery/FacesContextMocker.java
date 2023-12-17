package com.github.mbeier1406.gallery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * Mockt den FacesContext und gibt diesen zurück. Dabei wird die aktuelle Instance aus
 * {@link FacesContext#getCurrentInstance()} auf die gemockte Referenz gesetzt.
 * Diese Referenz wird zurückgegeben, so dass weiteres Mock-Verhalten hinzugefügt werden kann.
 * Mittels der Methode {@link FacesContext#release()} kann die Referenz wieder
 * aus {@link FacesContext#getCurrentInstance()} entfernt werden.
 * @return der gemockte {@link FacesContext}
 */
public abstract class FacesContextMocker extends FacesContext {

	public static FacesContext mockFacesContext() {
		FacesContext context = mock(FacesContext.class);
		ExternalContext externalContext = mock(ExternalContext.class);
		ServletContext servletContext = mock(ServletContext.class);
		when(context.getExternalContext()).thenReturn(externalContext);
		when(externalContext.getContext()).thenReturn(servletContext);
		when(servletContext.getRealPath(any())).thenReturn(".");
		FacesContext.setCurrentInstance(context);
		doAnswer(invocation -> {
			FacesContext.setCurrentInstance(null);
			return null;
		}).when(context).release();
		return context;
	}

}
