package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adaptadores.AmigosAdapter;
import com.example.myapplication.Adaptadores.FotosAdapter;
import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.api.AmigosControlador;
import com.example.myapplication.api.FotosControlador;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.modelos.Amigos;
import com.example.myapplication.modelos.Fotos;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PagFotos extends Fragment {
    private RecyclerView recyclerView;
    private FotosAdapter adapter;

    private List<Fotos> fotosList;
    private FotosControlador fotosControlador;


    public PagFotos() {
    }


    public static PagFotos newInstance(String param1, String param2) {
        PagFotos fragment = new PagFotos();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pag_fotos, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewFotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = RetrofitClient.getClient();
        fotosControlador = retrofit.create(FotosControlador.class);

        BDsqlite dbHelper = new BDsqlite(getActivity());
        getFotosAmigos(dbHelper.getUsuarioID());

    }
    private void getFotosAmigos(int id){
        Call<List<Fotos>> call = fotosControlador.getFotosAmigos(id);
        call.enqueue(new Callback<List<Fotos>>() {
            @Override
            public void onResponse(Call<List<Fotos>> call, Response<List<Fotos>> response) {

                if (response.isSuccessful()) {
                    fotosList = response.body();
                    if(fotosList.isEmpty()||!fotosList.get(0).getNICK().equals("TÚ")){
                        Toast.makeText(getActivity(), "sube una foto si quieres ver las de los demás", Toast.LENGTH_SHORT).show();
                        adapter = new FotosAdapter(getContext(), fotosList);
                        recyclerView.setAdapter(adapter);

                    }else{
                        adapter = new FotosAdapter(getContext(), fotosList);
                        recyclerView.setAdapter(adapter);
                    }



                } else {
                    Toast.makeText(getActivity(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Fotos>> call, Throwable t) {
                t.printStackTrace();
                Log.d("API_RESPONSE", "Err: " + t.getMessage());

            }
        });
    }
}