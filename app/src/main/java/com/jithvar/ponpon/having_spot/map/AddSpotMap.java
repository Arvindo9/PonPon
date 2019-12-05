package com.jithvar.ponpon.having_spot.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
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
import com.google.maps.android.BuildConfig;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.interfaceClass.GeoCodingAddressAPIsAddress;
import com.jithvar.ponpon.interfaceClass.MapAddressInterface;
import com.jithvar.ponpon.having_spot.handler.ImageHandler;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by KinG on 15-11-2017.
 * Created by ${EMAIL}.
 */

public class AddSpotMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,GeoCodingAddressAPIsAddress, MapAddressInterface {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String mobileNumber;
    private String userId;

    private float defaultZoom = 17;
    private GoogleMap mMap;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 10000;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private Marker selectedMarker;
    private LocationRequest mLocationRequest;
    private boolean isGpsOn;
    private AddressDataFull addressList;
    private TextView addressTv;
    private RelativeLayout layout;
    private Bitmap mapSnapShot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_spot_map);

        userId = getIntent().getStringExtra("userId");
        mobileNumber = getIntent().getStringExtra("mobileNumber");
        isGpsOn = getIntent().getBooleanExtra("GpsStatus", false);

        checkLocationPermission();
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkLocationPermission();
//        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.e("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.e("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        layout = (RelativeLayout) findViewById(R.id.layout_select);
        addressTv = (TextView) findViewById(R.id.address_tv);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setInterval(0);
//        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
        Log.e("dfgfdg", "fhhgjhj");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(defaultZoom));
        Toast.makeText(AddSpotMap.this,"Your Current Location", Toast.LENGTH_LONG).show();

        Log.e("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,
                longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.e("onLocationChanged", "Removing Location Updates");
        }
        Log.e("onLocationChanged", "Exit");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showMassage(int index){
        Toast.makeText(AddSpotMap.this, getResources().getString(index),
                Toast.LENGTH_SHORT).show();
    }

    //---------------------work----------------------------------

    @Override
    public void getAddressOfPinDrop(AddressDataFull addressList) {
        LatLng latLng = new LatLng(latitude, longitude);
        if (selectedMarker != null) {
            selectedMarker.remove();
        }
        //set marker, and book spot
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(addressList.getFormatted_address());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        selectedMarker = mMap.addMarker(markerOptions);

        this.addressList = addressList;

        if(isNetworkAvailable()) {
            onSelectedOnMap();
        }
        else{
            showMassage(R.id.no_internet);
        }

//        if(addressList != null && !addressList.isEmpty()){
        Log.e("latitude", String.valueOf(latitude));
        Log.e("longitude", String.valueOf(longitude));
//            Intent i = new Intent();
//            i.putExtra("Latitude", String.valueOf(latitude));
//            i.putExtra("Longitude", String.valueOf(longitude));
//            i.putExtra("AddressData", addressList);
//            setResult(RESULT_OK, i);
//            finish();
//        }
        Log.e("retur===============", String.valueOf(addressList));
    }

    private void onSelectedOnMap(){
        layout.setVisibility(View.VISIBLE);
        addressTv.setText(addressList.getFormatted_address());
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_location:
                takeSnapshot(addressList.getFormatted_address());
                break;
        }

    }

    private void takeSnapshot(final String address) {
        if (mMap == null) {
            return;
        }

//        final ImageView snapshotHolder = (ImageView) findViewById(R.id.snapshot_holder);

        final GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // Callback is called from the main thread, so we can modify the ImageView safely.
//                snapshotHolder.setImageBitmap(snapshot);
                mapSnapShot = Bitmap.createBitmap(snapshot, 100, 500, snapshot.getWidth()- 200,
                        snapshot.getHeight() - 1000);

//                mapSnapShot = Bitmap.createScaledBitmap(snapshot, snapshot.getWidth(), 200,
//                        false);
                DialogFragment newFragment1 = new MapDialog(mapSnapShot, address);
                newFragment1.show(getSupportFragmentManager(), "mapDialog");

            }
        };

        mMap.snapshot(callback);
    }

    @Override
    public void onSelectLocation(boolean answer) {
        if(answer){
//            ImageHandler imageHandler = new ImageHandler(mapSnapShot);
            ArrayList<ImageHandler> list = new ArrayList<>();
            Intent i = new Intent();
            i.putExtra("Latitude", String.valueOf(latitude));
            i.putExtra("Longitude", String.valueOf(longitude));
            i.putExtra("AddressData", addressList);
//            i.putExtra("SnapShot", mapSnapShot);
//            i.putExtra("SnapShot", list.add(new ImageHandler(mapSnapShot)));
            i.putExtra("SnapShot", new ImageHandler(mapSnapShot));
            setResult(RESULT_OK, i);
            finish();
        }
    }

    //----------------------map-----------------------------------
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        ;
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //get the current place details
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (selectedMarker != null) {
            selectedMarker.remove();
        }

        //set marker, and book spot
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(getResources().getString(R.string.loading));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        selectedMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(defaultZoom));

        String url = getUrl(latLng.latitude, latLng.longitude);
        GeoCodingAddressAPIsAddress geoCodingAddressAPIsAddress = AddSpotMap.this;
        new GeoCodingAddressAPIs(mMap, geoCodingAddressAPIsAddress).execute(url);
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        Log.e("latitude", String.valueOf(latitude));
        Log.e("longitude", String.valueOf(longitude));
    }

    private String getUrl(double latitude, double longitude) {
        StringBuilder googlePlacesUrl = new
        StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
        googlePlacesUrl.append("latlng=")
                .append(latitude)
                .append(",")
                .append(longitude)
                .append("&key=" + "AIzaSyBz2kTHp3QdwpPkNCvGWik7LnyY3P69R9k");
        Log.e("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    //---------------------permission------------------------------

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    public void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.i(TAG, "Displaying permission rationale to provide additional context.");
                showSnackbar(R.string.permission_rationale,
                        android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Request permission
                                ActivityCompat.requestPermissions(AddSpotMap.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        });
            }
            else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.");
                }
                else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    showSnackbar(R.string.permission_denied_explanation,
                            R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
            }
        }
    }

    //--------------------------------

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
