package com.example.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface InicioSesionControlador {
    @POST("Inicio_Sesion.php")
    @FormUrlEncoded
    Call<ResponseBody> postInicioSesion(

            @Field("correo") String correo,
            @Field("contrasena") String contrasena
    );
}
