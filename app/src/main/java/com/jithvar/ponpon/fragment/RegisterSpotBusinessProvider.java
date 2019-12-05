package com.jithvar.ponpon.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jithvar.ponpon.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Arvindo Mondal on 19/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class RegisterSpotBusinessProvider extends Fragment {

    private LocationManager loctionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_spot_business_provider, container, false);

        loctionManager = (LocationManager)
                getActivity().getSystemService(LOCATION_SERVICE);

        checkProvider();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loctionManager = null;
    }

    public boolean checkProvider() {
        boolean isGPSEnabled = loctionManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = loctionManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isNetworkEnabled && !isGPSEnabled) {
            AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
            d.setTitle("GPS Off");
            d.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }
            });
            d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            d.show();
            return false;

        } else {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
                //this code will call onRequestPermissionsResult();
            } else {
                getCurrentLocation();
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 12) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");

            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
                //this code will call onRequestPermissionsResult();
            } else {
                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getActivity(), "permission not granted", Toast.LENGTH_SHORT).show();
        }
        else {
            List<String> lacationProvider = loctionManager.getAllProviders();
            for (String provider : lacationProvider){
                Log.e("LocationProvider", provider);
            }

            Criteria c = new Criteria();
            c.setAccuracy(Criteria.ACCURACY_FINE);
            c.setAltitudeRequired(false);
            c.setBearingRequired(false);
            c.setCostAllowed(true);
            c.setPowerRequirement(Criteria.POWER_HIGH);

            String bestProvider = loctionManager.getBestProvider(c, true);
            Log.e(" best LocationProvider", bestProvider);

            loctionManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    new LocationClass());
//            startService(new Intent(this,
//                    LocationServerService.class));
//            if(isNetworkAvailable()) {
//                startService(new Intent(this,
//                        LocationServerService.class));
//            }
//            else {
//                startService(new Intent(this,
//                        LocationSqlService.class));
//            }
        }
    }

    private class LocationClass implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
