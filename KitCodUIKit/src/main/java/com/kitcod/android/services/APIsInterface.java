package com.kitcod.android.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface APIsInterface {
    @GET()
    @Streaming
    Call<ResponseBody> downloadImage(@Url String fileUrl);
}
