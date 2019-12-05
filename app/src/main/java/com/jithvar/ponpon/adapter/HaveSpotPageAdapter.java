package com.jithvar.ponpon.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jithvar.ponpon.having_spot.Active;
import com.jithvar.ponpon.having_spot.Expire;
import com.jithvar.ponpon.having_spot.InProgress;
import com.jithvar.ponpon.having_spot.Success;

/**
 * Created by KinG on 04-11-2017.
 * Created by ${EMAIL}.
 */

public class HaveSpotPageAdapter extends FragmentStatePagerAdapter {

    private int num_of_tabs;

    public HaveSpotPageAdapter(FragmentManager fm, int num_of_tabs) {
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
                return new Active();
        }

    }

    @Override
    public int getCount() {
        return num_of_tabs;
    }
}
