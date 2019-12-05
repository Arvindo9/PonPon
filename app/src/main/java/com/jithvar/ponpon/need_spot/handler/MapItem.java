package com.jithvar.ponpon.need_spot.handler;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by KinG on 18-11-2017.
 * Created by ${EMAIL}.
 */

public class MapItem implements ClusterItem {

    private final LatLng mPosition;
    private final int primaryID;
    private final int listIndex;
    public final int iconImage;
    private final String mTitle;
    private final String mSnippet;

    public MapItem(double lat, double lng, String title, String snippet, int  primaryID,
                   int listIndex, int iconImage) {
        this.primaryID = primaryID;
        this.listIndex = listIndex;
        this.iconImage = iconImage;
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public int getPrimaryID() {
        return primaryID;
    }

    public int getListIndex() {
        return listIndex;
    }
}
