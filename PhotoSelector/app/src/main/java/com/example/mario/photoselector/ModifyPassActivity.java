package com.example.mario.photoselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.photoselector.bd.PhotoSelectorDatabase;

/**
 * Created by Mario on 09/02/2016.
 */
public class ModifyPassActivity extends AppCompatActivity{

    // Componentes de la interfaz de Modificar Contraseña
    Button btnSavePass;
    EditText edtActualPwd,edtNewPwd, edtConfirmPwd;

    // Declaracion de los atributos Bundle (para obtener datos de la BD)
    String userName, userMail, userPassword;

    // Declarar instancia de la clase de la BD
    PhotoSelectorDatabase photoSelectorDatabase;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypass);

        // Se recuperan los datos de la tabla Usuario pasados en el Login mediante un objeto Bundle
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");
        userMail = bundle.getString("userMail");
        userPassword = bundle.getString("userPass");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Modificar contraseña");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Crear instancia de la BD en SQLite
        photoSelectorDatabase = new PhotoSelectorDatabase(context);
        // Se abre la BD
        photoSelectorDatabase = photoSelectorDatabase.open();

        // Referenciar componentes de la interfaz
        btnSavePass = (Button) findViewById(R.id.btnGuardarPwd);
        edtActualPwd = (EditText) findViewById(R.id.edtActualPass);
        edtNewPwd = (EditText) findViewById(R.id.edtNewPass);
        edtConfirmPwd = (EditText) findViewById(R.id.edtConfirmPass);

        // Crear Listener para la interaccion entre botones
        MiListener listener = new MiListener();
        btnSavePass.setOnClickListener(listener);

    }

    private class MiListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String actualPass = edtActualPwd.getText().toString();
            String newPass = edtNewPwd.getText().toString();
            String confirmPass = edtConfirmPwd.getText().toString();
            if(v.getId() == R.id.btnGuardarPwd) {
                // Intents para registrar la modificacion de contraseña y regresar a la activity anterior tras guardar
                if (actualPass == null || "".equalsIgnoreCase(actualPass)) {
                    String header = "La contraseña actual es requerida";
                    Toast.makeText(getApplicationContext(), header, Toast.LENGTH_SHORT).show();
                } else if (newPass == null || "".equalsIgnoreCase(newPass)) {
                    String header = "La nueva contraseña es requerida";
                    Toast.makeText(getApplicationContext(), header, Toast.LENGTH_SHORT).show();
                } else if (confirmPass == null || "".equalsIgnoreCase(confirmPass)) {
                    String header = "La confirmación de contraseña, es requerida";
                    Toast.makeText(getApplicationContext(), header, Toast.LENGTH_SHORT).show();
                } else if (!newPass.equalsIgnoreCase(confirmPass)) {
                    String header = "Las contraseñas no coinciden";
                    Toast.makeText(getApplicationContext(), header, Toast.LENGTH_SHORT).show();
                } else {
                    // Se procede con la actualización de los datos de la BD introducidos (actualizar contraseña)
                    photoSelectorDatabase.updateUsuariosBD(userName, userMail, userPassword, newPass);
                    Toast.makeText(ModifyPassActivity.this, "¡La contraseña se actualizó con éxito!", Toast.LENGTH_LONG).show();
                    // Una vez guardados los datos en la BD, se puede acceder a la siguiente activity
                    Intent intentPass = new Intent(ModifyPassActivity.this, FoldersActivity.class);
                    startActivity(intentPass);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Definir accion para regresar a la activity hija (Volver Atrás)
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ModifyPassActivity.this, FoldersActivity.class);
                startActivity(intent);
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
