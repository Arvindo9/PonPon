package com.jithvar.ponpon;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jithvar.ponpon.config.Config;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.dialog.VerifyLogin;
import com.jithvar.ponpon.fragment.setting.OtpVerifyPhone;
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

import static com.jithvar.ponpon.config.Config.EMAIL_OTP_SEND;
import static com.jithvar.ponpon.config.Config.LOGIN_URL;

/**
 * Created by Arvindo Mondal on 13/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class LoginActivity extends FragmentActivity implements VerifyLogin.OtpVerifyDialog{
    private static final int OTP_RESULT = 10;
    private TextInputLayout phoneLayout;
    private EditText phoneE;
    private ProgressDialog mProgressDialog;
    private String userName;
    private String email;
    private String phone;
//    private String information;
    private TextInputLayout userNameLayout;
    private EditText emailE;
    private EditText userNameE;
    private TextInputLayout emailLayout;
    private ProgressDialog progressDialog;
    private String otpSend = "123456";
    private String userId = "";
//    private EditText editText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        information = getIntent().getStringExtra("result_type");

        phoneE = (EditText) findViewById(R.id.phone);
        phoneLayout = (TextInputLayout) findViewById(R.id.phone_l);
        userNameE = (EditText) findViewById(R.id.user_name);
        userNameLayout = (TextInputLayout) findViewById(R.id.user_name_l);
        emailE = (EditText) findViewById(R.id.email);
        emailLayout = (TextInputLayout) findViewById(R.id.email_l);

        phoneE.addTextChangedListener(new MyTextWatcher(phoneE));
        emailE.addTextChangedListener(new MyTextWatcher(emailE));
        userNameE.addTextChangedListener(new MyTextWatcher(userNameE));
    }

    @Override
    protected void onStart() {
        super.onStart();

//        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
//        Account[] accounts = AccountManager.get(this).getAccounts();
//        for (Account account : accounts) {
//            if (gmailPattern.matcher(account.name).matches()) {
//                email = account.name;
//            }
//        }

//        Log.e("kdsflkkd ,fld", email);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                submitForm();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void submitForm() {

        if (!validatePhoneEmail()) {
            return;
        }

        if(!validateUserName()){
            return;
        }

//        if (!validatePhone()) {
//            return;
//        }

        userName = userNameE.getText().toString();
        email = emailE.getText().toString();
        phone = phoneE.getText().toString();

        new SendingDataToServer().execute();
    }

    private boolean validatePhoneEmail(){
        if (phoneE.getText().toString().trim().isEmpty() &&
                emailE.getText().toString().trim().isEmpty()) {
            phoneLayout.setError(getString(R.string.phone_req));
            requestFocus(phoneLayout);
            return false;
        }
        else if (!phoneE.getText().toString().trim().isEmpty() &&
                phoneE.getText().toString().length() != 10) {
            phoneLayout.setError(getString(R.string.phone_req));
            requestFocus(phoneLayout);
            return false;
        }
        else if (!emailE.getText().toString().trim().isEmpty() &&
                !emailE.getText().toString().contains("@") &&
                (!emailE.getText().toString().substring(emailE.getText().toString().indexOf("@")+1).
                        contains("."))){
            emailLayout.setError(getString(R.string.email_req));
            requestFocus(emailLayout);
            return false;
        }
        else {
            phoneLayout.setErrorEnabled(false);
            emailLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        if (emailE.getText().toString().trim().isEmpty() ||
                !emailE.getText().toString().contains("@") ||
                !emailE.getText().toString().substring(emailE.getText().toString().indexOf("@")+1).
                        contains(".")) {
            emailLayout.setError(getString(R.string.email_req));
            requestFocus(emailE);
            return false;
        } else {
            emailLayout.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePhone() {
        if (phoneE.getText().toString().trim().isEmpty() ||
                phoneE.getText().toString().length() != 10) {
            phoneLayout.setError(getString(R.string.phone_req));
            requestFocus(phoneLayout);
            return false;
        } else {
            phoneLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateUserName() {
        if (userNameE.getText().toString().trim().isEmpty()) {
            userNameLayout.setError(getString(R.string.user_name_req));
            requestFocus(userNameLayout);
            return false;
        } else {
            userNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void sendingOTP(){
        new ConnectingOTP().execute(Config.sendOpt(otpSend, phone));
    }

    @Override
    public void onFinishOtpDialog(String inputText) {
        if(inputText.equals(otpSend)) {
            Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show();

            DatabaseDB db = new DatabaseDB(LoginActivity.this);
            try {
                db.insertRegistrationTB(new DataBaseHandler(userId, userName, phone,
                        email, "Passive", "False", "True"));

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("result_type", "None");
                startActivity(i);
                finish();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "incorrect otp", Toast.LENGTH_SHORT).show();
        }
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
                case R.id.phone:
                    validatePhone();
                    break;

                case R.id.email:
                    validateEmail();
                    break;

                case R.id.user_name:
                    validateUserName();
                    break;
            }
        }
    }

    //-------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class SendingDataToServer extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(LoginActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage("Loading request...");
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
                        .add("start_type", "Login")
                        .add("UserName", userName)
                        .add("MobileNumber", phone)
                        .add("Email", email)
                        .build();
                Request request = new Request.Builder()
                        .url(LOGIN_URL)
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

                        DatabaseDB db = new DatabaseDB(LoginActivity.this);
                        try {
                            db.insertRegistrationTB(new DataBaseHandler(userId, userName, phone,
                                    email, "Passive", "False", "False"));

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    sendingOtpToEmail();
                                }
                            }).start();

                            sendingOTP();

//                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                            i.putExtra("result_type", "None");
//                            startActivity(i);
//                            finish();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "mobile number or email does not exist" +
                                        "\n please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "please try again", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                //--------------
                Toast.makeText(LoginActivity.this, "please try again",
                        Toast.LENGTH_SHORT).show();
            }


            //--remove it-----
//            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//            i.putExtra("result_type", "None");
//            startActivity(i);
//            finish();
            //--------------
        }

        private String requestServerForDataString() {
            try {
                URL url = new URL(LOGIN_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //connection.connect();

                String reqHead = "Accept:application/json";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("connection","Keep-Alive"+reqHead);
                @SuppressWarnings("deprecation")
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                entity.addPart("Member[Mobile]",new StringBody(phone));

                connection.addRequestProperty("content-length",entity.getContentLength()+"");
                connection.addRequestProperty(entity.getContentType().getName(),
                        entity.getContentType().getValue());

                OutputStream os = connection.getOutputStream();
                entity.writeTo(connection.getOutputStream());
                os.close();
                Log.d("HITTING","hitting url");
                connection.connect();
                Log.e("1-----------", String.valueOf(connection.getResponseCode()));
                Log.e("2----------", String.valueOf(HttpURLConnection.HTTP_OK));

                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    return readStream(connection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "fails";
        }

        private String readStream(InputStream inputStream) {

            String response = "";
            BufferedReader reader;
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            try {
                while ((line = reader.readLine())!=null){
                    builder.append(line);
                    Log.e("\n", builder.toString());
                }
                response = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.e("responset form server ", response);

            return response;
        }

        private String initializedData(String response) throws Exception {
            if(response != null && !response.equals("")){
                try {
                    JSONObject c = new JSONObject(response);
                    final String status = c.getString("status");

                    if(status.equals("Success")){
                        final String userId = c.getString("data");
                        response = "true";
                    }
                    else{
                        response = "false";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    response = "false";
                }
            }
            return response;
        }
    }


    //-----------------otp email send-------------

    private void sendingOtpToEmail(){
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

//            return
            otpEmailValueReturn(response.body().string());

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
            e.printStackTrace();
        }

//        return "";
    }

    private void otpEmailValueReturn(String result) {
        if (!result.equals("")) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if (jsonObject.getString("Status").equals("Success")) {
                    Log.e("email Status", "Success");
                }
                else{
                    Log.e("email Status", jsonObject.getString("Status"));
                }
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                e.printStackTrace();
            }
        }
    }

    //----------------otp------------------------.

    private void showOptDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        VerifyLogin verifyLogin = new VerifyLogin();
        verifyLogin.setCancelable(false);
//        verifyLogin.setDialogTitle("Enter OTP");
        verifyLogin.show(fragmentManager, "opt dialog");
    }

    void verifyOtp(){

        Intent i = new Intent(LoginActivity.this, OtpVerifyPhone.class);
        i.putExtra("OTPSend", otpSend);
        i.putExtra("userId", userId);
        i.putExtra("userName", userName);
        i.putExtra("phone", phone);
        i.putExtra("email", email);
        startActivity(i);
        finish();
    }

    private class ConnectingOTP extends AsyncTask<String, Integer, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Loading request...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
//            showOptDialog();
            verifyOtp();
//            startActivityForResult(i, OTP_RESULT);
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
}