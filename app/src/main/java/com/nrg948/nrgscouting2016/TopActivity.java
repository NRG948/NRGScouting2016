package com.nrg948.nrgscouting2016;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class TopActivity extends FragmentActivity {
    public static int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getIntent().getIntExtra("Mode",0);
        final int fragmentContainer = android.R.id.content;
        MenuFragment fragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().replace(fragmentContainer, (Fragment) fragment).commit();

    }
    public static void replaceFragment(Fragment currentFragment, Fragment newFragment) {
        replaceFragment(currentFragment.getFragmentManager(), ((ViewGroup) currentFragment.getView().getParent()).getId(), newFragment);
    }
    public static void replaceFragment(FragmentManager fm, int parentViewId, Fragment newFragment)
    {

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(parentViewId, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton)v).isChecked();
        switch (v.getId()){
            case R.id.radio_damaging:
                if(checked)
                    DataEntryFragment.team.methodOfScoring = Team.MethodOfScoring.DAMAGING;
                break;
            case R.id.radio_shooting:
                if(checked)
                    DataEntryFragment.team.methodOfScoring = Team.MethodOfScoring.SHOOTING;
                break;
            case R.id.radio_both:
                if(checked)
                    DataEntryFragment.team.methodOfScoring = Team.MethodOfScoring.BOTH;
                break;
            case R.id.radio_high:
                if(checked)
                    DataEntryFragment.team.methodOfShooting = Team.MethodOfShooting.HIGH;
                break;
            case R.id.radio_low:
                if(checked)
                    DataEntryFragment.team.methodOfShooting = Team.MethodOfShooting.LOW;
                break;
            case R.id.radio_high_and_low:
                if(checked)
                    DataEntryFragment.team.methodOfShooting = Team.MethodOfShooting.HIGH_AND_LOW;
                break;
            case R.id.radio_neither:
                if(checked)
                    DataEntryFragment.team.methodOfShooting = Team.MethodOfShooting.NEITHER;
                break;

        }
    }
}
