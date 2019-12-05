package com.jithvar.ponpon.interfaceClass;

import com.jithvar.ponpon.need_spot.handler.BookSpotData;

import java.util.ArrayList;

/**
 * Created by KinG on 18-11-2017.
 * Created by ${EMAIL}.
 */

public interface BookSpotList {
    void bookSpotData(ArrayList<BookSpotData> spotList);

    void loadNewSpot();
}
