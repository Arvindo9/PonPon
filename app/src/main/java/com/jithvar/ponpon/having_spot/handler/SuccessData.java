package com.jithvar.ponpon.having_spot.handler;

/**
 * Created by KinG on 22-11-2017.
 * Created by ${EMAIL}.
 */

public class SuccessData {
    private final String primary_id_spotTb;
    private final String primary_id_betTb;
    private final String spot_s_no;
    private final String spotName;
    private final String dateSpotTb;
    private final String enterTimeSpotTb;
    private final String exitTimeSpotTb;
    private final String expireTimeSpotTb;
    private final String betAmountSpotTb;
    private final String address;
    private final String placeID;
    private final double latitude;
    private final double longitude;
    private final String areaSize;
    private final String spotStatus;
    private final String enterTimeBetTb;
    private final String waitTimeBetTb;
    private final String betAmountBetTb;
    private final String statusPayment;
    private final String statusSpot;
    private final String dateBetTb;

    public SuccessData(String primary_id_spotTb, String primary_id_betTb, String spot_s_no,
                       String spotName, String dateSpotTb, String enterTimeSpotTb,
                       String exitTimeSpotTb, String expireTimeSpotTb, String betAmountSpotTb,
                       String address, String placeID, double latitude, double longitude,
                       String areaSize, String spotStatus, String enterTimeBetTb,
                       String waitTimeBetTb, String betAmountBetTb, String statusPayment,
                       String statusSpot, String dateBetTb) {
        this.primary_id_spotTb = primary_id_spotTb;
        this.primary_id_betTb = primary_id_betTb;
        this.spot_s_no = spot_s_no;
        this.spotName = spotName;
        this.dateSpotTb = dateSpotTb;
        this.enterTimeSpotTb = enterTimeSpotTb;
        this.exitTimeSpotTb = exitTimeSpotTb;
        this.expireTimeSpotTb = expireTimeSpotTb;
        this.betAmountSpotTb = betAmountSpotTb;
        this.address = address;
        this.placeID = placeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.areaSize = areaSize;
        this.spotStatus = spotStatus;
        this.enterTimeBetTb = enterTimeBetTb;
        this.waitTimeBetTb = waitTimeBetTb;
        this.betAmountBetTb = betAmountBetTb;
        this.statusPayment = statusPayment;
        this.statusSpot = statusSpot;
        this.dateBetTb = dateBetTb;
    }

    public String getPrimary_id_spotTb() {
        return primary_id_spotTb;
    }

    public String getPrimary_id_betTb() {
        return primary_id_betTb;
    }

    public String getSpot_s_no() {
        return spot_s_no;
    }

    public String getSpotName() {
        return spotName;
    }

    public String getDateSpotTb() {
        return dateSpotTb;
    }

    public String getEnterTimeSpotTb() {
        return enterTimeSpotTb;
    }

    public String getExitTimeSpotTb() {
        return exitTimeSpotTb;
    }

    public String getExpireTimeSpotTb() {
        return expireTimeSpotTb;
    }

    public String getBetAmountSpotTb() {
        return betAmountSpotTb;
    }

    public String getAddress() {
        return address;
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

    public String getSpotStatus() {
        return spotStatus;
    }

    public String getEnterTimeBetTb() {
        return enterTimeBetTb;
    }

    public String getWaitTimeBetTb() {
        return waitTimeBetTb;
    }

    public String getBetAmountBetTb() {
        return betAmountBetTb;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public String getStatusSpot() {
        return statusSpot;
    }

    public String getDateBetTb() {
        return dateBetTb;
    }
}
