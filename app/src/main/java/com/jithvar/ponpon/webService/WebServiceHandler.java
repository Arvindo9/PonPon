package com.jithvar.ponpon.webService;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.Config.URL_CHECK_OTP;
import static com.jithvar.ponpon.config.Config.URL_CHECK_UPDATE_USER;
import static com.jithvar.ponpon.config.Config.URL_SWIPE_ACCOUNT;
import static com.jithvar.ponpon.config.Config.URL_USER_PERSONAL_REG;

/**
 * Author       :   Mukesh Kumawat
 * Designation  :   Android Developer
 * E-mail       :   mukeshkmtskr@gmail.com
 * Date         :   18/02/2016
 * Company      :   Parasme Softwares & Technology
 * Purpose      :   Class to Handle All Web Services call in Ap
 * Description  :   Description.
 */
public class WebServiceHandler {
    private OkHttpClient okHttpClient;
    private RequestBody requestBody;
    private Request request;
    private Context context;
    private ProgressDialog progressDialog;
    public WebServiceListener serviceListener;

    public WebServiceHandler(Context context) {
        this.context= context;
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
    }

    public void registerPersonalUser(boolean isSwitching,String type,String acType,String regId, String userLegalName,String userPass,String userIncome,
                                     String userOccupation,String userGender,String userEstDate,String userAddrStreet,
                                     String userAddrCity,String userAddrState, String userPhone,String userEmail, String status){
        progressDialog.show();
        String switching;
        if(isSwitching)
            switching="true";
        else
            switching="false";

        requestBody = new FormBody.Builder()
                .add("swipe_account", switching)
                .add("user_acc_type", acType)
                .add("user_gcm_id", regId)
                .add("user_legal_name", userLegalName)
                .add("user_pass", userPass)
                .add("user_income", userIncome)
                .add("user_occupation", userOccupation)
                .add("user_gender", userGender)
                .add("user_est_date", userEstDate)
                .add("user_add_street", userAddrStreet)
                .add("user_add_city", userAddrCity)
                .add("user_add_state", userAddrState)
                .add("user_contact_phone", userPhone)
                .add("user_email", userEmail)
                .add("user_status", status)
                .build();

        if(type.equals("insert"))
        {
            request =  new Request.Builder()
                    .url(URL_USER_PERSONAL_REG)
                    .post(requestBody)
                    .build();
        }
        else{
            request =  new Request.Builder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-type", "application/json")
                    .url(URL_CHECK_UPDATE_USER)
                    .post(requestBody)
                    .build();
        }

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                serviceListener.onResponse(response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            }
        });
    }

    public void swipeAccountIfExists(String acType, String gcmId) {
        progressDialog.show();
        requestBody = new FormBody.Builder()
                .add("user_acc_type", acType)
                .add("user_gcm_id", gcmId)
                .build();
        request =  new Request.Builder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-type", "application/json")
                .url(URL_SWIPE_ACCOUNT)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                serviceListener.onResponse(response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            }
        });

    }

}