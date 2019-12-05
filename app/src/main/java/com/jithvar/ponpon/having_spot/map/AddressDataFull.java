package com.jithvar.ponpon.having_spot.map;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KinG on 16-11-2017.
 * Created by ${EMAIL}.
 */

public class AddressDataFull implements Serializable {
//    private final AddressData addressData;
    private final String formatted_address;
    private final String place_id;
    private final ArrayList<String> addressTypeList;
    private final ArrayList<AddressData> list1;

//    public AddressDataFull(AddressData addressData, String formatted_address, String place_id,
//                           ArrayList<String> addressTypeList) {
//
//        this.addressData = addressData;
//        this.formatted_address = formatted_address;
//        this.place_id = place_id;
//        this.addressTypeList = addressTypeList;
//    }

    AddressDataFull(ArrayList<AddressData> list1, String formatted_address, String place_id,
                           ArrayList<String> addressTypeList) {

        this.list1 = list1;
        this.formatted_address = formatted_address;
        this.place_id = place_id;
        this.addressTypeList = addressTypeList;
    }

//    public AddressData getAddressData() {
//        return addressData;
//    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public String getPlace_id() {
        return place_id;
    }

    public ArrayList<String> getAddressTypeList() {
        return addressTypeList;
    }

    public ArrayList<AddressData> getList1() {
        return list1;
    }
}
