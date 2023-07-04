package com.rodriguez.weathermapsapp.ui.home;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rodriguez.weathermapsapp.Helpers.Constantes;
import com.rodriguez.weathermapsapp.Helpers.Mappers.ClimaPorNombre;
import com.rodriguez.weathermapsapp.Helpers.Mappers.PronosticoClima;
import com.rodriguez.weathermapsapp.R;
import com.rodriguez.weathermapsapp.Sqlite.ConexionSqlite;
import com.rodriguez.weathermapsapp.Sqlite.CreacionBd;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {
    TextView txtTemperatura, txtCiudad, txtHumedad, txtViento, txtPresion, txtRafagaViento, txtDescripcionClima, txtFecha;
    ImageView estrella, iconoClima;
    LinearLayout contenedor;
    boolean activo;

    Button btnBuscar;

    EditText txtBuscar;


    TextView txtPronostico, pronostico1, pronostico2, pronostico3;
    ImageView iconoPronostico1, iconoPronostico2, iconoPronostico3;
    TextView fechaPronostico1, fechaPronostico2, fechaPronostico3;

    ConexionSqlite conn;
    String icono;

    boolean checkedTemp = false, checkedVelocidadViento = false, checkedPresion = false, checkedRafagaViento = false, checkedDistancia = false;
    String unidad = "";
    String grados = "";

    String formPresion = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_home, container, false);
        estrella = view.findViewById(R.id.btnStar);
        txtTemperatura = view.findViewById(R.id.tvTemperatura);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        txtBuscar = view.findViewById(R.id.txtBuscar);
        txtCiudad = view.findViewById(R.id.tvCiudad);
        txtHumedad = view.findViewById(R.id.tvHumedadClima);
        txtViento = view.findViewById(R.id.tvVientoClima);
        txtPresion = view.findViewById(R.id.tvPresionClima);
        txtRafagaViento = view.findViewById(R.id.tvPrecipitacionClima);
        txtDescripcionClima = view.findViewById(R.id.tvDescripcionClima);
        iconoClima = view.findViewById(R.id.ivIconoClima);
        txtFecha = view.findViewById(R.id.tvFecha);

        pronostico1 = view.findViewById(R.id.pronosticoTex1);
        pronostico2 = view.findViewById(R.id.pronosticoTex2);
        pronostico3 = view.findViewById(R.id.pronosticoTex3);

        iconoPronostico1 = view.findViewById(R.id.pronosticoImage1);
        iconoPronostico2 = view.findViewById(R.id.pronosticoImage2);
        iconoPronostico3 = view.findViewById(R.id.pronosticoImage3);

        fechaPronostico1 = view.findViewById(R.id.pronosticoFecha1);
        fechaPronostico2 = view.findViewById(R.id.pronosticoFecha2);
        fechaPronostico3 = view.findViewById(R.id.pronosticoFecha3);
        contenedor = view.findViewById(R.id.contenedorDetalle);

        txtPronostico = view.findViewById(R.id.txtPronostico);

        Bundle bundle = getArguments();
        if (bundle != null) {
            checkedTemp = bundle.getBoolean("temperatura");
            checkedVelocidadViento = bundle.getBoolean("velocidadViento");
            checkedPresion = bundle.getBoolean("presion");
            checkedRafagaViento = bundle.getBoolean("precipitacion");
        }

        if (checkedTemp) {
            unidad = "imperial";
            grados = "°F";
        } else {
            unidad = "metric";
            grados = "°C";
        }


        conn = new ConexionSqlite(getContext(), CreacionBd.DB_NAME, null, CreacionBd.DB_VERSION);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBuscarClimaPorCiudad();
            }
        });

        estrella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleStrella();
            }
        });

        return view;
    }

    private void toggleStrella() {
        if (activo) {
            estrella.setImageResource(R.drawable.ic_star_fill);
            agregarFavorito();
            activo = false;
        } else {
            estrella.setImageResource(R.drawable.ic_star_empty);
            activo = true;
        }
    }

    private void agregarFavorito() {
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put(CreacionBd.CAMPO_CIUDAD, txtCiudad.getText().toString());
        valores.put(CreacionBd.CAMPO_TEMPERATURA, txtTemperatura.getText().toString());
        valores.put(CreacionBd.CAMPO_DESCRIPCION, txtDescripcionClima.getText().toString());
        valores.put(CreacionBd.CAMPO_FECHA, txtFecha.getText().toString());
        valores.put(CreacionBd.CAMPO_IMAGEN, icono);
        Long idResultante = db.insert(CreacionBd.TABLA_FAVORITOS, CreacionBd.CAMPO_ID, valores);
        Toast.makeText(getContext(), "Id Registro: " + idResultante, Toast.LENGTH_SHORT).show();
        db.close();
    }

    private void btnBuscarClimaPorCiudad() {

        if (txtBuscar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Ingrese una ciudad", Toast.LENGTH_SHORT).show();
            return;
        }

        String urlClima = Constantes.baseUrl+"weather?q=" + txtBuscar.getText().toString() + "&appid=" + Constantes.apiKey + "&units=" + unidad + "&lang=es";
        String urlPronostico = Constantes.baseUrl+"forecast?q=" + txtBuscar.getText().toString() + "&appid=" + Constantes.apiKey + "&units=" + unidad + "&lang=es&cnt=20";

        RequestQueue requestClimaActual = Volley.newRequestQueue(getContext());
        StringRequest climaActual = new StringRequest(Request.Method.GET, urlClima, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Gson gson = new Gson();
                ClimaPorNombre clima = gson.fromJson(response, ClimaPorNombre.class);

                String temperatura = checkedTemp ? (int) clima.main.temp + "°F" : (int) clima.main.temp + "°C";
                String ciudad = clima.name;
                String humedad = clima.main.humidity + " %";
                String viento = checkedVelocidadViento ? Math.round((clima.wind.speed * 2.237) * 100d) / 100d + " mill/h" : clima.wind.speed + " m/s";
                String presion = checkedPresion ? Math.round((clima.main.pressure * 0.0295301) * 100d) / 100d + " inHg" : clima.main.pressure + " hPa";
                String descripcion = clima.weather[0].description;
                icono = Constantes.icono + clima.weather[0].icon + ".png";
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fechaActual = dateFormat.format(calendar.getTime());
                String rafagaViento = checkedRafagaViento ? Math.round((clima.wind.gust * 2.237) * 100d) / 100d + " mill/h" : (clima.wind.gust) + " m/s";

                contenedor.setBackground(getResources().getDrawable(R.drawable.rounded_corners));
                estrella.setImageResource(R.drawable.ic_star_empty);
                txtFecha.setText(fechaActual);
                txtTemperatura.setText(temperatura);
                txtCiudad.setText(ciudad);
                txtHumedad.setText("Humedad: " + humedad);
                txtViento.setText("Viento: " + viento);
                txtPresion.setText("Presion: " + presion);
                txtDescripcionClima.setText(descripcion);
                Picasso.get().load(icono).into(iconoClima);
                txtRafagaViento.setText("Rafaga de viento: " + rafagaViento);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() == null) {
                    Toast.makeText(getContext(), "No se encontró la ciudad", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (error.getMessage().isEmpty()) {
                    Toast.makeText(getContext(), "No ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        requestClimaActual.add(climaActual);

        RequestQueue requestPronostico = Volley.newRequestQueue(getContext());
        StringRequest pronostico = new StringRequest(Request.Method.GET, urlPronostico, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                PronosticoClima pronostico = gson.fromJson(response, PronosticoClima.class);
                String icono1 = Constantes.icono + pronostico.getList().get(0).getWeather().get(0).getIcon() + ".png";
                String icono2 = Constantes.icono + pronostico.getList().get(7).getWeather().get(0).getIcon() + ".png";
                String icono3 = Constantes.icono + pronostico.getList().get(15).getWeather().get(0).getIcon() + ".png";

                String fecha1 = pronostico.getList().get(0).getDt_txt().split(" ")[0];
                String fecha2 = pronostico.getList().get(7).getDt_txt().split(" ")[0];
                String fecha3 = pronostico.getList().get(15).getDt_txt().split(" ")[0];

                String temperatura1 = pronostico.getList().get(0).getMain().getTemp() + grados;
                String temperatura2 = pronostico.getList().get(7).getMain().getTemp() + grados;
                String temperatura3 = pronostico.getList().get(15).getMain().getTemp() + grados;


                txtPronostico.setText("Pronostico para los proximos 3 dias");
                pronostico1.setText(temperatura1);
                pronostico2.setText(temperatura2);
                pronostico3.setText(temperatura3);

                Picasso.get().load(icono1).into(iconoPronostico1);
                Picasso.get().load(icono2).into(iconoPronostico2);
                Picasso.get().load(icono3).into(iconoPronostico3);

                fechaPronostico1.setText(fecha1);
                fechaPronostico2.setText(fecha2);
                fechaPronostico3.setText(fecha3);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestPronostico.add(pronostico);
    }
}