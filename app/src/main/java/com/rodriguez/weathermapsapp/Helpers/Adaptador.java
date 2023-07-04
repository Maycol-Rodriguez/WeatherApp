package com.rodriguez.weathermapsapp.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rodriguez.weathermapsapp.Sqlite.Mappers.ClimaFavorito;
import com.rodriguez.weathermapsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {

    public Context context;
    public int layout;
    public ArrayList<ClimaFavorito> climaFavoritoArrayList;

    public Adaptador(Context context, int layout, ArrayList<ClimaFavorito> climaFavoritoArrayList) {
        this.context = context;
        this.layout = layout;
        this.climaFavoritoArrayList = climaFavoritoArrayList;
    }

    @Override
    public int getCount() {
        return climaFavoritoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        view = layoutInflater.inflate(R.layout.elemento, null);
        String imagen = climaFavoritoArrayList.get(position).getImagen();
        String temperatura = climaFavoritoArrayList.get(position).getTemperatura();
        String ciudad = climaFavoritoArrayList.get(position).getCiudad();
        String descripcion = climaFavoritoArrayList.get(position).getDescripcion();
        String fecha = climaFavoritoArrayList.get(position).getFecha();

        TextView txtCiudad = view.findViewById(R.id.bdCiudad);
        TextView txtTemperatura = view.findViewById(R.id.bdTemperatura);
        TextView txtDescripcion = view.findViewById(R.id.bdDescripcion);
        TextView txtFecha = view.findViewById(R.id.bdFecha);
        ImageView imgClima = view.findViewById(R.id.bdImgClima);

        txtCiudad.setText(ciudad);
        txtTemperatura.setText(temperatura);
        txtDescripcion.setText(descripcion);
        txtFecha.setText(fecha);
//        imgClima.setImageResource(context.getResources().getIdentifier(imagen, "drawable", context.getPackageName()));
        Picasso.get().load(imagen).into(imgClima);
        return view;
    }
}
