package com.example.mario.photoselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.photoselector.bd.PhotoSelectorDatabase;

/**
 * Created by Mario on 03/02/2016.
 */
public class RegisterActivity extends Activity {

    // Componentes de la interfaz del Registro
    Button btnSave, btnCancel;
    EditText edtUser, edtMailRegister, edtPass1, edtPass2;

    // Declarar instancia de la clase de la BD
    PhotoSelectorDatabase photoSelectorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        // Crear instancia de la BD en SQLite
        photoSelectorDatabase = new PhotoSelectorDatabase(this);
        // Se abre la BD
        photoSelectorDatabase = photoSelectorDatabase.open();

        // Referenciar componentes de la interfaz
        btnSave = (Button) findViewById(R.id.btnGuardar);
        btnCancel = (Button) findViewById(R.id.btnCancelar);
        edtUser = (EditText) findViewById(R.id.edtUser1);
        edtMailRegister = (EditText) findViewById(R.id.edtEmail1);
        edtPass1 = (EditText) findViewById(R.id.edtPassword1);
        edtPass2 = (EditText) findViewById(R.id.edtPassword2);

        // Crear Listener para la interaccion entre botones
        MiListener listener = new MiListener();
        btnSave.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);
    }

    private class MiListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String user = edtUser.getText().toString();
            String mail = edtMailRegister.getText().toString();
            String password1 = edtPass1.getText().toString();
            String password2 = edtPass2.getText().toString();
            // Intents para enlazar a las diferentes activities desde RegisterActivity
            if (v.getId() == R.id.btnGuardar) {
                // Se comprueba que ninguno de los campos se encuentra vacio
                if (user.equals("") || mail.equals("") || password1.equals("") || password2.equals("")) {
                    Toast.makeText(getApplicationContext(), "Compruebe que los campos no se encuentran vacíos", Toast.LENGTH_LONG).show();
                    return;
                }
                // Se comprueba que ambas contraseñas (contraseña introducida y su confirmación posterior) coindiden
                if (!password1.equals(password2)) {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //Se guardan los datos en la BD
                    photoSelectorDatabase.insertUsuariosBD(user, mail, password1);
                    Toast.makeText(getApplicationContext(), "¡Su cuenta ha sido registrada con éxito!", Toast.LENGTH_LONG).show();
                    // Una vez guardados los datos en la BD, se puede acceder a la siguiente activity
                    Intent intentRegister = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intentRegister);
                    // Cerramos la actividad para que el usuario no pueda regresar a esta actividad presionando el boton Atrás
                    finish();
                }
            } else if (v.getId() == R.id.btnCancelar) {
                // Si se pulsa el boton de Cancelar, se retrocede al Login y no se procede con el registro
                Intent intentCancel = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentCancel);
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
