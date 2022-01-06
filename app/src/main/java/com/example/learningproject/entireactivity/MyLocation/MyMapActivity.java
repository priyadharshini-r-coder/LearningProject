package com.example.learningproject.entireactivity.MyLocation;

import static java.util.Locale.getDefault;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityMyMapBinding;
import com.example.learningproject.entireactivity.EntireMainActivity;
import com.example.learningproject.entireactivity.apiRetrofit.CommonUrl;
import com.example.learningproject.entireactivity.apiRetrofit.IGoogleApi;
import com.example.learningproject.entireactivity.utils.DataParser;
import com.example.learningproject.entireactivity.utils.FetchUrl;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ActivityMyMapBinding binding;
    private GoogleMap mMap;

    // Play Services
    private  static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private  static final int PLAY_SERVICE_RES_REQUEST = 7001;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    DatabaseReference drivers;
    GeoFire geoFire;

    Marker mCurrent;


    SupportMapFragment mapFragment;

    // Car animation
    private List<LatLng> polyLineList;
    private Marker carMarker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition, currentPosition;
    private int index, next;
    // private Button btnGo;
    private SupportPlaceAutocompleteFragment places;
    private String destination;
    private PolylineOptions polylineOptions, blackpolylineOptions;
    private Polyline blackPolyline, grayPolyline;

    private IGoogleApi mService;

    Runnable drawPathRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if(index < polyLineList.size() -1)
            {
                index++;
                next = index + 1;
            }

            if(index < polyLineList.size() -1)
            {
                startPosition = polyLineList.get(index);
                endPosition = polyLineList.get(next);
            }

            final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    v = valueAnimator.getAnimatedFraction();
                    lng = v * endPosition.longitude+(1-v) * startPosition.longitude;
                    lat = v * endPosition.latitude+(1-v) * startPosition.latitude;
                    LatLng newPos = new LatLng(lat, lng);
                    carMarker.setPosition(newPos);
                    carMarker.setAnchor(0.5f, 0.5f);
                    carMarker.setRotation(getBearing(startPosition, newPos));
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(newPos)
                                    .zoom(15.5f)
                                    .build()
                    ));

                }
            });

            valueAnimator.start();
            handler.postDelayed(this, 3000);
        }
    };

    private float getBearing(LatLng startPosition, LatLng endPosition)
    {
        double lat = Math.abs(startPosition.latitude - endPosition.latitude);
        double lng = Math.abs(startPosition.longitude - endPosition.longitude);

        if(startPosition.latitude < endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng/lat)));
        else if(startPosition.latitude >= endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng/lat))) + 90);
        else if(startPosition.latitude >= endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng/lat)) + 180);
        else if(startPosition.latitude < endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng/lat))) + 270);

        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=DataBindingUtil.setContentView(this,R.layout.activity_my_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // init view


      binding.locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOnline) {
                if(isOnline)
                {
                    startLocationUpdates();
                    displayLocation();
                    Snackbar.make(mapFragment.getView(), "You are online", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    stopLocationUpdates();
                    mCurrent.remove();
                    mMap.clear();
//                    handler.removeCallbacks(drawPathRunnable);
                    Snackbar.make(mapFragment.getView(), "You are offline",Snackbar.LENGTH_SHORT).show();
                }
            }

        });
        binding.LogoutButton.setOnClickListener(view -> {
            stopLocationUpdates();
            //mCurrent.remove();
         //  mMap.clear();
               FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MyMapActivity.this, EntireMainActivity.class);
            startActivity(intent);
            finish();
        });
        polyLineList = new ArrayList<>();
         /*binding.btnGo.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(binding.locationSwitch.isChecked())
                 {
                     destination = binding.edtPlace.getText().toString();
                     destination = destination.replace(" ", "+");

                     getDirection();
                 }
                 else
                 {
                     Toast.makeText(MyMapActivity.this,"Please change your status to online", Toast.LENGTH_SHORT).show();
                 }
             }
         });
*/


places = (SupportPlaceAutocompleteFragment)getSupportFragmentManager().findFragmentById(R.id.place_autocomplete);
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place)
            {
                if(binding.locationSwitch.isChecked())
                {
                    destination = place.getAddress().toString();
                    destination = destination.replace(" ", "+");

                    getDirection();
                }
                else
                {
                    Toast.makeText(MyMapActivity.this,"Please change your status to online", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MyMapActivity.this, ""+status.toString(),Toast.LENGTH_SHORT).show();
            }
        });



        // Geo Fire
        drivers = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child("Users Location");
        geoFire = new GeoFire(drivers);

        setUpLocation();

        mService = CommonUrl.getGoogleAPI();
    }

    private void getDirection()
    {
        currentPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        String requestApi;

        try
        {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?"+
                    "mode=driving&"+
                    "transit_routing_preference = less_driving&"+
                    "origin+"+currentPosition.latitude+","+currentPosition.longitude+"&"+
                    "destination="+destination+"+"+
                    "key="+getResources().getString(R.string.google_maps_key);
            Log.d("UBER",requestApi); //Print Url for debug

            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            FetchUrl url=new FetchUrl();
                           // decodePoly(response);
                            url.execute(requestApi);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(MyMapActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
                        }







                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    // Press Ctrl+O
    // Becsuse we request runtime permission, we need override OnRequestPermissionResult method


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();

                        if (binding.locationSwitch.isChecked()) {
                            displayLocation();
                        }
                    }
                }
        }
    }

    private void setUpLocation()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Request Runtime Permission

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_REQUEST_CODE);
        }
        else
        {
            if(checkPlayServices())
            {
                buildGoogleApiClient();
                createLocationRequest();

                if(binding.locationSwitch.isChecked())
                {
                    displayLocation();
                }
            }
        }
    }

    private void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private boolean checkPlayServices()
    {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RES_REQUEST).show();
            else
            {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void stopLocationUpdates()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

    private void displayLocation()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null)
        {
            if(binding.locationSwitch.isChecked())
            {
                final double latitude = mLastLocation.getLatitude();
                final double longitude = mLastLocation.getLongitude();

                // Update to firebase
                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        // Add Marker

                        if(mCurrent != null)
                            mCurrent.remove(); // remove the previous marker
                        mCurrent = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                                .position(new LatLng(latitude, longitude))
                                .title("YOU"));

                        // Move camera to this position

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),15.0f));

                    }
                });
            }
        }
        else
        {
            Log.d("ERROR", "displayLocation: Cannot get your location");
        }
    }

    private void rotateMarker(final Marker mCurrent, final float i, GoogleMap mMap)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = mCurrent.getRotation();
        final long duration = 1500;

        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float)elapsed/duration);
                float rot = t*i+(1-t)*startRotation;
                mCurrent.setRotation(-rot > 180? rot/2:rot);

                if(t<0.1)
                {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void startLocationUpdates()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        displayLocation();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

//    private ActivityMyMapBinding binding;
//    public static String pickup_place;
//    public static String drop_place;
//    private GoogleMap mMap;
//    //    Bundle bundle=new Bundle();
//    ArrayList<LatLng> MarkerPoints;
//    GoogleApiClient mGoogleApiClient;
//    Location mLastLocation;
//    Marker mCurrLocationMarker;
//    LocationRequest mLocationRequest;
//    PlaceAutocompleteFragment placeAutoComplete1,placeAutoComplete2;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//       binding=DataBindingUtil. setContentView(this,R.layout.activity_my_map);
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkLocationPermission();
//        }
//        // Initializing
//        MarkerPoints = new ArrayList<>();
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//
//
//
//
//
//    }
//
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        mMap.getUiSettings().setMapToolbarEnabled(false);
//        mMap.getUiSettings().setZoomControlsEnabled(false);
//
//
//
//        //Initialize Google Play Services
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                buildGoogleApiClient();
//                mMap.setMyLocationEnabled(true);
//            }
//        }
//        else {
//            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
//        }
//        //auto complete box
//
//        placeAutoComplete1 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
////        placeAutoComplete1.setHint("Enter Source");
//        placeAutoComplete1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//
//
//            @Override
//            public void onPlaceSelected(Place place) {
//                Log.d("Maps", "Place selected: " + place.getName());
//                //Toast.makeText(MapsActivity.this,place.getName(), Toast.LENGTH_SHORT).show();
//                mMap.clear();
//                MarkerPoints.clear();
//                mMap.addMarker(new MarkerOptions()
//                        .position(place.getLatLng())
//                        .title(place.getName().toString()));
//                MarkerPoints.add(place.getLatLng());
//                MyMapActivity.pickup_place= (String) place.getAddress();
//            }
//            @Override
//            public void onError(Status status) {
//                Log.d("Maps", "An error occurred: " + status);
//            }
//        });
//
//        placeAutoComplete2 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete2);
//    //    placeAutoComplete2.setHint("Enter Destination");
//        placeAutoComplete2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                Log.d("Maps", "Place selected: " + place.getName());
//                //Toast.makeText(MapsActivity.this,place.getName(), Toast.LENGTH_SHORT).show();
//
//                mMap.addMarker(new MarkerOptions()
//                        .position(place.getLatLng())
//                        .title(place.getName().toString())
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                );
//                MyMapActivity.drop_place = (String) place.getAddress();
//                MarkerPoints.add(place.getLatLng());
//                if (MarkerPoints.size() == 2) {
//                    LatLng origin = MarkerPoints.get(0);
//                    LatLng dest = MarkerPoints.get(1);
//
//                    // Getting URL to the Google Directions API
//                    String url = getUrl(origin, dest);
//
//                    FetchUrl FetchUrl = new FetchUrl();
//
//                    // Start downloading json data from Google Directions API
//                    FetchUrl.execute(url);
//                    //move map camera
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//
//                }
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.d("Maps", "An error occurred: " + status);
//            }
//        });
//        Log.d("MyMarker","size"+MarkerPoints.size());
//
//      /*  Button clrbtn=(Button) findViewById(R.id.btnclr);
//        clrbtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mMap.clear();
//                MarkerPoints.clear();
//                placeAutoComplete1.setText("");
//                placeAutoComplete2.setText("");
//            }
//        });
//*/
//      /*  Button subbtn=(Button) findViewById(R.id.btnsubmit);
//        subbtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (MarkerPoints.size() == 2) {
////                    bundle.putParcelableArrayList("geolist", MarkerPoints);
////                    Fragment fragobj=new OlaRideFragment();
////                    fragobj.setArguments(bundle);
//                 *//*   startActivity(new Intent(MapsActivity.this, MainActivity.class)
//                            .putExtra("mylist", MarkerPoints)
//                            .putExtra("pickup",pickup_place)
//                            .putExtra("drop",drop_place)
//
//*//*
//
//                    ;
//                    //finish();
//
//                }
//                else
//                {
//                    Toast.makeText(MyMapActivity.this, "Please Enter 2 location", Toast.LENGTH_SHORT).show();
//                }
//
//                //finish();
//            }
//        });
//
//*/
//
//
//        //ends here
//
//        // Setting onclick event listener for the map
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//
//
//            @Override
//            public void onMapClick(LatLng point) {
//
//                // Already two locations
//                if (MarkerPoints.size() > 1) {
//                    MarkerPoints.clear();
//                    mMap.clear();
//
//                }
//
//                // Adding new item to the ArrayList
//                MarkerPoints.add(point);
//
//                // Creating MarkerOptions
//                MarkerOptions options = new MarkerOptions();
//
//                // Setting the position of the marker
//                options.position(point);
//
//                /*
//                 * For the start location, the color of marker is GREEN and
//                 * for the end location, the color of marker is RED.
//                 */
//                Log.d("MarkerPoints", "current: " + MarkerPoints);
//                if (MarkerPoints.size() == 1) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                } else if (MarkerPoints.size() == 2) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                }
//
//
//                // Add new marker to the Google Map Android API V2
//                mMap.addMarker(options);
//
//                // Checks, whether start and end locations are captured
//                if (MarkerPoints.size() == 2) {
//                    LatLng origin = MarkerPoints.get(0);
//                    LatLng dest = MarkerPoints.get(1);
//
//                    // Getting URL to the Google Directions API
//                    String url = getUrl(origin, dest);
//                    Log.d("onMapClick", url);
//                    FetchUrl FetchUrl = new FetchUrl();
//
//
//                    Geocoder geocoder = new Geocoder(getApplicationContext(), getDefault());
//                    try {
//                        List<Address> listAddresses = geocoder.getFromLocation(origin.latitude, origin.longitude, 1);
//                        List<Address> listAddresses2 = geocoder.getFromLocation(dest.latitude, dest.longitude, 1);
//
//                        if(null!=listAddresses&&listAddresses.size()>0){
//                            MyMapActivity.pickup_place = listAddresses.get(0).getAddressLine(0);
//                        }
//
//                        if(null!=listAddresses&&listAddresses2.size()>0){
//                            MyMapActivity.drop_place = listAddresses2.get(0).getAddressLine(0);
//                        }
//                        placeAutoComplete1.setText(MyMapActivity.pickup_place);
//                        placeAutoComplete2.setText(MyMapActivity.drop_place);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    // Start downloading json data from Google Directions API
//                    FetchUrl.execute(url);
//                    //move map camera
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//
//                }
//
//            }
//        });
//
//    }
//
//    private String getUrl(LatLng origin, LatLng dest) {
//
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//
//        // Sensor enabled
//        String sensor = "sensor=false";
//
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//
//
//        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//    }
//
//
//
//
//
//
//
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        }
//        statusCheck();
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
//
//        //Place current location marker
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
////        MarkerOptions markerOptions = new MarkerOptions();
////        markerOptions.position(latLng);
////        markerOptions.title("Current Position");
////        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
////        mCurrLocationMarker = mMap.addMarker(markerOptions);
//
//        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//
//        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//    public void checkLocationPermission(){
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Asking user if explanation is needed
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//        }
////        else {
////        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted. Do the
//                    // contacts-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                        mMap.setMyLocationEnabled(true);
//                    }
//
//                } else {
//
//                    // Permission denied, Disable the functionality that depends on this permission.
//                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            // other 'case' lines to check for other permissions this app might request.
//            // You can add here other case statements according to your requirement.
//        }
//    }
//
//    public void statusCheck() {
//        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        assert manager != null;
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps();
//
//        }
//    }
//
//    private void buildAlertMessageNoGps() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        new AlertDialog.Builder(this)
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    public void onClick(DialogInterface dialog, int id) {
//                        MyMapActivity.this.finishAffinity();
//
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }
//}