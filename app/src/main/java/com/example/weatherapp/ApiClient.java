package com.example.weatherapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/") // Base URL for OpenWeather API
                    .addConverterFactory(GsonConverterFactory.create()) // Converter for JSON to Java object
                    .build();
        }
        return retrofit;
    }
}
