package com.example.mario.photoselector.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mario on 04/02/2016.
 */
public class PhotoSelectorDatabase {

    public static final String nameDB = "bdPhotoSelector.db"; // Declaracion del nombre de la BD
    public static final int versionDB = 1; // Version actual de la BD

    // Clases privadas para establecer los atributos de las distintas tablas de la BD
    public class usuariosDBInfo {

        public static final String TABLE_NAME = "Usuario";
        public static final String IDUSUARIO_COLUMN = "idUsuario";
        public static final String USER_COLUMN = "nombreUsuario";
        public static final String EMAIL_COLUMN = "emailUsuario";
        public static final String PASS_COLUMN = "passwordUsuario";
    }

    private class fotosDBInfo {
        private static final String TABLE_NAME = "Foto";
        private static final String IDFOTOS_COLUMN = "idFoto";
        private static final String FOTO_COLUMN = "nombreFoto";
        private static final String VALIDA_COLUMN = "valida";
    }

    private class carpetasDBInfo {
        private static final String TABLE_NAME = "Carpeta";
        private static final String IDCARPETA_COLUMN = "idCarpeta";
        private static final String CARPETA_COLUMN = "nombreCarpeta";
        private static final String FECHACREACION_COLUMN = "fechaCreacion";
    }

    private class usuarioCarpetaDBInfo {
        private static final String TABLE_NAME = "Usuario_Has_Carpeta";
    }

    // Declaracion de la constante SQL_CREATE_USUARIO que contiene el script de creacion de la tabla Usuario en la BD
    public static final String SQL_CREATE_USUARIO = "CREATE TABLE " + usuariosDBInfo.TABLE_NAME +
            " ( " + usuariosDBInfo.IDUSUARIO_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + usuariosDBInfo.USER_COLUMN +
            " TEXT NOT NULL, " + usuariosDBInfo.EMAIL_COLUMN + " TEXT NOT NULL, " + usuariosDBInfo.PASS_COLUMN +
            " TEXT NOT NULL); ";

    // Declaracion de la constante SQL_CREATE_FOTOS que contiene el script de creacion de la tabla Fotos en la BD
    public static final String SQL_CREATE_FOTOS = "CREATE TABLE " + fotosDBInfo.TABLE_NAME +
            " ( " + fotosDBInfo.IDFOTOS_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + fotosDBInfo.FOTO_COLUMN +
            " TEXT NOT NULL " + fotosDBInfo.VALIDA_COLUMN + " INTEGER, " + "FOREIGN KEY (" + fotosDBInfo.IDFOTOS_COLUMN +
            ") REFERENCES " + usuariosDBInfo.TABLE_NAME + " (" + usuariosDBInfo.IDUSUARIO_COLUMN + "), " +
            "FOREIGN KEY (" + fotosDBInfo.IDFOTOS_COLUMN + ") REFERENCES " + carpetasDBInfo.TABLE_NAME +
            "(" + carpetasDBInfo.IDCARPETA_COLUMN + ")); ";

    // Declaracion de la constante SQL_CREATE_CARPETA que contiene el script de creacion de la tabla Carpeta en la BD
    public static final String SQL_CREATE_CARPETA = "CREATE TABLE " + carpetasDBInfo.TABLE_NAME +
            " ( " + carpetasDBInfo.IDCARPETA_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + carpetasDBInfo.CARPETA_COLUMN +
            " TEXT NOT NULL, " + carpetasDBInfo.FECHACREACION_COLUMN + " TEXT NOT NULL); ";

    // Declaracion de la constante SQL_CREATE_USUARIOCARPETA que contiene el script de creacion de la tabla UsuarioHasCarpeta en la BD
    public static final String SQL_CREATE_USUARIOCARPETA = "CREATE TABLE " + usuarioCarpetaDBInfo.TABLE_NAME +
            " ( " + usuariosDBInfo.IDUSUARIO_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + carpetasDBInfo.IDCARPETA_COLUMN +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + "FOREIGN KEY (" + usuariosDBInfo.IDUSUARIO_COLUMN +
            ") REFERENCES " + usuariosDBInfo.TABLE_NAME + " (" + usuariosDBInfo.IDUSUARIO_COLUMN + "), " +
            "FOREIGN KEY (" + carpetasDBInfo.IDCARPETA_COLUMN + ") REFERENCES " + carpetasDBInfo.TABLE_NAME +
            "(" + carpetasDBInfo.IDCARPETA_COLUMN + ")); ";

    // Declaracion de la variable para instanciar la BD
    public static SQLiteDatabase db;

    // Declaracion del contexto de la aplicacion que usa la BD
    private final Context context;

    // Variable para abrir/actualizar el helper de la BD
    private DatabaseHelper dbHelper;

    public PhotoSelectorDatabase(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context, nameDB, null, versionDB);
    }

    // Se abre la BD para poder escribir en ella
    public PhotoSelectorDatabase open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Se cierra la BD
    public void close() {
        db.close();
    }

    // Método para obtener la dupla email/contraseña para validar al usuario que ha iniciado sesión
    public Cursor getSingleEntry() {
        db = dbHelper.getReadableDatabase();
        String[] columnasUsuario = {usuariosDBInfo.USER_COLUMN, usuariosDBInfo.EMAIL_COLUMN, usuariosDBInfo.PASS_COLUMN};
        Cursor cursor = db.query(usuariosDBInfo.TABLE_NAME, columnasUsuario, null, null, null, null, null);
        return cursor;
        /*Cursor c = db.query(usuariosDBInfo.TABLE_NAME, null, usuariosDBInfo.EMAIL_COLUMN + "=?", new String[]{email}, null, null, null);
        if (c.getCount() < 1) {
            c.close();
            return "El email introducido no es correcto";
        } else {
            c.moveToFirst(); // Se mueve el cursor al inicio para obtener la contraseña correspondiente al email introducido
            String user = c.getString(c.getColumnIndex(usuariosDBInfo.USER_COLUMN));
            String password = c.getString(c.getColumnIndex(usuariosDBInfo.PASS_COLUMN));
            Toast.makeText(context, "¡Bienvenido " + user + "!", Toast.LENGTH_LONG).show();
            c.close();
            return password;
        }*/

    }

    // Método para guardar los valores insertados en la BD
    public void insertEntry(String user, String mail, String password) {
        ContentValues cValues = new ContentValues();
        // Asignar valores para cada fila de la tabla
        cValues.put(usuariosDBInfo.USER_COLUMN, user);
        cValues.put(usuariosDBInfo.EMAIL_COLUMN, mail);
        cValues.put(usuariosDBInfo.PASS_COLUMN, password);
        // Se inserta la nueva fila de usuario en la tabla
        db.insert(usuariosDBInfo.TABLE_NAME, null, cValues);
    }

    // Método para actualizar los valores de la BD
    public void updateEntry(String userName, String userMail, String userPassword, String newPassword) {
        String consulta = usuariosDBInfo.USER_COLUMN + " = ? AND " + usuariosDBInfo.EMAIL_COLUMN + " = ? AND " + usuariosDBInfo.PASS_COLUMN + " = ?";
        String args[] = {userName, userMail, userPassword};
        ContentValues cValues = new ContentValues();
        // Asignar valores para cada fila de la tabla
        //cValues.put(usuariosDBInfo.USER_COLUMN, user);
        cValues.put(usuariosDBInfo.PASS_COLUMN, newPassword);
        // Se lleva a cabo la actualizacion de la BD
        db.update(usuariosDBInfo.TABLE_NAME, cValues, consulta, args);
    }

    // Método para eliminar registros de la BD
    public int deleteEntry(String idUsuario) {
        int numberOfEntriesDeleted = db.delete(usuariosDBInfo.TABLE_NAME, usuariosDBInfo.IDUSUARIO_COLUMN + "=?", new String[]{idUsuario});
        return numberOfEntriesDeleted;
    }
}
