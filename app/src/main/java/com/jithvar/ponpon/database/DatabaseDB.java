package com.jithvar.ponpon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jithvar.ponpon.handler.DataBaseHandler;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Arvindo Mondal on 13/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class DatabaseDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ARVINDO";
    private static final int DATABASE_VERSION = 1;

    private static final String REGISTRATION_TABLE = "REGISTRATION_TABLE";
    private static final String ACCOUNT_STATUS_TABLE = "ACCOUNT_STATUS_TABLE";
    private static final String LOGIN_TIMES_TABLE = "LOGIN_TIMES_TABLE";
    private static final String USER_PROFILE_TABLE = "USER_PROFILE_TABLE";

    private static final String KEY_PRIMARY_ID = "PRIMARY_ID";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_ACCOUNT_STATUS = "ACCOUNT_STATUS";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_EMAIL_VALIDATE = "EMAIL_VALIDATE";
    private static final String KEY_PHONE_VALIDATE = "PHONE_VALIDATE";
    private static final String KEY_USER_FULL_NAME = "USER_NAME";
    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_PHONE = "PHONE_Number";
    private static final String KEY_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    private static final String KEY_ACCOUNT_SESSION = "ACCOUNT_SESSION";
    private static final String KEY_DATE_TIME = "DATE_TIME";
    private static final String KEY_TIMES = "TIMES";


    private static final String KEY_DOB = "DOB";
    private static final String KEY_cOUNTRY_CODE = "COUNTRY_CODE";
    private static final String KEY_HOUSE_NO = "HOUSE_NO";
    private static final String KEY_STREET = "STREET";
    private static final String KEY_STATE = "STATE_";
    private static final String KEY_LANDMARK = "LANDMARK";
    private static final String KEY_CITY = "CITY";
    private static final String KEY_COUNTRY = "COUNTRY";
    private static final String KEY_PINCODE = "PINCODE";
    private static final String KEY_GENDER = "GENDER";

    public DatabaseDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REGISTRATION_TB = "CREATE TABLE " + REGISTRATION_TABLE + "(" +
                KEY_PRIMARY_ID +" INTEGER PRIMARY KEY," +
                KEY_USER_ID + " VARCHAR(20)," +
                KEY_USER_NAME + " VARCHAR(100)," +
                KEY_PHONE + " VARCHAR(20)," +
                KEY_EMAIL + " VARCHAR(100)," +
                KEY_ACCOUNT_STATUS + " VARCHAR(10)," +
                KEY_EMAIL_VALIDATE + " VARCHAR(10)," +
                KEY_PHONE_VALIDATE + " VARCHAR(10)," +
                KEY_DATE_TIME + " DATETIME DEFAULT" + " CURRENT_TIMESTAMP " +
                ");";
        db.execSQL(CREATE_REGISTRATION_TB);

        String CREATE_STATUS_TB = "CREATE TABLE " + ACCOUNT_STATUS_TABLE + "(" +
                KEY_PRIMARY_ID +" INTEGER PRIMARY KEY," +
                KEY_USER_ID + " VARCHAR(20)," +
                KEY_ACCOUNT_SESSION + " VARCHAR(10)," +
                KEY_ACCOUNT_TYPE + " VARCHAR(10)," +
                KEY_DATE_TIME + " DATETIME DEFAULT" + " CURRENT_TIMESTAMP, " +
                "foreign key ( " + KEY_USER_ID + " ) references " +
                REGISTRATION_TABLE + " ( " + KEY_USER_ID + " ) " +
                ");";
        db.execSQL(CREATE_STATUS_TB);

        String CREATE_LOGIN_TIMES_TB = "CREATE TABLE " + LOGIN_TIMES_TABLE + "(" +
                KEY_PRIMARY_ID + " INTEGER PRIMARY KEY," +
                KEY_TIMES + " INTEGER " +
                ");";
        db.execSQL(CREATE_LOGIN_TIMES_TB);


        String CREATE_USER_PROFILE_TB = "create table " +  USER_PROFILE_TABLE + " ( "+
                KEY_PRIMARY_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_ID + " VARCHAR(20)," +
                KEY_USER_FULL_NAME + " VARCHAR(50)," +
                KEY_DOB + " date not null," +
                KEY_GENDER + " VARCHAR(15)," +
                KEY_cOUNTRY_CODE + " varchar(2)," +
                KEY_HOUSE_NO + " varchar(10)," +
                KEY_STREET + " varchar(100)," +
                KEY_LANDMARK + " varchar(50)," +
                KEY_CITY + " varchar(50) not null," +
                KEY_STATE + " varchar(11) not null," +
                KEY_COUNTRY + "varchar(11) not null," +
                KEY_PINCODE + " varchar(6)," +
                KEY_DATE_TIME + "DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "foreign key ( " + KEY_USER_ID + " ) references " +
                REGISTRATION_TABLE + " ( " + KEY_USER_ID + ")" +
                ");";

//        db.execSQL(CREATE_USER_PROFILE_TB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    public void insertRegistrationTB(DataBaseHandler handler) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, handler.getUserId());
        values.put(KEY_USER_NAME, handler.getUserName());
        values.put(KEY_PHONE, handler.getPhone());
        values.put(KEY_EMAIL, handler.getEmail());
        values.put(KEY_ACCOUNT_STATUS, handler.getAccountStatus());
        values.put(KEY_EMAIL_VALIDATE, handler.getEmailValidate());
        values.put(KEY_PHONE_VALIDATE, handler.getPhoneValidate());
        db.insert(REGISTRATION_TABLE, null, values);
        db.close();
    }

    public void insertAccountStatusTB(DataBaseHandler handler) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, handler.getPrimaryID());
        values.put(KEY_ACCOUNT_SESSION, handler.getAccountSession());
        values.put(KEY_ACCOUNT_TYPE, handler.getAccountType());
        db.insert(ACCOUNT_STATUS_TABLE, null, values);
        db.close();
    }

    public void insertLoginTimesTB(DataBaseHandler handler) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMES, handler.getTimes());
        db.insert(LOGIN_TIMES_TABLE, null, values);
        db.close();
    }

    public void insertUserDetails(DataBaseHandler handler) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, handler.getUserId());
        values.put(KEY_USER_NAME, handler.getUserName());
        values.put(KEY_DOB, handler.getDate());
        values.put(KEY_GENDER, handler.getGender());
        values.put(KEY_cOUNTRY_CODE, handler.getCountryCode());
        values.put(KEY_HOUSE_NO, handler.getHouseNumber());
        values.put(KEY_STREET, handler.getTimes());
        values.put(KEY_LANDMARK, handler.getTimes());
        values.put(KEY_CITY, handler.getTimes());
        values.put(KEY_STATE, handler.getTimes());
        values.put(KEY_COUNTRY, handler.getTimes());
        values.put(KEY_PINCODE, handler.getTimes());
        db.insert(USER_PROFILE_TABLE, null, values);
        db.close();
    }

    public void setAccountType(DataBaseHandler handler) throws SQLException {
        String query = "WITH " + ACCOUNT_STATUS_TABLE + "  AS " + "(" +
                " SELECT " + KEY_PRIMARY_ID +
                " FROM " +  ACCOUNT_STATUS_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1 " +
                " )" +
                " UPDATE " + ACCOUNT_STATUS_TABLE +
                " SET " + KEY_ACCOUNT_TYPE + " = '" + handler.getAccountType() + "' " +
                " where " + KEY_ACCOUNT_SESSION + " = 'Active' ";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_STATUS_TABLE, handler.getAccountType());
        db.execSQL(query);
        db.close();
    }

    public boolean isSessionExpired() throws SQLException{
        boolean accountActive = isAccountActive();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_ACCOUNT_SESSION  +
                " from " + ACCOUNT_STATUS_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            if(c.getString(0).equals("Active") && accountActive){
                c.close();
                return false;
            }
        }
        db.close();

        return true;
    }

    public boolean isLoginPassiveAccountOver() throws SQLException{
        boolean accountActive = isAccountActive();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_ACCOUNT_SESSION  +
                " from " + ACCOUNT_STATUS_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            if(c.getString(0).equals("Active") && accountActive){
                c.close();
                return false;
            }
        }
        db.close();

        return true;
    }

    public String getDateTimeStatusTB() throws SQLException{
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_DATE_TIME  +
                " from " + ACCOUNT_STATUS_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            c.close();
            return c.getString(0);
        }
        db.close();
        return null;
    }

    public boolean isAccountActive(/*String userId */) throws SQLException{
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_ACCOUNT_STATUS  +
                " from " + REGISTRATION_TABLE +
//                " where " +
//                KEY_USER_ID + " = " + userId +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            if(c.getString(0).equals("Active")){
                c.close();
                return true;
            }
        }
        db.close();

        return false;
    }

    public String getActiveAccountPrimaryId() throws SQLException{
        String s = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_PRIMARY_ID  +
                " from " + REGISTRATION_TABLE +
                " where " + KEY_ACCOUNT_STATUS + " = 'Active' " +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            s = c.getString(0);
            c.close();
        }
        db.close();

        return s;
    }

    public String  accountType() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String s = "";
        String query = "select " + KEY_ACCOUNT_TYPE + " from " + ACCOUNT_STATUS_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            s = c.getString(0);
            c.close();
            return s;
        }
        db.close();
        return s;
    }

    public boolean  emailVerified() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_EMAIL_VALIDATE + " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            if(c.getString(0).equals("True")){
                c.close();
                return true;
            }
            c.close();
        }
        db.close();
        return false;
    }

    public boolean  mobileVerified() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_PHONE_VALIDATE + " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            if(c.getString(0).equals("True")){
                c.close();
                return true;
            }
            c.close();
        }
        db.close();
        return false;
    }

    public String getUserId() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_USER_ID +
                " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            String strings = c.getString(0);
            c.close();
            return strings;
        }
        db.close();
        return null;
    }

    public String getMobileNumber() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_PHONE +
                " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            String strings = c.getString(0);
            c.close();
            return strings;
        }
        db.close();
        return null;
    }

    public String getEmailAddress() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_EMAIL +
                " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            String strings = c.getString(0);
            c.close();
            return strings;
        }
        db.close();
        return null;
    }

    public String[] getRegistrationData() throws SQLException {
        String[] strings = new String[5];

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_USER_ID + ", " + KEY_USER_NAME + ", " + KEY_PHONE + "," +
                KEY_EMAIL + " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            strings[0] = "1";
            strings[1] = c.getString(0);
            strings[2] = c.getString(1);
            strings[3] = c.getString(2);
            strings[4] = c.getString(3);
            c.close();
            return strings;
        }
        db.close();
        strings[0] = "2";
        return strings;
    }

    public int getLoginTimes() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_TIMES +
                " from " + LOGIN_TIMES_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            int s = c.getInt(0);
            c.close();
            return s;
        }
        db.close();
        return 5;
    }

    public String[] getUserProfileData() throws SQLException {
        String[] strings = new String[13];

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_USER_FULL_NAME + ", " + KEY_DOB + ", "  + KEY_GENDER + ", "+
                KEY_HOUSE_NO + "," +
                KEY_STREET + ", " + KEY_LANDMARK + ", " + KEY_CITY + "," +
                KEY_STATE + ", " + KEY_COUNTRY + ", " + KEY_PINCODE + "," +
                KEY_cOUNTRY_CODE + ", "  +
                KEY_DATE_TIME +
                " from " + USER_PROFILE_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()){
            strings[0] = "1";
            strings[1] = c.getString(0);
            strings[2] = c.getString(1);
            strings[3] = c.getString(2);
            strings[4] = c.getString(3);
            strings[5] = c.getString(4);
            strings[6] = c.getString(5);
            strings[7] = c.getString(6);
            strings[8] = c.getString(7);
            strings[9] = c.getString(8);
            strings[10] = c.getString(9);
            strings[11] = c.getString(10);
            strings[12] = c.getString(11);
            c.close();
            return strings;
        }
        db.close();
        strings[0] = "2";
        return strings;
    }


}
