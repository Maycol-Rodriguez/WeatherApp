package com.rodriguez.weathermapsapp.ui.bienvenida;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.rodriguez.weathermapsapp.Helpers.Constantes;
import com.rodriguez.weathermapsapp.Helpers.Mappers.ClimaPorNombre;
import com.rodriguez.weathermapsapp.MainActivity;
import com.rodriguez.weathermapsapp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Bienvenida extends AppCompatActivity {

    TextView txtTemperatura, txtCiudad, txtHumedad, txtViento, txtPresion, txtRafagaViento;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        txtTemperatura = findViewById(R.id.biTemperatura);
        txtCiudad = findViewById(R.id.biCiudad);
        txtHumedad = findViewById(R.id.biHumedadClima);
        txtViento = findViewById(R.id.biVientoClima);
        txtPresion = findViewById(R.id.biPresionClima);
        txtRafagaViento = findViewById(R.id.biPrecipitacionClima);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        obtenerUbicacion();
    }

    private void obtenerUbicacion() {
        if (ContextCompat.checkSelfPermission(Bienvenida.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(Bienvenida.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            double latitud = addresses.get(0).getLatitude();
                            double longitud = addresses.get(0).getLongitude();
                            System.out.println("Latitud: " + latitud);
                            System.out.println("Longitud: " + longitud);
                            String urlLatLong = Constantes.baseUrl + "weather?lat=" + latitud + "&lon=" + longitud + "&appid=" + Constantes.apiKey + "&units=metric&lang=es";
                            RequestQueue requestLagLong = Volley.newRequestQueue(Bienvenida.this);

                            StringRequest climaActualLagLong = new StringRequest(Request.Method.GET, urlLatLong, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Gson gson = new Gson();
                                    ClimaPorNombre clima = gson.fromJson(response, ClimaPorNombre.class);

                                    String temperatura = (int) clima.main.temp + "°C";
                                    String ciudad = clima.name;
                                    String humedad = clima.main.humidity + "%";
                                    String viento = clima.wind.speed + " m/s";
                                    String presion = clima.main.pressure + " hPa";
                                    String icono = Constantes.icono + clima.weather[0].icon + ".png";

                                    String rafagaViento = clima.wind.gust + "°";

                                    txtTemperatura.setText(temperatura);
                                    txtCiudad.setText(ciudad);
                                    txtHumedad.setText("Humedad: " + humedad);
                                    txtViento.setText("Velocidad de Viento: " + viento);
                                    txtPresion.setText("Presion: " + presion);
                                    txtRafagaViento.setText("Rafaga de Viento: " + rafagaViento);
//                                    Picasso.get().load(icono).into(iconoClima);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.getMessage() == null) {
                                        Toast.makeText(Bienvenida.this, "No se encontró la ciudad", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (error.getMessage().isEmpty()) {
                                        Toast.makeText(Bienvenida.this, "No ", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Toast.makeText(Bienvenida.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            requestLagLong.add(climaActualLagLong);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }

    public void openHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

