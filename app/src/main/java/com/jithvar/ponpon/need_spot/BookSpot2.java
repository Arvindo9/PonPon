package com.jithvar.ponpon.need_spot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.interfaceClass.TimeInterface;
import com.jithvar.ponpon.map.ViewLocationOnMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import static com.jithvar.ponpon.config.Config.SPOT_REGISTRATION_PROVIDER;

/**
 * Created by KinG on 08-11-2017.
 * Created by ${EMAIL}.
 */

public class BookSpot2 extends FragmentActivity implements TimeInterface{

    private int timeValue;
    private TextView spotStatusT;
    private TextView spotNoT;
    private TextView dateRegisterT;
    private TextView spotNameT;
    private TextView timeWaitP_T;
    private TextView timeExitP_T;
    private TextView betP_T;
    private TextView totalBetT;
    private TextView highestBetT;
    private TextView timeInS_T;
    private TextView timeWaitS_T;
    private TextView betS_T;
    private TextView locationT;
    private EditText betS_E;
    private TextInputLayout betS_Layout;
    private double betS_d;
    private String betS_s;
    private TextView areaSizeT;
    private String timeWaitS = "";
    private String timeInS = "";
    private String primaryIdSpotTb;
    private boolean accountStatus;
    private String userId;
    private String mobileNumber;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String address;
    private String areaSize;
    private int currentDate;
    private int timeInSInt;
    private int timeWaitSInt;
    private int dateP;
    private int timeWaitPInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_spot_2);
//
//        userId = getIntent().getStringExtra("UserId");
//        accountStatus = getIntent().getBooleanExtra("AccountStatus", false);
//        mobileNumber = getIntent().getStringExtra("MobileNumber");

        userId = USER_ID;
        mobileNumber = MOBILE_NUMBER;
        accountStatus = ACCOUNT_STATUS;

        primaryIdSpotTb = getIntent().getStringExtra("PrimaryIdSpotTb");

        spotStatusT = (TextView) findViewById(R.id.status_t);
        spotNoT = (TextView) findViewById(R.id.spot_no_t);
        spotNameT = (TextView) findViewById(R.id.spot_name_t);
        dateRegisterT = (TextView) findViewById(R.id.date_t);
        timeExitP_T = (TextView) findViewById(R.id.time_exit_p_t);
        timeWaitP_T = (TextView) findViewById(R.id.time_max_wait_p_t);
        betP_T = (TextView) findViewById(R.id.bet_amount_p_t);
        totalBetT = (TextView) findViewById(R.id.total_bet_t);
        highestBetT = (TextView) findViewById(R.id.highest_bet_t);
        timeInS_T = (TextView) findViewById(R.id.time_enter_tv);
        timeWaitS_T = (TextView) findViewById(R.id.time_wait_s_t);
        locationT = (TextView) findViewById(R.id.location_tv);
        areaSizeT = (TextView) findViewById(R.id.area_size_t);

        betS_E = (EditText) findViewById(R.id.bet_amount__s_et);
        betS_Layout = (TextInputLayout) findViewById(R.id.bet_amount__s_l);

        betS_E.addTextChangedListener(new MyTextWatcher(betS_E));

        new LoadDataFrromServer().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String s = String.valueOf(year + month + day);
        currentDate = Integer.parseInt(s);
    }

    private class MyTextWatcher implements TextWatcher {
        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.bet_amount__s_et:
                    validateBet();
                    break;
            }
        }
    }

    private boolean validateBet() {
        if (betS_E.getText().toString().trim().isEmpty()) {
            betS_Layout.setError(getString(R.string.bet_req));
            requestFocus(betS_Layout);
            return false;
        } else {
            betS_Layout.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateTime() {
        if (dateP >= currentDate &&
                timeWaitPInt >= timeInSInt && timeWaitPInt >= timeWaitSInt) {
            showMassage(R.string.select_valid_time);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.time_enter_btn:
                DialogFragment newFragment1 = new TimePickerFragment(BookSpot2.this);
                newFragment1.show(getSupportFragmentManager(), "timePicker");
                timeValue = 1;
                break;

            case R.id.time_exit_btn:
                DialogFragment newFragment2 = new TimePickerFragment(BookSpot2.this);
                newFragment2.show(getSupportFragmentManager(), "timePicker");
                timeValue = 2;
                break;

            case R.id.map_img:
                Intent i = new Intent(BookSpot2.this, ViewLocationOnMap.class);
                i.putExtra("Latitude", latitude);
                i.putExtra("Longitude", longitude);
                i.putExtra("Address", address);
                i.putExtra("AreaSize", areaSize);
                i.putExtra("PlaceId", "");
                startActivity(i);
                break;

            case R.id.add_spot:
                submitFrom();
                break;
        }
    }

    private void submitFrom() {
        if(!validateBet()){
            return;
        }
        if(timeInS.equals("") && !validateTime()){
            return;
        }
        if(timeWaitS.equals("") && !validateTime()){
            return;
        }

        betS_s = betS_E.getText().toString();

        Intent i = new Intent(BookSpot2.this, BookSpot3.class);
        i.putExtra("UserId", userId);
        i.putExtra("AccountStatus", accountStatus);
        i.putExtra("MobileNumber", mobileNumber);
        i.putExtra("SpotPrimaryId", primaryIdSpotTb);
        i.putExtra("SpotStatus", spotStatusT.getText().toString());
        i.putExtra("SpotNo", spotNoT.getText().toString());
        i.putExtra("SpotName", spotNameT.getText().toString());
        i.putExtra("Date", dateRegisterT.getText().toString());
        i.putExtra("TimeExitP", timeExitP_T.getText().toString());
        i.putExtra("TimeWaitP", timeWaitP_T.getText().toString());
        i.putExtra("BetP", betP_T.getText().toString());
        i.putExtra("TotalBet", totalBetT.getText().toString());
        i.putExtra("HighestBet", highestBetT.getText().toString());
        i.putExtra("Location", locationT.getText().toString());
        i.putExtra("TimeEnterS", timeInS);
        i.putExtra("TimeWaitS", timeWaitS);
        i.putExtra("BetS", betS_s);
        startActivity(i);

    }


    @Override
    public void TimeSet(String time, int timeInt) {
        switch (timeValue){
            case 1:
                timeInS_T.setText(time);
                timeInS = time;
                timeInSInt = timeInt;
                break;
            case 2: timeWaitS_T.setText(time);
                timeWaitS = time;
                timeWaitSInt = timeInt;
                break;
        }
    }

    //---------------time picker---------------------------------
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private TimeInterface timeInterface;

        public TimePickerFragment(){
            super();
        }

        @SuppressLint("ValidFragment")
        public TimePickerFragment(BookSpot2 activity){
            this();
            timeInterface = activity;
        }

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
            timeInterface.TimeSet(time, Integer.parseInt(s));
        }
    }

    //-----------------Send to server----------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadDataFrromServer extends AsyncTask<Void, String, String> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(BookSpot2.this, AlertDialog.THEME_HOLO_LIGHT);
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
                        .add("start_type", "LoadSpotData")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("Primary_ID", primaryIdSpotTb)
                        .build();
                Request request = new Request.Builder()
                        .url(BOOK_SPOT_2_DETAILS)
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
            Log.e("primaryid==== ", primaryIdSpotTb);
            Log.e("l========= ", result);
            if(!result.equals("")) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    switch (jsonObject.getString("Status")){
                        case "Success":
//                            showMassage(R.string.success);
                            spotNoT.setText(jsonObject.getString("Spot_S_No"));
                            spotNameT.setText(jsonObject.getString("SpotName"));

                            String dateRegister = jsonObject.getString("DateRegister");
                            dateRegisterT.setText(dateRegister);


                            String timeExitP = jsonObject.getString("ExitTime");
                            String timeWaitP = jsonObject.getString("WaitTime");
                            timeExitP_T.setText(timeExitP);
                            timeWaitP_T.setText(timeWaitP);

                            betP_T.setText(jsonObject.getString("BetAmountP"));
                            totalBetT.setText(jsonObject.getString("TotalBet"));
                            highestBetT.setText(jsonObject.getString("HighestBet"));

                            address = jsonObject.getString("Address");
                            areaSize = jsonObject.getString("AreaSize");
                            locationT.setText(address);
                            areaSizeT.setText(areaSize);
                            latitude = Double.parseDouble(jsonObject.getString("Latitude"));
                            longitude = Double.parseDouble(jsonObject.getString("Longitude"));

                            dateP = Integer.parseInt(dateRegister.substring(0, 2) +
                                    dateRegister.substring(3, 5) + dateRegister.substring(6));

                            timeWaitPInt = Integer.parseInt(timeWaitP.substring(0, 2) + timeWaitP
                                    .substring(3, 5) + timeWaitP.substring(6));
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
