package com.example.mario.photoselector;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mario on 17/02/2016.
 */
public class PhotoDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String nombreFoto = getIntent().getStringExtra("nombreFoto");
        Bitmap foto = getIntent().getParcelableExtra("imagenFoto");

        TextView tituloFoto = (TextView) findViewById(R.id.txttitlephoto);
        tituloFoto.setText(nombreFoto);

        ImageView imageFoto = (ImageView) findViewById(R.id.imagephoto);
        imageFoto.setImageBitmap(foto);
    }
}
