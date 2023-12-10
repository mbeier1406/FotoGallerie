package com.github.mbeier1406.gallery;

import java.io.Serializable;

/**
 * Einfacher Container zur 
 */
public class Photo implements Serializable {

	private static final long serialVersionUID = 1857057374045339804L;
	private final String name;
    private boolean selected;

    public Photo(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

	@Override
	public String toString() {
		return "Photo [name=" + name + ", selected=" + selected + "]";
	}

}
