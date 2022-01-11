package com.example.learningproject.newChanges.remote;

import com.example.learningproject.newChanges.model.FCMResponse;
import com.example.learningproject.newChanges.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAT9eP3yQ:APA91bGDfqMxpohJVpDUxl96BID_n_fjQkC6j4iAYHqt3LsP12yTLlo8yqjY0GR3TXfQ_KeBfreimjLTyAs03QU8hvBUalWAnppD4gQKosWtSavKSmMbjA5HJ__U50qmtQpVHLkl7JpU"

    })

    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
