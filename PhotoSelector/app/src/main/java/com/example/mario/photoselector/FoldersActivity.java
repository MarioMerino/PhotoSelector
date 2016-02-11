package com.example.mario.photoselector;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.mario.photoselector.bd.PhotoSelectorDatabase;


/**
 * Created by Mario on 09/02/2016.
 */
public class FoldersActivity extends AppCompatActivity {

    // Declarar instancia de la clase de la BD de Usuarios (Id, nombreUsuario, email, contraseña)
    PhotoSelectorDatabase photoSelectorDatabase;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders_activity);

        // Crear instancia de la BD en SQLite
        photoSelectorDatabase = new PhotoSelectorDatabase(context);
        // Se abre la BD
        photoSelectorDatabase = photoSelectorDatabase.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Lista de carpetas");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_folders, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Definir acciones para los botones de la Action Bar
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_logout:
                closeSession();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void closeSession() {
        Intent intentSession = new Intent(FoldersActivity.this, LoginActivity.class);
        startActivity(intentSession);
        finish();
    }

    public void openSettings() {
        Cursor c = photoSelectorDatabase.getSingleEntry();
        c.moveToFirst();
        String user = c.getString(0);
        String mail = c.getString(1);
        String pass = c.getString(2);
        c.close();
        // Pasar todos los datos de la tabla Usuario mediante un objeto Bundle, para usarlos en ModifyPassActivity
        Intent datosUpdate = new Intent("update_filter");
        Bundle bundle = new Bundle();
        bundle.putString("userName", user);
        bundle.putString("userMail", mail);
        bundle.putString("userPass", pass);
        datosUpdate.putExtras(bundle);
        startActivity(datosUpdate);
        finish();
    }
}
