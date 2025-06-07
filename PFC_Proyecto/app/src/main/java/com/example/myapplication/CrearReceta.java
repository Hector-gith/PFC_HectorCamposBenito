package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.SubirFotoControlador;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CrearReceta extends Fragment {

    private String foto;
    private String descripcion;
    private double lat;
    private double lon;
    private TextView tv_pregunta;
    private EditText ingredientes;
    private EditText restaurante;

    private EditText preparacion;

    private Button btn_ubicacion;
    private Button btn_subir;
    private Button btn_receta;
    private Button btn_restaurante;

    private SubirFotoControlador subirFotoControlador;

    private FusedLocationProviderClient fusedLocationClient;

    private MapView mapView;
    private GoogleMap googleMap;
    private int previousSoftInputMode;


    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public CrearReceta() {

    }

    public static CrearReceta newInstance(String param1, String param2) {
        CrearReceta fragment = new CrearReceta();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            foto = getArguments().getString("foto");
            descripcion = getArguments().getString("descripcion");

            Bundle bundle = new Bundle();
            bundle.putString("foto",null);
            setArguments(bundle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento

        return inflater.inflate(R.layout.fragment_crear_receta, container, false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previousSoftInputMode = requireActivity().getWindow().getAttributes().softInputMode;
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        TextInputLayout layoutIngredientes = view.findViewById(R.id.textInputLayout_ingredientes);
        TextInputLayout layoutPreparacion = view.findViewById(R.id.textInputLayout_Preparación);
        TextInputLayout layoutRestaurante = view.findViewById(R.id.textInputLayout_restaurante);
        tv_pregunta = view.findViewById(R.id.TV_pregunta);
        btn_subir = view.findViewById(R.id.BTN_subir);
        btn_receta = view.findViewById(R.id.BTN_receta);
        btn_restaurante = view.findViewById(R.id.BTN_restaurante);

        ingredientes = view.findViewById(R.id.ED_ingredientes);
        preparacion = view.findViewById(R.id.ED_preparacion);
        restaurante = view.findViewById(R.id.ED_restaurante);


        btn_ubicacion = view.findViewById(R.id.btn_ubicacion);
        mapView = view.findViewById(R.id.mapView);
        BDsqlite dbHelper = new BDsqlite(getActivity());

        // Ocultarlos inicialmente
        layoutIngredientes.setVisibility(View.GONE);
        layoutPreparacion.setVisibility(View.GONE);
        layoutRestaurante.setVisibility(View.GONE);
        mapView.setVisibility(View.GONE);
        btn_ubicacion.setVisibility(View.GONE);
        btn_subir.setVisibility(View.GONE);

        Retrofit retrofit = RetrofitClient.getClient();
        subirFotoControlador = retrofit.create(SubirFotoControlador.class);
        //prueba = view.findViewById(R.id.PRUEBA);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Inicializar MapView

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(map -> googleMap = map);

        // Lógica para solicitar permisos
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        obtenerUbicacion();
                        mostrarUbicacionEnMapa();
                    } else {
                    }
                });

        btn_receta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutIngredientes.setVisibility(View.VISIBLE);
                layoutPreparacion.setVisibility(View.VISIBLE);
                btn_subir.setVisibility(View.VISIBLE);

                btn_receta.setVisibility(View.GONE);
                btn_restaurante.setVisibility(View.GONE);
                tv_pregunta.setVisibility(view.GONE);
            }
        });
        btn_restaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutRestaurante.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.VISIBLE);
                btn_ubicacion.setVisibility(View.VISIBLE);
                btn_subir.setVisibility(View.VISIBLE);


                btn_receta.setVisibility(View.GONE);
                btn_restaurante.setVisibility(View.GONE);
                tv_pregunta.setVisibility(view.GONE);
            }
        });
        btn_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    obtenerUbicacion();
                    mostrarUbicacionEnMapa();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
        });
        btn_subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Subir foto")
                        .setMessage("¿Estás seguro que deseas subir esta foto?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(layoutRestaurante.getVisibility() == View.VISIBLE){
                                    if(lat==0&&lon==0){
                                        Toast.makeText(getActivity(),"Active o selecione la ubicación", Toast.LENGTH_SHORT).show();
                                    }else if(restaurante.getText().toString().trim().equals("")){
                                        Toast.makeText(getActivity(),"Ingrese el nombre del restaurante", Toast.LENGTH_SHORT).show();
                                    }else{
                                        SubirFotos(dbHelper.getUsuarioID(), foto, descripcion, ingredientes.getText().toString(), preparacion.getText().toString(),restaurante.getText().toString(), lat, lon);

                                    }
                                }else{
                                    if(ingredientes.getText().toString().trim().equals("")){
                                        Toast.makeText(getActivity(),"Complete el cuadro de los ingredientes", Toast.LENGTH_SHORT).show();

                                    }else if(preparacion.getText().toString().trim().equals("")){
                                        Toast.makeText(getActivity(),"Complete el cuadro de la preparación", Toast.LENGTH_SHORT).show();

                                    }else{
                                        SubirFotos(dbHelper.getUsuarioID(), foto, descripcion, ingredientes.getText().toString(), preparacion.getText().toString(),restaurante.getText().toString(), lat, lon);
                                    }
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.AplicacionFrame, new PagInicio());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        })
                        .show();
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void obtenerUbicacion() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                         lat = location.getLatitude();
                         lon = location.getLongitude();
                        //prueba.setText(""+lon);
                    } else {
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void mostrarUbicacionEnMapa() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
            if (location != null && googleMap != null) {
                LatLng ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(ubicacion).title("Mi ubicación"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
            }
        });
    }
    private void SubirFotos(int ID, String foto, String descripcion, String ingredientes, String preparacion, String restaurante, double latitud, double longitud) {
        Call<ResponseBody> call = subirFotoControlador.postFoto(ID, foto, descripcion, ingredientes, preparacion, restaurante, latitud, longitud);
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
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.AplicacionFrame, new PagInicio());
                            transaction.addToBackStack(null);
                            transaction.commit();

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
    // Métodos para ciclo de vida de MapView
    @Override public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
            if (mapViewBundle == null) {
                mapViewBundle = new Bundle();
                outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
            }
            mapView.onSaveInstanceState(mapViewBundle);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().getWindow().setSoftInputMode(previousSoftInputMode);
    }
}
