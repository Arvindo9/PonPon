package com.jithvar.ponpon.handler;

/**
 * Created by KinG on 12-11-2017.
 * Created by ${EMAIL}.
 */

public class BookSpot1Data {
    private final String primaryID;
    private final String spot_s_no;
    private final String spotName;
    private final String dateRegister;
    private final String exitTimeP;
    private final String waitTimeP;
    private final String betAmountP;
    private final String address;
    private final String statusSpot;
    private final String totalBet;
    private final String areaSize;

    public BookSpot1Data(String primaryID, String spot_s_no, String spotName, String dateRegister,
                         String exitTimeP, String waitTimeP, String betAmountP, String address,
                         String statusSpot, String totalBet, String areaSize) {

        this.primaryID = primaryID;
        this.spot_s_no = spot_s_no;
        this.spotName = spotName;
        this.dateRegister = dateRegister;
        this.exitTimeP = exitTimeP;
        this.waitTimeP = waitTimeP;
        this.betAmountP = betAmountP;
        this.address = address;
        this.statusSpot = statusSpot;
        this.totalBet = totalBet;
        this.areaSize = areaSize;
    }

    public String getPrimaryID() {
        return primaryID;
    }

    public String getSpot_s_no() {
        return spot_s_no;
    }

    public String getSpotName() {
        return spotName;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public String getExitTimeP() {
        return exitTimeP;
    }

    public String getWaitTimeP() {
        return waitTimeP;
    }

    public String getBetAmountP() {
        return betAmountP;
    }

    public String getAddress() {
        return address;
    }

    public String getStatusSpot() {
        return statusSpot;
    }

    public String getTotalBet() {
        return totalBet;
    }

    public String getAreaSize() {
        return areaSize;
    }
}
