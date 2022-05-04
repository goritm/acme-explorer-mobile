package com.gori.acmeexplorer.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeolocationService {
    @GET("geo/1.0/direct")
    Call<List<Geolocation>> getGeolocation(@Query("q") String cityName, @Query("appid") String appId);
}
