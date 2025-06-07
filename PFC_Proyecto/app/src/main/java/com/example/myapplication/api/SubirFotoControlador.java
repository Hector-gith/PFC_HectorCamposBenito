package com.example.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SubirFotoControlador {
    @POST("Subir_Foto.php")
    @FormUrlEncoded
    Call<ResponseBody> postFoto(
            @Field("ID") int ID,
            @Field("foto") String foto,
            @Field("descripcion") String descripcion,
            @Field("ingredientes") String ingredientes,
            @Field("preparacion") String preparacion,
            @Field("restaurante") String restaurante,
            @Field("latitud") double latitud,
            @Field("longitud") double longitud



    );
}
