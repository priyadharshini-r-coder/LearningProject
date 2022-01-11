package com.example.learningproject.entireactivity.service;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.learningproject.R;
import com.example.learningproject.entireactivity.apiRetrofit.IGoogleApi;
import com.example.learningproject.newChanges.common.CommonUrl;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetRiderFragment extends BottomSheetDialogFragment {
    String mLocation,mDestination;

    boolean isTapOnMap;

    IGoogleApi mService;

    TextView txtCalculate , txtLocation , txtDestination;


    public static BottomSheetRiderFragment newInstance(String location, String destination,boolean isTapOnMap)
    {
        BottomSheetRiderFragment f = new BottomSheetRiderFragment();
        Bundle args = new Bundle();
        args.putString("location" , location);
        args.putString("destination",destination);
        args.putBoolean("isTapOnMap" , isTapOnMap);
        f.setArguments(args);
        return f;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = getArguments().getString("location");
        mDestination = getArguments().getString("destination");
        isTapOnMap =getArguments().getBoolean("isTapOnMap");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_rider , container , false);
        txtLocation = (TextView)view.findViewById(R.id.txtLocation);
        txtDestination = (TextView)view.findViewById(R.id.txtDestination);
        txtCalculate = (TextView)view.findViewById(R.id.txtCalculate);


        mService = (IGoogleApi) CommonUrl.getGoogleService();
        getPrice(mLocation,mDestination);

        //Get Data
        if (!isTapOnMap) {
            //Call this fragment from Place Autocomplete Text view
            txtLocation.setText(mLocation);
            txtDestination.setText(mDestination);
        }
        return view;


    }

    private void getPrice(String mLocation, String mDestination) {
        String requestUrl = null;

        try{
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?"+
                    "mode=driving&"
                    +"transit_routing_preference=less_driving&"
                    +"origin="+mLocation+"&"
                    +"destination="+mDestination+"&"
                    +"key="+getResources().getString(R.string.google_maps_key);
            Log.e("LINK",requestUrl);//print for debbugging purposes
            mService.getPath(requestUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //Get Object
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray routes = jsonObject.getJSONArray("routes");

                        JSONObject object = routes.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");


                        JSONObject legsObject = legs.getJSONObject(0);

                        //Get Distance
                        JSONObject distance = legsObject.getJSONObject("distance");
                        String distance_text = distance.getString("text");

                        //use regex to extract double from String
                        //15.02
                        //this regex will remove all that is not digit
                        Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));



                        //Get time
                        JSONObject time = legsObject.getJSONObject("duration");
                        String time_text = time.getString("text");
                        Integer time_value = Integer.parseInt(time_text.replaceAll("\\D+", ""));


//                        String final_calculate = String.format("%s + %s = $%.2f " , distance_text , time_text,
//                                Common.getPrice(distance_value, time_value));


                        //

                        if (isTapOnMap)
                        {
                            String start_address = legsObject.getString("start_address");
                            String end_address = legsObject.getString("end_address");

                            txtLocation.setText(start_address);
                            txtDestination.setText(end_address);
                        }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("ERROR", t.getMessage());
                }
            });
        }

        catch (Exception ex){
            ex.printStackTrace();
        }



    }
}
