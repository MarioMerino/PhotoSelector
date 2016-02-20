package com.example.mario.photoselector;

/**
 * Created by Mario on 20/02/2016.
 */
public class DatosPrueba {

    private String description;

    private String imagePath;

    public DatosPrueba(String imagePath, String description) {
        this.imagePath = imagePath;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}
