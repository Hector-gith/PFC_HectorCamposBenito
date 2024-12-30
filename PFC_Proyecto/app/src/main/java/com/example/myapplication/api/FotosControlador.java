package com.example.myapplication.api;

import com.example.myapplication.modelos.Amigos;
import com.example.myapplication.modelos.Fotos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FotosControlador {
    @GET("FotosAmigos.php")
    Call<List<Fotos>> getFotosAmigos(
            @Query("id") int id

    );
}
