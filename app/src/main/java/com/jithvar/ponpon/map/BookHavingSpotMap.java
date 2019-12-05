package com.jithvar.ponpon.map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.handler.HaveSpotAddMapData;
import com.jithvar.ponpon.handler.MapData;
import com.jithvar.ponpon.handler.MapItem;
import com.jithvar.ponpon.handler.PermissionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;
import static com.jithvar.ponpon.config.Config.CUSTOMER_PROFILE;

/**
 * Created by KinG on 02-11-2017.
 * Created by ${EMAIL}.
 */

public class BookHavingSpotMap extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMapLongClickListener,
        AdapterView.OnItemSelectedListener,
        View.OnClickListener,         ClusterManager.OnClusterClickListener<MapItem>,
        ClusterManager.OnClusterInfoWindowClickListener<MapItem>,
        ClusterManager.OnClusterItemClickListener<MapItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MapItem>{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int MY_LOCATION_REQUEST_CODE = 2;
    private GoogleMap mMap;
    private Marker marker;
    private Spinner mSpinner;
    private double latitudeMark = 0.0;
    private double longitudeMark = 0.0;
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private CameraPosition mCameraPosition;
    private static final int DEFAULT_ZOOM = 15;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mShowPermissionDeniedDialog = false;
    private boolean isGpsOn;
    private String mobileNumber;
    private String userId;
    private ProgressDialog mProgressDialog;
    private String cLongitude;
    private String cLatitude;
    private String address;
    private String gLatitude;
    private ArrayList<HaveSpotAddMapData> mapDataList;
    private SupportMapFragment mapFragment;
    private boolean mapLoadType;
    private ClusterManager<MapItem> mClusterManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.book_spot_map);

        userId = getIntent().getStringExtra("userId");
        mobileNumber = getIntent().getStringExtra("mobileNumber");
        isGpsOn = getIntent().getBooleanExtra("GpsStatus", false);
        mapLoadType = false;

        TextView titleT = (TextView) findViewById(R.id.title);
        titleT.setText(getResources().getString(R.string.book_spot));

        mSpinner = (Spinner) findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.layers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        mapDataList = new ArrayList<>();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;

            case R.id.previous_spot:
                break;

            case R.id.current_location:
                break;

            case R.id.confirm:
                Intent i = new Intent();
                i.putExtra("Latitude", cLatitude);
                i.putExtra("Longitude", cLongitude);
                i.putExtra("Address", address);
                setResult(10, i);
                break;
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if(!mapLoadType) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object


                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(location.getLatitude(),
                                                location.getLongitude()), DEFAULT_ZOOM));
                            }
                        }
                    });

            mMap = googleMap;
            updateMapType();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setOnMapLongClickListener(this);
            mMap.setTrafficEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.setIndoorEnabled(true);
        }
        else{
            mapLoadType = true;
            setUpClusterer(googleMap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] results) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    results[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, results,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
//            mMap.setMyLocationEnabled(true);
//            mMyLocationCheckbox.setChecked(true);
        } else {
            mShowPermissionDeniedDialog = true;
        }
    }

    //-----------------------map cluster-------------------------------------------

    @Override
    public boolean onClusterClick(Cluster<MapItem> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MapItem> cluster) {

    }

    @Override
    public boolean onClusterItemClick(MapItem mapItem) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MapItem mapItem) {

    }

    //----------map cluster------------------
    private void setUpClusterer(final GoogleMap getMap) {
        // Position the map.
        int i = mapDataList.size()-1;
        if(i > -1) {
            final LatLng latLng = new LatLng(
                    Double.parseDouble(mapDataList.get(i).getLatitude()),
                    Double.parseDouble(mapDataList.get(i).getLongitude()));

            getMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

            mClusterManager = new ClusterManager<MapItem>(this, getMap);
            mClusterManager.setRenderer(new LocationRenderer(this, getMap));
            getMap.setOnCameraIdleListener(mClusterManager);
            getMap.setOnMarkerClickListener(mClusterManager);
            getMap.setOnInfoWindowClickListener(mClusterManager);
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
//            addItems(getMap);

//            mClusterManager.cluster();
            Log.e("map------------", "-------------");
        }
    }

    private void addItems() {
        for (int i = 0; i < mapDataList.size(); i++) {
            int image = Integer.parseInt(mapDataList.get(i).getTime().substring(0, 2));
            switch (image) {
                case 9:
                    image = 2;
                    break;
                case 10:
                    image = 3;
                    break;
                case 11:
                    image = 8;
                    break;
                case 12:
                    image = 5;
                    break;
                case 13:
                    image = 6;
                    break;
                case 15:
                    image = 9;
                    break;
                case 16:
                    image = 10;
                    break;
                case 17:
                    image = 1;
                    break;
                case 18:
                    image = 4;
                    break;
                case 19:
                    image = 7;
                    break;
                default:
                    image = 50;
                    break;
            }
            //for location address
            if( mapDataList.get(i).getAddress().equals("") ||
                    mapDataList.get(i).getAddress().equals("null") ||
                    mapDataList.get(i).getAddress() == null){
                Geocoder geocoder;
                List<Address> addresses = null;
                String address = "address loading...";
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(
                            Double.parseDouble(mapDataList.get(i).getLatitude()),
                            Double.parseDouble(mapDataList.get(i).getLongitude()),
                            1);

                    String addressLine = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    address = addressLine + ", " + city + ", " + state +
                            " " + country + " \n" +
                            postalCode + ", " + knownName;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MapItem offsetItem = new MapItem(
                        Double.parseDouble(mapDataList.get(i).getLatitude()),
                        Double.parseDouble(mapDataList.get(i).getLongitude()),
                        address,
                        mapDataList.get(i).getDate() + " " +
                                mapDataList.get(i).getTime(),
                        image);
                mClusterManager.addItem(offsetItem);
            }
            else {
                MapItem offsetItem = new MapItem(
                        Double.parseDouble(mapDataList.get(i).getLatitude()),
                        Double.parseDouble(mapDataList.get(i).getLongitude()),
                        mapDataList.get(i).getAddress(),
                        mapDataList.get(i).getDate() + " " +
                                mapDataList.get(i).getTime(),
                        image);
                mClusterManager.addItem(offsetItem);
            }

            Log.e("image ", String.valueOf(image) + "  " +
                    mapDataList.get(i).getTime());
        }
    }

    //--------------designing cluster-------------------------

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */

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
    }

    //-------------------------------------------

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mShowPermissionDeniedDialog) {
            PermissionUtils.PermissionDeniedDialog
                    .newInstance(false).show(getSupportFragmentManager(), "dialog");
            mShowPermissionDeniedDialog = false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateMapType();
    }

    private void updateMapType() {
        // No toast because this can also be called by the Android framework in onResume() at which
        // point mMap may not be ready yet.
        if (mMap == null) {
            return;
        }

        String layerName = ((String) mSpinner.getSelectedItem());
        if (layerName.equals(getString(R.string.normal))) {
            mMap.setMapType(MAP_TYPE_NORMAL);
        } else if (layerName.equals(getString(R.string.hybrid))) {
            mMap.setMapType(MAP_TYPE_HYBRID);
        } else if (layerName.equals(getString(R.string.satellite))) {
            mMap.setMapType(MAP_TYPE_SATELLITE);
        } else if (layerName.equals(getString(R.string.terrain))) {
            mMap.setMapType(MAP_TYPE_TERRAIN);
        } else if (layerName.equals(getString(R.string.none_map))) {
            mMap.setMapType(MAP_TYPE_NONE);
        } else {
            Log.i("LDA", "Error setting layer with name " + layerName);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("You are here")
                .draggable(true)
                .visible(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        latitudeMark = latLng.latitude;
        longitudeMark = latLng.longitude;

//        MapData.setLatLng(latLng.latitude, latLng.longitude);
        MapData.setLatLng(latitudeMark, longitudeMark);
    }

    //-------------------Load previous data from server-------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadingDataFromServer extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(BookHavingSpotMap.this, AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("start_type", "Load_Previous_Data")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("Latitude", cLatitude)
                        .add("Longitude", cLongitude)
                        .add("Street", mobileNumber)
                        .build();
                Request request = new Request.Builder()
                        .url(CUSTOMER_PROFILE)
                        .post(formBody)
                        .build();
                Response response = client.newCall(request).execute();

                return initialize(response.body().string());

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                e.printStackTrace();
            }

            return "false";
        }

        private String initialize(String result) {
            String s = "false";
            if(!result.equals("")) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    switch (jsonObject.getString("Status")){
                        case "Success":
                            int pageNumber = jsonObject.getInt("PageNumber");
                            String latitude = jsonObject.getString("Latitude");
                            String longitude = jsonObject.getString("Longitude");
                            String address = jsonObject.getString("Address");
                            String dateTime = jsonObject.getString("Edate");      // tab catagory


                            String date = dateTime.substring(0, 10);
                            String time = dateTime.substring(11, 16);

                            mapDataList.add(new HaveSpotAddMapData(pageNumber, latitude, longitude,
                                    address, date, time));
                            s = "ok";
                            break;

//                        case "Error":
//                            break;
//                        case "ErrorMobile":
//                            break;
//                        case "ErrorAccount":
//                            break;
//                        case "ErrorStart":
//                            break;
//                        case "ErrorFetch":
//                            break;
//                        case "ErrorDb":
//                            break;
                        default:
                            Toast.makeText(BookHavingSpotMap.this, getResources().getString(R.string
                                    .try_again), Toast.LENGTH_SHORT).show();
                            break;
                    }

                    Log.e("Status", jsonObject.getString("Status"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return s;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            Log.e("l========= ", result);
            if(result.equals("ok")) {
                //loading map
                mapLoadType = true;
                mapFragment.getMapAsync(BookHavingSpotMap.this);
            }
        }
    }
    //--------------------------------------------------------------------------

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
