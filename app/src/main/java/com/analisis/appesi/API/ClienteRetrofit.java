package com.analisis.appesi.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteRetrofit {
    private static final String BASE_URL = "https://morning-stream-44468.herokuapp.com/api/v2/";
    private static final String BASE_URL2 = "https://ia.v2.cromasesiia.com/";
    private static final String BASe_URL3 = "https://cromasesiia.com/api/v2/";
    private static ClienteRetrofit mInstancia;
    private Retrofit retrofit;

    private ClienteRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private ClienteRetrofit(String BASE_URL2){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private ClienteRetrofit(String BASe_URL3, int x){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASe_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ClienteRetrofit getInstance(){
        mInstancia = new ClienteRetrofit();
        return mInstancia;
    }

    public static synchronized ClienteRetrofit getInstance2(){
        mInstancia = new ClienteRetrofit(BASE_URL2);
        return mInstancia;
    }

    public static synchronized ClienteRetrofit getInstance3(){
        mInstancia = new ClienteRetrofit(BASe_URL3, 1);
        return mInstancia;
    }

    public API getApi(){
        return retrofit.create(API.class);
    }
}
