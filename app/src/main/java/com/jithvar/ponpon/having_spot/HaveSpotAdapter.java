package com.jithvar.ponpon.having_spot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.handler.HaveSpotData;
import com.jithvar.ponpon.map.ViewLocationOnMap;

import java.util.ArrayList;

/**
 * Created by KinG on 01-11-2017.
 * Created by ${EMAIL}.
 */

public class HaveSpotAdapter extends BaseAdapter {
    private final Activity activity;
    private final ArrayList<HaveSpotData> list;

    HaveSpotAdapter(Activity havingSpot, ArrayList<HaveSpotData> haveSpotDataList) {

        this.activity = havingSpot;
        this.list = haveSpotDataList;
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
            convertView = inflater.inflate(R.layout.home_spot_adapter, parent, false);
            holder = new DataHolder();
            holder.spotNo_tv = (TextView) convertView.findViewById(R.id.spot_no);
            holder.spotStatus_tv = (TextView) convertView.findViewById(R.id.spot_status_tv);
            holder.spotName_tv = (TextView) convertView.findViewById(R.id.spot_name_tv);
            holder.date_tv = (TextView) convertView.findViewById(R.id.date_tv);
            holder.enterTime_tv = (TextView) convertView.findViewById(R.id.time_enter_tv);
            holder.exitTime_tv = (TextView) convertView.findViewById(R.id.time_exit_tv);
            holder.totalBet = (TextView) convertView.findViewById(R.id.total_bet_t);
            holder.waitTime_tv = (TextView) convertView.findViewById(R.id.time_max_wait_tv);
            holder.betAmountSet_tv = (TextView) convertView.findViewById(R.id.bet_amount_tv);
            holder.betMaxSetOther_tv = (TextView) convertView.findViewById(R.id.max_bet_tv);
            holder.location_tv = (TextView) convertView.findViewById(R.id.location_tv);
            holder.mapLocation_tv = (Button) convertView.findViewById(R.id.map_button);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.linear_layout);
            convertView.setTag(holder);
        } else {
            holder = (DataHolder) convertView.getTag();
        }

        final HaveSpotData item = (HaveSpotData) this.getItem(position);
        assert item != null;

        holder.spotNo_tv.setText(item.getSpot_s_no());
        holder.spotName_tv.setText(item.getSpotName());
        holder.spotStatus_tv.setText(item.getSpotStatus());
        holder.date_tv.setText(item.getDate());
        holder.enterTime_tv.setText(item.getEnterTime());
        holder.exitTime_tv.setText(item.getExitTime());
        holder.betAmountSet_tv.setText(item.getBetAmount());
        holder.betMaxSetOther_tv.setText(item.getHighestBet());
        holder.location_tv.setText(item.getAddress());
        holder.waitTime_tv.setText(item.getWaitTime());
        holder.totalBet.setText(item.getTotalBet());

        holder.mapLocation_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ViewLocationOnMap.class);
                i.putExtra("Latitude", item.getLatitude());
                i.putExtra("Longitude", item.getLongitude());
                i.putExtra("Address", item.getAddress());
                i.putExtra("AreaSize", item.getAreaSize());
                i.putExtra("PlaceId", item.getPlaceID());
                activity.startActivity(i);
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, SpotDetails.class);
                i.putExtra("PrimaryId", item.getPrimary_id());
                i.putExtra("Latitude", item.getLatitude());
                i.putExtra("Longitude", item.getLongitude());
                i.putExtra("PlaceId", item.getPlaceID());
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
        private TextView enterTime_tv;
        private TextView exitTime_tv;
        private TextView waitTime_tv;
        private TextView totalBet;
        private TextView betAmountSet_tv;
        private TextView betMaxSetOther_tv;
        private TextView location_tv;
        private Button mapLocation_tv;
        private LinearLayout layout;
    }
}