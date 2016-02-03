package com.example.mario.photoselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends Activity {

       //Button btnSesion = (Button) findViewById(R.id.btnLogin);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        TextView btnRegister = (TextView) findViewById(R.id.btnRegistrar);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enlazar al activity del Registro
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /*btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enlazar a la primera activity tras iniciar sesión.
                Intent intent = new Intent(getApplicationContext(), )
            }
        });*/


    }

}
