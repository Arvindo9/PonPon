package com.jithvar.ponpon.having_spot.map;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.jithvar.ponpon.interfaceClass.GeoCodingAddressAPIsAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.Config.SPOT_REGISTRATION_PROVIDER;

/**
 * Created by KinG on 15-11-2017.
 * Created by ${EMAIL}.
 */

public class GeoCodingAddressAPIs extends AsyncTask<String, Void, ArrayList<AddressDataFull>> {

    private final GoogleMap mMap;
    private final GeoCodingAddressAPIsAddress geoCodingAddressAPIsAddress;

    GeoCodingAddressAPIs(GoogleMap mMap, GeoCodingAddressAPIsAddress geoCodingAddressAPIsAddress) {

        this.mMap = mMap;
        this.geoCodingAddressAPIsAddress = geoCodingAddressAPIsAddress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<AddressDataFull> doInBackground(String... string) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(string[0])
                    .build();
            Response response = client.newCall(request).execute();

            return JSONParser(response.body().string());

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<AddressDataFull> list) {
//        geoCodingAddressAPIsAddress.getAddressOfPinDrop(list);
        if(list != null && !list.isEmpty()) {
            geoCodingAddressAPIsAddress.getAddressOfPinDrop(list.get(0));
        }
        else {
            geoCodingAddressAPIsAddress.getAddressOfPinDrop(null);
        }
    }

    private ArrayList<AddressDataFull> JSONParser(String result){
        ArrayList<AddressDataFull> list = null;
        if(result != null && !result.equals("")){
            Log.e("l========= ", result);
            try {
                list = new ArrayList<>();
                ArrayList<AddressData> list1;
//                AddressData addressData = null;
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectM = jsonArray.getJSONObject(i);

                    JSONArray arrayOb1 = new JSONArray(objectM.getString("address_components"));
                    list1 = new ArrayList<>();
                    for (int j = 0; j < arrayOb1.length(); j++) {
                        JSONObject jsonObject1 = arrayOb1.getJSONObject(j);
                        String long_name = jsonObject1.getString("long_name");
                        String short_name = jsonObject1.getString("short_name");

                        JSONArray arrayOb2 = new JSONArray(jsonObject1.getString("types"));
                        ArrayList<String> typeList = new ArrayList<>();
                        for (int k = 0; k < arrayOb2.length(); k++) {
                            typeList.add(arrayOb2.getString(k));
                        }

                        String streetType = parserAddressType(typeList);
//                        addressData = new AddressData(long_name, short_name, streetType);
                        list1.add(new AddressData(long_name, short_name, streetType));
                    }

                    String formatted_address = objectM.getString("formatted_address");

//                JSONObject ob3 = object.getJSONObject("geometry");
                    String place_id = objectM.getString("place_id");

//                    String types = objectM.getString("types");
                    JSONArray arrayOb2 = new JSONArray(objectM.getString("types"));
                    ArrayList<String> addressTypeList = new ArrayList<>();
                    for (int k = 0; k < arrayOb2.length(); k++) {
                        addressTypeList.add(arrayOb2.getString(k));
                    }

                    list.add(new AddressDataFull(list1 , formatted_address, place_id,
                            addressTypeList));
                }





                Log.e("Status", jsonObject.getString("status"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Status", "failure");
            }
        }
        return list;
    }


    private String parserAddressType(ArrayList<String> list){
        String name = "";
        if(list.size() > 1){
            if(list.contains("political")){
                list.remove("political");

                if(list.size() < 2){
                    return list.get(0);
                }
            }
            else if(list.contains("street_address")){
                return "street_address";
            }
            else if(list.contains("route")){
                return "route";
            }
            else if(list.contains("intersection")){
                return "intersection";
            }
            else if(list.contains("country")){
                return "country";
            }
            else if(list.contains("postal_code")){
                return "postal_code";
            }
            else if(list.contains("sublocality_level_1")){
                return "sublocality_level_1";
            }
            else if(list.contains("sublocality_level_2")){
                return "sublocality_level_2";
            }
            else if(list.contains("sublocality_level_3")){
                return "sublocality_level_3";
            }
            else if(list.contains("sublocality_level_4")){
                return "sublocality_level_4";
            }
            else if(list.contains("sublocality_level_5")){
                return "sublocality_level_5";
            }
            else if(list.contains("administrative_area_level_1")){
                return "administrative_area_level_1";
            }
            else if(list.contains("administrative_area_level_2")){
                return "administrative_area_level_2";
            }
            else if(list.contains("administrative_area_level_3")){
                return "administrative_area_level_3";
            }
            else if(list.contains("administrative_area_level_4")){
                return "administrative_area_level_4";
            }
            else if(list.contains("administrative_area_level_5")){
                return "administrative_area_level_5";
            }
            else if(list.contains("neighborhood")){
                return "neighborhood";
            }
            else if(list.contains("subpremise")){
                return "subpremise";
            }
            else if(list.contains("premise")){
                return "premise";
            }
            else if(list.contains("natural_feature")){
                return "natural_feature";
            }
            else if(list.contains("airport")){
                return "airport";
            }
            else if(list.contains("park")){
                return "park";
            }
            else if(list.contains("colloquial_area")){
                return "colloquial_area";
            }
            else if(list.contains("locality")){
                return "locality";
            }
            else{
                return "political";
            }
        }
        else{
            name = list.get(0);
        }

        return name;
    }
}
