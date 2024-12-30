package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.api.RegistrarseControlador;
import com.example.myapplication.api.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Registrar extends Fragment {

    private Button btn_siguiente;
    private Button btn_volver;

    private EditText Correo, Nick, Contrasena, Rcontrasena;
    private RegistrarseControlador usuariosControlador;



    public Registrar() {
    }


    public static Registrar newInstance(String param1, String param2) {
        Registrar fragment = new Registrar();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registrar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_siguiente = view.findViewById(R.id.BTN_siguiente);
        btn_volver = view.findViewById(R.id.BTN_volver);
        Correo = view.findViewById(R.id.ED_registro_correo);
        Nick = view.findViewById(R.id.ED_registro_nick);
        Contrasena = view.findViewById(R.id.ED_registro_contrasena);
        Rcontrasena = view.findViewById(R.id.ED_registro_Rcontrasena);

        Retrofit retrofit = RetrofitClient.getClient();
        usuariosControlador = retrofit.create(RegistrarseControlador.class);

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.MainFrame, new InicioSesion());
                transaction.addToBackStack(null); // Agregar a la pila de retroceso
                transaction.commit();
            }
        });
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EmailValido(Correo.getText().toString())){
                    Correo.setError("Correo no valido");

                }else if (Nick.getText().toString().length()<4||Nick.getText().toString().length()>12) {
                    Nick.setError("Tiene que tener entre 4 y 12 caracteres");

                }else if (Contrasena.getText().toString().length()<8) {
                    Contrasena.setError("Tiene que tener como mínimo 8 caracteres");

                }else if (!Contrasena.getText().toString().equals(Rcontrasena.getText().toString())) {
                    Rcontrasena.setError("Las contraseñas deben coincidir");

                }else{

                    RegistrarUsr(Correo.getText().toString(), Nick.getText().toString(),Contrasena.getText().toString());

                }
            }
        });


    }
    private void RegistrarUsr(String correo, String nick, String contrasena) {
        Call<ResponseBody> call = usuariosControlador.postUsuario(correo, nick, contrasena);
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

                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.MainFrame, new InicioSesion());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                            if(message.equals("correo repetido")){
                                Correo.setError(message);
                                Correo.setText("");
                            }else{
                                Nick.setError(message);
                                Nick.setText("");
                            }

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
    public boolean EmailValido(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}