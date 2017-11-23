package com.jonda.munidenunciasapp.service;

import com.jonda.munidenunciasapp.models.Denuncia;
import com.jonda.munidenunciasapp.models.Usuario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    String API_BASE_URL = "https://usuarios-api-honda-ichiro.c9users.io";

    @GET("api/v1/usuarios")
    Call<List<Usuario>> getUsuarios();

    @FormUrlEncoded
    @POST("/api/v1/usuarios")
    Call<ResponseMessage> createUsuario(@Field("nombres") String nombre,
                                        @Field("correo") String correo,
                                        @Field("username") String username,
                                        @Field("password") String password);
//        @Multipart
//        @POST("/api/v1/productos")
//        Call<ResponseMessage> createUsuarioWithImage(
//                @Part("nombres") RequestBody nombre,
//                @Part("correo") RequestBody precio,
//                @Part("detalles") RequestBody detalles,
//                @Part("password") RequestBody password,
//                @Part MultipartBody.Part imagen
//     );







    @GET("api/v1/denuncias")
    Call<List<Denuncia>> getDenuncias();
    @FormUrlEncoded
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> createDenuncia(@Field("usuarios_id") String usuarios_id,
                                         @Field("titulo") String titulo,
                                         @Field("descripcion") String descripcion,
                                         @Field("latitud") String latitud,
                                         @Field("longitud") String longitud,
                                         @Field("ubicacion") String ubicacion);

    @Multipart
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> createDenunciaWithImage(
            @Part("usuarios_id") RequestBody usuarios_id,
            @Part("titulo") RequestBody titulo,
            @Part("descripcion") RequestBody descripcion,
            @Part("latitud") RequestBody latitud,
            @Part("longitud") RequestBody longitud,
            @Part("ubicacion") RequestBody ubicacion,
            @Part MultipartBody.Part imagen
    );


}
