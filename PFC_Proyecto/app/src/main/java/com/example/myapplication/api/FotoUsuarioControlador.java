package com.example.myapplication.api;

import com.example.myapplication.modelos.Fotos;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FotoUsuarioControlador {
    @GET("Foto_Usuario.php")
    Call<Fotos> getUsuarioFoto(
            @Query("ID") int ID
    );
    @POST("Foto_Usuario.php")
    @FormUrlEncoded
    Call<ResponseBody> eliminarFoto(

            @Field("id") int id
    );
}
