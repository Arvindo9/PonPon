package com.jithvar.ponpon.need_spot.handler;

/**
 * Created by KinG on 23-11-2017.
 * Created by ${EMAIL}.
 */

public class ExpireData {
    private final String primaryIdSpotTb;
    private final String primaryIdBetTb;
    private final String spot_s_no;
    private final String spotName;
    private final String dateSpotTb;
    private final String exitTimeP;
    private final String waitTimeP;
    private final String betAmountP;
    private final String address;
    private final String placeID;
    private final double latitude;
    private final double longitude;
    private final String areaSize;
    private final String spotStatusSpotTb;
    private final String enterTimeS;
    private final String waitTimeS;
    private final String betAmountS;
    private final String statusSpotBySeeker;

    public ExpireData(String primaryIdSpotTb, String primaryIdBetTb, String spot_s_no,
                      String spotName, String dateSpotTb, String exitTimeP, String waitTimeP,
                      String betAmountP, String address, String placeID, double latitude,
                      double longitude, String areaSize, String spotStatusSpotTb,
                      String enterTimeS, String waitTimeS, String betAmountS,
                      String statusSpotBySeeker) {
        this.primaryIdSpotTb = primaryIdSpotTb;
        this.primaryIdBetTb = primaryIdBetTb;
        this.spot_s_no = spot_s_no;
        this.spotName = spotName;
        this.dateSpotTb = dateSpotTb;
        this.exitTimeP = exitTimeP;
        this.waitTimeP = waitTimeP;
        this.betAmountP = betAmountP;
        this.address = address;
        this.placeID = placeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.areaSize = areaSize;
        this.spotStatusSpotTb = spotStatusSpotTb;
        this.enterTimeS = enterTimeS;
        this.waitTimeS = waitTimeS;
        this.betAmountS = betAmountS;
        this.statusSpotBySeeker = statusSpotBySeeker;
    }

    public String getPrimaryIdSpotTb() {
        return primaryIdSpotTb;
    }

    public String getPrimaryIdBetTb() {
        return primaryIdBetTb;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAreaSize() {
        return areaSize;
    }

    public String getSpotStatusSpotTb() {
        return spotStatusSpotTb;
    }

    public String getEnterTimeS() {
        return enterTimeS;
    }

    public String getWaitTimeS() {
        return waitTimeS;
    }

    public String getBetAmountS() {
        return betAmountS;
    }

    public String getStatusSpotBySeeker() {
        return statusSpotBySeeker;
    }
}
