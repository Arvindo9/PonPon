package com.jithvar.ponpon.handler;

/**
 * Created by KinG on 02-11-2017.
 * Created by ${EMAIL}.
 */

public class HaveSpotAddMapData {
    private final int pageNumber;
    private final String latitude;
    private final String longitude;
    private final String address;
    private final String date;
    private final String time;

    public HaveSpotAddMapData(int pageNumber, String latitude, String longitude, String address, String date, String time) {

        this.pageNumber = pageNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.date = date;
        this.time = time;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
