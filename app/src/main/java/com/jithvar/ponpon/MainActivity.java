package com.jithvar.ponpon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jithvar.ponpon.adapter.ExpandableListAdapterHome;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.fragment.FragmentHome;
import com.jithvar.ponpon.fragment.FragmentSetting;
import com.jithvar.ponpon.fragment.FragmentShare;
import com.jithvar.ponpon.fragment.setting.ActivateAccount;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arvindo Mondal on 13/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ExpandableListView expListView;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListAdapterHome listAdapter;
    private List<String> listDataHeader;
    private Fragment fragment = null;
    private String accountType = "";
    private int information;
    private DatabaseDB db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseDB(this);

        //business, personal, error
        information = getIntent().getIntExtra("result_type", 0);
        Log.e("result", accountType);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new FragmentHome()).commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle =
                new android.support.v7.app.ActionBarDrawerToggle(
                        this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        DisplayMetrics metrics;
//        metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        expListView = (ExpandableListView) findViewById(R.id.list_slidermenu);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

//        if(handOrientation.equals("Left"))
//            setDrawerMenuRight();
//        else
//            setDrawerMenuLeft();

//        mDrawerLayout.setDrawerListener(mDrawerToggle);

        prepareListData();
        listAdapter = new ExpandableListAdapterHome(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        // load slide menu items
//		fragment = new HomeFragment();
//		getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
//		mDrawerLayout.closeDrawer(expListView);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                int index = parent.getFlatListPosition(
                        ExpandableListView.getPackedPositionForGroup(groupPosition));
                parent.setItemChecked(index, true);

                if (index == 0) {
                    fragment = new FragmentHome();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromSetSpot", false);
                    fragment.setArguments(bundle);
//                    getFragmentManager().beginTransaction().replace(R.id.frame_container,
//                            fragment).addToBackStack("tag").commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            fragment).commit();
                    mDrawerLayout.closeDrawer(navigationView);
                }

                return false;
            }
        });


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                boolean fragmentYes = true;
                Intent i;

                switch (groupPosition) {
                    case 0:
                        break;

                    case 1:
                        switch (childPosition) {
                            case 0:
//                                fragment = new FragmentPendingTrans();
                                break;
                            case 1:
//                                fragment = new FragmentCompletedTrans();
                                break;
                            case 2:
//                                fragment = new FragmentNotification();
                                break;
                            case 3:
//                                fragment = new FragmentAccountRecover();
                                break;
                            case 4:
//                                fragment = new FragmentSpots();
                            default:
                                break;
                        }
                        break;

                    case 2:
                        switch (childPosition) {
                            case 0:
//                                fragment = new FragmentSupport();
                                break;
                            case 1:
//                                fragment = new FragmentAbouth();
                                break;
                            case 2:
                                fragment = new FragmentShare();
                                break;
                            default:
                                break;
                        }
                        break;

                    case 3:
                        switch (childPosition) {
                            case 0:
//                                fragment = new FragmentAcType();
                                break;
                            case 1:
//                                fragment = new FragmentProfile();
                                break;
                            case 2:
//                                fragment = new FragmentVehicle();
                                break;
                            case 3:
//                                fragment = new FragmentPattern();
                                //fragment = new FragmentPayVault();
                                break;
                            case 4:
                                break;
                            case 5:
                                fragmentYes = false;
                                i = new Intent(MainActivity.this, ActivateAccount.class);
                                startActivity(i);
                                mDrawerLayout.closeDrawer(navigationView);
//                                fragment = new ActivateAccount();
                                break;
                            case 6:
//                                fragment = new FragmentSetting();
                                break;

                            default:
                                break;
                        }
                        break;

                    default:
                        break;
                }
//                getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("tag").commit();
//                mDrawerLayout.closeDrawer(expListView);
                if(fragmentYes) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            fragment).commit();
                    mDrawerLayout.closeDrawer(navigationView);
                }
                return false;
            }
        });

        if (fragment == null) {
            defaultFragment(new FragmentHome());
        }



//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);


        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //displayView(0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void defaultFragment(Fragment fragment)
    {
        Bundle bundle=new Bundle();
        bundle.putBoolean("fromSetSpot",false);
        fragment.setArguments(bundle);
//        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                fragment).commit();
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            ImageView img=(ImageView) convertView.findViewById(R.id.imageView1);
            if(groupPosition==0)
                lblListHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            if(groupPosition==0)
                img.setBackgroundResource(R.drawable.home1);
            else if(groupPosition==1)
                img.setBackgroundResource(R.drawable.mgmt);
            else if(groupPosition==2)
                img.setBackgroundResource(R.drawable.talk);
            else if(groupPosition==3)
                img.setBackgroundResource(R.drawable.setup);

            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add(getResources().getString(R.string.home));
        listDataHeader.add(getResources().getString(R.string.account_management));
        listDataHeader.add(getResources().getString(R.string.talk_to_s));
        listDataHeader.add(getResources().getString(R.string.account_setups));

        List<String> bow = new ArrayList<>();

        List<String> accountManagement = new ArrayList<String>();
        accountManagement.add(getResources().getString(R.string.pending_transaction));
        accountManagement.add(getResources().getString(R.string.completed_transaction));
        accountManagement.add(getResources().getString(R.string.notification_center));
        accountManagement.add(getResources().getString(R.string.account_recovery));
        accountManagement.add(getResources().getString(R.string.spots));

        List<String> talkToUs = new ArrayList<String>();
        talkToUs.add(getResources().getString(R.string.support_us));
        talkToUs.add(getResources().getString(R.string.about_us));
        talkToUs.add(getResources().getString(R.string.share_app));

        List<String> accountSetup = new ArrayList<String>();
        accountSetup.add(getResources().getString(R.string.account_type));
        accountSetup.add(getResources().getString(R.string.customer_profile));
        accountSetup.add(getResources().getString(R.string.vehicle_profile));
        accountSetup.add(getResources().getString(R.string.payment_vault));
        accountSetup.add(getResources().getString(R.string.setting));
        accountSetup.add(getResources().getString(R.string.activate_account));
        accountSetup.add(getResources().getString(R.string.de_activate_account));

        //Header,child data
        listDataChild.put(listDataHeader.get(0), bow);
        listDataChild.put(listDataHeader.get(1), accountManagement);
        listDataChild.put(listDataHeader.get(2), talkToUs);
        listDataChild.put(listDataHeader.get(3), accountSetup);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        item.setCheckable(true);
//        int id = item.getItemId();
//        switch (id){
//            case R.id.about:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,
//                        new FragmentHome()).commit();//now replace the argument fragment
//                break;
//
//            case R.id.our_mission:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,
//                        new IndexPage()).commit();
//                break;
//
//
//            case R.id.our_vision:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,
//                        new IndexPage()).commit();
//                break;
//
//            case R.id.national_president:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,
//                        new IndexPage()).commit();
//                break;
//
//            case R.id.gallery:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,
//                        new IndexPage()).commit();
//                break;
//
//            case R.id.contact:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,
//                        new IndexPage()).commit();
//                break;
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}