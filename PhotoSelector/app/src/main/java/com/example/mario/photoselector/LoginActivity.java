package com.example.mario.photoselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

    //private static final String TAG = "LoginActivity";
    //private static final int REQUEST_SIGNUP = 0;

    // Componentes de la interfaz del Login
    Button btnSesion;
    TextView btnRegister;
    EditText edtMail;
    EditText edtPass;
    CheckBox checkPass;

    // Declarar instancia de la clase de la BD de Usuarios (Id, nombreUsuario, email, contraseña)
    LoginDatabase loginDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        // Crear instancia de la BD en SQLite
        loginDatabase = new LoginDatabase(this);
        // Se abre la BD
        loginDatabase = loginDatabase.open();

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

    private class MiListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String email = edtMail.getText().toString();
            String password = edtPass.getText().toString();
            // Se cargan las contraseñas para los respectivos emails registrados en la BD
            String cargarPassword = loginDatabase.getSingleEntry(email);
            if (v.getId() == R.id.btnLogin) {
                // Se comprueba si las contraseñas cargada coincide con la contraseña introducida por el usuario
                if (password.equals(cargarPassword)) {
                    Toast.makeText(LoginActivity.this, "¡Bienvenido a PhotoSelector!", Toast.LENGTH_LONG).show();
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
        loginDatabase.close();
    }
}
