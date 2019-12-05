package com.jithvar.ponpon;

import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.handler.DataBaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.Config.CUSTOMER_PROFILE;
import static com.jithvar.ponpon.config.Config.UPDATE_PROFILE;

/**
 * Created by KinG on 27-10-2017.
 * Created by ${EMAIL}.
 */

public class CustomerProfile extends Activity implements View.OnClickListener {

    private TextView userNameTv;
    private TextView accountTypeTv;
    private TextView addressTv;
    private TextView mobileTv;
    private TextView emailTv;
    private TextView genderTv;
    private TextView dobTv;

    private String userName;
    private String accountType;
    private String address;
    private String mobile;
    private String email;
    private String gender;
    private String dob;
    private boolean accountStatus;
    private ProgressDialog mProgressDialog;
    private String userId;
    private String mobileNumber;
    private DatabaseDB db;
    private String countryCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        userNameTv = (TextView) findViewById(R.id.user_name_tv);
        accountTypeTv = (TextView) findViewById(R.id.account_type_tv);
        addressTv = (TextView) findViewById(R.id.address_tv);
        mobileTv = (TextView) findViewById(R.id.mobile_tv);
        emailTv = (TextView) findViewById(R.id.email_tv);
        genderTv = (TextView) findViewById(R.id.gender_tv);
        dobTv = (TextView) findViewById(R.id.dob_tv);
        db = new DatabaseDB(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            accountStatus = db.isAccountActive();
            userId = db.getUserId();
            mobileNumber = db.getMobileNumber();
            email = db.getEmailAddress();
            accountType = db.accountType();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(accountStatus) {
            if (isNetworkAvailable()) {
                new LoadingDataFromServer().execute();
            }
            else{
                //offline
                setOfflineData();
            }
        }
        else{
            Toast.makeText(this, getResources().getString(R.string.activate_account), Toast
                    .LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void setData(){
        userNameTv.setText(userName);
        accountTypeTv.setText(accountType);
        addressTv.setText(address);
        mobileTv.setText(mobile);
        emailTv.setText(email);
        genderTv.setText(gender);
        dobTv.setText(dob);
    }

    //--------------------Offline-------------------

    private void setOfflineData(){

        try {
            String[] data = db.getUserProfileData();
            if(data[0].equals("1")) {
                userNameTv.setText(data[1]);
                accountTypeTv.setText(accountType);
                addressTv.setText(data[4] + ", " + data[5] + ", " + data[6] + ", " + data[7] + "," +
                        " " + data[8] + ", " + data[9] + ", " + data[10]
                );
                mobileTv.setText(mobile);
                emailTv.setText(email);
                genderTv.setText(data[3]);
                dobTv.setText(data[2]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //--------------------Server--------------------

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadingDataFromServer extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(CustomerProfile.this, AlertDialog.THEME_HOLO_LIGHT);
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
                        .add("start_type", "User_profile")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .build();
                Request request = new Request.Builder()
                        .url(CUSTOMER_PROFILE)
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
                            userName = jsonObject.getString("UserName");
                            gender = jsonObject.getString("Gender");
                            dob = jsonObject.getString("DOB");
                            countryCode = jsonObject.getString("CountryCode");
                            String houseNum = jsonObject.getString("HouseNumber");
                            String street = jsonObject.getString("Street");
                            String landmark = jsonObject.getString("LandMark");
                            String city = jsonObject.getString("City");
                            String state = jsonObject.getString("State_");
                            String country = jsonObject.getString("Country");
                            String pincode = jsonObject.getString("PinCode");

                            address = houseNum + ", " + street + ", " + landmark + ", " + city +
                                    ", " + state + ", " + country + ". " + "\n" + pincode;

                            setData();
                            break;

//                        case "Error":
//                            break;
//                        case "ErrorMobile":
//                            break;
//                        case "ErrorAccount":
//                            break;
//                        case "ErrorStart":
//                            break;
//                        case "ErrorFetch":
//                            break;
//                        case "ErrorDb":
//                            break;
                        default:
                            Toast.makeText(CustomerProfile.this, getResources().getString(R.string
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

    //------------------------------------------------

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
