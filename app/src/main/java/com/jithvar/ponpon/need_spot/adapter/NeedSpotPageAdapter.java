package com.jithvar.ponpon.need_spot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jithvar.ponpon.need_spot.Active;
import com.jithvar.ponpon.need_spot.Expire;
import com.jithvar.ponpon.need_spot.InProgress;
import com.jithvar.ponpon.need_spot.Success;

/**
 * Created by KinG on 07-11-2017.
 * Created by ${EMAIL}.
 */

public class NeedSpotPageAdapter extends FragmentStatePagerAdapter {

    private int num_of_tabs;

    public NeedSpotPageAdapter(FragmentManager fm, int num_of_tabs) {
        super(fm);
        this.num_of_tabs = num_of_tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InProgress();
            case 1:
                return new Active();
            case 2:
                return new Success();
            case 3:
                return new Expire();
            default:
                return new InProgress();
        }

    }

    @Override
    public int getCount() {
        return num_of_tabs;
    }
}
