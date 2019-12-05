package com.jithvar.ponpon.having_spot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.having_spot.adapter.ExpireAdapter;
import com.jithvar.ponpon.having_spot.adapter.SuccessAdapter;
import com.jithvar.ponpon.having_spot.handler.ExpireData;
import com.jithvar.ponpon.having_spot.handler.SuccessData;

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

import static com.jithvar.ponpon.config.Config.HAVE_SPOT_EXPIRE;
import static com.jithvar.ponpon.config.Config.HAVE_SPOT_SUCCESS;

/**
 * Created by KinG on 04-11-2017.
 * Created by ${EMAIL}.
 */

public class Expire extends Fragment implements AbsListView.OnScrollListener{

    private ProgressDialog mProgressDialog;
    private String userId;
    private String mobileNumber;
    private ArrayList<ExpireData> inProgressList;
    private ExpireAdapter adapter;
    private boolean loadNextList;
    private int pageNO;
    private DatabaseDB db;
    private boolean accountStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNO = -1;
        db = new DatabaseDB(getActivity());
        try {
            userId = db.getUserId();
            mobileNumber = db.getMobileNumber();
            accountStatus = db.isAccountActive();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.success_have, container, false);

        inProgressList = new ArrayList<>();
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        adapter = new ExpireAdapter(getActivity(), inProgressList);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        loadNextList = false;

        if (!isNetworkAvailable()){
            showMassage(R.string.no_internet);
        }
        else if(accountStatus){
            new LoadingDataFromServer().execute();
        }
        else{
            showMassage(R.string.account_activation);
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        inProgressList.clear();
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
                if(lastItem == totalItemCount && loadNextList && totalItemCount > 15)
                {
                    Log.e("lastItem", String.valueOf(lastItem));
                    Log.e("totalItemCount", String.valueOf(totalItemCount));
                    Log.e("firstVisibleItem", String.valueOf(firstVisibleItem));
//                    pageNO += 1;
                    if (!isNetworkAvailable()){
                        Toast.makeText(getActivity(), getResources().getString(R.string
                                        .no_internet),
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(accountStatus){
                        new LoadingDataFromServer().execute();
                    }
                    else{
                        showMassage(R.string.account_activation);
                    }
                }
                break;
        }
    }




    //------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadingDataFromServer extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
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
                        .add("start_type", "LoadSpotExpire")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .build();
                Request request = new Request.Builder()
                        .url(HAVE_SPOT_EXPIRE)
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
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jo = jsonArray.getJSONObject(0);
                    switch (jo.getString("Status")){
                        case "Success":
                            pageNO = jo.getInt("page");      // page no
                            JSONArray array = new JSONArray(jo.getString("jsonObj"));
                            for(int i = 0; i < array.length(); i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                String primary_ID_spotTb = jsonObject.getString
                                        ("Primary_ID_spotTb");
                                String spot_S_No = jsonObject.getString("Spot_S_No");
                                String spotName = jsonObject.getString("SpotName");
                                String dateSpotTb = jsonObject.getString("DateSpotTb");
                                String enterTimeSpotTb = jsonObject.getString("EnterTimeSpotTb");
                                String exitTimeSpotTb = jsonObject.getString("ExitTime");
                                String expireTimeSpotTb = jsonObject.getString("WaitTimeSpotTb");
                                String betAmountSpotTb = jsonObject.getString("BetAmountSpotTb");
                                String address = jsonObject.getString("Address");
                                String placeID = jsonObject.getString("PlaceID");
                                double latitude = Double.parseDouble(jsonObject.getString
                                        ("Latitude"));
                                double longitude = Double.parseDouble(jsonObject.getString
                                        ("Longitude"));
                                String areaSize = jsonObject.getString("AreaSize");
                                String spotStatus = jsonObject.getString("SpotStatus");
                                String primary_ID_betTb = jsonObject.getString("Primary_ID_betTb");
                                String enterTimeBetTb = jsonObject.getString("EnterTimeBetTb");
                                String waitTimeBetTb = jsonObject.getString("WaitTimeBetTb");
                                String betAmountBetTb = jsonObject.getString("BetAmountBetTb");
                                String statusPayment = jsonObject.getString("StatusPayment");
                                String statusSpot = jsonObject.getString("StatusSpotBySeeker");
                                String dateBetTb = jsonObject.getString("DateBetTb");

                                inProgressList.add(new ExpireData(primary_ID_spotTb,
                                        primary_ID_betTb, spot_S_No, spotName, dateSpotTb,
                                        enterTimeSpotTb, exitTimeSpotTb, expireTimeSpotTb,
                                        betAmountSpotTb, address, placeID, latitude, longitude,
                                        areaSize, spotStatus, enterTimeBetTb, waitTimeBetTb,
                                        betAmountBetTb, statusPayment, statusSpot, dateBetTb));
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        default:
                            Toast.makeText(getActivity(), getResources().getString(R.string
                                    .try_again), Toast.LENGTH_SHORT).show();

                            break;
                    }

                    Log.e("Status", jo.getString("Status"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            loadNextList = true;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showMassage(int id){
        Toast.makeText(getActivity(), getResources().getString(id), Toast.LENGTH_SHORT).show();
    }
}
