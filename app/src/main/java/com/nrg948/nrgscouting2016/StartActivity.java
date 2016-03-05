package com.nrg948.nrgscouting2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StartActivity extends AppCompatActivity {
    private int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(hasMode()){
            myOnClick(mode);
            return;
        }
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button client = (Button)findViewById(R.id.client_button);
        Button host = (Button)findViewById(R.id.host_button);
        client.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myOnClick(0);
            }
        });
        host.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myOnClick(1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void myOnClick(int mode){
        Intent i = new Intent(this, TopActivity.class);
        i.putExtra("Mode", mode);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private Activity getActivity(){
        return this;
    }

    private boolean hasMode(){
        String s = "";
        try{
            FileInputStream fileIn=openFileInput("devicemode.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            char[] inputBuffer= new char[100000];
            int charRead;
            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
               // Log.d("StartActivity", "HI"+s+"OH");
            }
            if(s.equals("1")){
                mode = 1;
            }else{
                mode = 0;
            }
            InputRead.close();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
