package com.example.snhslink.Network;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ImgBBApiService {
    @Multipart
    @POST("upload")
    Call<ImgBBResponse> uploadImage(
            @Query("key") String apiKey,
            @Part MultipartBody.Part image
    );

    @DELETE("delete/{deleteHash}")
    Call<Void> deleteImage(
            @Path("deleteHash") String deleteHash,
            @Query("key") String apiKey // ✅ API key as a query parameter
    );

}
