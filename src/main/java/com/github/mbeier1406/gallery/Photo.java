package com.github.mbeier1406.gallery;

import java.io.Serializable;

/**
 *
 */
public class Photo implements Serializable {
    
    private String name;
    private boolean selected;

    public Photo(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    
    
}
