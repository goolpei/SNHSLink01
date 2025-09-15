package com.example.snhslink.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImgBBApiClient {
    private static final String BASE_URL = "https://api.imgbb.com/1/";
    private static Retrofit retrofit = null;
    private static ImgBBApiService imgBBApiService = null;

    // ✅ Ensure Retrofit client is created only once
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // ✅ Provide ImgBBApiService instance
    public static ImgBBApiService getInstance() {
        if (imgBBApiService == null) {
            imgBBApiService = getClient().create(ImgBBApiService.class);
        }
        return imgBBApiService;
    }
}
