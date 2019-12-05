package com.jithvar.ponpon.having_spot.map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jithvar.ponpon.R;
import com.jithvar.ponpon.interfaceClass.MapAddressInterface;

/**
 * Created by KinG on 16-11-2017.
 * Created by ${EMAIL}.
 */

public class MapDialog extends DialogFragment {

    private Bitmap snapshot;
    private String address;

    public MapDialog(){
        super();
    }

    @SuppressLint("ValidFragment")
    public MapDialog(Bitmap snapshot, String address) {
        this();
        this.snapshot = snapshot;
        this.address = address;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_dialog, container);
        TextView addressTv = (TextView) view.findViewById(R.id.address_tv);
        TextView change_locationT = (TextView) view.findViewById(R.id.change_location);
        TextView selectT = (TextView) view.findViewById(R.id.select);
        ImageView mapImage = (ImageView) view.findViewById(R.id.map_img);
        final MapAddressInterface mapAddressInterface = (MapAddressInterface) getActivity();

//        mapImage.setImageBitmap(snapshot);

        BitmapDrawable ob = new BitmapDrawable(getResources(), snapshot);
//        mapImage.setBackgroundDrawable(ob);
        mapImage.setBackground(ob);

        addressTv.setText(address);
        change_locationT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert mapAddressInterface != null;
                mapAddressInterface.onSelectLocation(false);
                dismiss();
            }
        });

        selectT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert mapAddressInterface != null;
                mapAddressInterface.onSelectLocation(true);
                dismiss();
            }
        });


        return view;
    }
}
