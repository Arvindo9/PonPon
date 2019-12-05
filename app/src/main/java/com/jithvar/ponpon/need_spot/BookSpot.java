package com.jithvar.ponpon.need_spot;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.BuildConfig;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.config.AccountManagement;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.interfaceClass.BookSpotList;
import com.jithvar.ponpon.interfaceClass.SelectSpot;
import com.jithvar.ponpon.map.GeoLocation;
import com.jithvar.ponpon.need_spot.handler.BookSpotData;
import com.jithvar.ponpon.need_spot.handler.MapItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.jithvar.ponpon.config.Config.SPOT_REGISTRATION_PROVIDER;

/**
 * Created by KinG on 07-11-2017.
 * Created by ${EMAIL}.
 */

public class BookSpot extends FragmentActivity implements PlaceSelectionListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        ClusterManager.OnClusterClickListener<MapItem>,
        ClusterManager.OnClusterInfoWindowClickListener<MapItem>,
        ClusterManager.OnClusterItemClickListener<MapItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MapItem>,
        BookSpotList, SelectSpot {

    private boolean accountStatus;
    private DatabaseDB db;
    private String address;
//    private String startType = "LoadNearBySpotLatLon";

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String mobileNumber;
    private String userId;

    private float defaultZoom = 17;
    private GoogleMap mMap;
    double latitude;
    double longitude;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private Marker selectedMarker;
    private LocationRequest mLocationRequest;
    private ArrayList<BookSpotData> spotList;
    private boolean loadNewSpot;
    private ClusterManager<MapItem> mClusterManager;
    private LinearLayout layout;
    private TextView addressTv;
    private int spotListPosition;
    private int spotPrimaryId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_spot);

        db = new DatabaseDB(this);

        loadNewSpot = true;

        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);


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


        layout = (LinearLayout) findViewById(R.id.layout_select);
        addressTv = (TextView) findViewById(R.id.address_tv);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            userId = db.getUserId();
            mobileNumber = db.getMobileNumber();
            accountStatus = db.isAccountActive();

            AccountManagement.setAccountStatus(accountStatus);
            AccountManagement.setMobileNumber(mobileNumber);
            AccountManagement.setUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //------------------location updates---------------------

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

        Log.e("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,
                longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.e("onLocationChanged", "Removing Location Updates");
        }
        Log.e("onLocationChanged", "Exit");

    }

    //------------------place autocomplete-----------------------

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place Selected: " + place.getName());

        String placeId = place.getId();
        String placeName = String.valueOf(place.getName());
        String placeAddress = String.valueOf(place.getAddress());
        double latitude = place.getLatLng().latitude;
        double longitude = place.getLatLng().longitude;
        String locale = String.valueOf(place.getLocale());

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String date = year + "/" + month + "/" + day;

        BookSpotList bookSpotList = BookSpot.this;
        if(accountStatus && loadNewSpot) {
            new Thread(new BookSpotMapDataLoading(bookSpotList, latitude, longitude,
                    "LoadNearBySpotLatLonAddress",
                    userId, mobileNumber, date, placeId, placeName, placeAddress,
                    locale)).start();
            loadNewSpot = false;
        }
        else{
            showMassage(R.string.account_activation);
        }
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
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

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
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

        latitude = latLng.latitude;
        longitude = latLng.longitude;

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String date = year + "/" + month + "/" + day;

        BookSpotList bookSpotList = BookSpot.this;
        if(accountStatus && loadNewSpot) {
            new  Thread(new BookSpotMapDataLoading(bookSpotList, latitude, longitude,
                    "LoadNearBySpotLatLonAddress",
                    userId, mobileNumber, date, "", "",
                    "", "")).start();
            loadNewSpot = false;
        }
        else{
            showMassage(R.string.account_activation);
        }

        Log.e("latitude", String.valueOf(latitude));
        Log.e("longitude", String.valueOf(longitude));

    }

    //----------------cluster map design-----------------------

    @Override
    public void bookSpotData(ArrayList<BookSpotData> spotList) {
        this.spotList = spotList;
        BookSpot.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setUpClusterer(mMap);
            }
        });

        Log.e("setting cluster", "---------------");
    }

    @Override
    public void loadNewSpot() {
        this.loadNewSpot = true;
    }

    private void setUpClusterer(final GoogleMap getMap) {
        // Position the map.
        int i = spotList.size()-1;
        if(i > -1) {
            final LatLng latLng = new LatLng(
                    Double.parseDouble(spotList.get(i).getLatitude()),
                    Double.parseDouble(spotList.get(i).getLongitude()));

            getMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));

            mClusterManager = new ClusterManager<MapItem>(this, getMap);
            mClusterManager.setRenderer(new LocationRenderer(this, getMap));
            getMap.setOnCameraIdleListener(mClusterManager);
            getMap.setOnMarkerClickListener(mClusterManager);
            getMap.setOnInfoWindowClickListener(mClusterManager);
            getMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
            mClusterManager.setOnClusterClickListener(this);
            mClusterManager.setOnClusterInfoWindowClickListener(this);
            mClusterManager.setOnClusterItemClickListener(this);
            mClusterManager.setOnClusterItemInfoWindowClickListener(this);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    addItems();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClusterManager.cluster();
                        }
                    });
                }
            }).start();

//            addItems();
//            mClusterManager.cluster();
            Log.e("map------------", "-------------");
        }
    }

    private void addItems() {
        for (int i = 0; i < spotList.size(); i++) {
            int image = Integer.parseInt(spotList.get(i).getTotalBet());
            if(image == 0){
                image = 0;
            }
            else if(image == 1){
                image = 1;
            }
            else if(image == 2){
                image = 2;
            }
            else if(image == 3){
                image = 3;
            }
            else if(image == 4 || image == 5){
                image = 4;
            }
            else if(image > 5 && image <= 7){
                image = 5;
            }
            else if(image > 7 && image <= 10){
                image = 6;
            }
            else if(image > 10 && image <= 14){
                image = 7;
            }
            else if(image > 14 && image <= 19){
                image = 8;
            }
            else if(image > 19 && image <= 27){
                image = 9;
            }
            else if(image > 27 && image <= 35){
                image = 10;
            }
            else{
                image = 50;
            }

            MapItem offsetItem = new MapItem(
                    Double.parseDouble(spotList.get(i).getLatitude()),
                    Double.parseDouble(spotList.get(i).getLongitude()),
                    spotList.get(i).getAddress(),
                    "Desire Bet:" + spotList.get(i).getBetAmountP() + ", " +
                    "Total bet:" + spotList.get(i).getTotalBet() + ", " +
                    "Date:" + spotList.get(i).getDateRegister() + ", " +
                    "Expire time:" + spotList.get(i).getWaitTimeP(),
                    Integer.parseInt(spotList.get(i).getPrimaryID()),
                    i,
                    image);
            mClusterManager.addItem(offsetItem);

            Log.e("image ", String.valueOf(image) + "  " +
                    spotList.get(i).getTotalBet());
        }
    }

    private class LocationRenderer extends DefaultClusterRenderer<MapItem> {
        private final Context mContext;

        LocationRenderer(Context context, GoogleMap googleMap) {
            super(getApplicationContext(), googleMap, mClusterManager);
            mContext = context;

        }

        @Override
        protected void onBeforeClusterItemRendered(MapItem item, MarkerOptions markerOptions) {
            final BitmapDescriptor markerDescriptor;
//                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

            switch (item.iconImage){
                case 0:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
                    break;
                case 1:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                    break;
                case 2:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                    break;
                case 3:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
                    break;
                case 4:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    break;
                case 5:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                    break;
                case 6:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                    break;
                case 7:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                    break;
                case 8:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                    break;
                case 9:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
                    break;
                case 10:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
                    break;
                default:
                    markerDescriptor =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

            }
            markerOptions.icon(markerDescriptor)
                    .title(item.getTitle())
                    .snippet(item.getSnippet());
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<MapItem> cluster) {
            Log.e("call", "shouldRenderAsCluster");
            return cluster.getSize() > 1;
        }

        @Override
        protected void onClusterItemRendered(MapItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
            Log.e("call", "onClusterItemRendered");

        }
    }

    @Override
    public boolean onClusterClick(Cluster<MapItem> cluster) {

        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MapItem> cluster) {

    }

    @Override
    public boolean onClusterItemClick(MapItem mapItem) {
        layout.setVisibility(View.VISIBLE);
        String text = mapItem.getTitle() + "\n" + mapItem.getSnippet();
        addressTv.setText(text);
        spotListPosition = mapItem.getListIndex();
        spotPrimaryId = mapItem.getPrimaryID();
        Log.e("spotPrimaryId", String.valueOf(spotPrimaryId));
        Log.e("cluster item", "---------------");
        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(MapItem mapItem) {
        BookSpotData ob = spotList.get(mapItem.getListIndex());

        DialogFragment newFragment1 = new BooKSpotClusterInfoDialog(mapItem.getPrimaryID(),
                ob.getSpot_s_no(),
                ob.getSpotName(),
                ob.getStatusSpot(),
                ob.getDateRegister(),
                ob.getExitTimeP(),
                ob.getWaitTimeP(),
                ob.getBetAmountP(),
                ob.getTotalBet(),
                ob.getAddress(),
                ob.getAreaSize());
        newFragment1.show(getSupportFragmentManager(), "mapDialog");
        Log.e("cluster info", "---------------");
    }

    //-------------------work------------------------------------

    @Override
    public void selectThisSpot(int primaryId) {
        Intent i = new Intent(BookSpot.this, BookSpot2.class);
        i.putExtra("PrimaryIdSpotTb", String .valueOf(spotPrimaryId));
        startActivity(i);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_spot:
                Intent i = new Intent(BookSpot.this, BookSpot2.class);
                i.putExtra("PrimaryIdSpotTb", String .valueOf(spotPrimaryId));
                startActivity(i);
                break;

            case R.id.tabular_data:
                Intent i1 = new Intent(BookSpot.this, BookSpot1.class);
                i1.putExtra("SpotList", spotList);
                startActivity(i1);
                break;
        }
    }

    //-----------------Send to server----------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadDataFrromServer extends AsyncTask<Void, String, String> {

        private ProgressDialog mProgressDialog;
        private final double latitude;
        private final double longitude;

        LoadDataFrromServer(double lat, double lon){
            this.latitude = lat;
            this.longitude = lon;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
            mProgressDialog = new ProgressDialog(BookSpot.this, AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            double earthRadius = 6371.01;       //in km
            double distance = 2;                //in km
            double radius = distance / 6371;

            GeoLocation myLocation = GeoLocation.fromDegrees(latitude, longitude);
            GeoLocation[] boundingCoordinates =
                    myLocation.boundingCoordinates(distance, radius);

            double latitudeMin = boundingCoordinates[0].getLatitudeInRadians();
            double latitudeMax = boundingCoordinates[1].getLatitudeInRadians();
            double longitudeMin = boundingCoordinates[0].getLongitudeInRadians();
            double longitudeMax = boundingCoordinates[1].getLongitudeInRadians();
            double latiduteR = myLocation.getLatitudeInRadians();
            double longitudeR = myLocation.getLatitudeInRadians();

            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("start_type", "")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("LatitudeCInD", String.valueOf(latitude))
                        .add("LongitudeCInD", String.valueOf(longitude))
                        .add("LatitudeCInR", String.valueOf(latiduteR))
                        .add("LongitudeCInR", String.valueOf(longitudeR))
                        .add("LatitudeMin", String.valueOf(latitudeMin))
                        .add("LatitudeMax", String.valueOf(latitudeMax))
                        .add("LongitudeMin", String.valueOf(longitudeMin))
                        .add("LongitudeMax", String.valueOf(longitudeMax))
                        .add("Radius", String.valueOf(radius))
                        .add("Address", address)
                        .build();
                Request request = new Request.Builder()
                        .url(SPOT_REGISTRATION_PROVIDER)
                        .post(formBody)
                        .build();
                Response response = client.newCall(request).execute();

                return response.body().string();

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            Log.e("l========= ", result);
            if(!result.equals("")) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    switch (jsonObject.getString("Status")){
                        case "Success":
//                            showMassage(R.string.success);
                            break;

                        default:
                            showMassage(R.string.try_again);
                            break;
                    }

                    Log.e("Status", jsonObject.getString("Status"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showMassage(int id){
        Toast.makeText(this, getResources().getString(id), Toast.LENGTH_SHORT).show();
    }

    //------------------connection--------------------------------
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                                ActivityCompat.requestPermissions(BookSpot.this,
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
