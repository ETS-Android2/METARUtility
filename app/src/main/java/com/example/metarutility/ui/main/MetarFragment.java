package com.example.metarutility.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.metarutility.R;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.widget.TextView;

public class MetarFragment<policy> extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    EditText inputText;

    public MetarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MetarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetarFragment newInstance(String param1, String param2) {
        MetarFragment fragment = new MetarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

        /*default:
        //return inflater.inflate(R.layout.fragment_metar, container, false);
        */
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        //hide keyboard upon button press
        hideSoftKeyboard(getActivity());
        
        System.out.println("Button clicked");
        Log.i("METARUtility", "Button Clicked");

        //Get input from search text input
        String input = inputText.getText().toString();

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

                if (gustCheck == "null") {
                    gusts = null;
                }
                else {
                    windGustObject = metarInfo.getJSONObject("wind_gust");
                }

                //Setting variable values from API
                icaoCode = metarInfo.getString("station");
                airportName = stationInfo.getString("name");
                metar = metarInfo.getString("sanitized");
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

                //Filling TextViews with API information
                airportNameTextView.setText(icaoCode + " - " + airportName + "\n ");
                metarTextView.setText(metar);
                timeTextView.setText("Time: " + time +"\n ");
                flightRuleTextView.setText("Flight Rules: " + flightRules + "\n ");
                remarksTextView.setText("Remarks: \n" + remarks);
                tempTextView.setText("Temperature: " + temperature + "°C \n" +
                        "Dewpoint: " + dewpoint + "°C \n");
                altimeterTextView.setText("Altimeter: " + altimeter + " "
                        + altimeterUnits + " \n");

                if (visibility.equals("CAVOK")) {
                    visibilityTextView.setText("CAVOK - Ceiling and Visibility OK\n" +
                            "Visibility greater than 10 km " +
                            "\nNo clouds of operational significance \n ");
                }
                else {
                    visibilityTextView.setText("Visibility: " + visibility + " " + visibilityUnits);
                }


                //Formatting Wind Conditions in text view
                if (windDirection.equals("VRB") && gusts == null) {
                    windInfoTextView.setText("Winds variable at " + windSpeed +
                            " knots. \nNo wind gusts.\n ");
                }
                else if (windDirection.equals("VRB") && gusts != null) {
                    windInfoTextView.setText("Winds variable at "
                            + windSpeed + " knots. \nGusts up to " + gusts + " knots.\n ");
                }
                else if (!windDirection.equals("VRB") && gusts == null) {
                    windInfoTextView.setText("Winds at " + windSpeed +
                            " knots from " + windDirection + " degrees. \nNo wind gusts.\n ");
                }
                else {
                    windInfoTextView.setText("Winds at " + windSpeed +
                            " knots from " + windDirection
                            + " degrees. \nGusts up to " + gusts + " knots.\n ");
                }

                //check if RVR is empty.
                if (rvrArray.length() == 0) {
                    rvrTextView.setText("Runway Visual Range: No RVR Reported \n ");
                }
                else {
                    for (int i = 0; i < rvrArray.length(); i++){
                        rvrText = rvrText + (rvrArray.getString(i)) + "\n ";
                    }
                    rvrTextView.setText("Runway Visual Range: " +  rvrText);
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

                //check if cloudArray is empty
                if (cloudArray.length() == 0) {
                    cloudTextView.setText("No Clouds Reported");
                }
                else {
                    JSONObject cloudObject;
                    for (int i = 0; i < cloudArray.length(); i++) {
                        cloudObject = cloudArray.getJSONObject(i);
                        cloudType = cloudObject.getString("type");
                        cloudHeight = cloudObject.getString("altitude");

                        if (cloudType == "CLR") {
                            cloudText = cloudText + "No clouds detected below 12000 feet \n";
                        }
                        else if (cloudType.equals("SCT")) {
                            cloudText = cloudText + "Scattered clouds at "
                                    + cloudHeight + "00 feet\n";
                        }
                        else if (cloudType.equals("FEW")) {
                            cloudText = cloudText + "Few clouds at " + cloudHeight + "00 feet\n";
                        }
                        else if (cloudType.equals("BKN")) {
                            cloudText = cloudText + "Broken clouds at "
                                    + cloudHeight + "00 feet\n";
                        }
                        else if (cloudType.equals("OVC")) {
                            cloudText = cloudText + "Overcast at " + cloudHeight + "00 feet\n";
                        }
                        else if (cloudType.equals("VV")) {
                            cloudText = cloudText + "Vertical Visibility at "
                                    + cloudHeight + "00 feet\n";
                        }

                    }
                    //print to cloudTextView:
                    cloudTextView.setText("Cloud Conditions: \n" + cloudText);
                }


            }
            else {
                System.out.println("ERROR bad ICAO Code");
                airportNameTextView.setText("Error: ICAO Code is invalid. Please try again.");

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}