package com.rodriguez.weathermapsapp.ui.galeria;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.rodriguez.weathermapsapp.Helpers.Adaptador;
import com.rodriguez.weathermapsapp.Sqlite.Mappers.ClimaFavorito;
import com.rodriguez.weathermapsapp.R;
import com.rodriguez.weathermapsapp.Sqlite.ConexionSqlite;
import com.rodriguez.weathermapsapp.Sqlite.CreacionBd;

import java.util.ArrayList;

public class GaleriaFragment extends Fragment {

    GridView tabla;
    ArrayList<ClimaFavorito> climas;
    ArrayList<String> listaInformacion;
    ConexionSqlite conn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_galeria, container, false);
        tabla = view.findViewById(R.id.grid);
        conn = new ConexionSqlite(getContext(), CreacionBd.DB_NAME, null, CreacionBd.DB_VERSION);
        climas = new ArrayList<>();
        consultarClimasFavoritos();

        Adaptador adaptador = new Adaptador(getContext(), R.layout.elemento, climas);
        tabla.setAdapter(adaptador);

        return view;
    }

    private void consultarClimasFavoritos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        ClimaFavorito climaFavorito = null;
        climas = new ArrayList<ClimaFavorito>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CreacionBd.TABLA_FAVORITOS, null);
        while (cursor.moveToNext()) {
            climaFavorito = new ClimaFavorito();
            climaFavorito.setId(cursor.getInt(0));
            climaFavorito.setCiudad(cursor.getString(1));
            climaFavorito.setTemperatura(cursor.getString(2));
            climaFavorito.setFecha(cursor.getString(3));
            climaFavorito.setDescripcion(cursor.getString(4));
            climaFavorito.setImagen(cursor.getString(5));

            climas.add(climaFavorito);
        }
        obtenerListado();
    }

    private void obtenerListado() {
        listaInformacion = new ArrayList<String>();
        listaInformacion.add("Ciudad - Temperatura - Fecha - Descripcion");
        for (int i = 0; i < climas.size(); i++) {
            listaInformacion.add(climas.get(i).getCiudad() + " - " + climas.get(i).getTemperatura() + " - " + climas.get(i).getFecha() + " - " + climas.get(i).getDescripcion());
        }
    }
}