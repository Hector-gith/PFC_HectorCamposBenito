package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.api.InicioSesionControlador;
import com.example.myapplication.api.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InicioSesion extends Fragment {
    private Button btn_iniciar;

    private Button btn_registrar;

    private EditText Correo, Contrasena;
    private InicioSesionControlador inicioSesionControlador;


    public InicioSesion() {
    }


    public static InicioSesion newInstance(String param1, String param2) {
        InicioSesion fragment = new InicioSesion();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_iniciar = view.findViewById(R.id.BTN_iniciar);
        btn_registrar = view.findViewById(R.id.BTN_registrarse);

        Correo = view.findViewById(R.id.ED_correo);
        Contrasena = view.findViewById(R.id.ED_contrasena);

        Retrofit retrofit = RetrofitClient.getClient();
        inicioSesionControlador = retrofit.create(InicioSesionControlador.class);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.MainFrame, new Registrar());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btn_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarUsr(Correo.getText().toString(), Contrasena.getText().toString());
            }
        });
    }
    private void IniciarUsr(String correo, String contrasena) {
        Call<ResponseBody> call = inicioSesionControlador.postInicioSesion(correo, contrasena);
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
                            BDsqlite dbHelper = new BDsqlite(getActivity());

                            dbHelper.setUsuarioID(Integer.parseInt(message));
                            getActivity().finish();

                            Intent intent = new Intent(getActivity(), SecondActivity.class);
                            startActivity(intent);

                        } else {
                            if(message.equals("La contraseña no es correcta")){
                                Contrasena.setError(message);
                            }else{
                                Correo.setError(message);
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorMessage);
                        String message = errorJson.getString("message");
                        Toast.makeText(getActivity(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
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