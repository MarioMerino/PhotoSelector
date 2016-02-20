package com.example.mario.photoselector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;

/**
 * Created by Mario on 20/02/2016.
 */
public class FoldersVotesResultsActivity extends AppCompatActivity {

    Context context = this;
    public static FoldersAdapter adaptador;
    private Date fechaCarpeta;
    static ListView listaCarpetas;
    public static int editarPosicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foldersvotesresults);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Resultados de votaciones");

        listaCarpetas = (ListView) findViewById(R.id.listViewResults);
        adaptador = new FoldersAdapter(context);

        // Al hacer click en cada fila del listView (carpeta)...
        listaCarpetas.setOnItemClickListener(onListClick);

    }

    public AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Acceso al contenido de la carpeta
            editarPosicion = position;
            //Intent intentFolder = new Intent(context, SubfoldersResultsActivity.class);
            //context.startActivity(intentFolder);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_foldersresults, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Definir acciones para los botones de la Action Bar
        switch (item.getItemId()) {
            case R.id.action_goback:
                goToFoldersList();
                return true;
            case R.id.action_logout:
                closeSession();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void goToFoldersList() {
        Intent intent = new Intent(FoldersVotesResultsActivity.this, FoldersActivity.class);
        startActivity(intent);
    }

    private void closeSession() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // Titulo del AlertDialog
        alertDialogBuilder.setTitle("¿Seguro que desea cerrar sesión?");

        alertDialogBuilder
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentLogin =new Intent(FoldersVotesResultsActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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
}
