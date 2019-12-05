package com.jithvar.ponpon.need_spot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.interfaceClass.SelectSpot;

/**
 * Created by KinG on 19-11-2017.
 * Created by ${EMAIL}.
 */

public class BooKSpotClusterInfoDialog extends DialogFragment {

    private String address;
    private int primaryID;
    private String spot_s_no;
    private String spotName;
    private String statusSpot;
    private String dateRegister;
    private String exitTimeP;
    private String waitTimeP;
    private String betAmountP;
    private String totalBet;
    private String areaSize;

    public BooKSpotClusterInfoDialog(){
        super();
    }

    @SuppressLint("ValidFragment")
    public BooKSpotClusterInfoDialog(int primaryID, String spot_s_no, String spotName,
                                     String statusSpot, String dateRegister, String exitTimeP,
                                     String waitTimeP, String betAmountP, String totalBet,
                                     String address, String areaSize) {

        this();
        this.primaryID = primaryID;
        this.spot_s_no = spot_s_no;
        this.spotName = spotName;
        this.statusSpot = statusSpot;
        this.dateRegister = dateRegister;
        this.exitTimeP = exitTimeP;
        this.waitTimeP = waitTimeP;
        this.betAmountP = betAmountP;
        this.totalBet = totalBet;
        this.address = address;
        this.areaSize = areaSize;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cluster_info_dialog, container);
        TextView spotStatusT = (TextView) view.findViewById(R.id.status_t);
        TextView spotNoT = (TextView) view.findViewById(R.id.spot_no_t);
        TextView spotNameT = (TextView) view.findViewById(R.id.spot_name_t);
        TextView dateRegisterT = (TextView) view.findViewById(R.id.date_t);
        TextView timeExitP_T = (TextView) view.findViewById(R.id.time_exit_p_t);
        TextView timeWaitP_T = (TextView) view.findViewById(R.id.time_max_wait_p_t);
        TextView betP_T = (TextView) view.findViewById(R.id.bet_amount_p_t);
        TextView totalBetT = (TextView) view.findViewById(R.id.total_bet_t);
        TextView locationT = (TextView) view.findViewById(R.id.location_tv);
        TextView areaSizeT = (TextView) view.findViewById(R.id.area_size_t);
        TextView selectAnotherSpot = (TextView) view.findViewById(R.id.select_another_spot);

        final SelectSpot selectSpot = (SelectSpot) getActivity();

        spotStatusT.setText(statusSpot);
        spotNoT.setText(spot_s_no);
        spotNameT.setText(spotName);
        dateRegisterT.setText(dateRegister);
        timeExitP_T.setText(exitTimeP);
        timeWaitP_T.setText(waitTimeP);
        betP_T.setText(betAmountP);
        totalBetT.setText(totalBet);
        locationT.setText(address);
        areaSizeT.setText(areaSize);

        selectAnotherSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        selectAnotherSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSpot.selectThisSpot(primaryID);
                dismiss();
            }
        });

        return view;
    }
}
