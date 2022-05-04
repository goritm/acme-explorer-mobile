package com.gori.acmeexplorer.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static Retrofit retrofitInstance;

    private RetrofitSingleton(){

    }

    public static Retrofit getRetrofitInstance(){
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitInstance;
    }
}
