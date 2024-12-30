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

public interface SolicitudesControlador {
    @GET("Solicitudes.php")
    Call<List<Amigos>> getSolicitudes(
            @Query("id") int id

    );

    @POST("Solicitudes.php")
    @FormUrlEncoded
    Call<ResponseBody> gestionarRespuestaSolicitud(

            @Field("id_s") int id_s,
            @Field("id_r") int id_r,
            @Field("aceptado") int aceptado
    );
}
