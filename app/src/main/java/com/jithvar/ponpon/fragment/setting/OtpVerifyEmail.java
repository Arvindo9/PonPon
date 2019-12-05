package com.jithvar.ponpon.fragment.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jithvar.ponpon.MainActivity;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.fonts.ArizoniaFont;
import com.jithvar.ponpon.handler.DataBaseHandler;

import java.sql.SQLException;

/**
 * Created by KinG on 22-10-2017.
 * Created by ${EMAIL}.
 */

public class OtpVerifyEmail extends Activity implements View.OnClickListener{

    private EditText otpE;
    private TextInputLayout otpLayout;
    private String enterOtp;
    private String sendOtp;
    private String email;
    private String phone;
    private String userId;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_login_email);

        sendOtp = getIntent().getStringExtra("OTPSend");
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");

//        ((ArizoniaFont) findViewById(R.id.title)).setText(getResources().getString(R.string.email_verify));

        TextView titleTv = (TextView) findViewById(R.id.title);
        titleTv.setText(getResources().getString(R.string.email_verify));

        otpE = (EditText) findViewById(R.id.enter_otp);
        otpLayout = (TextInputLayout) findViewById(R.id.enter_otp_l);

        otpE.addTextChangedListener(new MyTextWatcher(otpE));

        Log.e("otpsend", sendOtp);
    }

    private boolean validateOtpEt() {
        if (otpE.getText().toString().trim().isEmpty() ||
                otpE.getText().toString().length() != 6) {
            otpLayout.setError(getString(R.string.otp_code_req));
            requestFocus(otpLayout);
            return false;
        } else {
            otpLayout.setErrorEnabled(false);
            verifyOtp();
        }

        return true;
    }

    private void verifyOtp() {
        enterOtp = otpE.getText().toString();
        if(enterOtp.equals(sendOtp)) {
            Toast.makeText(this, "verification successful", Toast.LENGTH_SHORT).show();

            DatabaseDB db = new DatabaseDB(this);
            try {
                if(db.mobileVerified()) {
                    db.insertRegistrationTB(new DataBaseHandler(userId, userName, phone,
                            email, "Passive", "True", "True"));
                }
                else{
                    db.insertRegistrationTB(new DataBaseHandler(userId, userName, phone,
                            email, "Passive", "True", "False"));
                }

                finish();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "incorrect otp", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                case R.id.enter_otp:
                    validateOtpEt();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;

        }
    }
}
