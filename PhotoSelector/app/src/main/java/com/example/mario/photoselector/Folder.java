package com.example.mario.photoselector;

import java.util.Date;

/**
 * Created by Mario on 12/02/2016.
 */
public class Folder {

    private String nombreCarpeta;
    private Date fechaCarpeta;

    public String getNombreCarpeta() {
        return nombreCarpeta;
    }

    public void setNombreCarpeta(String nombreCarpeta) {
        this.nombreCarpeta = nombreCarpeta;
    }

    public Date getFechaCarpeta() {
        return fechaCarpeta;
    }

    public void setFechaCarpeta(Date fechaCarpeta) {
        this.fechaCarpeta = fechaCarpeta;
    }
}
