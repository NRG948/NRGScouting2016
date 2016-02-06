package com.nrg948.nrgscouting2016;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    public Team newTeam;
    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        newTeam = new Team();
        v.findViewById(R.id.new_entry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataEntryFragment newFragment = new DataEntryFragment();
                TopActivity.replaceFragment(getFragment(),newFragment);
            }
        });
        return v;

    }
    private Fragment getFragment(){
        return this;
    }

}
