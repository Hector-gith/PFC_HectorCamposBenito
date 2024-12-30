package com.example.myapplication;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.api.DatoUsuarioControlador;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.SubirFotoPerfilControlador;
import com.example.myapplication.modelos.Usuarios;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AjustesPerfil extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageButton btn_fotoPerfil,btn_cerrarSesion,btn_volveratras;
    private ImageView img_fotoPerfil;
    private String fotoBase64;
    private SubirFotoPerfilControlador subirFotoPerfilControlador;




    public AjustesPerfil() {
    }


    public static AjustesPerfil newInstance(String param1, String param2) {
        AjustesPerfil fragment = new AjustesPerfil();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajustes_perfil, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        solicitarPermisos();

        btn_fotoPerfil = view.findViewById(R.id.BTN_fotoPerfil);
        btn_cerrarSesion = view.findViewById(R.id.BTN_cerrarSesion);
        btn_volveratras = view.findViewById(R.id.BTN_volveratras);
        img_fotoPerfil = view.findViewById(R.id.IMG_fotoPerfil);

        Retrofit retrofit = RetrofitClient.getClient();
        subirFotoPerfilControlador =retrofit.create(SubirFotoPerfilControlador.class);
        BDsqlite dbHelper = new BDsqlite(getActivity());

        btn_fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] opciones = {"Tomar foto", "Seleccionar de la galería"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Selecciona una opción");
                builder.setItems(opciones, (dialog, which) -> {
                    if (which == 0) {
                        abrirCamara();
                    } else if (which == 1) {
                        abrirGaleria();
                    }
                });
                builder.show();
            }
        });
        btn_volveratras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.AplicacionFrame, new PagInicio());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
              btn_cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmar Cierre de Sesión")
                        .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.CerrarSesion(dbHelper.getUsuarioID());
                                getActivity().finish();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }
    private void abrirCamara() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android API 33 o superior
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
            // Android API 32 o inferior
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

    private void abrirGaleria() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Permiso necesario")
                        .setMessage("Los permisos son necesarios para usar esta funcionalidad, puedes activarlos desde l")
                        .setPositiveButton("Aceptar", null)
                        .show();
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Permiso necesario")
                        .setMessage("Los permisos son necesarios para usar esta funcionalidad.")
                        .setPositiveButton("Aceptar", null)
                        .show();
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        }

    }
    private void solicitarPermisos() {
        if (getContext() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Para API 33 o superior
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                            100
                    );
                }
            } else {
                // Para API 32 o inferior
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                            100
                    );
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
                // Imagen capturada con la cámara
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Bitmap resizedBitmap = redimensionarImagen(imageBitmap, 360, 480);

                    fotoBase64 = convertirBitmapABase64(resizedBitmap);
                    SubirFotoPerf(dbHelper.getUsuarioID(),fotoBase64);
                    img_fotoPerfil.setImageBitmap(resizedBitmap);

            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                // Imagen seleccionada de la galería
                try {
                    Uri imageUri = data.getData();
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    Bitmap resizedBitmap = redimensionarImagen(imageBitmap, 360, 480);

                        fotoBase64 = convertirBitmapABase64(resizedBitmap);
                        SubirFotoPerf(dbHelper.getUsuarioID(),fotoBase64);
                        img_fotoPerfil.setImageBitmap(resizedBitmap);

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
    }

    private String convertirBitmapABase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
    }
    public static Bitmap redimensionarImagen(Bitmap original, int anchoDeseado, int altoDeseado) {
        return Bitmap.createScaledBitmap(original, anchoDeseado, altoDeseado, true);
    }
    private void SubirFotoPerf(int ID, String foto) {
        Call<ResponseBody> call = subirFotoPerfilControlador.postFotoPerfil(ID, foto);
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
                            Log.d("API_RESPONSE", message);
                            BDsqlite dbHelper = new BDsqlite(getActivity());
                            dbHelper.setUsuarioFoto_perfil(foto,ID);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


                        } else {
                            Log.d("API_RESPONSE", message);
                            Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
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