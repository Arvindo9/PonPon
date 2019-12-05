package com.jithvar.ponpon.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.handler.HavingSpotBusinessData;
import java.util.ArrayList;

/**
 * Created by Arvindo Mondal on 18/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class HavingSpotBusinessAdapter extends BaseAdapter{

    private final FragmentActivity activity;
    private final ArrayList<HavingSpotBusinessData> dataList;

    public HavingSpotBusinessAdapter(FragmentActivity activity,
                                     ArrayList<HavingSpotBusinessData> dataList) {

        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
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
            convertView = inflater.inflate(R.layout.having_spot_business_adapter, parent, false);
            holder = new DataHolder();
            holder.mapButton = (Button) convertView.findViewById(R.id.map_button);
            convertView.setTag(holder);
        } else {
            holder = (DataHolder) convertView.getTag();
        }

        final HavingSpotBusinessAdapter item = (HavingSpotBusinessAdapter) this.getItem(position);
        assert item != null;

        return null;
    }

    private class DataHolder {
        private Button mapButton;
    }
}
