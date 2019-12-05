package com.jithvar.ponpon.handler;

/**
 * Created by Arvindo Mondal on 13/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class DataBaseHandler {

    private String primaryID;
    private String accountSession;
    private String accountType;
    private String userId;
    private String userName;
    private String houseNumber;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String gender;
    private String date;
    private String countryCode;
    private String phone;
    private String email;
    private String accountStatus;
    private int times;
    private String emailValidate;
    private String phoneValidate;

    public DataBaseHandler(String userId, String userName ,String phone, String email,
                           String accountStatus,
                           String emailValidate, String phoneValidate) {
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.accountStatus = accountStatus;
        this.emailValidate = emailValidate;
        this.phoneValidate = phoneValidate;
    }

    public DataBaseHandler( int times) {
        this.times = times;
    }

    public DataBaseHandler(String primaryID, String accountSession, String accountType) {
        this.primaryID = primaryID;
        this.accountSession = accountSession;
        this.accountType = accountType;
    }

    public DataBaseHandler(String accountType, int tmp) {
        this.accountType = accountType;
    }

    public DataBaseHandler(String userId, String userName, String houseNumber, String street,
                           String landmark, String city,
                           String state, String country, String pincode, String gender, String date,
                           String countryCode) {
        this.userId = userId;
        this.userName = userName;
        this.houseNumber = houseNumber;
        this.street = street;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.gender = gender;
        this.date = date;
        this.countryCode = countryCode;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailValidate() {
        return emailValidate;
    }

    public String getPhoneValidate() {
        return phoneValidate;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountSession() {
        return accountSession;
    }

    public String getPrimaryID() {
        return primaryID;
    }

    public int getTimes() {
        return times;
    }

    public String getStreet() {
        return street;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPincode() {
        return pincode;
    }

    public String getGender() {
        return gender;
    }

    public String getDate() {
        return date;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }
}
