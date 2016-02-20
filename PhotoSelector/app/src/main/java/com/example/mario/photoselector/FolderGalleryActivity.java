package com.example.mario.photoselector;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.mario.photoselector.bd.PhotoSelectorDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mario on 15/02/2016.
 */
public class FolderGalleryActivity extends AppCompatActivity {

    PhotoSelectorDatabase photoSelectorDatabase = null;
    Context context = this;
    public static PhotosAdapter adaptadorFotos;
    static GridView listaFotos;
    private FloatingActionButton btnPhotoCamera;
    private FloatingActionButton btnPhoto;
    private Button btnVotacion;
    //public static ArrayList<Photo> listaPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foldergallery);

        // Crear instancia de la BD en SQLite
        photoSelectorDatabase = new PhotoSelectorDatabase(context);
        // Se abre la BD
        photoSelectorDatabase = photoSelectorDatabase.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Fotos de la carpeta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaFotos = (GridView) findViewById(R.id.gridPhotos);
        adaptadorFotos = new PhotosAdapter(context, R.layout.imagen_gridview); // Revisar constructor (añadir o no parámetro de ArrayList)
        listaFotos.setAdapter(adaptadorFotos);

        // Al hacer click en cada fila del gridView (carpeta)...
        listaFotos.setOnItemClickListener(onListClick);

        btnPhotoCamera = (FloatingActionButton) findViewById(R.id.btnAddPhotoCamera);
        btnPhotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int CAMERA_REQUEST = 1888;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        btnPhoto = (FloatingActionButton) findViewById(R.id.btnAddPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int SELECT_FILE = 1;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Seleccionar foto"), SELECT_FILE);
            }
        });

        btnVotacion = (Button) findViewById(R.id.btnRealizarVotacion);
        btnVotacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FolderGalleryActivity.this, SwipePhotosActivity.class);
                //intent.putExtra("photosGallery", PhotosAdapter.listaPhotos);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int CAMERA_REQUEST = 1888;
        final int SELECT_FILE = 1;
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Uri selectedImageUri = data.getData();
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Photo p = new Photo();
                p.setFoto(thumbnail);
                p.setNombreFoto(selectedImageUri.fromFile(destination));
                adaptadorFotos.addPhotos(p);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                Photo p = new Photo();
                p.setFoto(bm);
                p.setNombreFoto(selectedImageUri);
                adaptadorFotos.addPhotos(p);
            }

        }
    }

    public AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Photo foto = (Photo) parent.getItemAtPosition(position);

            Intent intent = new Intent(FolderGalleryActivity.this, PhotoDetailsActivity.class);
            intent.putExtra("nombreFoto", foto.getNombreFoto().toString());
            intent.putExtra("imagenFoto", foto.getFoto());

            startActivity(intent);
        }
    };

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
