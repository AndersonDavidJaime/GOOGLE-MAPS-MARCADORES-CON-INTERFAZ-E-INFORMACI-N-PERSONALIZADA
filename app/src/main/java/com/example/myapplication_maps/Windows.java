package com.example.myapplication_maps;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class Windows implements GoogleMap.InfoWindowAdapter{

    View view;
    Context context;


    public Windows(Context context,View view)
    {
        this.context=context;
        this.view= view;
    }


    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) { //sd

        try {
            Datosreq datosreq = (Datosreq) marker.getTag();
            TextView tvbnombre = view.findViewById(R.id.Nombre);
            TextView tvbubicacion = view.findViewById(R.id.Ubicacion);
            TextView tvbhorarios = view.findViewById(R.id.Horarios);

            tvbnombre.setText(datosreq.getNombre());
            tvbubicacion.setText(datosreq.getUbicacion());
            String pep = "";
            for (int x = 0; x < datosreq.getHorario().size(); x++) {
                pep += datosreq.getHorario().get(x) + "\n";
            }
            tvbhorarios.setText(pep);

            Log.i("imagenpepita", datosreq.getLogo());

            ImageView imageView = (ImageView) view.findViewById(R.id.imgAvatar);
            Glide.with(context).load(datosreq.getLogo()).into(imageView);

            //Glide.with(context).load("https://www.tooltyp.com/wp-content/uploads/2014/10/1900x920-8-beneficios-de-usar-imagenes-en-nuestros-sitios-web.jpg").into(imageView);

            return view;
        }catch (Exception ex){
            Log.i("imagenpepita",ex.getMessage());
        }
        return view;
    }
}
