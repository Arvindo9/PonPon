package com.jithvar.ponpon.handler;

/**
 * Created by Arvindo Mondal on 22/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class MapData {

    private static double latitudeMark = 0.0;
    private static double longitudeMark = 0.0;

    public static void setLatLng(double latitudeMark, double longitudeMark) {
        MapData.latitudeMark = latitudeMark;
        MapData.longitudeMark = longitudeMark;
    }

    public static double getLatitudeMark() {
        return latitudeMark;
    }

    public static double getLongitudeMark() {
        return longitudeMark;
    }
}
