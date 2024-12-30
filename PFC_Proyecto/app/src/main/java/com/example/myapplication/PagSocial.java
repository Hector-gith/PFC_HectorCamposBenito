package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Adaptadores.AmigosAdapter;
import com.example.myapplication.Adaptadores.SolicitudesAdapter;
import com.example.myapplication.Adaptadores.UsuariosAdapter;
import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.api.BuscarSolicitarUsuarioControlador;
import com.example.myapplication.api.FotoUsuarioControlador;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.modelos.Amigos;
import com.example.myapplication.modelos.Usuarios;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PagSocial extends Fragment {


    private Button btn_amigos, btn_solicitudes;
    private EditText ed_buscador;
    private UsuariosAdapter adapter;
    private RecyclerView recyclerView;
    private List<Amigos> amigosList;
    private BuscarSolicitarUsuarioControlador buscarSolicitarUsuarioControlador;

    public PagSocial() {
    }


    public static PagSocial newInstance(String param1, String param2) {
        PagSocial fragment = new PagSocial();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pag_social, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_amigos = view.findViewById(R.id.BTN_Amigos);
        btn_solicitudes = view.findViewById(R.id.BTN_Solicitudes);
        ed_buscador = view.findViewById(R.id.ED_buscador);
        recyclerView = view.findViewById(R.id.recyclerViewUsuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Retrofit retrofit = RetrofitClient.getClient();
        buscarSolicitarUsuarioControlador = retrofit.create(BuscarSolicitarUsuarioControlador.class);

        BDsqlite dbHelper = new BDsqlite(getActivity());
        getNumeroSolicitudes(dbHelper.getUsuarioID());

        ed_buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getUsuarios(dbHelper.getUsuarioID(), ed_buscador.getText().toString());

            }

        });

        btn_amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.AplicacionFrame, new ListaAmigos());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btn_solicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.AplicacionFrame, new ListaSolicitudes());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


    }
    private void Solicitar(int id) {

        BDsqlite dbHelper = new BDsqlite(getActivity());
        enviarSolicitud(dbHelper.getUsuarioID(), id);

    }
    private void getUsuarios(int id, String nick){
        Call<List<Amigos>> call = buscarSolicitarUsuarioControlador.getUsuarios(id, nick);
        call.enqueue(new Callback<List<Amigos>>() {
            @Override
            public void onResponse(Call<List<Amigos>> call, Response<List<Amigos>> response) {

                if (response.isSuccessful()) {
                    amigosList = response.body();
                    adapter = new UsuariosAdapter(amigosList, id -> {
                        Solicitar(id);
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
    private void getNumeroSolicitudes(int id){
        Call<String> call = buscarSolicitarUsuarioControlador.getNumeroSolicitudes(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    btn_solicitudes.setText(response.body()+" solicitudes");

                } else {
                    Toast.makeText(getActivity(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Log.d("API_RESPONSE", "Err: " + t.getMessage());

            }
        });
    }
    private void enviarSolicitud(int id_s, int id_r) {
        Call<ResponseBody> call = buscarSolicitarUsuarioControlador.mandarSolicitud(id_s, id_r);
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
