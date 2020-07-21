package com.metarutility.metarutility.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.metarutility.metarutility.R;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import android.widget.TextView;

public class MetarFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    EditText inputText;

    public MetarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_metar, container, false);
        Button searchMetarButton = (Button) view.findViewById(R.id.metarSearchButton);
        searchMetarButton.setOnClickListener(this);

        inputText = (EditText) view.findViewById(R.id.searchInput);
        return view;

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(
                Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        //hide keyboard upon button press
        hideSoftKeyboard(Objects.requireNonNull(getActivity()));

        //Get input from search text input
        String input = inputText.getText().toString();

        String uppercase = input.toUpperCase();
        inputText.setText(uppercase, TextView.BufferType.EDITABLE);

        JSONObject metarInfo;
        JSONObject stationInfo;
        MetarApi apiCall = new MetarApi();
        try {
            metarInfo = apiCall.GetMetarInfo(input);
            stationInfo = apiCall.GetStationInfo(input);

            //Setting up variables to load into TextView later
            JSONObject timeObject;
            JSONObject windSpeedObject;
            JSONObject windDirectionObject;
            JSONObject windGustObject = null;
            JSONObject visibilityObject;
            JSONObject temperatureObject;
            JSONObject dewpointObject;
            JSONObject altimeterObject;
            JSONObject unitsObject;

            JSONArray rvrArray;
            JSONArray wxArray;
            JSONArray cloudArray;

            String icaoCode = null;
            String airportName = null;
            String metar = null;
            String time = null;
            String flightRules = null;
            String windSpeed = null;
            String windDirection = null;
            String gusts = null;
            String gustCheck = null;
            String visibility = null;
            String rvrText = "";
            String wxText = "";
            String cloudText = "";
            String cloudType = null;
            String cloudHeight = null;
            String dewpoint = null;
            String temperature = null;
            String altimeter = null;
            String remarks = null;
            String visibilityUnits = null;
            String altimeterUnits = null;

            TextView airportNameTextView = view.findViewById(R.id.airportNameTextView);

            //if API call is not valid, metarInfo will be null
            if (metarInfo != null && stationInfo != null) {

                //for nested JSON values
                windSpeedObject = metarInfo.getJSONObject("wind_speed");
                timeObject = metarInfo.getJSONObject("time");
                windDirectionObject = metarInfo.getJSONObject("wind_direction");
                visibilityObject = metarInfo.getJSONObject("visibility");
                altimeterObject = metarInfo.getJSONObject("altimeter");
                temperatureObject = metarInfo.getJSONObject("temperature");
                dewpointObject = metarInfo.getJSONObject("dewpoint");
                unitsObject = metarInfo.getJSONObject("units");
                
                //this JSONObject can be null. Must check if it is null
                gustCheck = metarInfo.getString("wind_gust");
                if (gustCheck.equals("null")) {
                    gusts = null;
                }
                else {
                    windGustObject = metarInfo.getJSONObject("wind_gust");
                }

                //Setting variable values from API
                icaoCode = metarInfo.getString("station");
                airportName = stationInfo.getString("name");
                metar = metarInfo.getString("raw");
                time = timeObject.getString("dt");
                flightRules = metarInfo.getString("flight_rules");
                windSpeed = windSpeedObject.getString("repr");
                windDirection = windDirectionObject.getString("repr");
                visibility = visibilityObject.getString("repr");
                rvrArray = metarInfo.getJSONArray("runway_visibility");
                wxArray = metarInfo.getJSONArray("wx_codes");
                cloudArray = metarInfo.getJSONArray("clouds");
                remarks = metarInfo.getString("remarks");
                temperature = temperatureObject.getString("value");
                dewpoint = dewpointObject.getString("value");
                altimeter = altimeterObject.getString("value");
                visibilityUnits = unitsObject.getString("visibility");
                altimeterUnits = unitsObject.getString("altimeter");

                //check to see if windGustObject is not null to prevent error
                if (windGustObject != null) {
                    gusts = windGustObject.getString("repr");
                }
                else {
                    gusts = null;
                }

                //setting up Category Labels
                TextView windLabelTextView = view.findViewById(R.id.windLabelTextView);
                TextView visibilityLabelTextView = view.findViewById(R.id.visibilityLabelTextView);
                TextView weatherLabelTextView = view.findViewById(R.id.weatherLabelTextView);
                TextView cloudLabelTextView = view.findViewById(R.id.cloudLabelTextView);
                TextView tempLabelTextView = view.findViewById(R.id.tempLabelTextView);
                TextView altimeterLabelTextView = view.findViewById(R.id.altimeterLabelTextView);
                TextView remarksLabelTextView = view.findViewById(R.id.remarksLabelTextView);

                //Setting up TextViews
                TextView metarTextView = view.findViewById(R.id.metarTextView);
                TextView timeTextView = view.findViewById(R.id.timeTextView);
                TextView flightRuleTextView = view.findViewById(R.id.flightRuleTextView);
                TextView reportModifierTextView = view.findViewById(R.id.reportModifierTextView);
                TextView windInfoTextView = view.findViewById(R.id.windInfoTextView);
                TextView visibilityTextView = view.findViewById(R.id.visibilityTextView);
                TextView rvrTextView = view.findViewById(R.id.rvrTextView);
                TextView wxTextView = view.findViewById(R.id.wxTextView);
                TextView cloudTextView = view.findViewById(R.id.cloudTextView);
                TextView tempTextView = view.findViewById(R.id.tempTextView);
                TextView altimeterTextView = view.findViewById(R.id.altimeterTextView);
                TextView remarksTextView = view.findViewById(R.id.remarksTextView);

                //Filling Category Labels
                windLabelTextView.setText("Wind");
                visibilityLabelTextView.setText("Visibility");
                weatherLabelTextView.setText("Weather");
                cloudLabelTextView.setText("Cloud Conditions");
                tempLabelTextView.setText("Temperature");
                altimeterLabelTextView.setText("Altimeter");
                remarksLabelTextView.setText("Remarks");

                //Filling TextViews with API information
                airportNameTextView.setText(icaoCode + " - " + airportName + "\n ");
                metarTextView.setText(metar + "\n ");
                timeTextView.setText("Time: " + time +"\n ");
                flightRuleTextView.setText("Flight Rules: " + flightRules + "\n ");
                remarksTextView.setText(remarks + "\n");
                tempTextView.setText("Temperature: " + temperature + "°C \n \n" +
                        "Dewpoint: " + dewpoint + "°C \n");
                altimeterTextView.setText("Altimeter: " + altimeter + " "
                        + altimeterUnits + " \n");

                if (visibility.equals("CAVOK")) {
                    visibilityTextView.setText("CAVOK - Ceiling and Visibility OK\n" +
                            "Visibility greater than 10 km " +
                            "\nNo clouds of operational significance \n ");
                }
                else {
                    visibilityTextView.setText("Visibility: "
                            + visibility + " " + visibilityUnits + "\n");
                }


                //Formatting Wind Conditions in text view
                if (windDirection.equals("VRB") && gusts == null) {
                    windInfoTextView.setText("Winds variable at " + windSpeed +
                            " knots. \n \nNo wind gusts.\n ");
                }
                else if (windDirection.equals("VRB")) {
                    windInfoTextView.setText("Winds variable at "
                            + windSpeed + " knots.\n \nGusts up to " + gusts + " knots.\n ");
                }
                else if (gusts == null) {
                    windInfoTextView.setText("Winds at " + windSpeed +
                            " knots from " + windDirection + " degrees. \n \nNo wind gusts.\n ");
                }
                else {
                    windInfoTextView.setText("Winds at " + windSpeed +
                            " knots from " + windDirection
                            + " degrees. \n \nGusts up to " + gusts + " knots.\n ");
                }

                //check if RVR is empty.
                if (rvrArray.length() == 0) {
                    rvrTextView.setText("Runway Visual Range: No RVR Reported \n ");
                }
                else {
                    for (int i = 0; i < rvrArray.length(); i++){
                        rvrText = rvrText + (rvrArray.getString(i)) + "\n ";
                    }
                    rvrTextView.setText("Runway Visual Range: \n" +  rvrText);
                }

                //check if wxArray is empty
                if (wxArray.length() == 0) {
                    wxTextView.setText("No Weather Phenomena Reported \n ");
                }
                else {
                    JSONObject wxObject;
                    String stringContainer;
                    for (int i = 0; i < wxArray.length(); i++){
                        wxObject = wxArray.getJSONObject(i);
                        stringContainer = wxObject.getString("value");
                        wxText = wxText + stringContainer + "\n";
                    }
                    wxTextView.setText("Weather Conditions: \n" + wxText);
                }

                //Check if automated weather report
                String[] metarArray = metar.split(" ");
                boolean automated = false;

                for (int i = 0; i < metarArray.length; i++) {
                    if (metarArray[i].equals("AUTO")) {
                        automated = true;
                        break;
                    }
                }

                if (automated) {
                    reportModifierTextView.setText("Fully Automated Report: Yes\n");
                }
                else {
                    reportModifierTextView.setText("Fully Automated Report: No\n");
                }

                //check if cloudArray is empty
                if (cloudArray.length() == 0) {
                    cloudTextView.setText("No Clouds Reported \n");
                }
                else {
                    JSONObject cloudObject;
                    for (int i = 0; i < cloudArray.length(); i++) {
                        cloudObject = cloudArray.getJSONObject(i);
                        cloudType = cloudObject.getString("type");
                        cloudHeight = cloudObject.getString("altitude");

                        switch (cloudType) {
                            case "CLR":
                                cloudText = cloudText + "No clouds detected below 12000 feet \n";
                                break;
                            case "SCT":
                                cloudText = cloudText + "Scattered clouds at "
                                        + cloudHeight + "00 feet\n";
                                break;
                            case "FEW":
                                cloudText = cloudText + "Few clouds at "
                                        + cloudHeight + "00 feet\n";
                                break;
                            case "BKN":
                                cloudText = cloudText + "Broken clouds at "
                                        + cloudHeight + "00 feet\n";
                                break;
                            case "OVC":
                                cloudText = cloudText + "Overcast at " + cloudHeight + "00 feet\n";
                                break;
                            case "VV":
                                cloudText = cloudText + "Vertical Visibility at "
                                        + cloudHeight + "00 feet\n";
                                break;
                        }
                    }

                    //print to cloudTextView:
                    cloudTextView.setText(cloudText);
                }

            }
            else {
                //If API call results in a null JSONObject
                System.out.println("ERROR bad ICAO Code");
                airportNameTextView.setText("Error: ICAO Code is invalid. Please try again.\n");

            }

        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}