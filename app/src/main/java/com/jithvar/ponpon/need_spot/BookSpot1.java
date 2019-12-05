package com.jithvar.ponpon.need_spot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.config.AccountManagement;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.handler.BookSpot1Data;
import com.jithvar.ponpon.having_spot.HaveSpotAdapter;
import com.jithvar.ponpon.interfaceClass.BookSpotList;
import com.jithvar.ponpon.need_spot.adapter.BookSpot1Adapter;
import com.jithvar.ponpon.need_spot.handler.BookSpotData;

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
import static com.jithvar.ponpon.config.Config.BOOK_SPOT_LOAD_NEAR_BY;
import static com.jithvar.ponpon.config.Config.SPOT_REGISTRATION_PROVIDER;

/**
 * Created by KinG on 09-11-2017.
 * Created by ${EMAIL}.
 */

public class BookSpot1 extends Activity implements PlaceSelectionListener, BookSpotList {


    private String userId;
    private String mobileNumber;
    private boolean accountStatus;
    private DatabaseDB db;


    private double latitudeCurrent = 0.0;
    private double longitudeCurrent = 0.0;
    private String placeId = "";
    private String address = "";
    private String placeName = "";
    private ArrayList<BookSpot1Data> list;
    private BookSpot1Adapter adapter;
    private boolean loadNewSpot;
    private ArrayList<BookSpotData> spotList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_spot_1);

        spotList = (ArrayList<BookSpotData>) getIntent().getSerializableExtra("SpotList");
        if(spotList == null){
            spotList = new ArrayList<>();
        }
        loadNewSpot = true;

        ((TextView) findViewById(R.id.title)).setText(getResources().getString(R.string
                .have_spot));

        list = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new BookSpot1Adapter(this, list);
        listView.setAdapter(adapter);

        db = new DatabaseDB(this);

        if(!spotList.isEmpty()){
            loadAdapterData();
        }

        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);
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
//        new LoadDataFrromServer().execute();
    }

    //------------------place autocomplete-----------------------

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        Log.e(TAG, "Place Selected: " + place.getName());

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

        BookSpotList bookSpotList = BookSpot1.this;
        if(accountStatus && loadNewSpot) {
            spotList.clear();
            list.clear();
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

    //-----------------------work-------------------------------

    @Override
    public void bookSpotData(ArrayList<BookSpotData> spotList) {
        this.spotList = spotList;
//        if(!spotList.isEmpty()){
//
//        }
        loadAdapterData();
        Log.e("spotList size=====", String .valueOf(spotList.size()));
    }

    @Override
    public void loadNewSpot() {
        this.loadNewSpot = true;
    }

    private void loadAdapterData(){
        for (int i = 0; i <spotList.size(); i++){
            list.add(new BookSpot1Data(
                    spotList.get(i).getPrimaryID(), spotList.get(i).getSpot_s_no(),
                    spotList.get(i).getSpotName(), spotList.get(i).getDateRegister(),
                    spotList.get(i).getExitTimeP(), spotList.get(i).getWaitTimeP(),
                    spotList.get(i).getBetAmountP(), spotList.get(i).getAddress(),
                    spotList.get(i).getStatusSpot(), spotList.get(i).getTotalBet(),
                    spotList.get(i).getAreaSize()));
        }

        BookSpot1.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    //-----------------Send to server----------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadDataFrromServer extends AsyncTask<Void, String, String> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(BookSpot1.this, AlertDialog.THEME_HOLO_LIGHT);
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
                        .add("start_type", "LoadNearBySpot")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("Latitude", String.valueOf(latitudeCurrent))
                        .add("Longitude", String.valueOf(longitudeCurrent))
                        .add("PlaceID", placeId)
                        .add("Address", address)
                        .add("PlaceName", placeName)
                        .build();
                Request request = new Request.Builder()
                        .url(BOOK_SPOT_LOAD_NEAR_BY)
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
                            JSONArray array = new JSONArray(jsonObject.getString("jsonObj"));
                            for(int i = 0; i < array.length(); i++){
                                JSONObject jObject = array.getJSONObject(i);
                                String primaryID = jObject.getString("Primary_ID");
                                String Spot_S_No = jObject.getString("Spot_S_No");
                                String spotName = jObject.getString("SpotName");
                                String dateRegister = jObject.getString("DateRegister");
                                String exitTimeP = jObject.getString("ExitTime");
                                String waitTimeP = jObject.getString("WaitTime");
                                String betAmountP = jObject.getString("BetAmount");
                                String address = jObject.getString("Address");
                                String latitude = jObject.getString("Latitude");
                                String longitude = jObject.getString("Longitude");
                                String statusP = jObject.getString("Status");
                                String totalBetSet = jObject.getString("TotalBet");
                                String highestBet = jObject.getString("HighestBet");
                                String statusSpotBet = jObject.getString("StatusSpotBet");

//                                list.add(new BookSpot1Data(primaryID, Spot_S_No, spotName,
//                                        dateRegister, exitTimeP, waitTimeP, betAmountP, address,
//                                        latitude, longitude, statusP, totalBetSet, highestBet, statusSpotBet));

                                BookSpot1.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                            }
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
}
