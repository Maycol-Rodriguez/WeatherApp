package com.rodriguez.weathermapsapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rodriguez.weathermapsapp.R;
import com.rodriguez.weathermapsapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    TextView temperatura;
    ImageView estrella;
    private boolean activo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_home, container, false);
        estrella = view.findViewById(R.id.btnStar);
        temperatura = view.findViewById(R.id.tvTemperatura);

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
}