package com.jithvar.ponpon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jithvar.ponpon.config.AccountManagement;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.handler.DataBaseHandler;

import java.sql.SQLException;

public class StartActivity extends AppCompatActivity {

    private int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        checkDatabase();
        backgroundTask();
    }

    private void checkDatabase() {

        DatabaseDB db  = new DatabaseDB(StartActivity.this);
        int loginTimes = 5;
        try {
            loginTimes = db.getLoginTimes();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.e("loginTimes ", String.valueOf(loginTimes));

        try {
//            AccountManagement.setAccountStatus(false);
            if(!db.mobileVerified()){
                //for new registration
                result = 1;
            }
            else if(!db.isAccountActive() && loginTimes > 3){
                result = 1;
            }
            else if(!db.isAccountActive()){
                db.insertLoginTimesTB(new DataBaseHandler(++loginTimes));
                result = 2;
            }
            else if(db.isAccountActive() && db.isSessionExpired()){
                result = 3;
//                AccountManagement.setAccountStatus(true);
            }
            else if(db.isAccountActive() && !db.isSessionExpired()) {
                if (!db.accountType().equals("")) {
//                    AccountManagement.setAccountStatus(true);
                    if (db.accountType().equals("Personal")) {
                        result = 4;
                    } else if (db.accountType().equals("Business")) {
                        result = 5;
                    } else {
                        result = 6;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void backgroundTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    checkDatabase();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                checkDatabase();

                try {
//                    checkDatabase();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                move();
            }
        }).start();
    }

    private void move() {
        Log.e("result", String.valueOf(result));
        switch (result){
            case 0:
            case 1:
                Intent i3 = new Intent(this, LoginActivity.class);
//                i3.putExtra("result_type", result);
                startActivity(i3);
                finish();
                break;

            case 2:
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("result_type", result);
                startActivity(i);
                finish();
                break;

            case 3:
                Intent i4 = new Intent(this, LoginActivityAgain.class);
                i4.putExtra("result_type", result);
                startActivity(i4);
                finish();
                break;
            case 4:
            case 5:
            case 6:
                Intent i6 = new Intent(this, MainActivity.class);
                i6.putExtra("result_type", result);
                startActivity(i6);
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
