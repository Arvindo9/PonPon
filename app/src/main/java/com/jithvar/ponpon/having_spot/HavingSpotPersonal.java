package com.jithvar.ponpon.having_spot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.handler.MapData;
import com.jithvar.ponpon.map.MapHavingBusinessPersonal;
import com.jithvar.ponpon.map.MapHavingBusinessPersonal1;
import com.jithvar.ponpon.map.MapTmp;
import com.jithvar.ponpon.map.VisibleRegionDemoActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Arvindo Mondal on 18/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class HavingSpotPersonal extends Fragment implements View.OnClickListener {

    private static final int MAP_RESULT_CODE = 10;
    private TextView latitudeTv;
    private TextView longitudeTv;
    private TextView addressTv;
    private TextView betAmountTv;
    private TextView statusTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.having_spot_personal, container, false);
        latitudeTv = (TextView) rootView.findViewById(R.id.latitude_tv);
        longitudeTv = (TextView) rootView.findViewById(R.id.longitude_tv);
        addressTv = (TextView) rootView.findViewById(R.id.address_tv);
        betAmountTv = (TextView) rootView.findViewById(R.id.bet_amount_tv);
        statusTv = (TextView) rootView.findViewById(R.id.status_tv);

        ((Button) rootView.findViewById(R.id.map_button)).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_button:
//                Intent i = new Intent(getActivity(), VisibleRegionDemoActivity.class);
                Intent i = new Intent(getActivity(), MapTmp.class);
                startActivityForResult(i, MAP_RESULT_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MAP_RESULT_CODE && resultCode == RESULT_OK){
            if(MapData.getLatitudeMark() > 0 && MapData.getLongitudeMark() > 0){
                latitudeTv.setText(String.valueOf(MapData.getLatitudeMark()));
                longitudeTv.setText(String .valueOf(MapData.getLongitudeMark()));
            }
            else{
                Toast.makeText(getActivity(), "select location on map", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
