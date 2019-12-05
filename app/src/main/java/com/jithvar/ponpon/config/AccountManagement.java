package com.jithvar.ponpon.config;

import com.jithvar.ponpon.need_spot.BookSpot;
import com.jithvar.ponpon.need_spot.handler.BookSpotData;

import java.util.ArrayList;

/**
 * Created by KinG on 26-10-2017.
 * Created by ${EMAIL}.
 */

public class AccountManagement {

    public static boolean LOAD_MAP;
    private static ArrayList<BookSpotData> bookSpotData;
    public static boolean ACCOUNT_STATUS;
    public static String  USER_ID;
    public static String  MOBILE_NUMBER;

    public static void setAccountStatus(boolean status){
        ACCOUNT_STATUS = status;
    }

    public static void setUserId(String userId) {
        USER_ID = userId;
    }

    public static void setMobileNumber(String mobileNumber) {
        MOBILE_NUMBER = mobileNumber;
    }

    public static ArrayList<BookSpotData> getBookSpotData() {
        return bookSpotData;
    }

    public static void setBookSpotData(ArrayList<BookSpotData> bookSpotData) {
        AccountManagement.bookSpotData = bookSpotData;
    }
}
