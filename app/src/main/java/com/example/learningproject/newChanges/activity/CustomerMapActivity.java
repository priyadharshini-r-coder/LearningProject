package com.example.learningproject.newChanges.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityCustomerMapBinding;
import com.example.learningproject.newChanges.utils.Constants;
import com.example.learningproject.newChanges.utils.SharedPrefUtils;
import com.example.learningproject.newChanges.utils.Utils;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomerMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    GeoQuery geoQuery;
    private GoogleMap mMap;
    private ActivityCustomerMapBinding binding;
    private int id = 0;
    private LatLng pickupLocation;
    private int radius = 1;
    private boolean driverFound = false;
    private String driverFoundId;
    private boolean requestBol = false;
    private Marker driverMarker;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationListener;
    private Marker pickUpMarker;



    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //   binding= DataBindingUtil. setContentView(this,R.layout.activity_customer_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);



//       binding. btnLogout.setOnClickListener(view -> {
//           Intent intent = new Intent(CustomerMapActivity.this, MainActivity.class);
//           startActivity(intent);
//           SharedPrefUtils.setStringPreference(CustomerMapActivity.this, Constants.USER_ID, null);
//           finish();
//       });
//       binding. btnSelectSeats.setOnClickListener(view -> {
//           id = 1;
//           String userId;
//           if (requestBol) {
//               requestBol = false;
//               geoQuery.removeAllListeners();
//               driverLocationRef.removeEventListener(driverLocationListener);
//
//               if (driverFoundId != null) {
//                   DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("SignupOwners").child(driverFoundId);
//                   driverRef.setValue(null);
//                   driverFoundId = null;
//               }
//               driverFound = false;
//               radius = 1;
//               userId = SharedPrefUtils.getStringPreference(CustomerMapActivity.this, Constants.USER_ID);
//               if (userId != null) {
//                   DatabaseReference customerDatabaseRefrence = FirebaseDatabase.getInstance().getReference("customerRequest");
//                   GeoFire geoFire = new GeoFire(customerDatabaseRefrence);
//                   geoFire.removeLocation(userId);
//                   if (pickUpMarker != null) {
//                       pickUpMarker.remove();
//                   }
//                  binding.request.setText("Call Uber");
//               }
////               binding.llDriverInfo.setVisibility(View.GONE);
////               binding.tvDriverName.setText("");
////               binding.tvDriverPhoneNo.setText("");
////               binding.tvDriverVehicleType.setText("");
////               binding.imgDriverProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_account));
//
//
//           } else {
//               requestBol = true;
//               userId = SharedPrefUtils.getStringPreference(CustomerMapActivity.this, Constants.USER_ID);
//
//               if (userId != null) {
//                   DatabaseReference customerDatabaseRefrence = FirebaseDatabase.getInstance().getReference("customerRequest");
//                   GeoFire geoFire = new GeoFire(customerDatabaseRefrence);
//                   geoFire.setLocation(userId, new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()), (key, error) -> {
//
//                   });
//                   pickupLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//                   pickUpMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Hedd  re").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup)));
//
//                   binding.request.setText("Getting your Driver...");
//                   getClosestDriver();
//
//               }
//           }
//
//
//       });
        binding.request.setOnClickListener(view -> {
            String userId;
            if (requestBol) {
                requestBol = false;
                geoQuery.removeAllListeners();
                driverLocationRef.removeEventListener(driverLocationListener);

                if (driverFoundId != null) {
                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("SignupOwners").child(driverFoundId);
                    driverRef.setValue(null);
                    driverFoundId = null;
                }
                driverFound = false;
                radius = 1;
                userId = SharedPrefUtils.getStringPreference(CustomerMapActivity.this, Constants.USER_ID);
                if (userId != null) {
                    DatabaseReference customerDatabaseRefrence = FirebaseDatabase.getInstance().getReference("customerRequest");
                    GeoFire geoFire = new GeoFire(customerDatabaseRefrence);
                    geoFire.removeLocation(userId);
                    if (pickUpMarker != null) {
                        pickUpMarker.remove();
                    }
                    binding.request.setText("Call Uber");
                }
//               binding.llDriverInfo.setVisibility(View.GONE);
//               binding. tvDriverName.setText("");
//                binding.tvDriverPhoneNo.setText("");
//                binding.tvDriverVehicleType.setText("");
//                binding.imgDriverProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_account));


            } else {
                requestBol = true;
                userId = SharedPrefUtils.getStringPreference(CustomerMapActivity.this, Constants.USER_ID);

                if (userId != null) {
                    DatabaseReference customerDatabaseRefrence = FirebaseDatabase.getInstance().getReference("customerRequest");
                    GeoFire geoFire = new GeoFire(customerDatabaseRefrence);
                    geoFire.setLocation(userId, new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()), (key, error) -> {

                    });
                    pickupLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    pickUpMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Hedd  re").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup)));

                    binding.request.setText("Getting your Driver...");
                    getClosestDriver();

                }
            }
        });
//        binding.btnSettings.setOnClickListener(view -> {
//            Intent intent = new Intent(CustomerMapActivity.this, CustomerSettingActivity.class);
//            startActivity(intent);
//        });
    }

    private void getClosestDriver() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("DriverAvailable");
        GeoFire geoFire = new GeoFire(driverLocation);

        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {


            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driverFound && requestBol) {
                    driverFound = true;
                    driverFoundId = key;
                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("SignupOwners").child(driverFoundId);
                    SharedPrefUtils.setStringPreference(CustomerMapActivity.this, "DriverFoundId", driverFoundId);
                    if (id == 1) {
                        Intent intent = new Intent(CustomerMapActivity.this, UserSeatAvailability.class);
                        startActivity(intent);
                    }
                    String customerId = SharedPrefUtils.getStringPreference(CustomerMapActivity.this, Constants.USER_ID);
                    HashMap map = new HashMap();
                    map.put(Constants.CUSTOMER_RIDE_ID, customerId);
                    driverRef.updateChildren(map);

                    getDriverLocation();
                    getDriverInfo();
                    binding.request.setText("Looking for Driver Location");


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
                if (!driverFound) {
                    radius += 1;
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    private void getDriverInfo() {
      // binding.llDriverInfo.setVisibility(View.VISIBLE);
        DatabaseReference diverDatabase = FirebaseDatabase.getInstance().getReference().child("SignupOwners").child(driverFoundId);
     //   Utils.showProgressDialog(this, true);
        diverDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                  //  Utils.dismissProgressDialog();
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    assert map != null;
                    if (map.get("email") != null) {
                        String email = Objects.requireNonNull(map.get("email")).toString();
                    //    binding.tvDriverName.setText(email);
                    }
                    if (map.get("phoneno") != null) {
                        String phoneNo = Objects.requireNonNull(map.get("phoneno")).toString();
                       // binding.tvDriverPhoneNo.setText(phoneNo);

                    }
                    if (map.get("vehicleType") != null) {
                        String vehicleType = Objects.requireNonNull(map.get("vehicleType")).toString();
                     //   binding.tvDriverPhoneNo.setText(vehicleType);

                    }
                    if (map.get("profileImageUri") != null) {
                        String profileUrl = Objects.requireNonNull(map.get("profileImageUri")).toString();
                      //  Glide.with(getApplication()).load(profileUrl).into(binding.imgDriverProfileImage);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getDriverLocation() {
        driverLocationRef = FirebaseDatabase.getInstance().getReference().child("driverWorking").child(driverFoundId).child("l");
        driverLocationListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationlat = 0;
                    double locationlong = 0;
                    binding.request.setText("Driver Found");
                    if (map.get(0) != null) {
                        locationlat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationlat = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLong = new LatLng(locationlat, locationlong);
                    if (driverMarker != null) {
                        driverMarker.remove();
                    }
                    Location local = new Location("");
                    local.setLatitude(pickupLocation.latitude);
                    local.setLongitude(pickupLocation.longitude);

                    Location local2 = new Location("");
                    local2.setLatitude(driverLatLong.latitude);
                    local2.setLongitude(driverLatLong.longitude);

                    float distance = local.distanceTo(local2);
                    if (distance < 100) {
                        binding.request.setText("Driver's Arrived");
                    } else {
                        binding.request.setText("Driver Found:" + String.valueOf(distance));
                    }
                    binding.request.setText("Driver Found:" + String.valueOf(distance));

                    driverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLong).title("Your Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup)));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).
                        addOnConnectionFailedListener(this).
                        addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

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
    protected void onStop() {
        super.onStop();

    }
}