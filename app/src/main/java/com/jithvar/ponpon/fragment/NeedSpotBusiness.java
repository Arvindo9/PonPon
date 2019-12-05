package com.jithvar.ponpon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jithvar.ponpon.R;

/**
 * Created by Arvindo Mondal on 22/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class NeedSpotBusiness extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.need_spot_business, container, false);

        return rootView;
    }
}
