package com.jithvar.ponpon.need_spot.adapter;

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
import com.jithvar.ponpon.handler.BookSpot1Data;
import com.jithvar.ponpon.having_spot.SpotDetails;
import com.jithvar.ponpon.map.ViewLocationOnMap;
import com.jithvar.ponpon.need_spot.BookSpot1;
import com.jithvar.ponpon.need_spot.BookSpot2;

import java.util.ArrayList;

/**
 * Created by KinG on 12-11-2017.
 * Created by ${EMAIL}.
 */

public class BookSpot1Adapter extends BaseAdapter{
    private final BookSpot1 activity;
    private final ArrayList<BookSpot1Data> list;

    public BookSpot1Adapter(BookSpot1 bookSpot1, ArrayList<BookSpot1Data> list) {

        this.activity = bookSpot1;
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
            convertView = inflater.inflate(R.layout.book_spot_1_container, parent, false);
            holder = new DataHolder();
            holder.spotStatus_tv = (TextView) convertView.findViewById(R.id.spot_status_tv);
            holder.spotName_tv = (TextView) convertView.findViewById(R.id.spot_name_tv);
            holder.totalBetSet = (TextView) convertView.findViewById(R.id.total_bet_t);
            holder.betAmountP_tv = (TextView) convertView.findViewById(R.id.bet_amount_tv);
            holder.waitTime_tv = (TextView) convertView.findViewById(R.id.time_expire);
            holder.betMaxSetOther_tv = (TextView) convertView.findViewById(R.id.max_bet_tv);
            holder.location_tv = (TextView) convertView.findViewById(R.id.location_tv);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.linear_layout);
            convertView.setTag(holder);
        } else {
            holder = (DataHolder) convertView.getTag();
        }

        final BookSpot1Data item = (BookSpot1Data) this.getItem(position);
        assert item != null;

        holder.spotName_tv.setText(item.getSpotName());
        holder.spotStatus_tv.setText(item.getStatusSpot());
        holder.totalBetSet.setText(item.getTotalBet());
        holder.location_tv.setText(item.getAddress());
        holder.waitTime_tv.setText(item.getWaitTimeP());
        holder.betAmountP_tv.setText(item.getBetAmountP());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, BookSpot2.class);
                i.putExtra("PrimaryIdSpotTb", item.getPrimaryID());
                activity.startActivity(i);
            }
        });

        return convertView;
    }

    private class DataHolder{
        private TextView spotName_tv;
        private TextView spotStatus_tv;
        private TextView betAmountP_tv;
        private TextView waitTime_tv;
        private TextView totalBetSet;
        private TextView betMaxSetOther_tv;
        private TextView location_tv;
        private LinearLayout layout;
    }
}
