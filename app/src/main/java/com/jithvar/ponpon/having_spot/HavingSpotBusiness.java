package com.jithvar.ponpon.having_spot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.adapter.HavingSpotBusinessAdapter;
import com.jithvar.ponpon.fragment.RegisterSpotBusinessProvider;
import com.jithvar.ponpon.handler.HavingSpotBusinessData;

import java.util.ArrayList;

/**
 * Created by Arvindo Mondal on 18/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class HavingSpotBusiness extends Fragment {

    private ArrayList<HavingSpotBusinessData> dataList;
    private HavingSpotBusinessAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.having_spot_business, container, false);

        dataList = new ArrayList<>();
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        adapter = new HavingSpotBusinessAdapter(getActivity(), dataList);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new RegisterSpotBusinessProvider()).commit();
            }
        });

        return rootView;
    }






}
