package com.nrg948.nrgscouting2016;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        v.findViewById(R.id.new_entry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataEntryFragment newFragment = new DataEntryFragment();
                TopActivity.replaceFragment(getFragment(),newFragment);
            }
        });
        numberOfEntries= ((TextView)v.findViewById(R.id.number_of_entires));
        numberOfEntries.setText("Number of Entires " + teams.size());
        return v;

    }
    private Fragment getFragment(){
        return this;
    }

}
