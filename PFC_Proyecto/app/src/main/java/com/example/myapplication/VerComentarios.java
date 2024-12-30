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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Adaptadores.AmigosAdapter;
import com.example.myapplication.Adaptadores.ComentariosAdapter;
import com.example.myapplication.Adaptadores.FotosAdapter;
import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.api.AmigosControlador;
import com.example.myapplication.api.ComentariosControlador;
import com.example.myapplication.api.FotosControlador;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.modelos.Amigos;
import com.example.myapplication.modelos.Comentarios;
import com.example.myapplication.modelos.Fotos;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class VerComentarios extends Fragment {

    private int id_foto;
    private EditText ed_escribirComentario;
    private ImageButton btn_enviarComentario, btn_volveratras;
    private RecyclerView recyclerView;
    private ComentariosAdapter adapter;
    private List<Comentarios> comentariosList;
    private ComentariosControlador comentariosControlador;

    public VerComentarios() {
    }


    public static VerComentarios newInstance(String param1, String param2) {
        VerComentarios fragment = new VerComentarios();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_foto = getArguments().getInt("clave");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comentarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewComentarios);
        ed_escribirComentario = view.findViewById(R.id.ED_EscribirComentario);
        btn_enviarComentario = view.findViewById(R.id.BTN_EnviarComentario);
        btn_volveratras = view.findViewById(R.id.BTN_volveratras);

        BDsqlite dbHelper = new BDsqlite(getActivity());
        Retrofit retrofit = RetrofitClient.getClient();
        comentariosControlador = retrofit.create(ComentariosControlador.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getComentarios(id_foto);


        btn_enviarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ed_escribirComentario.getText().toString().trim().equals("")){
                    enviarComentario(dbHelper.getUsuarioID(), id_foto, ed_escribirComentario.getText().toString());
                }
            }
        });
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
    private void getComentarios(int id){
        Call<List<Comentarios>> call = comentariosControlador.getComentarios(id);
        call.enqueue(new Callback<List<Comentarios>>() {
            @Override
            public void onResponse(Call<List<Comentarios>> call, Response<List<Comentarios>> response) {

                if (response.isSuccessful()) {
                    comentariosList = response.body();
                    adapter = new ComentariosAdapter(getContext(), comentariosList);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(getActivity(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Comentarios>> call, Throwable t) {
                t.printStackTrace();
                Log.d("API_RESPONSE", "Err: " + t.getMessage());

            }
        });
    }
    private void enviarComentario(int id, int id_f, String comentario) {
        Call<ResponseBody> call = comentariosControlador.comentar(id, id_f, comentario);
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
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            getComentarios(id_foto);
                            ed_escribirComentario.setText("");

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            Log.d("API_RESPONSE", "Err: " + message);

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