package com.example.myapplication.api;


import com.example.myapplication.modelos.Usuarios;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SubirFotoPerfilControlador {
    @POST("Subir_FotoPerfil.php")
    @FormUrlEncoded
    Call<ResponseBody> postFotoPerfil(
            @Field("ID") int ID,
            @Field("foto") String foto

            );
}
