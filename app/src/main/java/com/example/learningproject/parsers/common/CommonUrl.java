package com.example.learningproject.parsers.common;

import com.example.learningproject.parsers.remote.FCMClient;
import com.example.learningproject.parsers.remote.GoogleMapsAPI;
import com.example.learningproject.parsers.remote.IFCMService;
import com.example.learningproject.parsers.remote.IGoogleAPI;

public class CommonUrl {

    public static final String driver_tb1 = "Drivers";
    public static final String user_driver_tb1 = "DriversInformation";
    public static final String user_rider_tb1 = "RidersInformation";
    public static final String pickup_request_tb1 = "PickupRequest";
    public static final String token_tb1 = "Tokens";

    public static final String fcmURL = "https://fcm.googleapis.com/";
    public static final String googleAPIUrl = "https://maps.googleapis.com";


    private static double base_fare = 2.55;
    private static double time_rate = 0.35;
    private static double distance_rate = 1.75;

    public static double getPrice(double km, int min)
    {
        return (base_fare+(time_rate*min) + (distance_rate*km));
    }






    public static IFCMService getFCMService()
    {
        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }

    public static IGoogleAPI getGoogleService()
    {
        return GoogleMapsAPI.getClient(googleAPIUrl).create(IGoogleAPI.class);

    }

}

