package com.jithvar.ponpon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.map.NeedSpotMap;

/**
 * Created by Arvindo Mondal on 22/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class NeedSpotPersonal extends Fragment implements View.OnClickListener{

    private static final int MAP_USE = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.need_spot_personal, container, false);
        ((Button) rootView.findViewById(R.id.use_map)).setOnClickListener(this);
        ((Button) rootView.findViewById(R.id.use_table)).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())){
            case R.id.use_map:
                Intent i = new Intent(getActivity(), NeedSpotMap.class);
                startActivityForResult(i, MAP_USE);
                break;

            case R.id.use_table:
                break;
        }
    }
}
