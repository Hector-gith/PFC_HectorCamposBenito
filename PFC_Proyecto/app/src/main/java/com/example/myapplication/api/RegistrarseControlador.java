package com.example.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegistrarseControlador
{
    @POST("Registro_Usuario.php")
    @FormUrlEncoded
    Call<ResponseBody> postUsuario(

            @Field("correo") String correo,
            @Field("nick") String nick,
            @Field("contrasena") String contrasena
    );
}
