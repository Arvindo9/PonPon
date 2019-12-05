package com.jithvar.ponpon.need_spot;

import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.jithvar.ponpon.interfaceClass.BookSpotList;
import com.jithvar.ponpon.map.GeoLocation;
import com.jithvar.ponpon.need_spot.handler.BookSpotData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jithvar.ponpon.config.Config.BOOK_SPOT_LOAD_NEAR_BY1;

/**
 * Created by KinG on 18-11-2017.
 * Created by ${EMAIL}.
 */

public class BookSpotMapDataLoading implements Runnable, Serializable{

    private final BookSpotList bookSpotList;
    private final double latitude;
    private final double longitude;
    private final String userId;
    private final String mobileNumber;
    private final String date;
    private final String placeId;
    private final String placeName;
    private final String placeAddress;
    private final String locale;
    private final String startType;
    private ArrayList<BookSpotData> list;

    BookSpotMapDataLoading( BookSpotList bookSpotList,
                                  double latitude, double longitude,
                                  String startType,
                                  String userId, String mobileNumber, String date,
                                  String placeId, String placeName, String placeAddress,
                                  String locale) {

        this.bookSpotList = bookSpotList;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startType = startType;
        this.userId = userId;
        this.mobileNumber = mobileNumber;
        this.date = date;
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.locale = locale;
    }

    private void parseJson(String result) {
        Log.e("l========= ", result);
        if(!result.equals("")) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                switch (jsonObject.getString("Status")){
                    case "Success":
                        JSONArray array = new JSONArray(jsonObject.getString("jsonObj"));
                        for(int i = 0; i < array.length(); i++){
                            JSONObject jObject = array.getJSONObject(i);
                            String primaryID = jObject.getString("Primary_ID");
                            String spot_S_No = jObject.getString("Spot_S_No");
                            String spotName = jObject.getString("SpotName");
                            String dateRegister = jObject.getString("DateRegister");
                            String exitTimeP = jObject.getString("ExitTime");
                            String waitTimeP = jObject.getString("WaitTime");
                            String betAmountP = jObject.getString("BetAmount");
                            String address = jObject.getString("Address");
                            String placeID = jObject.getString("PlaceID");
                            String latitude = jObject.getString("Latitude");
                            String longitude = jObject.getString("Longitude");
                            String latitudeRad = jObject.getString("LatitudeRad");
                            String longitudeRad = jObject.getString("LongitudeRad");
                            String areaSize = jObject.getString("AreaSize");
                            String statusSpot = jObject.getString("SpotStatus");
                            String totalBet = jObject.getString("TotalBet");

                            list.add(new BookSpotData(primaryID, spot_S_No, spotName,
                                    dateRegister,
                                    exitTimeP, waitTimeP, betAmountP, address, placeID,
                                    latitude, longitude, latitudeRad, longitudeRad,
                                    areaSize, statusSpot, totalBet));
                        }

                        bookSpotList.bookSpotData(list);
                        bookSpotList.loadNewSpot();
                        break;

                    default:
                        break;
                }

                Log.e("Status", jsonObject.getString("Status"));
            }catch (JSONException e) {
                e.printStackTrace();
                bookSpotList.loadNewSpot();
            }
        }
    }

    @Override
    public void run() {
        double earthRadius = 6371.01;       //in km
        double distance = 2;                //in km
        double radius = distance / 6371;
        this.list = new ArrayList<>();

//        GeoLocation myLocation = GeoLocation.fromDegrees(latitude, longitude);

        GeoLocation myLocation = GeoLocation.fromDegrees(latitude, longitude);
        GeoLocation[] boundingCoordinates =
                myLocation.boundingCoordinates(distance, radius);

        double latitudeMin = boundingCoordinates[0].getLatitudeInRadians();
        double latitudeMax = boundingCoordinates[1].getLatitudeInRadians();
        double longitudeMin = boundingCoordinates[0].getLongitudeInRadians();
        double longitudeMax = boundingCoordinates[1].getLongitudeInRadians();
        double latitudeR = myLocation.getLatitudeInRadians();
        double longitudeR = myLocation.getLongitudeInRadians();

        Log.e("latitudeMin", String.valueOf(latitudeMin));
        Log.e("latitudeMax", String.valueOf(latitudeMax));
        Log.e("longitudeMin", String.valueOf(longitudeMin));
        Log.e("longitudeMax", String.valueOf(longitudeMax));
        Log.e("latitudeR", String.valueOf(latitudeR));
        Log.e("longitudeR", String.valueOf(longitudeR));
        Log.e("radius", String.valueOf(radius));

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("start_type", startType)
                    .add("User_ID", userId)
                    .add("MobileNumber", mobileNumber)
                    .add("LatitudeCInD", String.valueOf(latitude))
                    .add("LongitudeCInD", String.valueOf(longitude))
                    .add("LatitudeCInR", String.valueOf(latitudeR))
                    .add("LongitudeCInR", String.valueOf(longitudeR))
                    .add("LatitudeMin", String.valueOf(latitudeMin))
                    .add("LatitudeMax", String.valueOf(latitudeMax))
                    .add("LongitudeMin", String.valueOf(longitudeMin))
                    .add("LongitudeMax", String.valueOf(longitudeMax))
                    .add("Radius", String.valueOf(radius))
                    .add("DateCurrent", date)
                    .add("PlaceId", placeId)
                    .add("PlaceName", placeName)
                    .add("PlaceAddress", placeAddress)
                    .add("Locale", locale)
                    .build();
            Request request = new Request.Builder()
                    .url(BOOK_SPOT_LOAD_NEAR_BY1)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();

            parseJson(response.body().string());

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
            e.printStackTrace();
            bookSpotList.loadNewSpot();
        }

    }
}
