package com.example.metarutility.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.metarutility.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MetarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MetarFragment extends Fragment implements View.OnClickListener {

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

    @Override
    public void onClick(View v) {
        System.out.println("Button clicked");
        Log.i("METARUtility", "Button Clicked");

        //Get input from search text input
        String input = inputText.getText().toString();

        JSONObject metarInfo;
        MetarApi apiCall = new MetarApi();
        try {
            metarInfo = apiCall.GetMetarInfo(input);
            String station = metarInfo.getString("station");

            System.out.println(station);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}