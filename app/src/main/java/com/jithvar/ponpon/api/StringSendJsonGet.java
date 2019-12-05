package com.jithvar.ponpon.api;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.having_spot.AddSpot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.Config.SPOT_REGISTRATION_PROVIDER;

/**
 * Created by KinG on 09-11-2017.
 * Created by ${EMAIL}.
 */

public class StringSendJsonGet implements Runnable{

    private final RequestBody requestBody;
    private final String url;
    private String result;

    StringSendJsonGet(RequestBody requestBody, String url){
        this.url = url;
        this.requestBody = requestBody;
    }

    @Override
    public void run() {
        try {
            OkHttpClient client = new OkHttpClient();
//            RequestBody formBody = new FormBody.Builder()
//                    .add("start_type", "BookSpot")
            Request request = new Request.Builder()
                    .url(SPOT_REGISTRATION_PROVIDER)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();

            result = response.body().string();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
            e.printStackTrace();
        }
    }
}
