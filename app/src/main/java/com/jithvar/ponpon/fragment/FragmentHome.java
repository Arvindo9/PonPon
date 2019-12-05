package com.jithvar.ponpon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.handler.DataBaseHandler;
import com.jithvar.ponpon.having_spot.HavingSpot;
import com.jithvar.ponpon.need_spot.NeedSpot;

import java.sql.SQLException;

/**
 * Created by Arvindo Mondal on 14/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class FragmentHome extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private Spinner mSpinner;
    private String accountType = "";
    private DatabaseDB db;
    private boolean accountStatus;
    private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseDB(getActivity());
        try {
            userId = db.getUserId();
            accountStatus = db.isAccountActive();
            accountType = db.accountType();
            Log.e("accountType", accountType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mSpinner = (Spinner) rootView.findViewById(R.id.accountType);
        ((ImageView) rootView.findViewById(R.id.have_spot)).setOnClickListener(this);
        ((ImageView) rootView.findViewById(R.id.need_spot)).setOnClickListener(this);
        ((ImageView) rootView.findViewById(R.id.find_vehicle)).setOnClickListener(this);
        ((ImageView) rootView.findViewById(R.id.transaction)).setOnClickListener(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.account_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
        if(accountType.equals(getResources().getString(R.string.business))) {
            mSpinner.setSelection(1);
        }
        else if(accountType.equals(getResources().getString(R.string.personal))) {
            mSpinner.setSelection(2);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.find_vehicle:
                break;

            case R.id.have_spot:
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,
//                        new HavingSpotPersonal()).commit();
                getActivity().startActivity(new Intent(getActivity(), HavingSpot.class));
                break;

            case R.id.transaction:
                break;

            case R.id.need_spot:
                getActivity().startActivity(new Intent(getActivity(), NeedSpot.class));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 1){
            if(accountStatus){
                try {
                    db.setAccountType(new DataBaseHandler(accountType, 1));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            accountType = "Business";
        }
        else if(position == 2){if(accountStatus){
            try {
                db.setAccountType(new DataBaseHandler(accountType, 1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
            accountType = "Personal";
        }


        Log.e("accountType", accountType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}