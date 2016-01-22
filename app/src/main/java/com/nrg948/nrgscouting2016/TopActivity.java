package com.nrg948.nrgscouting2016;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TopActivity extends FragmentActivity {
    public static int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getIntent().getIntExtra("Mode",0);
        setContentView(R.layout.activity_top);

    }
}
