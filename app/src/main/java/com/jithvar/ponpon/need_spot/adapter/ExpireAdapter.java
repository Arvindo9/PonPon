package com.jithvar.ponpon.need_spot.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.map.ViewLocationOnMap;
import com.jithvar.ponpon.need_spot.ActiveDetails;
import com.jithvar.ponpon.need_spot.handler.ActiveData;
import com.jithvar.ponpon.need_spot.handler.ExpireData;

import java.util.ArrayList;

/**
 * Created by KinG on 23-11-2017.
 * Created by ${EMAIL}.
 */

public class ExpireAdapter extends BaseAdapter {
    private final Activity activity;
    private final ArrayList<ExpireData> list;

    public ExpireAdapter(FragmentActivity activity, ArrayList<ExpireData> inProgressList) {

        this.activity = activity;
        this.list = inProgressList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DataHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.need_expire_adapter, parent, false);
            holder = new DataHolder();
            holder.spotNo_tv = (TextView) convertView.findViewById(R.id.spot_no);
            holder.spotStatus_tv = (TextView) convertView.findViewById(R.id.spot_status_tv);
            holder.spotName_tv = (TextView) convertView.findViewById(R.id.spot_name_tv);
            holder.date_tv = (TextView) convertView.findViewById(R.id.date_tv);
            holder.exitTimeP_tv = (TextView) convertView.findViewById(R.id.time_exit_p_tv);
            holder.waitTimeP_tv = (TextView) convertView.findViewById(R.id.time_expire_p_tv);
            holder.betAmountP_tv = (TextView) convertView.findViewById(R.id.bet_amount_p_tv);
            holder.betAmountS_tv = (TextView) convertView.findViewById(R.id.bet_amount_s_tv);
            holder.enterTimeS_tv = (TextView) convertView.findViewById(R.id.time_enter_seeker_tv);
            holder.waitTimeS_tv = (TextView) convertView.findViewById(R.id.time_wait_seeker_tv);
            holder.address_tv = (TextView) convertView.findViewById(R.id.address_tv);
            holder.areaSize_tv = (TextView) convertView.findViewById(R.id.area_size_tv);
            holder.mapLocationB = (Button) convertView.findViewById(R.id.map_button);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.linear_layout);
            convertView.setTag(holder);
        } else {
            holder =(DataHolder) convertView.getTag();
        }

        final ExpireData item = (ExpireData) this.getItem(position);
        assert item != null;

        holder.spotNo_tv.setText(item.getSpot_s_no());
        holder.spotName_tv.setText(item.getSpotName());
        holder.spotStatus_tv.setText(item.getSpotStatusSpotTb());
        holder.date_tv.setText(item.getDateSpotTb());
        holder.exitTimeP_tv.setText(item.getExitTimeP());
        holder.waitTimeP_tv.setText(item.getWaitTimeP());
        holder.betAmountP_tv.setText(item.getBetAmountP());
        holder.betAmountS_tv.setText(item.getAddress());
        holder.enterTimeS_tv.setText(item.getEnterTimeS());
        holder.waitTimeS_tv.setText(item.getWaitTimeS());
        holder.address_tv.setText(item.getAddress());
        holder.areaSize_tv.setText(item.getAreaSize());

        holder.mapLocationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ViewLocationOnMap.class);
                i.putExtra("Latitude", item.getLatitude());
                i.putExtra("Longitude", item.getLongitude());
                i.putExtra("Address", item.getAddress());
                i.putExtra("AreaSize", item.getAreaSize());
                i.putExtra("PlaceId", item.getPlaceID());
                activity.startActivity(new Intent(activity, ViewLocationOnMap.class));
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ActiveDetails.class);
                i.putExtra("PrimaryIdSpotTb", item.getPrimaryIdSpotTb());
                i.putExtra("PrimaryIdBetTb", item.getPrimaryIdBetTb());
                activity.startActivity(i);
            }
        });

        return convertView;
    }

    private class DataHolder{
        private TextView spotName_tv;
        private TextView spotNo_tv;
        private TextView spotStatus_tv;
        private TextView date_tv;
        private TextView exitTimeP_tv;
        private TextView waitTimeP_tv;
        private TextView enterTimeS_tv;
        private TextView waitTimeS_tv;
        private TextView betAmountP_tv;
        private TextView betAmountS_tv;
        private TextView address_tv;
        private TextView areaSize_tv;
        private Button mapLocationB;
        private LinearLayout layout;
    }
}
