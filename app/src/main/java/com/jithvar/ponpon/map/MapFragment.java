package com.jithvar.ponpon.map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.handler.MapItem;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Arvindo Mondal on 10/8/17.
 * Company name Jithvar
 * Email arvindo@jithvar.com
 */

public class MapFragment extends Fragment {

    private SupportMapFragment mapFragment;
    private SearchableSpinner employName;
    private int pos;
    private static String dateToTrack = "";
    private String empIdToTrack = "";
    private String empNameToTrack = "";

//    protected Location mLastLocation;

    // Declare a variable for the cluster manager.
    private ClusterManager<MapItem> mClusterManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //--------------designing cluster-------------------------

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class LocationRenderer extends DefaultClusterRenderer<MapItem> {
        private final Context mContext;

        LocationRenderer(Context context, GoogleMap googleMap) {
            super(getActivity().getApplicationContext(), googleMap, mClusterManager);
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

    //--------------map class---------------------------

    private class GoogleMapArvi implements OnMapReadyCallback{

        @Override
        public void onMapReady(final GoogleMap googleMap) {
//            mClusterManager.setRenderer(new OwnRendring(getApplicationContext()
//                    ,googleMap,mClusterManager));
        }
    }

    //---------------date picker-----------------
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


//            Log.e("date select-----", year + "-" + month + "-" + day);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month++;
            String monthStr = String.valueOf(month);
            String dayStr = String.valueOf(day);
            if(monthStr.length() < 2){
                monthStr = "0" + String.valueOf(month);
            }
            if(dayStr.length() < 2){
                dayStr = "0" + String.valueOf(day);
            }

            dateToTrack = year + "-" + monthStr + "-" + dayStr;

//            Log.e("month", String.valueOf(month));
            Log.e("date select", dateToTrack);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
