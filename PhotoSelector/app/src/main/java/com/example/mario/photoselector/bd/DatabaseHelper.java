package com.example.mario.photoselector.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mario on 04/02/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    // Al constructor se le pasa el nombre de la BD y la version actual
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, null, version);
    }

    // Creacion de la BD mediante la ejecución del script SQL que contiene las tablas de la BD
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PhotoSelectorDatabase.SQL_CREATE_USUARIO);
        /*db.execSQL(PhotoSelectorDatabase.SQL_CREATE_FOTOS);
        db.execSQL(PhotoSelectorDatabase.SQL_CREATE_CARPETA);
        db.execSQL(PhotoSelectorDatabase.SQL_CREATE_USUARIOCARPETA);*/
    }

    // Si se cambia la BD entre versiones, debería ejecutarse aquí el script de migración
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Indicación mediante un log de la versión actualizada
        Log.w("TaskDBAdapter", "Actualizando de la versión " + oldVersion + " a " + newVersion + ", lo cual destruirá todos los datos antiguos.");
        // Para actualizar la BD existente, se lleva a cabo la eliminacion de la tabla antigua y se crea una nueva
        db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
        // Se crea la nueva tabla
        onCreate(db);
    }
}
