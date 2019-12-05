package com.jithvar.ponpon.need_spot.handler;

import java.io.Serializable;

/**
 * Created by KinG on 18-11-2017.
 * Created by ${EMAIL}.
 */

public class BookSpotData implements Serializable {
    private final String primaryID;
    private final String spot_s_no;
    private final String spotName;
    private final String dateRegister;
    private final String exitTimeP;
    private final String waitTimeP;
    private final String betAmountP;
    private final String address;
    private final String placeID;
    private final String latitude;
    private final String longitude;
    private final String latitudeRad;
    private final String longitudeRad;
    private final String areaSize;
    private final String statusSpot;
    private final String totalBet;

    public BookSpotData(String primaryID, String spot_s_no, String spotName, String dateRegister,
                        String exitTimeP, String waitTimeP, String betAmountP, String address,
                        String placeID, String latitude, String longitude, String latitudeRad,
                        String longitudeRad, String areaSize, String statusSpot, String totalBet) {

        this.primaryID = primaryID;
        this.spot_s_no = spot_s_no;
        this.spotName = spotName;
        this.dateRegister = dateRegister;
        this.exitTimeP = exitTimeP;
        this.waitTimeP = waitTimeP;
        this.betAmountP = betAmountP;
        this.address = address;
        this.placeID = placeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latitudeRad = latitudeRad;
        this.longitudeRad = longitudeRad;
        this.areaSize = areaSize;
        this.statusSpot = statusSpot;
        this.totalBet = totalBet;
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

    public String getPlaceID() {
        return placeID;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitudeRad() {
        return latitudeRad;
    }

    public String getLongitudeRad() {
        return longitudeRad;
    }

    public String getAreaSize() {
        return areaSize;
    }

    public String getStatusSpot() {
        return statusSpot;
    }

    public String getTotalBet() {
        return totalBet;
    }
}
