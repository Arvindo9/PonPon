package com.jithvar.ponpon.having_spot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.handler.HaveSpotData;
import com.jithvar.ponpon.handler.SpotDetailsData;
import com.jithvar.ponpon.map.ViewLocationOnMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.Config.HAVE_SPOT_DATA_LOAD;
import static com.jithvar.ponpon.config.Config.HAVE_SPOT_DATA_SEEKER;
import static com.jithvar.ponpon.config.Config.SPOT_DETAIL_PROVIDER;
import static com.jithvar.ponpon.config.Config.SPOT_LIST_SEEKER;

/**
 * Created by KinG on 03-11-2017.
 * Created by ${EMAIL}.
 */

public class SpotDetails extends Activity implements AbsListView.OnScrollListener{

    private ArrayList<SpotDetailsData> list;
    private String primaryId;
    private Button mapLocationT;
    private TextView locationT;
    private TextView spotNoT;
    private TextView area_sizeT;
    private TextView exitTimeT;
    private TextView enterTimeT;
    private TextView dateT;
    private TextView spotNameT;
    private TextView spotStatusT;
    private TextView waitTimeT;
    private TextView betAmountSetT;
    private SpotBetSeekerAdapter adapter;
    private boolean loadNextList;
    private DatabaseDB db;
    private int pageNO;
    private String userId;
    private String mobileNumber;
    private boolean accountStatus;
    private ProgressDialog mProgressDialog;
    private TextView dateRegisterT;
    private TextView timeRegisterT;
    private ProgressDialog mProgressDialog1;
    private String address;
    private String areaSize;
    private double longitude;
    private double latitude;
    private String placeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_status_details);

        primaryId = getIntent().getStringExtra("PrimaryId");
        latitude = getIntent().getDoubleExtra("Latitude", 0.0);
        longitude = getIntent().getDoubleExtra("Longitude", 0.0);
        placeID = getIntent().getStringExtra("PlaceId");

        Log.e("lat", String.valueOf(latitude));
        Log.e("lon", String.valueOf(longitude));

        ((TextView) findViewById(R.id.title)).setText(getResources().getString(R.string
                .spot_status));

        spotNoT = (TextView) findViewById(R.id.spot_no);
        spotStatusT = (TextView) findViewById(R.id.status_tv);
        spotNameT = (TextView) findViewById(R.id.spot_name_tv);
        dateT = (TextView) findViewById(R.id.date_tv);
        enterTimeT = (TextView) findViewById(R.id.time_enter_tv);
        exitTimeT = (TextView) findViewById(R.id.time_exit_tv);
        waitTimeT = (TextView) findViewById(R.id.wait_time_tv);
        betAmountSetT = (TextView) findViewById(R.id.bet_amount_tv);
        area_sizeT = (TextView) findViewById(R.id.area_size_tv);
        locationT = (TextView) findViewById(R.id.location_tv);
        dateRegisterT = (TextView) findViewById(R.id.date_reg_tv);
        timeRegisterT = (TextView) findViewById(R.id.time_reg_tv);
        mapLocationT = (Button) findViewById(R.id.map_button);

        list = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new SpotBetSeekerAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        loadNextList = false;

        pageNO = -1;
        db = new DatabaseDB(this);

        try {
            userId = db.getUserId();
            mobileNumber = db.getMobileNumber();
            accountStatus = db.isAccountActive();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!isNetworkAvailable()){
            Toast.makeText(SpotDetails.this, getResources().getString(R.string.no_internet), Toast
                    .LENGTH_SHORT).show();
        }
        else{
            new LoadingDetailsData().execute();
            new LoadingSeekerData().execute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        list.clear();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        switch(view.getId())
        {
            case R.id.list_view:
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount && loadNextList && totalItemCount > 10)
                {
                    pageNO += 1;
                    if (!isNetworkAvailable()){
                        Toast.makeText(SpotDetails.this, getResources().getString(R.string
                                        .no_internet),
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new LoadingSeekerData().execute();
                    }
                }
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;

            case R.id.map_button:
                Intent i = new Intent(SpotDetails.this, ViewLocationOnMap.class);
                i.putExtra("Latitude", latitude);
                i.putExtra("Longitude", longitude);
                i.putExtra("Address", areaSize);
                i.putExtra("AreaSize", areaSize);
                i.putExtra("PlaceId", placeID);
                startActivity(i);
                break;
        }
    }

    //------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadingDetailsData extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SpotDetails.this, AlertDialog.THEME_HOLO_LIGHT);
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
                        .add("start_type", "SpotDetailData")
                        .add("PrimaryId", primaryId)
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .build();
                Request request = new Request.Builder()
                        .url(SPOT_DETAIL_PROVIDER)
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
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jo = jsonArray.getJSONObject(0);
                JSONObject jsonObject1 = jo.getJSONObject("jsonObj");
                switch (jsonObject1.getString("Status")){
                    case "Success":
                        JSONArray array = new JSONArray(jsonObject1.getString("ArrayJson"));
                        JSONObject jsonObject = array.getJSONObject(0);
                        String spot_S_No = jsonObject.getString("Spot_S_No");
                        String spotName = jsonObject.getString("SpotName");
                        String date = jsonObject.getString("DateRegister");
                        String enterTime = jsonObject.getString("EnterTime");
                        String exitTime = jsonObject.getString("ExitTime");
                        String waitTime = jsonObject.getString("WaitTime");
                        String betAmount = jsonObject.getString("BetAmount");
                        address = jsonObject.getString("Address");
                        areaSize = jsonObject.getString("AreaSize");
                        String spotStatus = jsonObject.getString("SpotStatus");
                        String dateTimeDT = jsonObject.getString("DateTimeDT");

                        String d = dateTimeDT.substring(0, 10);
                        String dateRegister = d.substring(8) + "/" + d.substring(5, 7) + "/" +
                                d.substring(0, 4);
                        String timeRegister = dateTimeDT.substring(11, 16);

                        spotStatusT.setText(spotStatus);
                        spotNoT.setText(spot_S_No);
                        spotNameT.setText(spotName);
                        dateT.setText(date);
                        enterTimeT.setText(enterTime);
                        exitTimeT.setText(exitTime);
                        waitTimeT.setText(waitTime);
                        betAmountSetT.setText(betAmount);
                        locationT.setText(address);
                        area_sizeT.setText(areaSize);
                        dateRegisterT.setText(dateRegister);
                        timeRegisterT.setText(timeRegister);

                        break;
                    default:
                        Toast.makeText(SpotDetails.this, getResources().getString(R.string
                                .try_again), Toast.LENGTH_SHORT).show();

                        break;
                }

                Log.e("Status", jsonObject1.getString("Status"));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadingSeekerData extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog1 = new ProgressDialog(SpotDetails.this, AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog1.setMessage(getResources().getString(R.string.loading));
            mProgressDialog1.setIndeterminate(false);
            mProgressDialog1.setCancelable(false);
            mProgressDialog1.show();
            loadNextList = false;
            Log.e("primaryid", primaryId);
            Log.e("userId", userId);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("CountId", String.valueOf(pageNO))
                        .add("start_type", "SpotSeekerData")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("Primary_ID", primaryId)
                        .build();
                Request request = new Request.Builder()
                        .url(SPOT_LIST_SEEKER)
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
            mProgressDialog1.dismiss();
            loadNextList = true;
            Log.e("l========= ", result);
            if(!result.equals("")) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jo = jsonArray.getJSONObject(0);
                    JSONObject jsonObject1 = jo.getJSONObject("jsonObj");
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    switch (jsonObject1.getString("Status")){
                        case "Success":
                            pageNO = jsonObject1.getInt("CountId");      // page no
                            JSONArray array = new JSONArray(jsonObject1.getString("ArrayJson"));
                            for(int i =0; i < array.length(); i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                String primary_ID = jsonObject.getString("Primary_ID");
                                String userID = jsonObject.getString("User_ID_Seeker");
                                String userName = jsonObject.getString("UserName");
                                String enterTime = jsonObject.getString("EnterTime");
                                String waitTime = jsonObject.getString("WaitTime");
                                String betAmount = jsonObject.getString("BetAmount");
                                String statusPayment = jsonObject.getString("StatusPayment");
                                String statusSpotByProvier = jsonObject.getString
                                        ("StatusSpotByProvier");
                                String statusSpotBySeeker = jsonObject.getString
                                        ("StatusSpotBySeeker");
                                String dateT = jsonObject.getString("DateTimeDT");

                                String d = dateT.substring(0, 10);
                                String dateRegister = d.substring(8) + "/" + d.substring(5, 7) + "/" +
                                        d.substring(0, 4);
//                                String dateRegister = dateT.substring(0, 10);
                                String timeRegister = dateT.substring(11, 16);

                                list.add(new SpotDetailsData(primaryId, primary_ID, userID,
                                        userName, dateRegister, timeRegister,
                                        enterTime, waitTime, betAmount, statusPayment,
                                        statusSpotByProvier, statusSpotBySeeker));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        default:
                            Toast.makeText(SpotDetails.this, getResources().getString(R.string
                                    .try_again), Toast.LENGTH_SHORT).show();

                            break;
                    }

                    Log.e("Status", jsonObject1.getString("Status"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //-------------------------------------------------------------------

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
