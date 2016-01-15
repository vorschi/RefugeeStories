package at.ac.tuwien.inso.refugeestories.utils.tasks;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Prediction;

/**
 * Created by Amer Salkovic on 14.1.2016.
 */
public class MyGooglePlacesApi {

    String API_BASE_PLACES = "https://maps.googleapis.com/maps/api/place/autocomplete";
    String API_BASE_DETAILS = "https://maps.googleapis.com/maps/api/place/details";
    String OUTPUT = "/json";
    String KEY = "AIzaSyCGffgl0fFsQ3K84I2Ft4Ywxe_kZb878Gc";

    public List<Prediction> getPredictions(String constraint) {
        if(constraint == null || "".equals(constraint)) {
            return null;
        }

        // request to google places web service
        StringBuilder urlBuilder = new StringBuilder(API_BASE_PLACES + OUTPUT);
        urlBuilder.append("?input=" + constraint);
        urlBuilder.append("&key=" + KEY);
        //Log.i(this.getClass().getSimpleName(), "URL: " + urlBuilder.toString());

        URL url = null;
        StringBuilder response = new StringBuilder();
        try {
            url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                response.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // parse response to predictions
        List<Prediction> predictions = null;
        try {
            predictions = new ArrayList<>();
            JSONObject responseObj = new JSONObject(response.toString());
            JSONArray responseArray = responseObj.getJSONArray("predictions");
            for (int i = 0; i < responseArray.length(); i++) {
                predictions.add(new Prediction(
                                responseArray.getJSONObject(i).getString("description"),
                                responseArray.getJSONObject(i).getString("place_id")
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return predictions;
    }

    public LatLng getPlaceDetails(String placeId) {
        if(placeId == null || "".equals(placeId)) {
            return null;
        }

        // request to google places web service
        StringBuilder urlBuilder = new StringBuilder(API_BASE_DETAILS + OUTPUT);
        urlBuilder.append("?placeid=" + placeId);
        urlBuilder.append("&key=" + KEY);
        //Log.i(this.getClass().getSimpleName(), "URL: " + urlBuilder.toString());

        URL url = null;
        StringBuilder response = new StringBuilder();
        try {
            url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                response.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng latlng = null;
        try {
            JSONObject responseObj = new JSONObject(response.toString());
            JSONObject resultArray = responseObj.getJSONObject("result");
            JSONObject geometry = resultArray.getJSONObject("geometry");
            latlng = new LatLng((double) geometry.getJSONObject("location").get("lat"),
                    (double) geometry.getJSONObject("location").get("lng"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latlng;
    }

}
