package com.jithvar.ponpon.having_spot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.jithvar.ponpon.R;
import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.dialog.AddressChange;
import com.jithvar.ponpon.having_spot.map.AddSpotMap;
import com.jithvar.ponpon.having_spot.map.AddressDataFull;
import com.jithvar.ponpon.interfaceClass.AddressInterface;
import com.jithvar.ponpon.interfaceClass.DateInterface;
import com.jithvar.ponpon.interfaceClass.TimeInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.jithvar.ponpon.having_spot.handler.ImageHandler;

import static com.jithvar.ponpon.config.Config.SPOT_REGISTRATION_PROVIDER;

/**
 * Created by KinG on 01-11-2017.
 * Created by ${EMAIL}.
 */

public class AddSpot extends AppCompatActivity implements DateInterface, TimeInterface,
        AddressInterface {

    private TextView locationTv;
    private TextView timeWaitingTv;
    private TextView timeOutTv;
    private TextView timeInTv;
    private TextView dateTv;
    private TextView spotNoTv;
    private EditText setBetAmountEt;
    private TextInputLayout setBetAmountLayout;
    private String betAmount = "";
    private Button dateBtn;
    private int timeValue;
    private String timeIn = "";
    private String timeOut = "";
    private String timeWait = "";
    private LocationManager locationManager;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private Button gpsOff;
    private Button gpsOn;
    private TextView gpsOnTv;
    private String userId;
    private String mobileNumber;
    private boolean accountStatus;
    private DatabaseDB db;
    private ProgressDialog mProgressDialog;
    private EditText spotNameE;
    private TextInputLayout spotNameLayout;
    private String spotNo = "";
    private String spotName;
    private String address = "";
    private String latitude = "";
    private String longitude = "";
    private String date = "";
    private String areaSize = "";
//    private String status = "";
    private ArrayList<AddressDataFull> addressList;
    private AddressDataFull addressData;
    private Bitmap mapSnapShot;
    private ImageView mapIv;
    private TextInputLayout areaSizeYLayout;
    private TextInputLayout areaSizeXLayout;
    private EditText areaSizeYE;
    private EditText areaSizeXE;
    private int timeInInt;
    private int timeOutInt;
    private int timeWaitInt;
    private int currentDate;
    private int dateInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_spot);

        mapIv = (ImageView) findViewById(R.id.map_img);
        spotNoTv = (TextView) findViewById(R.id.spot_no);
        dateTv = (TextView) findViewById(R.id.date_tv);
        timeInTv = (TextView) findViewById(R.id.time_enter_tv);
        timeOutTv = (TextView) findViewById(R.id.time_exit_tv);
        timeWaitingTv = (TextView) findViewById(R.id.time_max_wait_tv);
        locationTv = (TextView) findViewById(R.id.location_tv);
        setBetAmountEt = (EditText) findViewById(R.id.bet_amount_et);
        setBetAmountLayout = (TextInputLayout) findViewById(R.id.bet_amount_l);
        spotNameE = (EditText) findViewById(R.id.spot_name_et);
        spotNameLayout = (TextInputLayout) findViewById(R.id.spot_name_l);
        areaSizeXE = (EditText) findViewById(R.id.area_size_x_et);
        areaSizeXLayout = (TextInputLayout) findViewById(R.id.area_size_x_l);
        areaSizeYE = (EditText) findViewById(R.id.area_size_y_et);
        areaSizeYLayout = (TextInputLayout) findViewById(R.id.area_size_y_l);

        spotNameE.addTextChangedListener(new MyTextWatcher(spotNameE));
        setBetAmountEt.addTextChangedListener(new MyTextWatcher(setBetAmountEt));
        areaSizeXE.addTextChangedListener(new MyTextWatcher(areaSizeXE));
        areaSizeYE.addTextChangedListener(new MyTextWatcher(areaSizeYE));

        gpsOnTv = (TextView) findViewById(R.id.gps_on_tv);
        gpsOn = (Button) findViewById(R.id.gps_on);

        db = new DatabaseDB(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(checkProvider()){
            gpsOnTv.setText(getResources().getString(R.string.on));
        }
        else {
            gpsOnTv.setText(getResources().getString(R.string.off));
        }

        try {
            userId = db.getUserId();
            mobileNumber = db.getMobileNumber();
            accountStatus = db.isAccountActive();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String s = String.valueOf(year + month + day);
        currentDate = Integer.parseInt(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean checkProvider() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return !(!isNetworkEnabled && !isGPSEnabled);
    }

    private boolean onGps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isNetworkEnabled && !isGPSEnabled) {
            // btnShowLocation.setChecked(false);
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setTitle(getResources().getString(R.string.gps_off));
            d.setMessage(getResources().getString(R.string.gps_setting));
            d.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }
            });
            d.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            d.show();
            return false;

        }
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_btn:
                DialogFragment newFragment = new DatePickerFragment(AddSpot.this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;

            case R.id.time_enter_btn:
                DialogFragment newFragment1 = new TimePickerFragment(AddSpot.this);
                newFragment1.show(getSupportFragmentManager(), "timePicker");
                timeValue = 1;
                break;

            case R.id.time_exit_btn:
                DialogFragment newFragment2 = new TimePickerFragment(AddSpot.this);
                newFragment2.show(getSupportFragmentManager(), "timePicker");
                timeValue = 2;
                break;

            case R.id.time_max_wait_btn:
                DialogFragment newFragment3 = new TimePickerFragment(AddSpot.this);
                newFragment3.show(getSupportFragmentManager(), "timePicker");
                timeValue = 3;
                break;

            case R.id.location_edit_btn:
                android.app.FragmentManager manager = getFragmentManager();
                AddressChange addressChange = new AddressChange(address);
                addressChange.show(manager, "Change Address");
                break;

            case R.id.map_img:
                if(accountStatus) {
                    Intent i = new Intent(AddSpot.this, AddSpotMap.class);
                    i.putExtra("userId", userId);
                    i.putExtra("mobileNumber", mobileNumber);
                    i.putExtra("GpsStatus", checkProvider());
                    startActivityForResult(i, 10);
                }
                break;

            case R.id.gps_on:
                onGps();
                break;

            case R.id.add_spot:
                if(accountStatus) {
                    bookSpot();
                }
                Log.e("Account status", String.valueOf(accountStatus));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){
            latitude = data.getStringExtra("Latitude");
            longitude = data.getStringExtra("Longitude");
            addressData = (AddressDataFull) data.getSerializableExtra("AddressData");
//            mapSnapShot = data.getParcelableExtra("SnapShot");
//            mapSnapShot = data.getParcelableExtra("SnapShot");


//            ArrayList<ImageHandler> list =(ArrayList<ImageHandler>)
//                    data.getSerializableExtra("SnapShot");
//            mapSnapShot = list.get(0).getMapSnapShot();

            ImageHandler imageHandler =(ImageHandler) data.getSerializableExtra("SnapShot");
            mapSnapShot = imageHandler.getMapSnapShot();

            locationTv.setText(addressData.getFormatted_address());
            address = addressData.getFormatted_address();

            BitmapDrawable ob = new BitmapDrawable(getResources(), mapSnapShot);
            mapIv.setBackground(ob);
//            mapIv.setBackgroundDrawable(ob);
        }
    }

    @Override
    public void DateSet(String date, int dateInt) {
        dateTv.setText(date);
        this.date = date;
        this.dateInt = dateInt;
    }

    @Override
    public void TimeSet(String time,int timeInt) {
        switch (timeValue){
            case 1: timeInTv.setText(time);
                timeIn = time;
                timeInInt = timeInt;
                break;
            case 2: timeOutTv.setText(time);
                timeOut = time;
                timeOutInt = timeInt;
                break;
            case 3: timeWaitingTv.setText(time);
                timeWait = time;
                timeWaitInt = timeInt;
                break;
        }
    }

    @Override
    public void AddressSet(String address) {
        this.address = address;
    }

    private class MyTextWatcher implements TextWatcher {
        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.bet_amount_et:
                    validateBetAmount();
                    break;

                case R.id.spot_name_et:
                    validateSpotName();
                    break;

                case R.id.area_size_x_et:
                    validateAreaX();
                    break;

                case R.id.area_size_y_et:
                    validateAreaY();
                    break;
            }
        }
    }

    private boolean validateBetAmount() {
        if (setBetAmountEt.getText().toString().trim().isEmpty()) {
            setBetAmountLayout.setError(getString(R.string.bet_req));
            requestFocus(setBetAmountLayout);
            return false;
        } else {
            setBetAmountLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateSpotName() {
        if (spotNameE.getText().toString().trim().isEmpty()) {
            spotNameLayout.setError(getString(R.string.spot_name_req));
            requestFocus(spotNameLayout);
            return false;
        } else {
            spotNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAreaX() {
        if (areaSizeXE.getText().toString().trim().isEmpty()) {
            areaSizeXLayout.setError(getString(R.string.area_x_req));
            requestFocus(areaSizeXLayout);
            return false;
        } else {
            areaSizeXLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAreaY() {
        if (areaSizeYE.getText().toString().trim().isEmpty()) {
            areaSizeYLayout.setError(getString(R.string.area_y_req));
            requestFocus(areaSizeYLayout);
            return false;
        } else {
            areaSizeYLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateTime() {
        if (timeWaitInt >= timeOutInt &&
                timeWaitInt >= timeInInt && timeOutInt >= timeInInt) {
            showMassage(R.string.select_valid_time);
            return false;
        }
        return true;
    }

    private boolean validateDate() {
        if (currentDate <= dateInt) {
            showMassage(R.string.select_valid_date);
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void bookSpot() {
        if(!validateBetAmount()){
            return;
        }

        if(!validateSpotName()){
            return;
        }
        if(!validateAreaX()){
            return;
        }
        if(!validateAreaY()){
            return;
        }

        if(date.equals("") && !validateDate()){
            showMassage(R.string.enter_date);
            return;
        }

        if(timeIn.equals("") && !validateTime()){
            showMassage(R.string.time_enter);
            return;
        }

        if(timeOut.equals("") && !validateTime()){
            showMassage(R.string.time_exit);
            return;
        }

        if(timeWait.equals("") && !validateTime()){
            showMassage(R.string.time_wait);
            return;
        }

        if(latitude.equals("")){
            showMassage(R.string.set_add_map);
            return;
        }
        if(longitude.equals("")){
            showMassage(R.string.set_add_map);
            return;
        }
        if(address.equals("")){
            showMassage(R.string.set_add_map);
            return;
        }

        areaSize = areaSizeXE.getText().toString() + "x" + areaSizeYE.getText().toString();
        betAmount = setBetAmountEt.getText().toString();
        spotName = spotNameE.getText().toString();
//        status = "Active";

        new SendingDataToServer().execute();
    }

    //---------------date picker---------------------------------
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private DateInterface dateInterface;

        public DatePickerFragment(){
            super();
        }

        @SuppressLint("ValidFragment")
        public DatePickerFragment(AddSpot activity){
            this();
            dateInterface = activity;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String monthStr = String.valueOf(month);
            String dayStr = String.valueOf(day);
            if(monthStr.length() < 2){
                monthStr = "0" + String.valueOf(month);
            }
            if(dayStr.length() < 2){
                dayStr = "0" + String.valueOf(day);
            }

            String date = dayStr + "/" + monthStr + "/" + year;
            int d = Integer.parseInt(String.valueOf(year + month + day));
            Log.e("date select", date);
            dateInterface.DateSet(date, d);

        }
    }

    //---------------time picker---------------------------------
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private TimeInterface timeInterface;

        public TimePickerFragment(){
            super();
        }

        @SuppressLint("ValidFragment")
        public TimePickerFragment(AddSpot activity){
            this();
            timeInterface = activity;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = hourOfDay + ":" + minute;
            Log.e("date select", time);
            String s = String.valueOf(hourOfDay + minute);
            timeInterface.TimeSet(time, Integer.parseInt(s));
        }
    }

    //-----------------Send to server----------------------------
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class SendingDataToServer extends AsyncTask<Void, String, String> {


        private String street_address = "";
        private String route = "";
        private String intersection = "";
        private String country = "";
        private String postal_code = "";
        private String sublocality_level_5 = "";;
        private String sublocality_level_4 = "";;
        private String sublocality_level_3 = "";;
        private String sublocality_level_2 = "";;
        private String sublocality_level_1 = "";;
        private String administrative_area_level_5 = "";;
        private String administrative_area_level_4 = "";;
        private String administrative_area_level_3 = "";;
        private String administrative_area_level_2 = "";;
        private String administrative_area_level_1 = "";;
        private String neighborhood = "";
        private String premise = "";;
        private String natural_feature = "";;
        private String airport = "";;
        private String park = "";;
        private String colloquial_area = "";;
        private String locality = "";;
        private String political = "";;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(AddSpot.this, AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            for (int i = 0; i < addressData.getList1().size(); i++){
//                AddressData addressData1 = addressData.getList1().get(i);
//                addressData1.
                switch (addressData.getList1().get(i).getStreetType()){
                    case "street_address": street_address = "street_address"; break;
                    case "route": route = "route"; break;
                    case "intersection": intersection = "intersection"; break;
                    case "country": country = "country"; break;
                    case "postal_code": postal_code = "postal_code"; break;
                    case "sublocality_level_5": sublocality_level_5 = "sublocality_level_5"; break;
                    case "sublocality_level_4": sublocality_level_4 = "sublocality_level_4"; break;
                    case "sublocality_level_3": sublocality_level_3 = "sublocality_level_3"; break;
                    case "sublocality_level_2": sublocality_level_2 = "sublocality_level_2"; break;
                    case "sublocality_level_1": sublocality_level_1 = "sublocality_level_1"; break;
                    case "administrative_area_level_5": administrative_area_level_5 =
                            "administrative_area_level_5"; break;
                    case "administrative_area_level_4": administrative_area_level_3 =
                            "administrative_area_level_4"; break;
                    case "administrative_area_level_3": administrative_area_level_2 =
                            "administrative_area_level_3"; break;
                    case "administrative_area_level_2": administrative_area_level_1 =
                            "administrative_area_level_2"; break;
                    case "administrative_area_level_1":
                        administrative_area_level_1 = "administrative_area_level_1"; break;
                    case "neighborhood": neighborhood = "neighborhood"; break;
                    case "premise": premise = "premise"; break;
                    case "natural_feature": natural_feature = "natural_feature"; break;
                    case "airport": airport = "airport"; break;
                    case "park": park = "park"; break;
                    case "colloquial_area": colloquial_area = "colloquial_area"; break;
                    case "locality": locality = "locality"; break;
                    case "political": political = "political"; break;
                }
            }

            String latitudeRad = String .valueOf(Double.parseDouble(latitude) * Math.PI / 180);
            String longitudeRad = String .valueOf(Double.parseDouble(longitude) * Math.PI / 180);


            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("start_type", "BookSpot")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("Spot_S_No", spotNo)
                        .add("SpotName", spotName)
                        .add("DateRegister", date)
                        .add("EnterTime", timeIn)
                        .add("ExitTime", timeOut)
                        .add("WaitTime", timeWait)
                        .add("BetAmount", betAmount)
                        .add("Address", address)
                        .add("Latitude", latitude)
                        .add("Longitude", longitude)
                        .add("LatitudeRad", latitudeRad)
                        .add("LongitudeRad", longitudeRad)
                        .add("AreaSize", areaSize)
                        .add("Address", addressData.getFormatted_address())
                        .add("PlaceID", addressData.getPlace_id())
//                        .add("Status", status)

                        .add("street_address", street_address)
                        .add("route", route)
                        .add("intersection", intersection)
                        .add("country", country)
                        .add("postal_code", postal_code)
                        .add("sublocality_level_5", sublocality_level_5)
                        .add("sublocality_level_4", sublocality_level_4)
                        .add("sublocality_level_3", sublocality_level_3)
                        .add("sublocality_level_2", sublocality_level_2)
                        .add("sublocality_level_1", sublocality_level_1)
                        .add("administrative_area_level_5", administrative_area_level_5)
                        .add("administrative_area_level_4", administrative_area_level_4)
                        .add("administrative_area_level_3", administrative_area_level_3)
                        .add("administrative_area_level_2", administrative_area_level_2)
                        .add("administrative_area_level_1", administrative_area_level_1)
                        .add("neighborhood", neighborhood)
                        .add("premise", premise)
                        .add("natural_feature", natural_feature)
                        .add("airport", airport)
                        .add("park", park)
                        .add("colloquial_area", colloquial_area)
                        .add("locality", locality)
                        .add("political", political)
                        .build();
                Request request = new Request.Builder()
                        .url(SPOT_REGISTRATION_PROVIDER)
                        .post(formBody)
                        .build();
                Response response = client.newCall(request).execute();

                return response.body().string();

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            Log.e("l========= ", result);
            if(!result.equals("")) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    switch (jsonObject.getString("Status")){
                        case "Success":
                            showMassage(R.string.success);
                            break;
                        default:
                            Toast.makeText(AddSpot.this, getResources().getString(R.string
                                    .try_again), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    Log.e("Status", jsonObject.getString("Status"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showMassage(int id){
        Toast.makeText(this, getResources().getString(id), Toast.LENGTH_SHORT).show();
    }
}