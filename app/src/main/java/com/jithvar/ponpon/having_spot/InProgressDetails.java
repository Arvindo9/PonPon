package com.jithvar.ponpon.having_spot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.having_spot.dialog.RejectSpotDialog;
import com.jithvar.ponpon.having_spot.handler.InProgressData;
import com.jithvar.ponpon.interfaceClass.DealCancel;
import com.jithvar.ponpon.interfaceClass.TimeInterface;
import com.jithvar.ponpon.map.ViewLocationOnMap;
import com.jithvar.ponpon.need_spot.BookSpot2;
import com.jithvar.ponpon.need_spot.BookSpot3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.AccountManagement.ACCOUNT_STATUS;
import static com.jithvar.ponpon.config.AccountManagement.MOBILE_NUMBER;
import static com.jithvar.ponpon.config.AccountManagement.USER_ID;
import static com.jithvar.ponpon.config.Config.BOOK_SPOT_2_DETAILS;
import static com.jithvar.ponpon.config.Config.HAVE_SPOT_DATA_LOAD;
import static com.jithvar.ponpon.config.Config.HAVE_SPOT_IN_PROGRESS_DETAILS;

/**
 * Created by KinG on 21-11-2017.
 * Created by ${EMAIL}.
 */

public class InProgressDetails extends FragmentActivity implements TimeInterface, DealCancel {

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
    private TextView spotStatusAmountT;
    private TextView timeEnterP_T;
    private TextView timeExpireP_T;
    private TextView betAmountST;
    private TextView betAmountP_T;
    private TextView addressT;
    private String address;
    private String areaSize;
    private TextView timeEditEditP_T;
    private String timeEditEditP;
    private int timeEditInt;
    private Button applyChangesBtn;
    private int timeWaitPInt;
    private String placeID;
    private TextView timeWaitP_T;
    private TextView confirmStatusT;
    private boolean rejectSpot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_progress_details);

        primaryIdSpotTb = getIntent().getStringExtra("PrimaryIdSpotTb");
        primaryIdBetTb = getIntent().getStringExtra("PrimaryIdBetTb");

        spotStatusT = (TextView) findViewById(R.id.status_spot_tv);
        spotStatusAmountT = (TextView) findViewById(R.id.amount_status_tv);
        spotNoT = (TextView) findViewById(R.id.spot_no_t);
        spotNameT = (TextView) findViewById(R.id.spot_name_t);
        dateRegisterT = (TextView) findViewById(R.id.date_t);
        timeEnterP_T = (TextView) findViewById(R.id.time_enter_p_tv);
        timeExitP_T = (TextView) findViewById(R.id.time_exit_p_tv);
        timeWaitP_T = (TextView) findViewById(R.id.time_expire_p_tv);
        betAmountP_T = (TextView) findViewById(R.id.bet_amount_p_tv);
        betAmountST = (TextView) findViewById(R.id.bet_amount_s_tv);
        timeInS_T = (TextView) findViewById(R.id.time_enter_seeker_tv);
        timeWaitS_T = (TextView) findViewById(R.id.time_wait_seeker_tv);
        addressT = (TextView) findViewById(R.id.address_tv);
        areaSizeT = (TextView) findViewById(R.id.area_size_tv);
        timeEditEditP_T = (TextView) findViewById(R.id.time_edit_wait_tv);
        confirmStatusT = (TextView) findViewById(R.id.confirm_status_tv);

        applyChangesBtn = (Button) findViewById(R.id.apply_btn);

        rejectSpot = false;
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
                i.putExtra("PlaceId", "");
                startActivity(i);
                break;

            case R.id.time_wait_edit_btn:
//                DialogFragment newFragment1 = new TimePickerFragment(InProgressDetails.this);
                DialogFragment newFragment1 = new TimePickerFragment();
                newFragment1.show(getSupportFragmentManager(), "timePicker");
                break;

            case R.id.reject_seeker:
                DialogFragment newFragment2 = new RejectSpotDialog();
                newFragment2.show(getSupportFragmentManager(), "dealCancel");
                break;

            case R.id.apply_btn:
                if (!isNetworkAvailable()){
                    showMassage(R.string.no_internet);
                }
                else if(accountStatus){
                    new ApplyChangesToServer().execute();
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

    //-----------------time-------------------------------------

    private boolean validateTime() {
        if (timeEditInt < timeWaitPInt) {
            showMassage(R.string.select_valid_time);
            return false;
        }
        return true;
    }

    @Override
    public void TimeSet(String time, int timeInt) {
        if(validateTime()) {
            timeEditEditP_T.setText(time);
            timeEditEditP = time;
            timeEditInt = timeInt;
            applyChangesBtn.setVisibility(View.VISIBLE);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private TimeInterface timeInterface;

        public TimePickerFragment(){
            super();
        }

//        @SuppressLint("ValidFragment")
//        public TimePickerFragment(InProgressDetails activity){
//            this();
//            timeInterface = activity;
//        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = hourOfDay + ":" + minute;
            Log.e("date select", time);
            String s = String.valueOf(hourOfDay + minute);
            timeInterface = (TimeInterface) getActivity();
            timeInterface.TimeSet(time, Integer.parseInt(s));
        }
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
                        .add("start_type", "InProgressDetails")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("Primary_ID_spotTb", primaryIdSpotTb)
                        .add("Primary_ID_betTb", primaryIdBetTb)
                        .build();
                Request request = new Request.Builder()
                        .url(HAVE_SPOT_IN_PROGRESS_DETAILS)
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
                    JSONObject jsonObject1 = jo.getJSONObject("jsonObj");
                    switch (jsonObject1.getString("Status")){
                        case "Success":
                            JSONArray array = new JSONArray(jsonObject1.getString("ArrayJson"));
                            for(int i = 0; i < array.length(); i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                String spot_S_No = jsonObject.getString("Spot_S_No");
                                String spotName = jsonObject.getString("SpotName");
                                String dateSpotTb = jsonObject.getString("DateSpotTb");
                                String timeEnterSpotTb = jsonObject.getString
                                        ("EnterTimeSpotTb");
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
                                String dateBetTb = jsonObject.getString("DateBetTb");

                                spotNoT.setText(spot_S_No);
                                spotNameT.setText(spotName);
                                dateRegisterT.setText(dateSpotTb);
                                timeEnterP_T.setText(timeEnterSpotTb);
                                timeExitP_T.setText(timeExitSpotTb);
                                timeWaitP_T.setText(timeWaitP);
                                betAmountP_T.setText(betAmountSpotTb);
                                addressT.setText(address);
                                areaSizeT.setText(areaSize);
                                spotStatusT.setText(spotStatus);
                                spotStatusAmountT.setText(statusPayment);
                                timeInS_T.setText(timeEnterBetTb);
                                timeWaitS_T.setText(timeWaitBetTb);
                                betAmountST.setText(betAmountBetTb);
                                confirmStatusT.setText(statusSpotBySeeker);

                                timeWaitPInt = Integer.parseInt(timeWaitP.substring(0, 2) + timeWaitP
                                        .substring(3, 5) + timeWaitP.substring(6));

                            }

                            break;
                        default:
                            Toast.makeText(InProgressDetails.this, getResources().getString(R.string
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

    //-------------------------apply change to spot--------------------

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class ApplyChangesToServer extends AsyncTask<Void, String, String> {

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
                        .add("start_type", "InProgressDetails")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("TimeWaitChange", timeEditEditP)
                        .add("DealCancel", String.valueOf(rejectSpot))
                        .add("Primary_ID_betTb", primaryIdBetTb)
                        .build();
                Request request = new Request.Builder()
                        .url(HAVE_SPOT_IN_PROGRESS_DETAILS)
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
                    JSONObject jsonObject1 = jo.getJSONObject("jsonObj");
                    switch (jsonObject1.getString("Status")){
                        case "Success":
                            showMassage(R.string.apply_success);
                            finish();
                            break;
                        default:
                            Toast.makeText(InProgressDetails.this, getResources().getString(R.string
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
