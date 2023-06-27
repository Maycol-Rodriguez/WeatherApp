package com.rodriguez.weathermapsapp.ui.configuracion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rodriguez.weathermapsapp.R;

import java.util.ArrayList;
import java.util.List;


public class ConfiguracionFragment extends Fragment {
    Spinner fuente;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_configuracion, container, false);
        fuente = view.findViewById(R.id.spnFuente);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getNumberList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuente.setAdapter(adapter);
        return view;
    }

    private List<Integer> getNumberList() {
        List<Integer> numberList = new ArrayList<>();
        numberList.add(12);
        numberList.add(14);
        numberList.add(16);
        return numberList;
    }
}