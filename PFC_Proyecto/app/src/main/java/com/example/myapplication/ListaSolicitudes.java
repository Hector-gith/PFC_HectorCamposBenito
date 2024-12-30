package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Adaptadores.SolicitudesAdapter;
import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.SolicitudesControlador;
import com.example.myapplication.modelos.Amigos;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListaSolicitudes extends Fragment {


    private RecyclerView recyclerView;
    private SolicitudesAdapter adapter;
    private List<Amigos> amigosList;
    private SolicitudesControlador solicitudesControlador;
    private ImageButton btn_volveratras;
    private int aceptado;

    public ListaSolicitudes() {
    }


    public static ListaSolicitudes newInstance(String param1, String param2) {
        ListaSolicitudes fragment = new ListaSolicitudes();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_solicitudes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewSolicitudes);
        btn_volveratras = view.findViewById(R.id.BTN_volveratras);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Retrofit retrofit = RetrofitClient.getClient();
        solicitudesControlador = retrofit.create(SolicitudesControlador.class);

        BDsqlite dbHelper = new BDsqlite(getActivity());
        getSolicitudes(dbHelper.getUsuarioID());

        btn_volveratras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.AplicacionFrame, new PagSocial());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }
    private void eliminarAmigo(int id) {
        for (int i = 0; i < amigosList.size(); i++) {
            if (amigosList.get(i).getID() == id) {
                amigosList.remove(i);
                adapter.notifyItemRemoved(i);
                break;
            }
        }
    }
    private void aceptarSolicitud(int id) {
        eliminarAmigo(id);
        aceptado = 1;
        BDsqlite dbHelper = new BDsqlite(getActivity());

        gestionarRespuestaSolicitud(id, dbHelper.getUsuarioID(), aceptado);
    }

    private void rechazarSolicitud(int id) {
        eliminarAmigo(id);
        aceptado = 0;
        BDsqlite dbHelper = new BDsqlite(getActivity());

        gestionarRespuestaSolicitud(id, dbHelper.getUsuarioID(), aceptado);
    }
    private void getSolicitudes(int id){
        Call<List<Amigos>> call = solicitudesControlador.getSolicitudes(id);
        call.enqueue(new Callback<List<Amigos>>() {
            @Override
            public void onResponse(Call<List<Amigos>> call, Response<List<Amigos>> response) {

                if (response.isSuccessful()) {
                    amigosList = response.body();
                    adapter = new SolicitudesAdapter(amigosList, new SolicitudesAdapter.OnAmigoClickListener() {
                        @Override
                        public void onAceptarClick(int id) {
                            aceptarSolicitud(id);
                        }

                        @Override
                        public void onRechazarClick(int id) {
                            rechazarSolicitud(id);
                        }
                    });
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(getActivity(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Amigos>> call, Throwable t) {
                t.printStackTrace();
                Log.d("API_RESPONSE", "Err: " + t.getMessage());

            }
        });
    }
    private void gestionarRespuestaSolicitud(int id_s, int id_r, int aceptado) {
        Call<ResponseBody> call = solicitudesControlador.gestionarRespuestaSolicitud(id_s, id_r, aceptado);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject responseJson = new JSONObject(responseBody);
                        String status = responseJson.getString("status");
                        String message = responseJson.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error en la respuesta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorMessage);
                        String message = errorJson.getString("message");
                        Toast.makeText(getActivity(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "El servidor no responde, espere...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}