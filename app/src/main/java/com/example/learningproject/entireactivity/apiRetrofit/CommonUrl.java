package com.example.learningproject.entireactivity.apiRetrofit;

import android.location.Location;

import com.example.learningproject.entireactivity.model.SigningReport;
import com.example.learningproject.entireactivity.model.User;

public class CommonUrl {

    public static final String driver_tb1 = "Drivers";
    public static final String user_driver_tb1 = "DriversInformation";
    public static final String user_rider_tb1 = "RidersInformation";
    public static final String pickup_request_tb1 = "PickupRequest";
    public static final String token_tb1 = "Tokens";
    public static SigningReport currentUser;


    public static Location mLastLocation = null;


    public static final String baseUrl = "https://maps.googleapis.com";
    public static IGoogleApi getGoogleAPI()
    {
        return RetrofitClient.getClient(baseUrl).create(IGoogleApi.class);
    }
}
