package com.jithvar.ponpon.handler;

/**
 * Created by KinG on 04-11-2017.
 * Created by ${EMAIL}.
 */

public class SpotDetailsData {


    private final String userID;
    private final String userName;
    private final String dateRegister;
    private final String timeRegister;
    private final String enterTime;
    private final String waitTime;
    private final String betAmount;
    private final String statusPayment;
    private final String statusSpotByProvier;
    private final String statusSpotBySeeker;
    private final String primaryIdSpotTb;
    private final String primaryIdBetTb;

    public SpotDetailsData(String primaryIdSpotTb,String primaryIdBetTb,
                           String userID, String userName,
                           String dateRegister,
                           String timeRegister, String enterTime, String waitTime,
                           String betAmount, String statusPayment, String statusSpotByProvier,
                           String statusSpotBySeeker) {
        this.primaryIdSpotTb = primaryIdSpotTb;
        this.primaryIdBetTb = primaryIdBetTb;

        this.userID = userID;
        this.userName = userName;
        this.dateRegister = dateRegister;
        this.timeRegister = timeRegister;
        this.enterTime = enterTime;
        this.waitTime = waitTime;
        this.betAmount = betAmount;
        this.statusPayment = statusPayment;
        this.statusSpotByProvier = statusSpotByProvier;
        this.statusSpotBySeeker = statusSpotBySeeker;
    }

    public String getPrimaryIdSpotTb() {
        return primaryIdSpotTb;
    }

    public String getPrimaryIdBetTb() {
        return primaryIdBetTb;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public String getTimeRegister() {
        return timeRegister;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public String getBetAmount() {
        return betAmount;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public String getStatusSpotByProvier() {
        return statusSpotByProvier;
    }

    public String getStatusSpotBySeeker() {
        return statusSpotBySeeker;
    }
}
