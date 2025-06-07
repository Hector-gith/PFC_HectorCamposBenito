package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.api.FotoUsuarioControlador;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.SubirFotoControlador;
import com.example.myapplication.modelos.Fotos;
import com.example.myapplication.modelos.Usuarios;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PagInicio extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Fotos foto;
    private TextView nick;
    private ImageButton btn_perfil;
    private ImageButton btn_tomarFoto;
    private Button btn_eliminarFoto;
    private String fotoB64;
    private FotoUsuarioControlador fotoUsuarioControlador;

    public PagInicio() {
    }


    public static PagInicio newInstance(String param1, String param2) {
        PagInicio fragment = new PagInicio();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pag_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SecondActivity activity  = (SecondActivity) getActivity();
        solicitarPermisos();

        nick = view.findViewById(R.id.TV_nick);
        btn_perfil = view.findViewById(R.id.BTN_perfil);
        btn_tomarFoto = view.findViewById(R.id.BTN_tomarFoto);
        btn_eliminarFoto = view.findViewById(R.id.BTN_EliminarFoto);
        Retrofit retrofit = RetrofitClient.getClient();

        fotoUsuarioControlador = retrofit.create(FotoUsuarioControlador.class);
        BDsqlite dbHelper = new BDsqlite(getActivity());


        if(dbHelper.getUsuarioFoto_perfil()!=null){
            Bitmap bitmapPerfil = base64ToBitmap(dbHelper.getUsuarioFoto_perfil());
            btn_perfil.setImageBitmap(bitmapPerfil);
        }


        nick.setText(dbHelper.getUsuarioNick());

        btn_tomarFoto.setEnabled(false);
        FotoUsuario(dbHelper.getUsuarioID());


        btn_tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
            }
        });
        btn_eliminarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Eliminar foto")
                        .setMessage("¿Estás seguro que deseas eliminar tu foto?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarFoto(dbHelper.getUsuarioID());
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.AplicacionFrame, new AjustesPerfil());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void solicitarPermisos() {
        if (getContext() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Para Android 13 (API 33) o superior
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES},
                            100
                    );
                }
            } else {
                // Para Android 12 (API 32) o inferior
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                            100
                    );
                }
            }
        }
    }

    private void abrirCamara() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33) o superior
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Permiso necesario")
                        .setMessage("Los permisos son necesarios para usar esta funcionalidad.")
                        .setPositiveButton("Aceptar", null)
                        .show();
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } else {
            // Android 12 (API 32) o inferior
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Permiso necesario")
                        .setMessage("Los permisos son necesarios para usar esta funcionalidad.")
                        .setPositiveButton("Aceptar", null)
                        .show();
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BDsqlite dbHelper = new BDsqlite(getActivity());

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 600, 800, true);;
                fotoB64 = convertirBitmapABase64(resizedBitmap);

                    SubirFoto subirFoto = new SubirFoto();
                    Bundle bundle = new Bundle();
                    bundle.putString("clave",fotoB64);
                    subirFoto.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.AplicacionFrame, subirFoto);
                    transaction.addToBackStack(null);
                    transaction.commit();


            }
        }
    }
    private String convertirBitmapABase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
    }
    public static Bitmap redimensionarImagen(Bitmap original, int anchoDeseado, int altoDeseado) {
        return Bitmap.createScaledBitmap(original, anchoDeseado, altoDeseado, true);
    }
    public Bitmap base64ToBitmap(String base64String) {
        // Decodificar el Base64 a un arreglo de bytes
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    private void FotoUsuario(int id){
        Call<Fotos> call = fotoUsuarioControlador.getUsuarioFoto(id);
        call.enqueue(new Callback<Fotos>() {
            @Override
            public void onResponse(Call<Fotos> call, Response<Fotos> response) {
                if (response.isSuccessful()) {
                    foto = response.body();

                    if(foto != null && esFechaValida(foto.getFECHA())){
                        btn_tomarFoto.setEnabled(false);
                        Bitmap bitmap = base64ToBitmap(foto.getFOTO());
                        btn_tomarFoto.setImageBitmap(bitmap);
                        btn_tomarFoto.setBackground(null);
                        btn_eliminarFoto.setEnabled(true);
                        btn_eliminarFoto.setVisibility(View.VISIBLE);
                    }else{
                        btn_tomarFoto.setEnabled(true);
                    }


                } else {
                }
            }
            @Override
            public void onFailure(Call<Fotos> call, Throwable t) {
                btn_tomarFoto.setEnabled(true);
                t.printStackTrace();
            }
        });
    }
    private void eliminarFoto(int id) {
        Call<ResponseBody> call = fotoUsuarioControlador.eliminarFoto(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        btn_tomarFoto.setEnabled(true);
                        String responseBody = response.body().string();
                        JSONObject responseJson = new JSONObject(responseBody);
                        String status = responseJson.getString("status");
                        String message = responseJson.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.AplicacionFrame, new PagInicio());
                            transaction.addToBackStack(null);
                            transaction.commit();
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
    private boolean esFechaValida(String fechaFoto) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaFotoDate = formatoFecha.parse(fechaFoto);
            Date fechaHoy = formatoFecha.parse(formatoFecha.format(new Date()));

            return !fechaFotoDate.before(fechaHoy);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}