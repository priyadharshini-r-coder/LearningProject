package com.example.learningproject.uberclone.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityCustomerMapsBinding;
import com.example.learningproject.databinding.ActivityDriverMapsBinding;
import com.example.learningproject.uberclone.utils.Constants;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;


public class CustomerMapsActivity extends FragmentActivity implements OnMapReadyCallback
, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {


    private GoogleMap mMap;
    private ActivityCustomerMapsBinding binding;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    FirebaseAuth auth;
    FirebaseUser user;
    String userId;
    private  DatabaseReference customerDatabase;
    LatLng customerPickupLocation;
   DatabaseReference driverAvailableRef;
    private int radius=1;
    private boolean driverFound=false;
    private String driverFoundId;
    FusedLocationProviderClient client;
    LocationCallback callback;
    DatabaseReference driverLocationRef;
    DatabaseReference driversRef;
    private String customerId;
    private Marker driverMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerMapsBinding.inflate(getLayoutInflater());
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        callback=new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };

        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        customerDatabase=FirebaseDatabase.getInstance().getReference().child("Customers Request");
        client=LocationServices.getFusedLocationProviderClient(this);
        customerId=FirebaseAuth.getInstance().getCurrentUser().getUid();


      driverLocationRef =FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers Working");
      driverAvailableRef=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers Available");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                logoutCustomer();
            }
        });
        binding.callCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeoFire geoFire=new GeoFire(customerDatabase);
                geoFire.setLocation(userId,new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()));
                customerPickupLocation= new LatLng(lastLocation.getLatitude(),
                        lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(customerPickupLocation).title(
                        "Pick Up Customer From Here"
                ));
                binding.callCab.setText("Getting your Driver");
                getClosestDriverCab();

            }
        });

        binding.settings.setOnClickListener(view -> {
            Intent intent= new Intent(CustomerMapsActivity.this,SettingsActivity.class);
            intent.putExtra(Constants.IntentRole.role,"Customers");
            startActivity(intent);
        });

    }

    private void getClosestDriverCab() {
        GeoFire geoFire=new GeoFire(driverAvailableRef);
        GeoQuery query=geoFire.queryAtLocation(new GeoLocation(customerPickupLocation.latitude
        ,customerPickupLocation.longitude),radius);
        query.removeAllListeners();
        query.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!driverFound)
                {
                    driverFound=true;
                    driverFoundId=key;
                    driversRef =FirebaseDatabase.getInstance().getReference().child("Users")
                            .child("Drivers").child(driverFoundId);
                    HashMap driverMap=new HashMap();
                    driverMap.put("CustomerRideId",customerId);
                    driversRef.updateChildren(driverMap);
                    gettingDriverLocation();
                    binding.callCab.setText("Looking for Driver Location");


                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                if(!driverFound)
                {
                    radius=radius+1;
                    getClosestDriverCab();
                }
            }
        });


    }

    private void gettingDriverLocation() {
     driverLocationRef.child(driverFoundId).child("l")
             .addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     if(snapshot.exists())
                     {
                         List<Object> driverLocationMap=(List<Object>) snapshot.getValue();
                         double locationLat=0;
                         double locationLng=0;
                         binding.callCab.setText("Driver Found");
                         if(driverLocationMap.get(0) !=null)
                         {
                             locationLat =Double.parseDouble(driverLocationMap.get(0).toString());

                         }
                         if(driverLocationMap.get(1) !=null)
                         {
                             locationLng =Double.parseDouble(driverLocationMap.get(1).toString());
                         }
                         LatLng driverLatLng=new LatLng(locationLat,locationLng);
                         if(driverMarker !=null)
                         {
                             driverMarker.remove();
                         }

                         //customer location
                         Location location1=new Location("");
                         location1.setLatitude(customerPickupLocation.latitude);
                         location1.setLongitude(customerPickupLocation.longitude);
                         //Driver location

                         Location location2=new Location("");
                         location2.setLatitude(driverLatLng.latitude);
                         location2.setLongitude(driverLatLng.longitude);

                          float distance=location1.distanceTo(location2);
                          binding.callCab.setText("Driver Found"+String.valueOf(distance));
                         driverMarker=mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Your Driver is here"));

                     }

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Need permission")
                        .setMessage("Please grant permission.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(CustomerMapsActivity.this, new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                }, 1);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(CustomerMapsActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1);
            }
        }
       // mMap.setMyLocationEnabled(true);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case 1:
                if(grantResults.length>0  && grantResults [0]==PackageManager.PERMISSION_GRANTED )
                {
                    if(ContextCompat.checkSelfPermission(CustomerMapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        client.requestLocationUpdates(locationRequest,callback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Enable your Location",Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    protected  synchronized void buildGoogleApiClient()
    {
        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastLocation= location;
        LatLng latLng =new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();

    }
    private void logoutCustomer() {
        Intent intent= new Intent(CustomerMapsActivity.this,WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);


    }
}