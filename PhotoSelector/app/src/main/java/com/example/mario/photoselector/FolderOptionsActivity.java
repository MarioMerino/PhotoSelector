package com.example.mario.photoselector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mario.photoselector.bd.PhotoSelectorDatabase;

import java.util.Date;

/**
 * Created by Mario on 13/02/2016.
 */
public class FolderOptionsActivity extends AppCompatActivity {

    // Declarar instancia de la clase de la BD
    PhotoSelectorDatabase photoSelectorDatabase;
    Context context = this;

    private ImageView btnEditarCarpeta;
    private ImageView btnAnadirPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optionsfolder);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Opciones de carpeta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Crear instancia de la BD en SQLite
        photoSelectorDatabase = new PhotoSelectorDatabase(context);
        // Se abre la BD
        photoSelectorDatabase = photoSelectorDatabase.open();

        btnEditarCarpeta = (ImageView) findViewById(R.id.btnEditFolder);
        btnEditarCarpeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View addView = getLayoutInflater().inflate(R.layout.nombre_carpetas, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // Titulo del AlertDialog
                alertDialogBuilder.setTitle("Cambiar nombre de carpeta");
                alertDialogBuilder.setView(addView);

                // Al pulsar el botón aceptar del Dialog:
                alertDialogBuilder
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // if this button is clicked, close
                                // current activity
                                EditText edtNombreCarpeta = (EditText) addView.findViewById(R.id.edtNombreCarpeta);
                                String nombreCarpeta = edtNombreCarpeta.getEditableText().toString();
                                Date fechaCarpeta = new Date();
                                //photoSelectorDatabase.updateCarpetasBD(nombreCarpeta, String.valueOf(fechaCarpeta));
                                int updatePosition = FoldersAdapter.editarPosicion;
                                FoldersAdapter.listaCarpetas.get(updatePosition).setNombreCarpeta(nombreCarpeta);
                                FoldersAdapter.listaCarpetas.get(updatePosition).setFechaCarpeta(fechaCarpeta);
                                FoldersActivity.adaptador.notifyDataSetChanged();
                                //finish();
                            }
                        })
                                // Al pulsar el botón Cancelar del Dialog:
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        btnAnadirPersonas = (ImageView) findViewById(R.id.btnAddPeople);
        btnAnadirPersonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_optionsfolder, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Definir accion para regresar a la activity hija (Volver Atrás)
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                deleteFolder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Definir acción para borrar la carpeta actual desde sus opciones de carpeta
    public void deleteFolder() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // Titulo del AlertDialog
        alertDialogBuilder.setTitle("¿Eliminar carpeta?");

        alertDialogBuilder
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int deletePosition = FoldersAdapter.editarPosicion;
                        FoldersAdapter.listaCarpetas.remove(deletePosition);
                        FoldersActivity.adaptador.notifyDataSetChanged();
                        finish();
                    }
                 })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Se cierra la BD
        photoSelectorDatabase.close();
    }

}
