package com.example.metarutility.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.metarutility.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.example.metarutility.utils.Constants.MAPVIEW_BUNDLE_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AirportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AirportFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    GoogleMap map;
    MapView mapView;
    EditText inputText;

    public AirportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_airport, container, false);
        Button searchAirportButton = (Button) view.findViewById(R.id.airportSearchButton);
        searchAirportButton.setOnClickListener(this);
        inputText = (EditText) view.findViewById(R.id.airportSearchInput);

        mapView = (MapView) view.findViewById(R.id.mapView);

        initializeMap(savedInstanceState);

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
    
    // TODO: Rename and change types and number of parameters
    public static AirportFragment newInstance(String param1, String param2) {
        AirportFragment fragment = new AirportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void initializeMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);

    }

    public void setMapView(String latCoordinate, String longCoordinate) {
        //Setting camera view for MapView
        String coordinates = latCoordinate + "," + longCoordinate;
        String[] latlong = coordinates.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        LatLng location = new LatLng(latitude, longitude);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        hideSoftKeyboard(Objects.requireNonNull(getActivity()));

        //hide keyboard upon button press
        hideSoftKeyboard(Objects.requireNonNull(getActivity()));

        //Get input from search text input
        String input = inputText.getText().toString();

        JSONObject stationInfo;
        MetarApi apiCall = new MetarApi();



        try {
            stationInfo = apiCall.GetStationInfo(input);

            TextView airportNameTextView = (TextView) view.findViewById(R.id.airportNameTextView);

            //Setting up variables to load into TextView later
            JSONArray runwayArray;
            JSONObject runwayObject;

            String airportName;
            String icaoCode;
            String latitude;
            String longitude;
            String city;
            String country;
            String elevationFeet;
            String elevationMeters;
            String runways = "";
            String airportType;
            String website;
            String wiki;
            String notes;

            //Check if API call is successful
            if (stationInfo != null) {

                icaoCode = stationInfo.getString("icao");
                airportName = stationInfo.getString("name");
                latitude = stationInfo.getString("latitude");
                longitude = stationInfo.getString("longitude");
                city = stationInfo.getString("city");
                country = stationInfo.getString("country");
                elevationFeet = stationInfo.getString("elevation_ft");
                elevationMeters = stationInfo.getString("elevation_m");
                airportType = stationInfo.getString("type");
                website = stationInfo.getString("website");
                wiki = stationInfo.getString("wiki");

                //Setting up TextViews
                TextView coordinatesTextView = (TextView)
                        view.findViewById(R.id.coordinatesTextView);
                TextView locationTextView = (TextView) view.findViewById(R.id.locationTextView);
                TextView elevationTextView = (TextView) view.findViewById(R.id.elevationTextView);
                TextView runwayTextView = (TextView) view.findViewById(R.id.runwayTextView);
                TextView websiteTextView = (TextView) view.findViewById(R.id.websiteTextView);
                TextView wikiTextView = (TextView) view.findViewById(R.id.wikiTextView);
                TextView airportTypeTextView =
                        (TextView) view.findViewById(R.id.airportTypeTextView);

                //Set map views
                setMapView(latitude, longitude);

                //handling runway parsing
                runwayArray = stationInfo.getJSONArray("runways");
                String length;
                String width;
                String surface;
                boolean lights;
                String ident1;
                String ident2;

                for (int i = 0; i < runwayArray.length(); i++) {
                    runwayObject = runwayArray.getJSONObject(i);
                    length = runwayObject.getString("length_ft");
                    width = runwayObject.getString("width_ft");
                    surface = runwayObject.getString("surface");
                    lights = runwayObject.getBoolean("lights");
                    ident1 = runwayObject.getString("ident1");
                    ident2 = runwayObject.getString("ident2");

                    String runwayInfoLine = ident1 + " / " + ident2 + ": \n";
                    runwayInfoLine += "Length: " + length + " feet\n";
                    runwayInfoLine += "Width: " + width + " feet.\n";
                    runwayInfoLine += "Surface Type: " + surface + "\n";
                    if (lights == true) {
                        runwayInfoLine += "Lights: Yes\n";
                    }
                    else {
                        runwayInfoLine += "Lights: No\n";
                    }

                    runwayInfoLine += "\n";

                    runways += runwayInfoLine;

                }

                //Create links:
                String airportLinkText = "<a href='" + website + "'>Airport Website</a>" ;
                String wikiLinkText = "<a href='" + wiki + "'>Wikipedia Page</a>";

                //Filling TextViews with API information
                airportNameTextView.setText(icaoCode + " - " + airportName + "\n ");
                coordinatesTextView.setText("\nCoordinates: " + latitude + longitude + "\n");
                locationTextView.setText("City: " + city + ", " + country + "\n");
                elevationTextView.setText("Elevation: " + elevationFeet + " feet, "
                        + elevationMeters + " meters \n");
                runwayTextView.setText("Runway Information: \n \n" + runways);
                websiteTextView.setText(Html.fromHtml(airportLinkText));
                websiteTextView.setMovementMethod(LinkMovementMethod.getInstance());
                wikiTextView.setText(Html.fromHtml(wikiLinkText));
                wikiTextView.setMovementMethod(LinkMovementMethod.getInstance());

            }
            else {
                //If API call results in a null JSONObject:
                System.out.println("ERROR bad ICAO Code");
                airportNameTextView.setText("Error: ICAO Code is invalid. Please try again. \n");
            }

        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}