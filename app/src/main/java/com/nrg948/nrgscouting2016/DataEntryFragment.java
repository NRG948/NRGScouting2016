package com.nrg948.nrgscouting2016;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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
    private boolean newTeam = true;
    public CheckBox[] defenses;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data_entry, container, false);
        if(newTeam)
            team = new Team();
        defenses = new CheckBox[] {(CheckBox)v.findViewById(R.id.cheval_de_frise), (CheckBox)v.findViewById(R.id.drawbridge), (CheckBox)v.findViewById(R.id.low_bar),
                (CheckBox)v.findViewById(R.id.moat), (CheckBox)v.findViewById(R.id.portcullis), (CheckBox)v.findViewById(R.id.ramparts),
                (CheckBox)v.findViewById(R.id.rough_terrain), (CheckBox)v.findViewById(R.id.sally_port), (CheckBox)v.findViewById(R.id.stone_wall)};
        for(int i = 0; i < 9; i ++){
           if(!newTeam) defenses[i].setChecked(team.defenses[i]);
        }
        Spinner spinner = (Spinner) v.findViewById(R.id.climb_spinner);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.climb_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(!newTeam)spinner.setSelection(6 - team.speedOfClimb.getSpeed());
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
        if(!newTeam){
            teamNumberText.setText("" + team.teamNumber);
            numberOfBoulders.setText("" + team.numberOfBoulders);
            exceptionalCircumstances.setText("" + team.exceptionalCircumstances);
            comments.setText(team.comments);
            String s = team.methodOfScoring.getMethod();
            RadioButton scoringButton = new RadioButton(getContext());
            switch (s){
                case "Shooting":
                    scoringButton = (RadioButton)v.findViewById(R.id.radio_shooting);
                    break;
                case "Damaing":
                    scoringButton = (RadioButton)v.findViewById(R.id.radio_damaging);
                    break;
                case "Both":
                    scoringButton = (RadioButton)v.findViewById(R.id.radio_both);
                    break;
            }
            scoringButton.toggle();
            RadioButton shootingButton = new RadioButton(getContext());
            s = team.methodOfShooting.getMethod();
            switch (s){
                case "High":
                    shootingButton = (RadioButton)v.findViewById(R.id.radio_high);
                    break;
                case "Low":
                    shootingButton = (RadioButton)v.findViewById(R.id.radio_low);
                    break;
                case "High and Low":
                    shootingButton = (RadioButton)v.findViewById(R.id.radio_high_and_low);
                    break;
                case "Neither":
                    shootingButton = (RadioButton)v.findViewById(R.id.radio_neither);
                    break;
            }
            shootingButton.toggle();

        }
        Button submitButton = (Button)v.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DataEntryFragment", "hi");
                try {
                    team.teamNumber = Integer.parseInt(teamNumberText.getText().toString());
                    team.numberOfBoulders = Integer.parseInt(numberOfBoulders.getText().toString());
                    if (team.speedOfClimb.equals(Team.SpeedOfClimb.AVERAGE)){}
                }catch (Exception e){
                    Snackbar.make(v, "Not all the information has been filled", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    team = new Team();
                    return;
                }
                team.exceptionalCircumstances = exceptionalCircumstances.getText().toString();
                team.comments = comments.getText().toString();
                for(int i = 0; i < MenuFragment.teams.size(); i ++) {
                    Team team1 = MenuFragment.teams.get(i);
                    if (team1.teamNumber == team.teamNumber) {
                        MenuFragment.teams.remove(team1);
                        return;
                    }
                }
                MenuFragment.teams.add(team);
                Collections.sort(MenuFragment.teams, new Comparator<Team>() {
                    @Override
                    public int compare(Team lhs, Team rhs) {
                        return lhs.teamNumber < rhs.teamNumber ? -1 : (lhs == rhs ? 0 : 1);
                    }
                });
                MenuFragment.numberOfEntries.setText("Number of Entires " + MenuFragment.teams.size());
                for (int i = 0; i < 9; i++) {
                    team.defenses[i] = defenses[i].isChecked();
                }
                getActivity().getSupportFragmentManager().popBackStack(getFragment().getClass().getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

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

    private DataEntryFragment getFragment(){
        return this;
    }

    public void setTeam(Team team){
        this.team = team;
        newTeam = false;
    }

    private String formatString(String s){
        s = s.toLowerCase();
        s = s.replace(" ", "_");
        s = "radio_" + s;
        return s;
    }

}
