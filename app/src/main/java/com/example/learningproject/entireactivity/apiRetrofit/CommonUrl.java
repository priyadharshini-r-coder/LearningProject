package com.example.learningproject.entireactivity.apiRetrofit;

public class CommonUrl {
    public static final String baseUrl = "https://maps.googleapis.com";
    public static IGoogleApi getGoogleAPI()
    {
        return RetrofitClient.getClient(baseUrl).create(IGoogleApi.class);
    }
}
