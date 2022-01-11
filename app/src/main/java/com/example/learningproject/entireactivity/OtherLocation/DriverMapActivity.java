package com.example.learningproject.entireactivity.OtherLocation;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.learningproject.R;


import com.example.learningproject.entireactivity.apiRetrofit.CommonUrl;
import com.example.learningproject.entireactivity.apiRetrofit.IGoogleApi;

import com.example.learningproject.entireactivity.model.Rider;
import com.example.learningproject.entireactivity.model.Token;

import com.example.learningproject.entireactivity.service.BottomSheetRiderFragment;
import com.example.learningproject.entireactivity.service.CustomInfoWindow;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverMapActivity extends  FragmentActivity   implements NavigationView.OnNavigationItemSelectedListener ,
        OnMapReadyCallback ,
        GoogleApiClient.ConnectionCallbacks ,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener

{

    SupportMapFragment mapFragment;

    //Location
    private GoogleMap mMap;

    //Play Services
    private static final int MY_PERMISSION_REQUEST_CODE = 7192;
    private static final int PLAY_SERVICE_RES_REQUEST = 300193;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;




    DatabaseReference ref;
    GeoFire geoFire;

    Marker mUserMarker , markerDestination;


    //Bottomsheet
    ImageView imgExpandable;
    BottomSheetRiderFragment mBottomSheet;
    Button btnPickupRequest;
    int distance = 3; //This is the maximum distance we can search in
    private static final int LIMIT = 3;

    //Send Alert
    IGoogleApi mService;

    //Presence System
    DatabaseReference driversAvailable;

    PlaceAutocompleteFragment place_location ,place_destination;
    AutocompleteFilter typeFilter;

    String mPlaceLocation , mPlaceDestination;





    boolean isDriverFound = false;
    String driverId = "";
    int radius = 1 ; //This is within 1 Kilometre




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);


        mService = CommonUrl.getGoogleAPI();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Maps
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






        //Init View
        imgExpandable = (ImageView)findViewById(R.id.imgExpandable);


        btnPickupRequest = (Button) findViewById(R.id.btnPickupRequest);
        btnPickupRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isDriverFound)
                    requestPickupHere(FirebaseAuth.getInstance().getCurrentUser().getUid());
                else
                    sendRequestToDriver(driverId);

            }});


        place_destination = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_destination);
        place_location = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_location);
        typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setTypeFilter(3)
                .build();


        //Event
        place_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mPlaceLocation = place.getAddress().toString();
                //Remove the old marker first because now we selecting new destination apart from where we are

                mMap.clear();//these one clears so as to make way for the new marker because we are removing the old one

                //So now we add marker at new location
                mUserMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup))
                        .title("Pickup Here"));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

            }

            @Override
            public void onError(Status status) {

            }
        });
        place_destination.setOnPlaceSelectedListener(new PlaceSelectionListener() {


            @Override
            public void onPlaceSelected(Place place) {
                mPlaceDestination = place.getAddress().toString();

                //Add new Destination Marker
                mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup))
                        .title("Destination"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));


                //Show information in bottom
                BottomSheetRiderFragment mBottomSheet = BottomSheetRiderFragment.newInstance(mPlaceLocation,mPlaceDestination,true);
                mBottomSheet.show(getSupportFragmentManager(),mBottomSheet.getTag());


/*
                //from here
                 if (markerDestination != null)
                markerDestination.remove();
            markerDestination = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))
            .position(place.getLatLng())
            .title("Destination"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),15.0f));
            //show the destination on the bottom sheet viewer using the BottomSheetRiderFragment
            BottomSheetRiderFragment mBottomSheet = BottomSheetRiderFragment.newInstance(String.format("%f,%f", mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                    String.format("%f,%f",place.getLatLng().latitude, place.getLatLng().longitude),true);
            mBottomSheet.show(getSupportFragmentManager(),mBottomSheet.getTag());
            //to here
*/
            }

            @Override
            public void onError(Status status) {

            }
        });


        setUpLocation();

        updateFirebaseToken();



    }


    private void updateFirebaseToken() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference(CommonUrl.token_tb1);

//        Token token = new Token(FirebaseInstanceId.getInstance().getToken());
//        tokens.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue(token);
    }


    private void sendRequestToDriver(String driverId) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(CommonUrl.token_tb1);

//        tokens.orderByKey().equalTo(driverId)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
//                        {
//                            Token token = postSnapShot.getValue(Token.class);
//
//                            //Make raw payload convert LatLong to json
//                            String json_lat_lng = new Gson().toJson(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
//                            String riderToken = FirebaseInstanceId.getInstance().getToken();
//                            Notification data = new Notification(riderToken, json_lat_lng); // send it to driver app and we will deserialize it again
//                            Sender content = new Sender(token.getToken(),data); //send this data to token
//
//                            mService.sendMessage(content)
//                                    .enqueue(new Callback<FCMResponse>() {
//                                        @Override
//                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
//                                            if (response.body().success == 1)
//                                                Toast.makeText(Home.this ,"Request Sent!" , Toast.LENGTH_SHORT).show();
//                                            else
//                                                Toast.makeText(Home.this ,"Failed !" , Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<FCMResponse> call, Throwable t) {
//                                            Log.e("Error" , t.getMessage());
//                                        }
//                                    });
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

    }

    private void requestPickupHere(String uid) {

        DatabaseReference dbRequest = FirebaseDatabase.getInstance().getReference(CommonUrl.pickup_request_tb1);
        GeoFire mGeoFire = new GeoFire(dbRequest);
        mGeoFire.setLocation(uid , new GeoLocation(mLastLocation.getLatitude() , mLastLocation.getLongitude()));


        if (mUserMarker.isVisible())
            mUserMarker.remove();
        //Add new Marker

        mUserMarker = mMap.addMarker(new MarkerOptions().title("Pickup Here")
                .snippet("")
                .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mUserMarker.showInfoWindow();

        btnPickupRequest.setText("Getting your DRIVER...");

        findDriver();




    }

    private void findDriver() {

        final DatabaseReference drivers = FirebaseDatabase.getInstance().getReference(CommonUrl.driver_tb1);
        GeoFire gfDrivers = new GeoFire(drivers);

        GeoQuery geoQuery = gfDrivers.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                radius );
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                //if found
                if (!isDriverFound)
                {
                    isDriverFound = true;
                    driverId = key;
                    btnPickupRequest.setText("CALL DRIVER");
                    // Toast.makeText(Home.this , ""+key , Toast.LENGTH_SHORT).show();
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
                //if the driver is not found we will increase the distance
                if(!isDriverFound && radius < LIMIT)
                {
                    radius++;
                    findDriver();

                }

                else
                {
                    Toast.makeText(DriverMapActivity.this, "No drivers available near you location", Toast.LENGTH_SHORT).show();
                    btnPickupRequest.setText("REQUEST PICKUP");
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {

                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();

                    }
                }

        }
    }

    private void setUpLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            //Request runtime permission
            ActivityCompat.requestPermissions(this , new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION


            },MY_PERMISSION_REQUEST_CODE);
        }
        else
        {

            if(checkPlayServices()){

                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();

            }




        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {



            //Create LatLng from mLastLocation and this is the center point
            LatLng center = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            //distance is on metres
            //heading 0 is north side 90 is east 180 is south 270 is west
            //base on compact
            LatLng northSide = SphericalUtil.computeOffset(center , 10000,0);
            LatLng southSide = SphericalUtil.computeOffset(center , 10000,180);



            LatLngBounds bounds = LatLngBounds.builder()
                    .include(northSide)
                    .include(southSide)
                    .build();


            place_location.setBoundsBias(bounds);
            place_location.setFilter(typeFilter);

            place_destination.setBoundsBias(bounds);
            place_location.setFilter(typeFilter);










            //Presense System
            driversAvailable = FirebaseDatabase.getInstance().getReference(CommonUrl.driver_tb1);
            driversAvailable.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //if you have any change from Drivers table , we will reload all drivers available
                    loadAllAvailableDriver(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();




            loadAllAvailableDriver(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));



            Log.d("EDMTDEV", String.format("Your location changed : %f / %f " ,latitude ,longitude ));
        }
        else
        {
            Log.d("EDMTDEV","Cannot get your location");
        }
    }

    private void loadAllAvailableDriver(final LatLng location) {

        //First we need to delete all markers on the map including our location marker and available drivers marker
        mMap.clear();
        mUserMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup))
                .position(location)
                .title(String.format("You")));



        //Move the camera to this position
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));



        //Load all drivers available in the distance of 3 killometres
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference(CommonUrl.driver_tb1);
        GeoFire gf = new GeoFire(driverLocation);

        GeoQuery geoQuery = gf.queryAtLocation(new GeoLocation(location.latitude, location.longitude) , distance);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, final GeoLocation location) {
                //Use key to get email from table users
                //Table User is available when driver registers an account and updates information
                FirebaseDatabase.getInstance().getReference(CommonUrl.user_driver_tb1)
                        .child(key)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Because The Rider and the User model has the same properties
                                //We can use Rider Model to get the user here
                                Rider rider = dataSnapshot.getValue(Rider.class);

                                //Add driver to map
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(location.latitude, location.longitude))
                                        .flat(true)
                                        .title(rider.getName())
                                        .snippet("Phone : " +rider.getPhone())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (distance <= LIMIT) //distance for drivers within 3 km
                {
                    distance++;
                    loadAllAvailableDriver(location);
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode,this , PLAY_SERVICE_RES_REQUEST).show();

            else{
                Toast.makeText(this, "This device is not supported" , Toast.LENGTH_SHORT).show();
                finish();
            }

            return false;

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


      /*  try{
//this is the map styled code
            boolean isSuccess = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this,R.raw.uber_style_map)
            );
            if (!isSuccess)
                Log.e("ERROR","Map Style Load failed");
        } catch (Resources.NotFoundException ex){
            ex.printStackTrace();
        }    */

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setInfoWindowAdapter(new CustomInfoWindow(this));

        mMap.setOnMapClickListener(latLng -> {
        /*
        //this method allows us to click on the map and set the destination
        //so we first check the markers destination wether it is set or not
        //if it is null just remove available marker and set a new one
        if (markerDestination != null)
            markerDestination.remove();
        markerDestination = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))
        .position(latLng)
        .title("Destination"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f));
        //show the destination on the bottom sheet viewer using the BottomSheetRiderFragment
        BottomSheetRiderFragment mBottomSheet = BottomSheetRiderFragment.newInstance(String.format("%f,%f", mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                String.format("%f,%f",latLng.latitude, latLng.longitude),true);
        mBottomSheet.show(getSupportFragmentManager(),mBottomSheet.getTag());
        //Show information in bottom
       // BottomSheetRiderFragment mBottomSheet = BottomSheetRiderFragment.newInstance(mPlaceLocation,mPlaceDestination,true);
        //mBottomSheet.show(getSupportFragmentManager(),mBottomSheet.getTag());
        */



        });


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }
}