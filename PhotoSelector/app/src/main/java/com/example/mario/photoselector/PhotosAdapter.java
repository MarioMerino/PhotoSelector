package com.example.mario.photoselector;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mario on 16/02/2016.
 */
public class PhotosAdapter extends BaseAdapter {

    private Context context;
    private int layoutResourceId;
    public static ArrayList<Photo> listaPhotos;

    public PhotosAdapter(Context context, int resource) {
        this.context = context;
        this.layoutResourceId = resource;
        listaPhotos = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return listaPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPhotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View fila = convertView;
        ViewHolder holder = null;

        if(fila == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            fila = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.tituloImagen = (TextView) fila.findViewById(R.id.txtImageGrid);
            holder.imagen = (ImageView) fila.findViewById(R.id.imageGrid);
            fila.setTag(holder);
        } else {
            holder = (ViewHolder) fila.getTag();
        }

        Photo foto = listaPhotos.get(position);
        holder.tituloImagen.setText(foto.getNombreFoto().toString());
        holder.imagen.setImageBitmap(foto.getFoto());
        return fila;
    }

    public static class ViewHolder {
        TextView tituloImagen;
        ImageView imagen;
    }

    public void addPhotos(Photo p) {
        this.listaPhotos.add(p);
        notifyDataSetChanged();
    }
}
