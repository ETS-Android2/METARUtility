package com.example.metarutility.ui.main;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class MetarApi {

    /*
     * AVWX Token is required for this application to work. Sign up for token at
     * https://account.avwx.rest/ and generate token. Add generated token as value
     * for variable 'authToken'
     */

    String authToken = "65H43GKqAoSvN89VGZ5WD-z26OpDn9PGCU3NMFHb3e4";

    public JSONObject GetStationInfo(String station) throws IOException, JSONException {
        /*Pulling Airport information from API and formatting into a JSON String
         */

        String stationInfo = null;
        // Format: https://avwx.rest/api/station/KLAX?format=json&token=65H43GKqAoSvN89VGZ5WD-z26OpDn9PGCU3NMFHb3e4
        String urlFormed = "https://avwx.rest/api/station/" + station + "?format=json&token=" + authToken;
        URL getStationRequest = new URL(urlFormed);

        HttpURLConnection connection = (HttpURLConnection) getStationRequest.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((stationInfo = in.readLine()) != null) {
                response.append(stationInfo);
            } in .close();

            stationInfo = response.toString();
        } else {
            System.out.println("Error Fetching Data");
        }

        JSONObject stationJson = new JSONObject(stationInfo);
        return stationJson;
    }

    public JSONObject GetMetarInfo(String station) throws IOException, JSONException {
        /*Pulling METAR information from API and formatting into a JSON String
         */

        String stationInfo = null;
        //format: https://avwx.rest/api/metar/KLAX?format=json&token=65H43GKqAoSvN89VGZ5WD-z26OpDn9PGCU3NMFHb3e4
        String urlFormed = "https://avwx.rest/api/metar/" + station + "?format=json&token=" + authToken;

        URL getStationRequest = new URL(urlFormed);

        HttpURLConnection connection = (HttpURLConnection) getStationRequest.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((stationInfo = in.readLine()) != null) {
                response.append(stationInfo);
            } in .close();

            stationInfo = response.toString();
        } else {
            System.out.println("Error Fetching Data");
        }

        JSONObject stationJson = new JSONObject(stationInfo);

        return stationJson;
    }


}
