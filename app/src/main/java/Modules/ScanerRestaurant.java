package Modules;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Models.MarkerObject;

/**
 * Created by Ngoc Khanh on 8/18/2017.
 */

public class ScanerRestaurant {

    private static final String SCANER_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDbJNAIga7UdLndYFKrqV6MbK89b7F0G2k";
    private static final String TAG = "SCANER RESTAURANT";
    private ScanerRestaurantListener listener;
    private LatLng location;
    private double lat;
    private double lon;
    private int radius;
    private String type;

    public ScanerRestaurant(ScanerRestaurantListener listener, String type, int radius,double lat,double lon) {
        this.listener = listener;
        this.location = location;
        this.radius = radius;
        this.type=type;
        this.lat=lat;
        this.lon=lon;
    }

    public ScanerRestaurant() {
    }

    public void excute() throws UnsupportedEncodingException {
        listener.onScanerStart();
        new DownloadRawData().execute(createUrl());
    }

    public String createUrl() {
        String urlLocation = lat+","+lon;
        String urlType=type.toLowerCase().replace(" ","_");
        Log.i(TAG, "SSSS: "+ urlType);
        String urlRadius = String.valueOf(radius);



        return SCANER_URL_API + "location=" + urlLocation + "&radius=" + urlRadius + "&type="+urlType + "&key=" + GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String res) {
            try {
                parseJson(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJson(String data) throws JSONException {
        if (data == null) return;
        List<MarkerObject> markerRestaurants = new ArrayList<MarkerObject>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonResults = jsonData.getJSONArray("results");

        for (int i = 0; i < jsonResults.length(); i++) {
            JSONObject jsonResult = jsonResults.getJSONObject(i);
            MarkerObject markerRestaurant=new MarkerObject();
            JSONObject jsonGeometry= jsonResult.getJSONObject("geometry");
            JSONObject jsonLocation= jsonGeometry.getJSONObject("location");
            //set Location
            markerRestaurant.setLatLng(new LatLng(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lng")));
            markerRestaurant.setTitles(jsonResult.getString("name"));



            //
            markerRestaurants.add(markerRestaurant);
        }
        listener.onScanerSuccess(markerRestaurants);

    }

}
