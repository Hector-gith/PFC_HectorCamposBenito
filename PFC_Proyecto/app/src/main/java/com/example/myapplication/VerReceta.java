package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class VerReceta extends Fragment {

    private ImageButton btn_volveratras;
    private TextView tv_ingredientes;
    private TextView tv_preparacion;
    private String ingredientes;
    private String preparacion;


    public VerReceta() {
    }


    public static VerReceta newInstance(String param1, String param2) {
        VerReceta fragment = new VerReceta();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingredientes = getArguments().getString("ingredientes");
            preparacion = getArguments().getString("preparacion");
            Bundle bundle = new Bundle();
            bundle.putString("ingredientes",null);
            bundle.putString("preparacion",null);

            setArguments(bundle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ver_receta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_volveratras = view.findViewById(R.id.BTN_volveratras);
        tv_ingredientes = view.findViewById(R.id.TV_ingredientes);
        tv_preparacion = view.findViewById(R.id.TV_preparacion);

        tv_ingredientes.setText(ingredientes);
        tv_preparacion.setText(preparacion);

        btn_volveratras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.AplicacionFrame, new PagFotos());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}