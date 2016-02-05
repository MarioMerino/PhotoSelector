package com.example.mario.photoselector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mario on 04/02/2016.
 */
public class LoginDatabase {

    private static final String nameDB = "usuarios.db"; // Declaracion del nombre de la BD
    private static final int versionDB = 1; // Version actual de la BD

    // Clase privada para establecer los atributos de la tabla de la BD implicada
    private class usuariosDBInfo {

        private static final String TABLE_NAME = "Usuarios";
        private static final String ID_COLUMN = "usuarioID";
        private static final String USER_COLUMN = "nombreUsuario";
        private static final String EMAIL_COLUMN = "emailUsuario";
        private static final String PASS_COLUMN = "passwordUsuario";
    }

    // Declaracion de la variable SQL_CREATE que contiene el script de creación de las tablas implicadas en la BD
    public static final String SQL_CREATE = "CREATE TABLE " + usuariosDBInfo.TABLE_NAME +
            " ( " + usuariosDBInfo.ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + usuariosDBInfo.USER_COLUMN +
            " VARCHAR(50), " + usuariosDBInfo.EMAIL_COLUMN + " VARCHAR(50), " + usuariosDBInfo.PASS_COLUMN +
            " TEXT); ";

    // Declaracion de la variable para instanciar la BD
    public static SQLiteDatabase db;

    // Declaracion del contexto de la aplicacion que usa la BD
    private final Context context;

    // Variable para abrir/actualizar el helper de la BD
    private DatabaseHelper dbHelper;

    public LoginDatabase(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context, nameDB, null, versionDB);
    }

    // Se abre la BD para poder escribir en ella
    public LoginDatabase open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Se cierra la BD
    public void close() {
        db.close();
    }

    // Método para obtener la dupla email/contraseña para validar al usuario que ha iniciado sesión
    public String getSingleEntry(String email) {
        Cursor c = db.query(usuariosDBInfo.TABLE_NAME, null, usuariosDBInfo.EMAIL_COLUMN + "=?", new String[]{email}, null, null, null);
        if (c.getCount() < 1) {
            c.close();
            return "El email introducido no es correcto";
        }
        c.moveToFirst(); // Se mueve el cursor al inicio para obtener la contraseña correspondiente al email introducido
        String password = c.getString(c.getColumnIndex(usuariosDBInfo.PASS_COLUMN));
        c.close();
        return password;
    }

    // Método para guardar los valores insertados en la BD
    public void insertEntry(String user, String mail, String password1) {
        ContentValues cValues = new ContentValues();
        // Asignar valores para cada fila de la tabla
        cValues.put(usuariosDBInfo.USER_COLUMN, user);
        cValues.put(usuariosDBInfo.EMAIL_COLUMN, mail);
        cValues.put(usuariosDBInfo.PASS_COLUMN, password1);
        // Se inserta la nueva fila de usuario en la tabla
        db.insert(usuariosDBInfo.TABLE_NAME, null, cValues);
    }

    // Método para actualizar los valores de la BD
    public void updateEntry(String user, String mail, String password1) {
        ContentValues cValues = new ContentValues();
        // Asignar valores para cada fila de la tabla
        cValues.put(usuariosDBInfo.USER_COLUMN, user);
        cValues.put(usuariosDBInfo.EMAIL_COLUMN, mail);
        cValues.put(usuariosDBInfo.PASS_COLUMN, password1);
        // Se lleva a cabo la actualizacion de la BD
        db.update(usuariosDBInfo.TABLE_NAME, cValues, usuariosDBInfo.USER_COLUMN + "=?", new String[]{user});
    }

    // Método para eliminar registros de la BD
    public int deleteEntry(String user) {
        int numberOfEntriesDeleted = db.delete(usuariosDBInfo.TABLE_NAME, usuariosDBInfo.USER_COLUMN + "=?", new String[]{user});
        return numberOfEntriesDeleted;
    }
}
