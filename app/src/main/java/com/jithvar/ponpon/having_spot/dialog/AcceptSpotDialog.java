package com.jithvar.ponpon.having_spot.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.interfaceClass.DealAcceptProvider;
import com.jithvar.ponpon.interfaceClass.DealCancel;

/**
 * Created by KinG on 22-11-2017.
 * Created by ${EMAIL}.
 */

public class AcceptSpotDialog extends DialogFragment {

    public AcceptSpotDialog(){
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accept_spot_dialog, container);
        Button confirmB = (Button) view.findViewById(R.id.confirm);
        Button cancelB = (Button) view.findViewById(R.id.not_now);

        final DealAcceptProvider dealAcceptProvider = (DealAcceptProvider) getActivity();
        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealAcceptProvider.dealAccept(true);
                dismiss();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }
}
