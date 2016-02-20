package com.example.mario.photoselector;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Mario on 16/02/2016.
 */
public class Photo {

    private Bitmap foto;
    private Uri nombreFoto;
    private boolean esValidaFoto;

    public Photo() {
        super();
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public Uri getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(Uri nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public boolean isEsValidaFoto() {
        return esValidaFoto;
    }

    public void setEsValidaFoto(boolean esValidaFoto) {
        this.esValidaFoto = esValidaFoto;
    }

}
