package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.api.DatoUsuarioControlador;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.SubirFotoControlador;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SubirFoto extends Fragment {

    private String foto;
    private Button btn_receta;
    private ImageView img_foto;
    private EditText descripcion;
    private SubirFotoControlador subirFotoControlador;

    public SubirFoto() {
    }


    public static SubirFoto newInstance(String param1, String param2) {
        SubirFoto fragment = new SubirFoto();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            foto = getArguments().getString("clave");
                  
            Bundle bundle = new Bundle();
            bundle.putString("clave",null);
            setArguments(bundle);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subir_foto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img_foto = view.findViewById(R.id.IMG_foto);
        descripcion = view.findViewById(R.id.ED_descripcion);

        btn_receta = view.findViewById(R.id.BTN_receta);
        BDsqlite dbHelper = new BDsqlite(getActivity());


        Retrofit retrofit = RetrofitClient.getClient();
        subirFotoControlador = retrofit.create(SubirFotoControlador.class);
        Bitmap bitmap = base64ToBitmap(foto);

        img_foto.setImageBitmap(bitmap);

        btn_receta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CrearReceta crearReceta = new CrearReceta();
                Bundle bundle = new Bundle();
                bundle.putString("foto",foto);
                bundle.putString("descripcion",descripcion.getText().toString());

                crearReceta.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.AplicacionFrame, crearReceta);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }


    public Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}