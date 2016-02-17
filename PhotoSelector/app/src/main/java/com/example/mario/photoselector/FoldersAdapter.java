package com.example.mario.photoselector;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mario on 12/02/2016.
 */
public class FoldersAdapter extends BaseAdapter {

    public static ArrayList<Folder> listaCarpetas;
    private Context contexto;
    public static int editarPosicion;

    public FoldersAdapter(Context contexto) {
        listaCarpetas = new ArrayList<>();
        this.contexto = contexto;
    }

    @Override
    public int getCount() {
        return listaCarpetas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaCarpetas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0l;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView == null) {
            convertView = View.inflate(contexto, R.layout.linea_carpetas, null);
        }

        TextView txtnombre = (TextView) convertView.findViewById(R.id.txtnombrecarpeta);
        TextView txtfecha = (TextView) convertView.findViewById(R.id.txtDate);
        ImageView imageOptions = (ImageView) convertView.findViewById(R.id.btnOpcionesCarpeta);
        imageOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPosicion = position; // Variable para acceder en otras activities al elemento de la listView indicado (y eliminar/actualizar/insertar en él)
                Intent intentOptions = new Intent(contexto, FolderOptionsActivity.class);
                contexto.startActivity(intentOptions);
            }
        });
        Folder f = (Folder) getItem(position);
        txtnombre.setText(f.getNombreCarpeta());
        txtfecha.setText(f.getFechaCarpeta().toString());

        return convertView;
    }

    public void addCarpeta(Folder f) {
        this.listaCarpetas.add(f);
        notifyDataSetChanged();
    }
}
