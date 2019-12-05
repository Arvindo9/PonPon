package com.jithvar.ponpon.handler;

/**
 * Created by KinG on 01-11-2017.
 * Created by ${EMAIL}.
 */

public class HaveSpotData {
    private final String userIDSeeker;
    private final String spot_s_no;
    private final String spotName;
    private final String date;
    private final String enterTime;
    private final String exitTime;
    private final String waitTime;
    private final String betAmount;
    private final String address;
    private final String placeID;
    private final double latitude;
    private final double longitude;
    private final String areaSize;
    private final String spotStatus;
    private final String totalBet;
    private final String highestBet;
    private final String primary_id;

    public HaveSpotData(String primary_id, String userIDSeeker, String spot_s_no, String spotName,
                        String date, String enterTime, String exitTime, String waitTime,
                        String betAmount, String address, String placeID, double latitude,
                        double longitude, String areaSize, String spotStatus, String totalBet,
                        String highestBet) {
        this.primary_id = primary_id;
        this.userIDSeeker = userIDSeeker;
        this.spot_s_no = spot_s_no;
        this.spotName = spotName;
        this.date = date;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.waitTime = waitTime;
        this.betAmount = betAmount;
        this.address = address;
        this.placeID = placeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.areaSize = areaSize;
        this.spotStatus = spotStatus;
        this.totalBet = totalBet;
        this.highestBet = highestBet;
    }

    public String getSpot_s_no() {
        return spot_s_no;
    }

    public String getSpotName() {
        return spotName;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public String getBetAmount() {
        return betAmount;
    }

    public String getAddress() {
        return address;
    }

    public String getSpotStatus() {
        return spotStatus;
    }

    public String getDate() {
        return date;
    }

    public String getUserIDSeeker() {
        return userIDSeeker;
    }

    public String getPlaceID() {
        return placeID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAreaSize() {
        return areaSize;
    }

    public String getPrimary_id() {
        return primary_id;
    }

    public String getTotalBet() {
        return totalBet;
    }

    public String getHighestBet() {
        return highestBet;
    }
}
