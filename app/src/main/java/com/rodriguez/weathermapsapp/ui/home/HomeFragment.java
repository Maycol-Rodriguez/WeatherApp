package com.rodriguez.weathermapsapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.rodriguez.weathermapsapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {
    TextView txtTemperatura, txtCiudad, txtHumedad,txtViento, txtPresion, txtPrecipitacion, txtDescripcionClima, txtFecha;
    ImageView estrella, iconoClima;
    LinearLayout contenedor;
    private boolean activo;

    Button btnBuscar;

    EditText txtBuscar;

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
        txtPrecipitacion = view.findViewById(R.id.tvPrecipitacionClima);
        txtDescripcionClima = view.findViewById(R.id.tvDescripcionClima);
        iconoClima = view.findViewById(R.id.ivIconoClima);
        txtFecha = view.findViewById(R.id.tvFecha);

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
            activo = false;
        } else {
            estrella.setImageResource(R.drawable.ic_star_empty);
            activo = true;
        }
    }

    private void btnBuscarClimaPorCiudad() {
        if (txtBuscar.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Ingrese una ciudad", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Constantes.baseUrl + "weather?q=" + txtBuscar.getText().toString() + "&appid=" + Constantes.apiKey+"&units=metric&lang=es";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String unidad = "";

                if(unidad=="metric"){
                    unidad = "°C";
                }else{
                    unidad = "°F";
                }
                Gson gson = new Gson();
                ClimaPorNombre clima = gson.fromJson(response, ClimaPorNombre.class);

                String temperatura = (int) clima.main.temp + "°C";// Cambiar la metrica
                String ciudad = clima.name;
                String humedad = clima.main.humidity + "%";
                String viento = clima.wind.speed + " m/s";
                String presion = clima.main.pressure + " hPa";
                String descripcion = clima.weather[0].description;
                String icono = Constantes.icono + clima.weather[0].icon + ".png";
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String currentDate = dateFormat.format(calendar.getTime());
                String precipitacion = clima.wind.deg + "°";

                estrella.setImageResource(R.drawable.ic_star_empty);
                txtFecha.setText(currentDate);
                txtTemperatura.setText(temperatura);
                txtCiudad.setText(ciudad);
                txtHumedad.append(humedad);
                txtViento.append(viento);
                txtPresion.append(presion);
                txtDescripcionClima.setText(descripcion);
                Picasso.get().load(icono).into(iconoClima);
                txtPrecipitacion.append(precipitacion);

                Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() == null) {
                    Toast.makeText(getContext(), "No se encontró la ciudad", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(error.getMessage().isEmpty()){
                    Toast.makeText(getContext(), "No ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }
}