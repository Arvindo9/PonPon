package com.jithvar.ponpon.having_spot;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.handler.HaveSpotData;

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

/**
 * Created by KinG on 01-11-2017.
 * Created by ${EMAIL}.
 */

public class HavingSpot1 extends Activity implements AbsListView.OnScrollListener {

    private ProgressDialog mProgressDialog;
    private String userId;
    private String mobileNumber;
    private ArrayList<HaveSpotData> haveSpotDataList;
    private HaveSpotAdapter adapter;
    private boolean loadNextList;
    private int pageNO;
    private DatabaseDB db;
    private boolean accountStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.having_spot1);
        pageNO = 0;

        db = new DatabaseDB(this);

        haveSpotDataList = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new HaveSpotAdapter(this, haveSpotDataList);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        loadNextList = false;

        ((TextView)findViewById(R.id.title)).setText(getResources().getString(R.string.have_spot));
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            userId = db.getUserId();
            mobileNumber = db.getMobileNumber();
            accountStatus = db.isAccountActive();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!isNetworkAvailable()){
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast
                    .LENGTH_SHORT).show();
        }
        else{
            new LoadingDataFromServer().execute();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        haveSpotDataList.clear();
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
                if(lastItem == totalItemCount && loadNextList)
                {
                    pageNO += 1;
                    if (!isNetworkAvailable()){
                        Toast.makeText(HavingSpot1.this, getResources().getString(R.string
                                        .no_internet),
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new LoadingDataFromServer().execute();
                    }
                }
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                if(accountStatus) {
                    startActivity(new Intent(HavingSpot1.this, AddSpot.class));
                }
            break;

            case R.id.back:
                finish();
                break;
        }
    }


    //------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadingDataFromServer extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(HavingSpot1.this, AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            loadNextList = false;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("CountId", String.valueOf(pageNO))
                        .add("start_type", "LoadHavingSpot")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .build();
                Request request = new Request.Builder()
                        .url(HAVE_SPOT_DATA_LOAD)
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
            loadNextList = true;
            Log.e("l========= ", result);
            if(!result.equals("")) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    switch (jsonObject.getString("Status")){
                        case "Success":
                            pageNO = jsonObject.getInt("CountId");      // page no
                            JSONArray array = new JSONArray(jsonObject.getString("ArrayJson"));
                            for(int i =0; i < array.length(); i++){
                                String primary_ID = jsonObject.getString("Primary_ID");
                                String spot_S_No = jsonObject.getString("Spot_S_No");
                                String spotName = jsonObject.getString("SpotName");
                                String date = jsonObject.getString("Date");
                                String enterTime = jsonObject.getString("EnterTime");
                                String exitTime = jsonObject.getString("ExitTime");
                                String waitTime = jsonObject.getString("WaitTime");
                                String betAmount = jsonObject.getString("BetAmount");
                                String address = jsonObject.getString("Address");
                                String spotStatus = jsonObject.getString("SpotStatus");
                                String waitTime2 = jsonObject.getString("WaitTime2");
                                String betAmount2 = jsonObject.getString("BetAmount2");

                                String userIDSeeker = null;
//                                haveSpotDataList.add(new HaveSpotData(primary_ID, userIDSeeker, spot_S_No,
//                                        spotName, date,
//                                        enterTime, exitTime, waitTime, betAmount, address,
//                                        spotStatus, waitTime2, betAmount2));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        default:
                            Toast.makeText(HavingSpot1.this, getResources().getString(R.string
                                    .try_again), Toast.LENGTH_SHORT).show();

                            break;
                    }

                    Log.e("Status", jsonObject.getString("Status"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
