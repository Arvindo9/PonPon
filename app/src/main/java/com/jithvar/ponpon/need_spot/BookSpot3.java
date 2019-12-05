package com.jithvar.ponpon.need_spot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jithvar.ponpon.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.Config.BOOK_SPOT_2_DETAILS;
import static com.jithvar.ponpon.config.Config.BOOK_SPOT_3_DO_PAYMENT;

/**
 * Created by KinG on 09-11-2017.
 * Created by ${EMAIL}.
 */

public class BookSpot3 extends Activity {

    private String primaryIdPaymentTB;
    private int timeValue;
    private TextView spotStatusT;
    private TextView spotNoT;
    private TextView dateRegisterT;
    private TextView spotNameT;
    private TextView timeWaitP_T;
    private TextView timeExitP_T;
    private TextView betP_T;
    private TextView timeInS_T;
    private TextView timeWaitS_T;
    private TextView betS_T;
    private TextView locationT;
    private TextView areaSizeT;
    private String timeWaitS;
    private boolean accountStatus;
    private String userId;
    private String mobileNumber;
    private String spotStatus;
    private String spotName;
    private String spotNo;
    private String dateRegister;
    private String timeExitP;
    private String timeWaitP;
    private String betP;
    private String timeExitS;
    private String location;
    private String areaSize;
    private String betS;
    private String spotPrimaryId;
    private String timeEnterS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_spot_3);



        userId = getIntent().getStringExtra("UserId");
        accountStatus = getIntent().getBooleanExtra("AccountStatus", false);
        mobileNumber = getIntent().getStringExtra("MobileNumber");
        spotStatus = getIntent().getStringExtra("SpotStatus");
        spotNo = getIntent().getStringExtra("SpotNo");
        spotName = getIntent().getStringExtra("SpotName");
        dateRegister = getIntent().getStringExtra("Date");
        timeExitP = getIntent().getStringExtra("TimeExitP");
        timeWaitP = getIntent().getStringExtra("TimeWaitP");
        betP = getIntent().getStringExtra("BetP");
        timeEnterS = getIntent().getStringExtra("TimeEnterS");
        timeWaitS = getIntent().getStringExtra("TimeWaitS");
        location = getIntent().getStringExtra("Location");
        areaSize = getIntent().getStringExtra("AreaSize");
        betS = getIntent().getStringExtra("BetS");
        spotPrimaryId = getIntent().getStringExtra("SpotPrimaryId");

        spotStatusT = (TextView) findViewById(R.id.status_t);
        spotNoT = (TextView) findViewById(R.id.spot_no_t);
        spotNameT = (TextView) findViewById(R.id.spot_name_t);
        dateRegisterT = (TextView) findViewById(R.id.date_t);
        timeExitP_T = (TextView) findViewById(R.id.time_exit_p_t);
        timeWaitP_T = (TextView) findViewById(R.id.time_max_wait_p_t);
        betP_T = (TextView) findViewById(R.id.bet_amount_p_t);
        timeInS_T = (TextView) findViewById(R.id.time_enter_tv);
        timeWaitS_T = (TextView) findViewById(R.id.time_wait_s_t);
        locationT = (TextView) findViewById(R.id.location_tv);
        areaSizeT = (TextView) findViewById(R.id.area_size_t);
        betS_T = (TextView) findViewById(R.id.your_bet);
    }

    @Override
    protected void onStart() {
        super.onStart();

        spotStatusT.setText(spotStatus);
        spotNoT.setText(spotNo);
        spotNameT.setText(spotName);
        dateRegisterT.setText(dateRegister);
        timeExitP_T.setText(timeExitP);
        timeWaitP_T.setText(timeWaitP);
        betP_T.setText(betP);
        timeInS_T.setText(timeEnterS);
        timeWaitS_T.setText(timeWaitS);
        locationT.setText(location);
        areaSizeT.setText(areaSize);
        betS_T.setText(betS);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pay:
                new PaymentGateway().execute();
                break;
        }
    }

    //---------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class PaymentGateway extends AsyncTask<Void, String, String> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(BookSpot3.this, AlertDialog.THEME_HOLO_LIGHT);
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
                        .add("start_type", "PaymentGateway")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("Primary_ID_SpotTB", spotPrimaryId)
                        .add("EnterTime", timeEnterS)
                        .add("WaitTime", timeWaitS)
                        .add("BetAmount", betS)
                        .add("StatusPayment", "Paid")
                        .build();
                Request request = new Request.Builder()
                        .url(BOOK_SPOT_3_DO_PAYMENT)
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
                            showMassage(R.string.success);
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
