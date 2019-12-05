package com.jithvar.ponpon.fragment.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jithvar.ponpon.LoginActivity;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.config.AccountManagement;
import com.jithvar.ponpon.config.Config;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.dialog.ChangeEmail;
import com.jithvar.ponpon.dialog.VerifyLogin;
import com.jithvar.ponpon.handler.DataBaseHandler;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
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
import java.sql.SQLException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.Config.ACTIVATE_ACCOUNT;
import static com.jithvar.ponpon.config.Config.EMAIL_OTP_SEND;
import static com.jithvar.ponpon.config.Config.LOGIN_URL;

/**
 * Created by KinG on 22-10-2017.
 * Created by ${EMAIL}.
 */

public class ActivateAccount extends Activity implements View.OnClickListener,
        ChangeEmail.ChangeEmailDialog {

    private TextView mobileTv;
    private TextView emailTv;
    private TextView mobileVerifyTv;
    private TextView emailVerifyTv, editEmailTv;
    private DatabaseDB db;
    private String otpSend = "123456";
    private String phone;
    private String email;
    private ProgressDialog progressDialog;
    private CheckBox phoneCheckBox, emailCheckBox;
    private String userId, userName;
    private ProgressDialog mProgressDialog;
//    private boolean mobileBoxB, emailBoxB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseDB(this);
        setContentView(R.layout.fragment_activate_account);

        mobileTv = (TextView) findViewById(R.id.mobile_tv);
        emailTv = (TextView) findViewById(R.id.email_tv);
        mobileVerifyTv = (TextView) findViewById(R.id.mobile_verify);
        emailVerifyTv = (TextView) findViewById(R.id.email_verify);
        phoneCheckBox = (CheckBox) findViewById(R.id.mobile_checked);
        emailCheckBox = (CheckBox) findViewById(R.id.email_checked);
//        ((Button) findViewById(R.id.activate_account)).setOnClickListener(this);
//        ((Button) findViewById(R.id.deactivate_account)).setOnClickListener(this);
        editEmailTv = (TextView) findViewById(R.id.edit_email);
        editEmailTv.setOnClickListener(this);
        phoneCheckBox.setOnClickListener(this);
        emailCheckBox.setOnClickListener(this);
        mobileVerifyTv.setOnClickListener(this);
        emailVerifyTv.setOnClickListener(this);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public View onCreateView1(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
                Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_activate_account, container, false);
        mobileTv = (TextView) rootView.findViewById(R.id.mobile_tv);
        emailTv = (TextView) rootView.findViewById(R.id.email_tv);
        mobileVerifyTv = (TextView) rootView.findViewById(R.id.mobile_verify);
        emailVerifyTv = (TextView) rootView.findViewById(R.id.email_verify);
        phoneCheckBox = (CheckBox) rootView.findViewById(R.id.mobile_checked);
        emailCheckBox = (CheckBox) rootView.findViewById(R.id.email_checked);
        ((Button) rootView.findViewById(R.id.activate_account)).setOnClickListener(this);
        ((Button) rootView.findViewById(R.id.deactivate_account)).setOnClickListener(this);
        editEmailTv = (TextView) rootView.findViewById(R.id.edit_email);
        editEmailTv.setOnClickListener(this);
        phoneCheckBox.setOnClickListener(this);
        emailCheckBox.setOnClickListener(this);
        mobileVerifyTv.setOnClickListener(this);
        emailVerifyTv.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        String[] data = null;
        try {
            data = db.getRegistrationData();

            userId = data[1];
            userName = data[2];
            phone = data[3];
            email = data[4];
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mobileTv.setText(phone);
        emailTv.setText(email);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(db.emailVerified()){
//                emailBoxB = true;
                emailCheckBox.setChecked(!emailCheckBox.isChecked());
                emailCheckBox.setEnabled(false); // disable checkbox
//                emailCheckBox.setChecked(false);
                emailVerifyTv.setVisibility(View.GONE);
                editEmailTv.setVisibility(View.GONE);
            }

            if(db.mobileVerified()){
//                mobileBoxB = true;
                phoneCheckBox.setChecked(!phoneCheckBox.isChecked());
                phoneCheckBox.setEnabled(false); // disable checkbox
//                phoneCheckBox.setChecked(false);
                mobileVerifyTv.setVisibility(View.GONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        emailCheckBox.setEnabled(false); // disable checkbox
        phoneCheckBox.setEnabled(false); // disable checkbox
    }

    private ActivateAccount getContext(){
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mobile_verify:
                try {
                    if(!db.mobileVerified()) {
                        new ConnectingOTP("mobile").execute(Config.sendOpt(otpSend, phone));
                    }
                    else{
                        Toast.makeText(getContext(), getResources().getString(R.string
                                .already_verify), Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.email_verify:
                try {
                    if(!db.emailVerified()) {
                        new SendingOtpToEmail().execute();
//                        new ConnectingOTP("email").execute(Config.sendEmailOpt(otpSend, email));
//                        new CheckEmailActivate()
                    }
                    else{
                        Toast.makeText(getContext(), getResources().getString(R.string
                                .already_verify), Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.activate_account:
                try {
                    if(db.mobileVerified()){
                        String[] data = null;
                        data = db.getRegistrationData();

                        userId = data[1];
                        userName = data[2];
                        phone = data[3];
                        email = data[4];


                        new SendingDataToServer().execute();
                    }

//                    if(db.emailVerified() && db.mobileVerified()){
//                        String[] data = null;
//                        data = db.getRegistrationData();
//
//                        userId = data[1];
//                        userName = data[2];
//                        phone = data[3];
//                        email = data[4];
//
//                        new SendingDataToServer().execute();
//
//                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.deactivate_account:
                break;

            case R.id.edit_email:
                FragmentManager fragmentManager = getFragmentManager();
                ChangeEmail changeEmail = new ChangeEmail();
                changeEmail.setCancelable(true);
//        verifyLogin.setDialogTitle("Enter OTP");
                changeEmail.show(fragmentManager, "email dialog");
                break;
        }
    }

    void verifyMobileOtp(){
        Intent i = new Intent(getContext(), OtpVerifyPhone.class);
        i.putExtra("OTPSend", otpSend);
        i.putExtra("userId", userId);
        i.putExtra("userName", userName);
        i.putExtra("phone", phone);
        i.putExtra("email", email);
        startActivity(i);
    }

    void verifyEmailOtp(){
        Intent i = new Intent(getContext(), OtpVerifyEmail.class);
        i.putExtra("OTPSend", otpSend);
        i.putExtra("userId", userId);
        i.putExtra("userName", userName);
        i.putExtra("phone", phone);
        i.putExtra("email", email);
        startActivity(i);



    }

    @Override
    public void onChangedEmail(String email) {
        emailTv.setText(email);
        this.email = email;
//        new SendingOtpToEmail().execute();
    }

    private class ConnectingOTP extends AsyncTask<String, Integer, String> {
        private String type;

        ConnectingOTP(String string) {
            type = string;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Loading request...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(type.equals("mobile")) {
                verifyMobileOtp();
            }
            else{
                verifyEmailOtp();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                Log.e("hit Url",params[0]);
                response=run(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        private String run(String url) throws IOException {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    //-------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class SendingDataToServer extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.e("User_ID", userId);
            Log.e("UserName", userName);
            Log.e("MobileNumber", phone);
            Log.e("EmailId", email);

//            requestServerForDataString();
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("start_type", "Activate")
                        .add("User_ID", userId)
                        .add("UserName", userName)
                        .add("MobileNumber", phone)
                        .add("EmailId", email)
                        .build();
                Request request = new Request.Builder()
                        .url(ACTIVATE_ACCOUNT)
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
            mProgressDialog.dismiss();
            Log.e("l========= ", result);
            if(!result.equals("")) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("Status").equals("Success")) {
                        userId = jsonObject.getString("User_ID");

                        DatabaseDB db = new DatabaseDB(getContext());
                        try {
                            db.insertRegistrationTB(new DataBaseHandler(userId, userName, phone,
                                    email, "Active", "True", "True"));

                            db.insertAccountStatusTB(new DataBaseHandler(db.getActiveAccountPrimaryId(),
                                    "Active", "None"));

//                            AccountManagement.setAccountStatus(true);

                            Toast.makeText(getContext(), "Success",
                                    Toast.LENGTH_SHORT).show();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(jsonObject.getString("Status").equals("ErrorEmailNotVerify")){
                        Toast.makeText(getContext(), "Email not verified",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "mobile number or email does not exist" +
                                        "\n please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "please try again", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                //--------------
                Toast.makeText(getContext(), "please try again",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class SendingOtpToEmail extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(ActivateAccount.this, AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage("Sending...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
//            requestServerForDataString();
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("start_type", "EmailVerification")
                        .add("User_ID", userId)
                        .add("EmailId", email)
                        .build();
                Request request = new Request.Builder()
                        .url(EMAIL_OTP_SEND)
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
            mProgressDialog.dismiss();
            Log.e("l========= ", result);
            Log.e("userId ", userId);
            Log.e("email ", email);
            if (!result.equals("")) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("Status").equals("Success")) {
                        Toast.makeText(ActivateAccount.this, "Link send to your email",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivateAccount.this, "mobile number or email does not exist" +
                                        "\n please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ActivateAccount.this, "please try again", Toast.LENGTH_SHORT).show();
                }
            } else {
                //--------------
                Toast.makeText(ActivateAccount.this, "please try again",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
