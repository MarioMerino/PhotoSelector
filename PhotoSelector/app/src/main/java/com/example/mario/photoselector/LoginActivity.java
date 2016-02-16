package com.example.mario.photoselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.photoselector.bd.PhotoSelectorDatabase;

public class LoginActivity extends Activity {

    // Componentes de la interfaz del Login
    Button btnSesion;
    TextView btnRegister;
    EditText edtMail;
    EditText edtPass;
    CheckBox checkPass;

    // Declarar instancia de la clase de la BD de Usuarios (Id, nombreUsuario, email, contraseña)
    PhotoSelectorDatabase photoSelectorDatabase;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        // Crear instancia de la BD en SQLite
        photoSelectorDatabase = new PhotoSelectorDatabase(context);
        // Se abre la BD
        photoSelectorDatabase = photoSelectorDatabase.open();

        // Referenciar los componentes
        btnSesion = (Button) findViewById(R.id.btnLogin);
        btnRegister = (TextView) findViewById(R.id.btnRegistrar);
        edtMail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPassword);
        checkPass = (CheckBox) findViewById(R.id.checkPassword);

        // Crear Listener para la interaccion entre botones
        MiListener listener = new MiListener();
        btnSesion.setOnClickListener(listener);
        btnRegister.setOnClickListener(listener);
        checkPass.setOnClickListener(listener);
    }

    public class MiListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String email = edtMail.getText().toString();
            String password1 = edtPass.getText().toString();
            // Se cargan las contraseñas para los respectivos emails registrados en la BD
            Cursor c = photoSelectorDatabase.getUsuariosBD();
            c.moveToFirst();
            boolean loginStatus = false; // Variable de estado, para indicar cuando se puede iniciar sesion
            String user = "";

            if (v.getId() == R.id.btnLogin) {
                do {
                    if(email.equals(c.getString(1)) && password1.equals(c.getString(2))) {
                        loginStatus = true;
                        user = c.getString(0);
                    }
                }while (c.moveToNext()); // Se mueve un cursor por la tabla, para obtener la contraseña correspondiente al email introducido
                // Se comprueba si la contraseña cargada coincide con la contraseña introducida por el usuario
                //if (password1.equals(cargarPassword)) {
                // Si se inicia sesion correctamente...
                if(loginStatus) {
                    Toast.makeText(LoginActivity.this, "¡Bienvenido "+ user +"!", Toast.LENGTH_LONG).show();
                    // Pasar todos los datos de la tabla Usuario mediante un objeto Bundle, para usarlos en proximas activities (como Modificar Contraseña)
                    /*Intent datosUpdate = new Intent("update_filter");
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", user);
                    bundle.putString("userMail", email);
                    bundle.putString("userPass", password1);
                    datosUpdate.putExtras(bundle);
                    startActivity(datosUpdate);*/
                    // Si el login es correcto, pasa a la siguiente pantalla
                    Intent intentLogin = new Intent(LoginActivity.this, FoldersActivity.class);
                    startActivity(intentLogin);

                    // Cerramos la actividad para que el usuario no pueda regresar a esta actividad presionando el boton Atrás
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesión. Email o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            } else if(v.getId() == R.id.btnRegistrar){
                // Si se pulsa el botón de registrarse, se accede a la activity de registro
                Intent intentRegistrar = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegistrar);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Se cierra la BD
        photoSelectorDatabase.close();
    }
}
