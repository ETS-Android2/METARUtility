package com.metarutility.metarutility.ui.main;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.metarutility.metarutility.R;
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

import static com.metarutility.metarutility.utils.Constants.MAPVIEW_BUNDLE_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AirportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AirportFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

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

        //Setting up button to change between default and satellite view
        ToggleButton mapToggle = (ToggleButton) view.findViewById(R.id.toggleButton);
        mapToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setSatelliteMap();
                }
                else {
                    setStandardMap();
                }
            }
        });


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

    public static AirportFragment newInstance(String param1, String param2) {
        AirportFragment fragment = new AirportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void initializeMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);

    }

    public void setMapView(String latCoordinate, String longCoordinate, String type) {
        //Setting camera view for MapView, set view to coordinates and sets zoom view
        String coordinates = latCoordinate + "," + longCoordinate;
        String[] latlong = coordinates.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        LatLng location = new LatLng(latitude, longitude);

        switch(type) {

            case ("small_airport"):
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, (float)14.9));
                break;

            case ("large_airport"):
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
                break;

            case ("medium_airport"):
            default:
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
    }

    public void setStandardMap() {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void setSatelliteMap() {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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

        //hides keyboard upon button press
        hideSoftKeyboard(Objects.requireNonNull(getActivity()));

        //Get input from search text input
        String input = inputText.getText().toString();
        String uppercase = input.toUpperCase();
        inputText.setText(uppercase, TextView.BufferType.EDITABLE);

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
                notes = stationInfo.getString("note");

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
                TextView notesTextView = (TextView) view.findViewById(R.id.notesTextView);
                TextView runwayLabelTextView =
                        (TextView) view.findViewById(R.id.runwayLabelTextView);

                //Set map views
                setMapView(latitude, longitude, airportType);

                //Handling runway information parsing from JSONArray
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

                    if (lights) {
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
                coordinatesTextView.setText("\nCoordinates: " + latitude + ", " + longitude);
                locationTextView.setText("City: " + city + ", " + country);
                elevationTextView.setText("Elevation: " + elevationFeet + " feet / "
                        + elevationMeters + " meters");
                runwayTextView.setText(runways);
                websiteTextView.setText(Html.fromHtml(airportLinkText));
                websiteTextView.setMovementMethod(LinkMovementMethod.getInstance());
                wikiTextView.setText(Html.fromHtml(wikiLinkText));
                wikiTextView.setMovementMethod(LinkMovementMethod.getInstance());
                runwayLabelTextView.setText("\nRunway Information");

                if (!notes.equals("null")) {
                    notesTextView.setText("Notes: " + notes + "\n");
                }
                else {
                    notesTextView.setText("");
                }

                //Airport Type Parsing
                switch (airportType) {
                    case ("small_airport"):
                        airportTypeTextView.setText("Airport Type: Small Airport");
                        break;

                    case ("medium_airport"):
                        airportTypeTextView.setText("Airport Type: Mid-Size Airport");
                        break;

                    case ( "large_airport"):
                        airportTypeTextView.setText("Airport Type: Large Airport");
                        break;

                    default:
                        airportTypeTextView.setText("");
                }

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