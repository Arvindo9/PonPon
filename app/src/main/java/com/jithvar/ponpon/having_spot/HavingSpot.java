package com.jithvar.ponpon.having_spot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.adapter.HaveSpotPageAdapter;

/**
 * Created by KinG on 04-11-2017.
 * Created by ${EMAIL}.
 */

public class HavingSpot extends FragmentActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager view;
    private HaveSpotPageAdapter adpter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        view = (ViewPager)findViewById(R.id.viewPager);

        ((TextView) findViewById(R.id.title)).setText(getResources().getString(R.string
                .have_spot));

        tabLayout = (TabLayout)findViewById(R.id.tabs_layout);

        tabLayout.addTab(tabLayout.newTab().setText("InProgress"));
        tabLayout.addTab(tabLayout.newTab().setText("Active"));
        tabLayout.addTab(tabLayout.newTab().setText("Success"));
        tabLayout.addTab(tabLayout.newTab().setText("Expire"));

        tabLayout.setOnTabSelectedListener(this);
        adpter = new HaveSpotPageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        view.setAdapter(adpter);
        view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        view.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu,menu);
//        return true;
//    }
}
