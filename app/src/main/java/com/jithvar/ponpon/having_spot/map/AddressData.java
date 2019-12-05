package com.jithvar.ponpon.having_spot.map;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KinG on 16-11-2017.
 * Created by ${EMAIL}.
 */

public class AddressData implements Serializable {

    private final String long_name;
    private final String short_name;
    private final String streetType;

    AddressData(String long_name, String short_name, String streetType) {

        this.long_name = long_name;
        this.short_name = short_name;
        this.streetType = streetType;
    }

    public String getLong_name() {
        return long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public String getStreetType() {
        return streetType;
    }
}
