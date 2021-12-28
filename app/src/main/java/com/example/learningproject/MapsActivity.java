package com.example.learningproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.learningproject.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity {

    //Initialise variables
   // Button btnLocation;
    public ActivityMapsBinding binding;
   // TextView textView1, textView2, textView3, textView4, textView5;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        binding.btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission
                if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when permission granted
                    getLocation();

                } else {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    //Initialise address List
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //setLatitude
                        binding.text.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude:</b><br></font>" + addressList.get(0).getLatitude()));
                        //setLongitude
                        binding.text1.setText(Html.fromHtml("<font color='#6200EE'><b>Longitude:</b><br></font>" + addressList.get(0).getLongitude()));

                        //setCountryName
                        binding.textView2.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude:</b><br></font>" + addressList.get(0).getCountryCode()));

                        //setlocality
                        binding.textView3.setText(Html.fromHtml("<font color='#6200EE'><b>Locality:</b><br></font>" + addressList.get(0).getLocality()));

                        //address
                        binding.textView4.setText(Html.fromHtml("<font color='#6200EE'><b>Address:</b><br></font>" + addressList.get(0).getAddressLine(0)));
                    }
                    //set latitude on Textview

                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


}