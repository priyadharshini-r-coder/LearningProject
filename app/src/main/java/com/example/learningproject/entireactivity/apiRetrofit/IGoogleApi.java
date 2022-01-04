package com.example.learningproject.entireactivity.apiRetrofit;

import com.squareup.okhttp.Call;

import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApi {
    @GET
    Call getPath(@Url String url );
}
