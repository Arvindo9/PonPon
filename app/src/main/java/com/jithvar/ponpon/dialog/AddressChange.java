package com.jithvar.ponpon.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.interfaceClass.AddressInterface;

/**
 * Created by KinG on 05-11-2017.
 * Created by ${EMAIL}.
 */

public class AddressChange extends DialogFragment{

    private String address;
    private EditText AddressE;

    public AddressChange(){

    }
    // Empty constructor required for DialogFragment
    @SuppressLint("ValidFragment")
    public AddressChange(String address) {
        this();
        this.address = address;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException{
        View view = inflater.inflate(R.layout.address_change_dialog, container);
        AddressE = (EditText) view.findViewById(R.id.edit_address_et);
        Button doneButton = (Button) view.findViewById(R.id.done);

        // set this instance as callback for editor action
//        AddressE.setOnEditorActionListener(this);
        AddressE.setText(address);
        AddressE.requestFocus();

        getDialog().getWindow().setLayout(200, 200);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(getResources().getString(R.string.edit_address));

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressInterface activity = (AddressInterface) getActivity();
                activity.AddressSet(AddressE.getText().toString());
                dismiss();
            }
        });

        return view;
    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        // Return input text to activity
//        AddressInterface activity = (AddressInterface) getActivity();
//        activity.AddressSet(AddressE.getText().toString());
//        this.dismiss();
//        return true;
//    }
}
