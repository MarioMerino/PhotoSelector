package com.example.mario.photoselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mario.photoselector.swipecard.FlingCardListener;
import com.example.mario.photoselector.swipecard.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 18/02/2016.
 */
public class SwipePhotosActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {

    public static ViewSwipeHolder viewSwipeHolder;
    public static SwipePhotosAdapter swipePhotosAdapter;
    private SwipeFlingAdapterView flingContainer;
    //private ArrayList<Photo> listPhotos;
    private ArrayList<DatosPrueba> listDatosPrueba;
    Context context = this;

    public static void removeBackground() {
        viewSwipeHolder.background.setVisibility(View.GONE);
        swipePhotosAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipephotos);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        //listPhotos = getIntent().getExtras().getStringArrayList("photosGallery");
        //swipePhotosAdapter = new SwipePhotosAdapter(listPhotos, SwipePhotosActivity.this);

        // Ejemplo de prueba del funcionamiento del sistema de votaciones de fotos de la galería... :
        listDatosPrueba = new ArrayList<>();
        listDatosPrueba.add(new DatosPrueba("http://www.forodefotos.com/attachments/naturaleza/16230d1294773821-fotos-de-paisajes-fotos-de-paisajes-nevado.jpg", "Foto1"));
        listDatosPrueba.add(new DatosPrueba("http://www.paisajesimagenes.com/wp-content/uploads/Paisajes-preciosos..jpg", "Foto2"));
        listDatosPrueba.add(new DatosPrueba("http://www.paisajesimagenes.com/wp-content/uploads/Clases-de-paisajes.jpg", "Foto3"));
        listDatosPrueba.add(new DatosPrueba("http://2.bp.blogspot.com/-J3pxJQmRNaw/TmVDyc7Ux7I/AAAAAAAALQk/MJCGmM8HPVE/s1600/Paisaje-m%25C3%25A1gico.jpg", "Foto4"));
        listDatosPrueba.add(new DatosPrueba("http://fotosmundo.net/fotos/paisajes/paisaje-islandia-1.jpg", "Foto5"));

        swipePhotosAdapter = new SwipePhotosAdapter(listDatosPrueba, SwipePhotosActivity.this);
        flingContainer.setAdapter(swipePhotosAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                listDatosPrueba.remove(0);
                swipePhotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                listDatosPrueba.remove(0);
                swipePhotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if(listDatosPrueba.isEmpty()){
                    Intent intentResults = new Intent(SwipePhotosActivity.this, FoldersVotesResultsActivity.class);
                    startActivity(intentResults);
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                swipePhotosAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onActionDownPerform() {
        Log.e("action", "bingo");
    }

    public static class ViewSwipeHolder {
        public static FrameLayout background;
        //public TextView DataText;
        public ImageView cardImage;
    }

    public class SwipePhotosAdapter extends BaseAdapter {

        public Context context;
        //public ArrayList<Photo> listaSwipePhotos;
        public List<DatosPrueba> listaSwipePhotos;

        private SwipePhotosAdapter(ArrayList<DatosPrueba> listPhotos, Context contexto) {
            this.listaSwipePhotos = listPhotos;
            this.context = contexto;
        }

        @Override
        public int getCount() {
            return listaSwipePhotos.size();
        }

        @Override
        public Object getItem(int position) {
            return listaSwipePhotos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View fila = convertView;

            if (fila == null) {
                LayoutInflater inflater = getLayoutInflater();
                fila = inflater.inflate(R.layout.photocard, parent, false);

                viewSwipeHolder = new ViewSwipeHolder();
                //viewSwipeHolder.DataText = (TextView) fila.findViewById(R.id.bookText);
                viewSwipeHolder.background = (FrameLayout) fila.findViewById(R.id.background);
                viewSwipeHolder.cardImage = (ImageView) fila.findViewById(R.id.cardImage);
                fila.setTag(viewSwipeHolder);
            } else {
                viewSwipeHolder = (ViewSwipeHolder) convertView.getTag();
            }

            //viewSwipeHolder.DataText.setText(listaSwipePhotos.get(position).getDescription() + "");

            Glide.with(SwipePhotosActivity.this).load(listaSwipePhotos.get(position).getImagePath()).into(viewSwipeHolder.cardImage);
            //viewSwipeHolder.DataText.setText(listaSwipePhotos.get(position).getNombreFoto().toString() + "");
            //Glide.with(SwipePhotosActivity.this).load(listaSwipePhotos.get(position).getFoto()).into(viewSwipeHolder.cardImage); // Posiblemente haya que modificar más adelante .with(context) por .with(SwipePhotosActivity.class)

            return fila;
        }

        /*public void addSwipePhotos(Photo p) {
            this.listaSwipePhotos.add(p);
            notifyDataSetChanged();
        }*/
    }
}
