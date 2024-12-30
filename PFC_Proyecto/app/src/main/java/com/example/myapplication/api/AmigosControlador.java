package com.example.myapplication.api;

import com.example.myapplication.modelos.Amigos;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AmigosControlador {
    @GET("Amigos.php")
    Call<List<Amigos>> getAmigos(
            @Query("id") int id

    );
    @POST("Amigos.php")
    @FormUrlEncoded
    Call<ResponseBody> eliminarAmigo(

            @Field("id") int id,
            @Field("id_a") int id_a
    );
}
