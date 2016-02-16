package com.example.mario.photoselector;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mario.photoselector.bd.PhotoSelectorDatabase;

/**
 * Created by Mario on 15/02/2016.
 */
public class FolderGallery extends AppCompatActivity {

    PhotoSelectorDatabase photoSelectorDatabase = null;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foldergallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Crear instancia de la BD en SQLite
        photoSelectorDatabase = new PhotoSelectorDatabase(context);
        // Se abre la BD
        photoSelectorDatabase = photoSelectorDatabase.open();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Definir accion para regresar a la activity hija (Volver Atrás)
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Se cierra la BD
        photoSelectorDatabase.close();
    }
}
