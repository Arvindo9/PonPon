package com.jithvar.ponpon;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jithvar.ponpon.database.DatabaseDB;
import com.jithvar.ponpon.handler.DataBaseHandler;
import com.jithvar.ponpon.interfaceClass.DateInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Calendar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.jithvar.ponpon.config.Config.UPDATE_PROFILE;

/**
 * Created by KinG on 27-10-2017.
 * Created by ${EMAIL}.
 */

public class UpdateProfile extends AppCompatActivity implements DateInterface {

    private EditText userNameE;
    private TextInputLayout userNameLayout;
    private EditText streetE;
    private TextInputLayout streetLayout;
    private EditText landmarkE;
    private TextInputLayout landmarkLayout;
    private EditText cityE;
    private TextInputLayout cityLayout;
    private TextInputLayout stateLayout;
    private EditText stateE;
    private EditText countryE;
    private TextInputLayout countryLayout;
    private EditText pincodeE;
    private TextInputLayout pincodeLayout;
    private RadioGroup radioSexGroup;
    private RadioButton genderRb;
    private String userName;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private ProgressDialog mProgressDialog;
    private String gender;
    private String dateToTrack = "";
    private Button dateButton;
    private static String date = "";
    private String countryCode;
    private EditText countryCodyE;
    private TextInputLayout countryCodeLayout;
    private String userId = "";
    private String mobileNumber;
    private DatabaseDB db;
    private String houseNumber;
    private TextInputLayout houseLayout;
    private EditText houseE;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        db = new DatabaseDB(this);

        userNameE = (EditText) findViewById(R.id.user_name);
        userNameLayout = (TextInputLayout) findViewById(R.id.user_name_l);
        streetE = (EditText) findViewById(R.id.street_et);
        streetLayout = (TextInputLayout) findViewById(R.id.street_l);
        landmarkE = (EditText) findViewById(R.id.landmark_et);
        landmarkLayout = (TextInputLayout) findViewById(R.id.landmark_l);
        cityE = (EditText) findViewById(R.id.city_et);
        cityLayout = (TextInputLayout) findViewById(R.id.city_l);
        stateE = (EditText) findViewById(R.id.state_et);
        stateLayout = (TextInputLayout) findViewById(R.id.state_l);
        countryE = (EditText) findViewById(R.id.country_et);
        countryLayout = (TextInputLayout) findViewById(R.id.country_l);
        pincodeE = (EditText) findViewById(R.id.pincode_et);
        pincodeLayout = (TextInputLayout) findViewById(R.id.pincode_l);
        countryCodyE = (EditText) findViewById(R.id.country_code_et);
        countryCodeLayout = (TextInputLayout) findViewById(R.id.country_code_l);
        houseE = (EditText) findViewById(R.id.house_et);
        houseLayout = (TextInputLayout) findViewById(R.id.house_l);

        userNameE.addTextChangedListener(new MyTextWatcher(userNameE));
        streetE.addTextChangedListener(new MyTextWatcher(streetE));
        landmarkE.addTextChangedListener(new MyTextWatcher(landmarkE));
        cityE.addTextChangedListener(new MyTextWatcher(cityE));
        stateE.addTextChangedListener(new MyTextWatcher(stateE));
        countryE.addTextChangedListener(new MyTextWatcher(countryE));
        pincodeE.addTextChangedListener(new MyTextWatcher(pincodeE));
        countryCodyE.addTextChangedListener(new MyTextWatcher(countryCodyE));
        houseE.addTextChangedListener(new MyTextWatcher(houseE));

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        dateButton = (Button) findViewById(R.id.dob);

        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        genderRb = (RadioButton) findViewById(selectedId);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                submitForm();
                break;

            case R.id.dob:
                DialogFragment newFragment = new DatePickerFragment(this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }

    @Override
    public void DateSet(String date, int dateInt) {
        dateButton.setText(date);
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
                case R.id.user_name:
                    validateUserName();
                    break;

                case R.id.street_et:
                    validateStreet();
                    break;

                case R.id.landmark_et:
                    validateLandMark();
                    break;

                case R.id.city_et:
                    validateCity();
                    break;

                case R.id.state_et:
                    validateStreet();
                    break;

                case R.id.country_et:
                    validateCountry();
                    break;

                case R.id.pincode_et:
                    validatePinCode();
                    break;

                case R.id.country_code_et:
                    validateCountryCode();
                    break;

                case R.id.house_et:
                    validateHouseNumber();
                    break;
            }
        }
    }

    private boolean validateHouseNumber() {
        if (houseE.getText().toString().trim().isEmpty()) {
            houseLayout.setError(getString(R.string.house_no));
            requestFocus(houseLayout);
            return false;
        } else {
            houseLayout.setErrorEnabled(false);
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void submitForm() {

        if (!validateUserName()) {
            return;
        }

        if (!validateHouseNumber()) {
            return;
        }

        if (!validateStreet()) {
            return;
        }
        if (!validateLandMark()) {
            return;
        }
        if (!validateCity()) {
            return;
        }

        if (!validateState()) {
            return;
        }

        if (!validateCountry()) {
            return;
        }

        if (!validatePinCode()) {
            return;
        }

        if(date.equals("")){
            Toast.makeText(UpdateProfile.this, getResources().getString(R.string.enter_date),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        userName = userNameE.getText().toString();
        houseNumber = houseE.getText().toString();
        street = streetE.getText().toString();
        landmark = landmarkE.getText().toString();
        city = cityE.getText().toString();
        state = stateE.getText().toString();
        country = countryE.getText().toString();
        pincode = pincodeE.getText().toString();
        countryCode = countryCodyE.getText().toString();

        new SendingDataToServer().execute();
    }

    private boolean validatePinCode() {
        if (pincodeE.getText().toString().trim().isEmpty() &&
                pincodeE.getText().toString().length() < 4 &&
                pincodeE.getText().toString().length() > 6) {
            pincodeLayout.setError(getString(R.string.pincode));
            requestFocus(pincodeLayout);
            return false;
        } else {
            pincodeLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCountry() {
        if (countryE.getText().toString().trim().isEmpty()) {
            countryLayout.setError(getString(R.string.country_req));
            requestFocus(countryLayout);
            return false;
        } else {
            countryLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateState() {
        if (stateE.getText().toString().trim().isEmpty()) {
            stateLayout.setError(getString(R.string.state_req));
            requestFocus(stateLayout);
            return false;
        } else {
            stateLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCity() {
        if (cityE.getText().toString().trim().isEmpty()) {
            cityLayout.setError(getString(R.string.city_req));
            requestFocus(cityLayout);
            return false;
        } else {
            cityLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLandMark() {
        if (landmarkE.getText().toString().trim().isEmpty()) {
            landmarkLayout.setError(getString(R.string.landmark_req));
            requestFocus(landmarkLayout);
            return false;
        } else {
            landmarkLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateStreet() {
        if (streetE.getText().toString().trim().isEmpty()) {
            streetLayout.setError(getString(R.string.street_req));
            requestFocus(streetLayout);
            return false;
        } else {
            streetLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateUserName() {
        if (userNameE.getText().toString().trim().isEmpty()) {
            userNameLayout.setError(getString(R.string.user_name_req));
            requestFocus(userNameLayout);
            return false;
        } else {
            userNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCountryCode() {
        if (countryCodyE.getText().toString().trim().isEmpty() &&
                (countryCodyE.getText().toString().length() > 0 &&
                countryCodyE.getText().toString().length() < 3)) {
            countryCodeLayout.setError(getString(R.string.country_code_req));
            requestFocus(countryCodeLayout);
            return false;
        } else {
            countryCodeLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //---------------date picker-----------------
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private DateInterface dateInterface;

        public DatePickerFragment(){
            super();
        }

        @SuppressLint("ValidFragment")
        public DatePickerFragment(UpdateProfile activity){
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

//            date = year + "-" + monthStr + "-" + dayStr;
            date = dayStr + "/" + monthStr + "/" + year;
            Log.e("date select", date);
            dateInterface.DateSet(date, Integer.parseInt(String.valueOf(year + month + day)));
        }
    }

    //--------------------Server--------------------

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class SendingDataToServer extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(UpdateProfile.this, AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage("Loading request...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
//            requestServerForDataString();
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("start_type", "Update_profile")
                        .add("User_ID", userId)
                        .add("MobileNumber", mobileNumber)
                        .add("UserName", userName)
                        .add("HouseNumber", houseNumber)
                        .add("Street", street)
                        .add("Landmark", landmark)
                        .add("City", city)
                        .add("State", state)
                        .add("Country", country)
                        .add("Pincode", pincode)
                        .add("Gender", gender)
                        .add("Dob", date)
                        .add("CountryCode", countryCode)
                        .build();
                Request request = new Request.Builder()
                        .url(UPDATE_PROFILE)
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
                            db.insertUserDetails(new DataBaseHandler(userId, userName,
                                    houseNumber, street,
                                    landmark, city, state, country, pincode, gender, date,
                                    countryCode));
                            Toast.makeText(UpdateProfile.this, getResources().getString(R.string
                                    .success), Toast.LENGTH_SHORT).show();
                            break;

//                        case "Error":
//                            break;
//                        case "ErrorMobile":
//                            break;
//                        case "ErrorAccount":
//                            break;
//                        case "ErrorStart":
//                            break;
//                        case "ErrorFetch":
//                            break;
//                        case "ErrorDb":
//                            break;
                        default:
                            Toast.makeText(UpdateProfile.this, getResources().getString(R.string
                                    .try_again), Toast.LENGTH_SHORT).show();

                            break;
                    }

                    Log.e("Status", jsonObject.getString("Status"));
                }catch (SQLException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
