package com.nrg948.nrgscouting2016;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }
    public static ArrayList<Team> teams = new ArrayList<Team>();
    public static TextView numberOfEntries;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        ((TextView)v.findViewById(R.id.device_mode)).setText((TopActivity.mode == 1)? "Host" : "Client");
        v.findViewById(R.id.new_entry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataEntryFragment newFragment = new DataEntryFragment();
                TopActivity.replaceFragment(getFragment(), newFragment);
            }
        });
        v.findViewById(R.id.all_entries_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataFragment newFragment = new DataFragment();
                TopActivity.replaceFragment(getFragment(), newFragment);
            }
        });
        v.findViewById(R.id.sync_buton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlueToothFragment newFragment = new BlueToothFragment();
                TopActivity.replaceFragment(getFragment(), newFragment);
            }
        });
        numberOfEntries= ((TextView)v.findViewById(R.id.number_of_entires));
        numberOfEntries.setText("Number of Entires " + teams.size());
        saveData();
        return v;

    }
    private Fragment getFragment(){
        return this;
    }

    public void saveData(){
        try {
            FileOutputStream fileout = getActivity().openFileOutput("mytextfile.txt", getActivity().MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(serializeData());
            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String serializeData(){
        String serialized = "";
        for(Team t : teams){
            serialized += t.serialize();
        }
        return serialized;
    }
}
