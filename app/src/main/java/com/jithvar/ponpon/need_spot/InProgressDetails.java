package com.jithvar.ponpon.need_spot;

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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.interfaceClass.DealAccept;
import com.jithvar.ponpon.interfaceClass.DealCancel;
import com.jithvar.ponpon.map.FindLocationOnMap;
import com.jithvar.ponpon.map.ViewLocationOnMap;
import com.jithvar.ponpon.need_spot.dialog.AcceptSpotInProgressDialog;
import com.jithvar.ponpon.need_spot.dialog.RejectSpotInProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.Config.NEED_ACTIVE_DEAL_CANCEL;
import static com.jithvar.ponpon.config.Config.NEED_ACTIVE_DETAILS;
import static com.jithvar.ponpon.config.Config.NEED_IN_PROGRESS;
import static com.jithvar.ponpon.config.Config.NEED_IN_PROGRESS_DEAL_ACCEPT;
import static com.jithvar.ponpon.config.Config.NEED_IN_PROGRESS_DEAL_CANCEL;

/**
 * Created by KinG on 23-11-2017.
 * Created by ${EMAIL}.
 */

public class InProgressDetails extends FragmentActivity implements DealCancel, DealAccept {

    private TextView spotStatusT;
    private TextView spotNoT;
    private TextView dateRegisterT;
    private TextView spotNameT;
    private TextView timeExitP_T;
    private TextView timeInS_T;
    private TextView timeWaitS_T;
    private TextView areaSizeT;
    private String primaryIdSpotTb;
    private boolean accountStatus;
    private String userId;
    private String mobileNumber;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private ProgressDialog mProgressDialog;
    private String primaryIdBetTb;
    private TextView betAmountST;
    private TextView betAmountP_T;
    private TextView addressT;
    private String address;
    private String areaSize;
    private Button applyChangesBtn;
    private TextView timeWaitP_T;
    private TextView confirmStatusT;
    private boolean rejectSpot;
    private boolean acceptSpot;
    private TextView amountStatusT;
    private TextView totalBetT;
    private TextView highestBetT;
    private String placeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.need_in_progress_details);

        primaryIdSpotTb = getIntent().getStringExtra("PrimaryIdSpotTb");
        primaryIdBetTb = getIntent().getStringExtra("PrimaryIdBetTb");

        spotStatusT = (TextView) findViewById(R.id.status_spot_tv);
        spotNoT = (TextView) findViewById(R.id.spot_no_t);
        spotNameT = (TextView) findViewById(R.id.spot_name_tv);
        dateRegisterT = (TextView) findViewById(R.id.date_tv);
        timeExitP_T = (TextView) findViewById(R.id.time_exit_p_tv);
        timeWaitP_T = (TextView) findViewById(R.id.time_expire_p_tv);
        betAmountP_T = (TextView) findViewById(R.id.bet_amount_p_tv);
        betAmountST = (TextView) findViewById(R.id.bet_amount_s_tv);
        timeInS_T = (TextView) findViewById(R.id.time_enter_seeker_tv);
        timeWaitS_T = (TextView) findViewById(R.id.time_wait_seeker_tv);
        addressT = (TextView) findViewById(R.id.address_tv);
        areaSizeT = (TextView) findViewById(R.id.area_size_tv);
        confirmStatusT = (TextView) findViewById(R.id.confirm_status_tv);
        amountStatusT = (TextView) findViewById(R.id.amount_status_tv);
        totalBetT = (TextView) findViewById(R.id.total_bet_t);
        highestBetT = (TextView) findViewById(R.id.highest_bet_tv);

        applyChangesBtn = (Button) findViewById(R.id.apply_btn);

        rejectSpot = false;
        acceptSpot = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseDB db = new DatabaseDB(this);
        try {
            userId = db.getUserId();
            mobileNumber = db.getMobileNumber();
            accountStatus = db.isAccountActive();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!isNetworkAvailable()){
            showMassage(R.string.no_internet);
        }
        else if(accountStatus){
            new LoadDataFromServer().execute();
        }
        else{
            showMassage(R.string.account_activation);
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.map_img:
                Intent i = new Intent(InProgressDetails.this, ViewLocationOnMap.class);
                i.putExtra("Latitude", latitude);
                i.putExtra("Longitude", longitude);
                i.putExtra("Address", address);
                i.putExtra("AreaSize", areaSize);
                i.putExtra("PlaceId", placeID);
                startActivity(i);
                break;
            case R.id.map_track_button:
                Intent i1 = new Intent(InProgressDetails.this, FindLocationOnMap.class);
                i1.putExtra("Latitude", latitude);
                i1.putExtra("Longitude", longitude);
                i1.putExtra("Address", address);
                i1.putExtra("AreaSize", areaSize);
                i1.putExtra("PlaceId", placeID);
                startActivity(i1);
                break;

            case R.id.reject_seeker:
                DialogFragment newFragment2 = new RejectSpotInProgressDialog();
                newFragment2.show(getSupportFragmentManager(), "dealCancel");
                break;

            case R.id.confirm_pay:
                DialogFragment newFragment3 = new AcceptSpotInProgressDialog();
                newFragment3.show(getSupportFragmentManager(), "dealAccept");
                break;

            case R.id.apply_btn:
                if (!isNetworkAvailable()){
                    showMassage(R.string.no_internet);
                }
                else if(accountStatus){
                    if(rejectSpot) {
                        new ApplyChangesToServer().execute("RejectTheDeal",
                                NEED_IN_PROGRESS_DEAL_CANCEL, String.valueOf(rejectSpot));
                    }
                    else if(acceptSpot){
                        new ApplyChangesToServer().execute("AcceptTheDeal",
                                NEED_IN_PROGRESS_DEAL_ACCEPT, String.valueOf(acceptSpot));
                    }
                }
                else{
                    showMassage(R.string.account_activation);
                }
                break;
        }
    }

    @Override
    public void dealCancel() {
        rejectSpot = true;
        applyChangesBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void dealAccept() {
        acceptSpot = true;
        applyChangesBtn.setVisibility(View.VISIBLE);
    }


    //------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadDataFromServer extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(InProgressDetails.this, AlertDialog
                    .THEME_HOLO_LIGHT);
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
                        .add("start_type", "NeedActiveDetails")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("Primary_ID_spotTb", primaryIdSpotTb)
                        .add("Primary_ID_betTb", primaryIdBetTb)
                        .build();
                Request request = new Request.Builder()
                        .url(NEED_ACTIVE_DETAILS)
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
                            JSONArray array = new JSONArray(jo.getString("jsonObj"));
                            for(int i = 0; i < array.length(); i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                String spot_S_No = jsonObject.getString("Spot_S_No");
                                String spotName = jsonObject.getString("SpotName");
                                String dateSpotTb = jsonObject.getString("DateSpotTb");
                                String timeExitSpotTb = jsonObject.getString("ExitTime");
                                String timeWaitP = jsonObject.getString("WaitTimeSpotTb");
                                String betAmountSpotTb = jsonObject.getString("BetAmountSpotTb");
                                address = jsonObject.getString("Address");
                                placeID = jsonObject.getString("PlaceID");
                                latitude = Double.parseDouble(jsonObject.getString
                                        ("Latitude"));
                                longitude = Double.parseDouble(jsonObject.getString
                                        ("Longitude"));
                                String areaSize = jsonObject.getString("AreaSize");
                                String spotStatus = jsonObject.getString("SpotStatus");
                                String timeEnterBetTb = jsonObject.getString("EnterTimeBetTb");
                                String timeWaitBetTb = jsonObject.getString("WaitTimeBetTb");
                                String betAmountBetTb = jsonObject.getString("BetAmountBetTb");
                                String statusPayment = jsonObject.getString("StatusPayment");
                                String statusSpotByProvier = jsonObject.getString
                                        ("StatusSpotByProvier");
                                String statusSpotBySeeker = jsonObject.getString
                                        ("StatusSpotBySeeker");
                                String totalBet = jsonObject.getString("TotalBet");
                                String highestBet = jsonObject.getString("HighestBet");
                                String dateBetTb = jsonObject.getString("DateBetTb");

                                spotNoT.setText(spot_S_No);
                                spotNameT.setText(spotName);
                                dateRegisterT.setText(dateSpotTb);
                                timeExitP_T.setText(timeExitSpotTb);
                                timeWaitP_T.setText(timeWaitP);
                                betAmountP_T.setText(betAmountSpotTb);
                                addressT.setText(address);
                                areaSizeT.setText(areaSize);
                                spotStatusT.setText(spotStatus);
                                amountStatusT.setText(statusPayment);
                                timeInS_T.setText(timeEnterBetTb);
                                timeWaitS_T.setText(timeWaitBetTb);
                                betAmountST.setText(betAmountBetTb);
                                totalBetT.setText(totalBet);
                                highestBetT.setText(highestBet);
                                confirmStatusT.setText(statusSpotBySeeker);
                            }
                            break;
                        default:
                            showMassage(R.string.try_again);
                            break;
                    }
                    Log.e("Status", jo.getString("Status"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //-------------------------apply change to spot--------------------

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class ApplyChangesToServer extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(InProgressDetails.this, AlertDialog
                    .THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("start_type", params[0])
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("DealConfirm", params[2])
                        .add("Primary_ID_SpotTb", primaryIdSpotTb)
                        .add("Primary_ID_BetTb", primaryIdBetTb)
                        .build();
                Request request = new Request.Builder()
                        .url(params[1])
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
                            showMassage(R.string.apply_success);
                            finish();
                            break;
                        default:
                            showMassage(R.string.try_again);
                            break;
                    }

                    Log.e("Status", jo.getString("Status"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //---------------------------------------------

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showMassage(int id){
        Toast.makeText(InProgressDetails.this, getResources().getString(id), Toast.LENGTH_SHORT)
                .show();
    }
}
