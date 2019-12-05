package com.jithvar.ponpon.having_spot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.handler.HaveSpotData;
import com.jithvar.ponpon.handler.SpotDetailsData;
import com.jithvar.ponpon.map.ViewLocationOnMap;

import java.util.ArrayList;

/**
 * Created by KinG on 04-11-2017.
 * Created by ${EMAIL}.
 */

class SpotBetSeekerAdapter extends BaseAdapter{
    private final SpotDetails activity;
    private final ArrayList<SpotDetailsData> list;

    SpotBetSeekerAdapter(SpotDetails spotDetails, ArrayList<SpotDetailsData> list) {

        this.activity = spotDetails;
        this.list = list;
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
            convertView = inflater.inflate(R.layout.spot_bet_seeker_adapter, parent, false);
            holder = new DataHolder();
            holder.userNameT = (TextView) convertView.findViewById(R.id.user_name);
            holder.statusT = (TextView) convertView.findViewById(R.id.user_status_tv);
            holder.dateT = (TextView) convertView.findViewById(R.id.date_tv);
            holder.timeT = (TextView) convertView.findViewById(R.id.time_tv);
            holder.enterTimeT = (TextView) convertView.findViewById(R.id.time_enter_tv);
            holder.waitTimeT = (TextView) convertView.findViewById(R.id.time_max_wait_tv);
            holder.betAmountSetT = (TextView) convertView.findViewById(R.id.bet_amount_tv);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.linear_layout);
            convertView.setTag(holder);
        } else {
            holder = (DataHolder) convertView.getTag();
        }

        final SpotDetailsData item = (SpotDetailsData) this.getItem(position);
        assert item != null;

        holder.userNameT.setText(item.getUserName());
        holder.statusT.setText(item.getStatusPayment());
        holder.dateT.setText(item.getDateRegister());
        holder.timeT.setText(item.getTimeRegister());
        holder.enterTimeT.setText(item.getEnterTime());
        holder.waitTimeT.setText(item.getWaitTime());
        holder.betAmountSetT.setText(item.getBetAmount());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProviderAcceptSeeker.class);
                i.putExtra("PrimaryIdSpotTb", item.getPrimaryIdSpotTb());
                i.putExtra("PrimaryIdBetTb", item.getPrimaryIdBetTb());
                i.putExtra("UserId", item.getUserID());
                activity.startActivity(i);
            }
        });

        return convertView;
    }

    private class DataHolder{
        private TextView userNameT;
        private TextView statusT;
        private TextView dateT;
        private TextView timeT;
        private TextView enterTimeT;
        private TextView waitTimeT;
        private TextView betAmountSetT;
        private LinearLayout layout;
    }
}