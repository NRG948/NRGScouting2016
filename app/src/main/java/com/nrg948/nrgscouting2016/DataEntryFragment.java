package com.nrg948.nrgscouting2016;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DataEntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DataEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataEntryFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    public static Team team;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data_entry, container, false);
        team = new Team();
        Spinner spinner = (Spinner) v.findViewById(R.id.climb_spinner);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.climb_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        v.findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });
        final EditText teamNumberText = (EditText)v.findViewById(R.id.team_number);
        final EditText numberOfBoulders = (EditText)v.findViewById(R.id.number_of_boulders);
        final EditText exceptionalCircumstances = (EditText)v.findViewById(R.id.exceptional_circumstances);
        final EditText comments = (EditText)v.findViewById(R.id.comments);
        v.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team.teamNumber = Integer.parseInt(teamNumberText.getText().toString());
                team.numberOfBoulders = Integer.parseInt(numberOfBoulders.getText().toString());
                team.exceptionalCircumstances = exceptionalCircumstances.getText().toString();
                team.comments = comments.getText().toString();
                for(Team team1 : MenuFragment.teams){
                    if(team1.teamNumber == team.teamNumber){
                        MenuFragment.teams.remove(team1);
                    }
                    MenuFragment.teams.add(team);
                }
                Collections.sort(MenuFragment.teams, new Comparator<Team>() {
                    @Override
                    public int compare(Team lhs, Team rhs) {
                        return lhs.teamNumber < rhs.teamNumber ? -1 : (lhs == rhs ? 0 : 1);
                    }
                });
                MenuFragment.numberOfEntries.setText("Number of Entires " + MenuFragment.teams.size());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return v;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) return;
        team.speedOfClimb = Team.SpeedOfClimb.values()[position-1];
        Log.d("DataEntryFragment", ""+team.speedOfClimb.getSpeed());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
