package com.nrg948.nrgscouting2016;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class DataFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<Team> teams;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teamitem_list, container, false);
        ListView list = (ListView)view.findViewById(R.id.data_list);
        teams = MenuFragment.teams;
        Team[] t =new Team[teams.size()];
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getContext(), teams.toArray(t));
        list.setAdapter(adapter);
        return view;
    }
    private class MySimpleArrayAdapter extends ArrayAdapter<Team>{
        Context context;
        Team[] values;
        public MySimpleArrayAdapter(Context context, Team[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.fragment_teamitem, parent, false);
            final Team team = values[position];
            ((TextView)rowView.findViewById(R.id.team_number)).setText(""+team.teamNumber);
            ((TextView)rowView.findViewById(R.id.number_of_boulders)).setText(""+team.numberOfBoulders);
            ((TextView)rowView.findViewById(R.id.method_of_scoring)).setText(team.methodOfScoring.getMethod());
            ((TextView)rowView.findViewById(R.id.method_of_shooting)).setText(team.methodOfShooting.getMethod());
            ((TextView)rowView.findViewById(R.id.speed_of_climb)).setText(""+team.speedOfClimb.getSpeed());
            ((TextView)rowView.findViewById(R.id.comments)).setText(team.comments);
            ((TextView)rowView.findViewById(R.id.exceptional_circumstances)).setText(team.exceptionalCircumstances);
            ((CheckBox)rowView.findViewById(R.id.cheval_de_frise)).setChecked(team.defenses[0]);
            ((CheckBox)rowView.findViewById(R.id.drawbridge)).setChecked(team.defenses[1]);
            ((CheckBox)rowView.findViewById(R.id.low_bar)).setChecked(team.defenses[2]);
            ((CheckBox)rowView.findViewById(R.id.moat)).setChecked(team.defenses[3]);
            ((CheckBox)rowView.findViewById(R.id.portcullis)).setChecked(team.defenses[4]);
            ((CheckBox)rowView.findViewById(R.id.ramparts)).setChecked(team.defenses[5]);
            ((CheckBox)rowView.findViewById(R.id.rough_terrain)).setChecked(team.defenses[6]);
            ((CheckBox)rowView.findViewById(R.id.sally_port)).setChecked(team.defenses[7]);
            ((CheckBox)rowView.findViewById(R.id.stone_wall)).setChecked(team.defenses[8]);
            ((Button)rowView.findViewById(R.id.edit_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataEntryFragment newFragment = new DataEntryFragment();
                    newFragment.setTeam(team);
                    TopActivity.replaceFragment(getFragment(), newFragment);
                }
            });
            return rowView;
        }
    }
    Fragment getFragment(){
        return this;
    }
}
