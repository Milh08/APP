package com.analisis.appesi.API;

import com.analisis.appesi.Modelo.A_Iniciar_Sesion_Response;
import com.analisis.appesi.Modelo.Carga_De_Datos_IA;
import com.analisis.appesi.Modelo.Cromann;
import com.analisis.appesi.Modelo.IMG;
import com.analisis.appesi.Modelo.Post_Localizacion;
import com.analisis.appesi.Modelo.RespuestaAI;
import com.analisis.appesi.Modelo.Respuesta_Croma;
import com.analisis.appesi.Modelo.Respuesta_Muestra;
import com.analisis.appesi.Modelo.Respuesta_Municipio;
import com.analisis.appesi.Modelo.User;
import com.analisis.appesi.Modelo.UserLote;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API {
    @FormUrlEncoded
    @POST("login")
    Call<A_Iniciar_Sesion_Response> Datos_Login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("cromann")
    Call<Post_Localizacion> Datos_De_Muestas(
            @Field("latitud") String latitud,
            @Field("longitud") String longitud,
            @Field("lote_id") int lote_id,
            @Field("profundidad") int profundidad,
            @Field("user_id") int user_id
    );

    @GET("users")
    Call<List<User>> getUsuario();

    @GET("user/lotes/{id}")
    Call<List<UserLote>> Informacion_Lotes(@Path("id") int id);

    @GET("municipios/{id}")
    Call<Respuesta_Municipio> Obtener_Region(@Path("id") int id);

    @GET("lote/muestras/{id}")
    Call<List<Respuesta_Croma>> Datos_Croma(@Path("id") int id);

    @GET("cromann/{id}")
    Call<Respuesta_Muestra> informacion_Croma(@Path("id") int id);

    @FormUrlEncoded
    @PUT("cromann/{id}")
    Call<ResponseBody> IA(
            @Path("id") int id,
            @Field("ind_oxg") Boolean indOxg,
            @Field("ind_mat_org") Boolean indMatOrg,
            @Field("ind_trans_sist") Boolean indTransSist,
            @Field("ind_n_elem") Boolean indNElem,
            @Field("ind_romp") Boolean indRomp,
            @Field("ind_mat_viva") Boolean indMatViva,
            @Field("ind_bio") Boolean indBio,
            @Field("ind_pro_n") Boolean indProN
    );


    @PUT("cromann/{id}")
    Call<Carga_De_Datos_IA> ia(
            @Path("id") int id,
            @Body Cromann cromann
    );

    @Multipart
    @PUT("croma/img/{id}")// eliminar este metodo y usar el de arriba
    Call<ResponseBody> cargar_Imagen(
            @Path("id") int id,
            @Part MultipartBody.Part file
    );
    // Nueva funciones para subir imagen
    @Multipart
    @POST("img/")
    Call<IMG> Subir_Img(
            @Part MultipartBody.Part file,
            @Part("data") String id
    );

    @FormUrlEncoded
    @PUT("cromann/{id}")
    Call<ResponseBody> Ruta_Img(
            @Path("id") int id,
            @Field("img") String img
    );
    // Fin de nuevas funciones

    // **** QUEDA PENDIENTE AGREGAR LA RESPUESTA DEL BACK DE IA ****
    @Multipart
    @PUT("upload/")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("uploads/")
    Call<RespuestaAI> uploads(
            @Part MultipartBody.Part file
    );
}
