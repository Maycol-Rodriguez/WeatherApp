package com.rodriguez.weathermapsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.rodriguez.weathermapsapp.databinding.ActivityMainBinding;
import com.rodriguez.weathermapsapp.ui.home.HomeFragment;
import com.rodriguez.weathermapsapp.ui.opciones.OpcionesFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_galeria, R.id.nav_configuracion)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void configurarUnidades(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new OpcionesFragment());
        fragmentTransaction.commit();
    }

    public void enviarCambios(View view) {
        Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();

        MaterialSwitch swTemperatura = findViewById(R.id.swTemperatura);
        MaterialSwitch swVelocidadV = findViewById(R.id.swVelocidad);
        MaterialSwitch swPresion = findViewById(R.id.swPresion);
        MaterialSwitch swRafagaViento = findViewById(R.id.swRafagaViento);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putBoolean("temperatura", swTemperatura.isChecked());
        bundle.putBoolean("velocidadViento", swVelocidadV.isChecked());
        bundle.putBoolean("presion", swPresion.isChecked());
        bundle.putBoolean("precipitacion", swRafagaViento.isChecked());

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, homeFragment);
        fragmentTransaction.commit();
    }

    public void guardarCambios(View view) {

    }
}