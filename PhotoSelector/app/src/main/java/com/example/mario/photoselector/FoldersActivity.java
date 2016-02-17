package com.example.mario.photoselector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mario.photoselector.bd.PhotoSelectorDatabase;

import java.util.Date;


/**
 * Created by Mario on 09/02/2016.
 */
public class FoldersActivity extends AppCompatActivity {

    // Declarar instancia de la clase de la BD de Usuarios (Id, nombreUsuario, email, contraseña)
    PhotoSelectorDatabase photoSelectorDatabase = null;
    Context context = this;
    public static FoldersAdapter adaptador;
    private FloatingActionButton btnCreateFolder;
    private EditText edtNombreCarpeta;
    String id = null;
    private Date fechaCarpeta;
    static ListView listaCarpetas;

    public static int editarPosicion;

    //Cursor cursorReload;

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

        listaCarpetas = (ListView) findViewById(R.id.listViewFolders);
        //cursorReload = photoSelectorDatabase.getCarpetasDB();
        //startManagingCursor(cursorReload);
        adaptador = new FoldersAdapter(context);
        listaCarpetas.setAdapter(adaptador);

        // Al hacer click en cada fila del listView (carpeta)...
        listaCarpetas.setOnItemClickListener(onListClick);

        // Animacion para el FloatingActionButton al hacer scroll
        listaCarpetas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int btn_initPosY = btnCreateFolder.getScrollY();
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    btnCreateFolder.animate().cancel();
                    btnCreateFolder.animate().translationYBy(150);
                } else {
                    btnCreateFolder.animate().cancel();
                    btnCreateFolder.animate().translationY(btn_initPosY);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        btnCreateFolder = (FloatingActionButton) findViewById(R.id.btnAddFolder);
        btnCreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View addView = getLayoutInflater().inflate(R.layout.nombre_carpetas, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // Titulo del AlertDialog
                alertDialogBuilder.setTitle("Nueva carpeta");

                // Mensaje del Dialog
                //alertDialogBuilder.setMessage("Nombre de carpeta:");

                fechaCarpeta = new Date();
                alertDialogBuilder.setView(addView);
                // Obtener el nombre de carpeta introducido en el Dialog
                edtNombreCarpeta = (EditText) addView.findViewById(R.id.edtNombreCarpeta);

                if (id != null) {
                    // Consulta de la tabla por id
                    Cursor c = photoSelectorDatabase.getById(id);
                    // Mover el cursor a la primera posición, en ese caso
                    while (c.moveToNext()) {
                        // Obtener el texto del nombre y añadirlo al edtNombreCarpeta, junto al tiempo de creacion:
                        edtNombreCarpeta.setText(c.getString(1));
                        fechaCarpeta.setTime(Long.parseLong(c.getString(2)));
                    }

                    if (c != null && !c.isClosed()) {
                        c.close();
                    }
                }

                // Al pulsar el botón aceptar del Dialog:
                alertDialogBuilder
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // if this button is clicked, close
                                // current activity
                                edtNombreCarpeta = (EditText) addView.findViewById(R.id.edtNombreCarpeta);
                                String nombreCarpeta = edtNombreCarpeta.getEditableText().toString();
                                Date fechaCarpeta = new Date();
                                if (id == null) {
                                    //photoSelectorDatabase.insertCarpetasBD(nombreCarpeta, String.valueOf(fechaCarpeta));

                                    Folder f = new Folder();
                                    f.setNombreCarpeta(nombreCarpeta);
                                    f.setFechaCarpeta(fechaCarpeta);
                                    adaptador.addCarpeta(f);
                                }
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

    }

    public AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TextView titleView = (TextView) view.findViewById(R.id.txtnombrecarpeta);
            //String nombreFolder = titleView.getText().toString();
            //Toast.makeText(FoldersActivity.this, nombreFolder, Toast.LENGTH_SHORT).show();
            // Acceso al contenido de la carpeta
            editarPosicion = position;
            Intent intentFolder = new Intent(context, FolderGalleryActivity.class);
            context.startActivity(intentFolder);
        }
    };

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
        Cursor c = photoSelectorDatabase.getUsuariosBD();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Se cierra la BD
        photoSelectorDatabase.close();
    }
}
