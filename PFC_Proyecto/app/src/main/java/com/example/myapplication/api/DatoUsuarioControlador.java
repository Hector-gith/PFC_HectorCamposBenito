package com.example.myapplication.api;

import com.example.myapplication.modelos.Usuarios;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DatoUsuarioControlador {
    @GET("Datos_Usuario.php")
    Call<Usuarios> getUsuarioDatos(
            @Query("ID") int ID
    );
}
