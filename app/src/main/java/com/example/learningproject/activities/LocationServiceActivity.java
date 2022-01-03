package com.example.learningproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningproject.MainActivity;
import com.example.learningproject.MapsActivity;
import com.example.learningproject.R;

import com.example.learningproject.view.LocationModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import java.util.Locale;
import java.util.Objects;

public class LocationServiceActivity  extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
  /*  private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private final ArrayList<Marker> markers = new ArrayList<>();
    RelativeLayout products_select_option;
    ImageButton select_btn, product1, product2;
    Button myCurrentloc,book_button;
    private static final String TAG = LocationServiceActivity.class.getSimpleName();
    private final static int PERMISSION_MY_LOCATION = 3;
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    private MapFragment mapFragment;
   public final Context context=LocationServiceActivity.this;

    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private LocationRequest request;
    View mapView;
    private boolean mRequestingLocationUpdates;
    CameraUpdate cLocation;
    double latitude,longitude;
    Marker now;

    Geocoder geocoder;
    List<Address> addresses;
    private  Context  context1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setupLocationManager();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tracking Application");

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        CheckMapPermission();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Buttons Select Product option
        select_btn = (ImageButton) findViewById(R.id.img_selected);
        product1 = (ImageButton) findViewById(R.id.product_type_1_button);
        product2 = (ImageButton) findViewById(R.id.product_type_2_button);
        products_select_option = (RelativeLayout) findViewById(R.id.products_select_option);
        myCurrentloc = (Button) findViewById(R.id.myCLocation);

        book_button = (Button) findViewById(R.id.book_button);
        book_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), FindDriver.class);
                startActivity(i);

            }
        });
    }

            @Override
            protected void onStart() {
                super.onStart();
                googleApiClient.connect();
                //mGoogleApiClient.connect();

            }


            @Override
            public void onBackPressed() {
                DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
                if (drawer.isDrawerOpen( GravityCompat.START )) {
                    drawer.closeDrawer( GravityCompat.START );
                } else {
                    super.onBackPressed();
                }
            }

            @Override
            protected void onResume() {
                super.onResume();
                if(googleApiClient.isConnected()){
                    setInitialLocation();

                }

                LocationManager service = (LocationManager) getSystemService( LOCATION_SERVICE );
                boolean enabled = service.isProviderEnabled( LocationManager.GPS_PROVIDER );

                // Check if enabled and if not send user to the GPS settings
                if (!enabled) {
                    buildAlertMessageNoGps();
                }
                if(enabled){

                }


            }


            @Override
            protected void onPause() {
                super.onPause();

            }

            @Override
            protected void onDestroy() {
                super.onDestroy();
                if (googleApiClient.isConnected()) {
                    googleApiClient.disconnect();
                }
            }


            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate( R.menu.main, menu );
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

                return super.onOptionsItemSelected( item );
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

                DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
                drawer.closeDrawer( GravityCompat.START );
                return true;


            }

   *//* private void countDownTimer() {
        new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                onMapReady(mMap);
            }
        }.start();
    }*//*

    *//**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *//*

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
      *//*  mMap = googleMap;

        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (Marker marker : markers)
                    marker.remove();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LocationModel locationModel = snapshot.getValue(LocationModel.class);
                    if (locationModel != null)
                        plotMarkers(locationModel);
                }
                countDownTimer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*//*

        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style ) );

            if (!success) {
                Log.e( TAG, "Style parsing failed." );
            }
        } catch (Resources.NotFoundException e) {
            Log.e( TAG, "Can't find style. Error: ", e );
        }


        if (ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then over   riding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //This line will show your current location on Map with GPS dot
        mMap.setMyLocationEnabled( true );
        locationButton();
    }

            private void setupLocationManager() {
                //buildGoogleApiClient();
                if (googleApiClient == null) {

                    googleApiClient = new GoogleApiClient.Builder( this )
                            .addConnectionCallbacks( this )
                            .addOnConnectionFailedListener( this )
                            .addApi( LocationServices.API )
                            .addApi( Places.GEO_DATA_API )
                            .addApi( Places.PLACE_DETECTION_API )
                            .build();
                    //mGoogleApiClient = new GoogleApiClient.Builder(this);
                }
                googleApiClient.connect();
                createLocationRequest();
            }


            protected void createLocationRequest() {

                request = new LocationRequest();
                request.setSmallestDisplacement( 10 );
                request.setFastestInterval( 50000 );
                request.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
                request.setNumUpdates( 3 );

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest( request );
                builder.setAlwaysShow( true );

                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings( googleApiClient,
                                builder.build() );


                result.setResultCallback( new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(@NonNull LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        switch (status.getStatusCode()) {

                            case LocationSettingsStatusCodes.SUCCESS:
                                setInitialLocation();
                                break;

                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied, but this can be fixed
                                // by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(
                                            LocationServiceActivity.this,
                                            REQUEST_CHECK_SETTINGS );
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;

                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way
                                // to fix the settings so we won't show the dialog.
                                break;
                        }
                    }
                } );


            }



            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.d("onActivityResult()", Integer.toString(resultCode));

                //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
                switch (requestCode) {
                    case REQUEST_CHECK_SETTINGS:
                        switch (resultCode) {
                            case Activity.RESULT_OK: {

                                setInitialLocation();

                                Toast.makeText(LocationServiceActivity.this, "Location enabled", Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = true;
                                break;
                            }
                            case Activity.RESULT_CANCELED: {
                                // The user was asked to change settings, but chose not to
                                Toast.makeText(LocationServiceActivity.this, "Location not enabled", Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        break;
                }
            }



            private void setInitialLocation() {

                if (ActivityCompat.checkSelfPermission( LocationServiceActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( LocationServiceActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        mLastLocation=location;
                        double lat =location.getLatitude();
                        double lng=location.getLongitude();

                        LocationServiceActivity.this.latitude=lat;
                        LocationServiceActivity.this.longitude=lng;
                        try {
                            if(now !=null){
                                now.remove();
                            }
                            LatLng positionUpdate = new LatLng( LocationServiceActivity.this.latitude,LocationServiceActivity.this.longitude );
                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom( positionUpdate, 15 );
                            now=mMap.addMarker(new MarkerOptions().position(positionUpdate)
                                    .title("Your Location"));

                            mMap.animateCamera( update );
                            //myCurrentloc.setText( ""+latitude );

                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            Log.e("MapException",ex.getMessage());
                        }
                        try {
                            geocoder=new Geocoder(LocationServiceActivity.this,Locale.ENGLISH);
                            addresses=geocoder.getFromLocation(latitude,longitude,1);
                            StringBuilder str = new StringBuilder();
                            if (Geocoder.isPresent()) {
                        *//*Toast.makeText(getApplicationContext(),
                                "geocoder present", Toast.LENGTH_SHORT).show();*//*
                                android.location.Address returnAddress = addresses.get(0);

                                String localityString = returnAddress.getAddressLine (0);
                                //String city = returnAddress.getAddressLine(1);
                                //String region_code = returnAddress.getAddressLine(2);
                                //String zipcode = returnAddress.getAddressLine(3);

                                str.append( localityString ).append( "" );
                                // str.append( city ).append( "" ).append( region_code ).append( "" );
                                // str.append( zipcode ).append( "" );

                                myCurrentloc.setText(str);
                                Toast.makeText(getApplicationContext(), str,
                                        Toast.LENGTH_SHORT).show();
                                Log.d("Tag", String.valueOf(str));

                            } else {
                    *//*    Toast.makeText(getApplicationContext(),
                                "geocoder not present", Toast.LENGTH_SHORT).show();*//*
                            }
                        }
                        catch (IOException e)
                        {
                            Log.e("tag",e.getMessage());
                        }
                    }
                });


            }


            private void updateCamera(){

            }

            private void CheckMapPermission() {


                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

                    if (ActivityCompat.checkSelfPermission( LocationServiceActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission( LocationServiceActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions( LocationServiceActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1002 );
                    } else {

                        setupLocationManager();
                    }
                } else {
                    setupLocationManager();
                }

            }


            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult( requestCode, permissions, grantResults );


                switch (requestCode) {
                    case 1002: {
                        // If request is cancelled, the result arrays are empty.
                        if (grantResults.length > 0
                                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
                                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {

                                setupLocationManager();

                            }
                        } else {

                            Toast.makeText( LocationServiceActivity.this, "Permission Denied", Toast.LENGTH_SHORT ).show();
                            //finish();
                        }
                    }
                    break;
                }

            }


            public void getLatLang(String placeId) {
                Places.GeoDataApi.getPlaceById( googleApiClient, placeId )
                        .setResultCallback( new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                    final Place place = places.get( 0 );

                                    LatLng latLng = place.getLatLng();

                                    try {

                                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom( latLng, 15 );
                                        mMap.animateCamera( update );


                                    } catch (Exception ex) {

                                        ex.printStackTrace();
                                        Log.e( "MapException", ex.getMessage() );

                                    }

                                    Log.i( "place", "Place found: " + place.getName() );
                                } else {
                                    Log.e( "place", "Place not found" );
                                }
                                places.release();
                            }
                        } );
            }


            @Override
            public void onConnected(@Nullable Bundle bundle) {
                //AlertMessageNoGps();


            }


            @Override
            public void onConnectionSuspended(int i) {
                //checkLocaionStatus();
            }



            @Override
            public void onLocationChanged(Location location) {


            }


            @Override
            public void onPointerCaptureChanged(boolean hasCapture) {

            }





            //GET CURRENT LOCATION BUTTON POSITION....
            private void locationButton() {

                MapFragment mapFragment = (MapFragment) getFragmentManager()
                        .findFragmentById( R.id.map );

                View locationButton = ((View) mapFragment.getView().findViewById( Integer.parseInt( "1" ) ).
                        getParent()).findViewById( Integer.parseInt( "2" ) );
                if (locationButton != null && locationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                    // location button is inside of RelativeLayout
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

                    // Align it to - parent BOTTOM|LEFT
                    params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
                    params.addRule( RelativeLayout.ALIGN_PARENT_LEFT );
                    params.addRule( RelativeLayout.ALIGN_PARENT_RIGHT, 0 );
                    params.addRule( RelativeLayout.ALIGN_PARENT_TOP, 0 );

                    // Update margins, set to 10dp
                    final int margin = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 150,
                            getResources().getDisplayMetrics() );
                    params.setMargins( margin, margin, margin, margin );

                    locationButton.setLayoutParams( params );
                }

            }


            //Button Location Search
            public void myLocation(View view) {

                //CHANGE ACTIVITY
                Intent intent = new Intent( LocationServiceActivity.this, LocationAutoActivity.class );
                startActivity( intent );


            }


            public void destination(View view) {
                Intent intent = new Intent( LocationServiceActivity.this, LocationAutoActivity.class );
                startActivity( intent );

            }


            //Select product option button click
            public void img_selected(View view) {

                products_select_option.setVisibility( View.VISIBLE );

            }

            //Select product option button click
            public void product_type_1_button(View view) {
                products_select_option.setVisibility( View.GONE );

            }

            //Select product option button click
            public void product_type_2_button(View view) {
                products_select_option.setVisibility( View.GONE );

            }

            //Enable Location Button
            private void checkLocaionStatus() {


            }



            protected void buildAlertMessageNoGps() {

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please Turn ON your GPS Connection")
                        .setTitle( "GPS Not Enabled" )
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @NonNull
    @Override
    public Loader<Object> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Object> loader, Object data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Object> loader) {

    }*/

public GoogleMap mMap;
   ArrayList<LatLng> MarkerOptions;
   GoogleApiClient mGoogleApiClient;
   Location currentLocation;
   Marker mCurrLocationMarker;
   LocationRequest mLocationRequest;
   FusedLocationProviderClient fusedLocationProviderClient;
    AppCompatEditText locationSearch;
    TextView locationLabel;

   @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_location_service);
       locationLabel=findViewById(R.id.loc_txt);
       if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
       {
           checkLocationPermission();
       }
       MarkerOptions =new ArrayList<>();
       SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
       Objects.requireNonNull(mapFragment).getMapAsync(this::onMapReady);
      // assert mapFragment != null;
     // mapFragment.getMapAsync(this);
   }


    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 404);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                getAddressFromLocation(currentLocation, getApplicationContext(), new LocationServiceActivity.GeoCoderHandler());
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(LocationServiceActivity.this);
            }
        });
    }
    public static void getAddressFromLocation(final Location location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // sending back first address line and locality
                        result = address.getAddressLine(0) + ", " + address.getLocality() + ", " + address.getCountryName();
                    }
                } catch (IOException e) {
                    Log.e("Tag", "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==404) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            }
        }
    }
    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String result;
            if (message.what == 1) {
                Bundle bundle = message.getData();
                result = bundle.getString("address");
            } else {
                result = null;
            }
            locationLabel.setText(result);
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    public void searchLocation(View view) {
        locationSearch = findViewById(R.id.et_search);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            assert addressList != null;
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if (MarkerOptions.size() > 1) {
                    MarkerOptions.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                //
                MarkerOptions.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (MarkerOptions.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (MarkerOptions.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (MarkerOptions.size() >= 2) {
                    LatLng origin = MarkerOptions.get(0);
                    LatLng dest = MarkerOptions.get(1);

                    // Getting URL to the Google Directions API
                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }

            }
        });
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    // Fetches data from url passed
    @SuppressLint("StaticFieldLeak")
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Location mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }*/
}