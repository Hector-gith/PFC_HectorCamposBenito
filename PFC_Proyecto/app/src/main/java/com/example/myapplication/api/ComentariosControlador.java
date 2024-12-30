package com.example.myapplication.api;

import com.example.myapplication.modelos.Amigos;
import com.example.myapplication.modelos.Comentarios;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ComentariosControlador {
    @GET("Comentarios.php")
    Call<List<Comentarios>> getComentarios(
            @Query("id") int id

    );
    @POST("Comentarios.php")
    @FormUrlEncoded
    Call<ResponseBody> comentar(

            @Field("id") int id,
            @Field("id_f") int id_f,
            @Field("comentario") String comentario

    );
}
