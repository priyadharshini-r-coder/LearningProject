package com.example.learningproject.parsers.remote;

import com.example.learningproject.parsers.model.FCMResponse;
import com.example.learningproject.parsers.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAFdAjWAA:APA91bFMl9Yp7R1KxeINme-o4iCREbUlNhU2HkdhfAcXynwMdQB_LBcTFyjSlAZ0lyWZpZg4CCG3lU31iLLjcU4ztFXKvCnS_7uqP14HLQMIZxrPceWhAz356NFtlWxDgzSwxa4ClM3q"

    })

    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
