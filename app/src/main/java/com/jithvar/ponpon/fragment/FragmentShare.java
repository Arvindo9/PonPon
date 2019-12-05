package com.jithvar.ponpon.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.jithvar.ponpon.R;

/**
 * Created by Arvindo Mondal on 25/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */

public class FragmentShare extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_share, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        String filepath = Environment.getExternalStorageDirectory().getPath();
//        File file = new File(filepath, "ponpon.html");
//
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("text/html");
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//
//        //String shareBody="Here is body";
//        //sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, value)
//        //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//        startActivity(Intent.createChooser(sharingIntent, "Share Via"));





        Intent shareIntent2 = new Intent(Intent.ACTION_SEND);
//                shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent2.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_app_sub));
        shareIntent2.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_txt));
        shareIntent2.setType("text/plain");
        shareIntent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent2);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }
}
