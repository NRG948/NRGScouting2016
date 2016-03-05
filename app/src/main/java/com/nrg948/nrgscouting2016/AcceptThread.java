package com.nrg948.nrgscouting2016;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Sean on 3/4/2016.
 */
public class AcceptThread extends Thread {
    private BluetoothSocket bTSocket;
    BluetoothAdapter bTAdapter;
    UUID mUUID;
    Context context;
    public AcceptThread(BluetoothAdapter bTAdapter, UUID mUUID, Context context) {
        this.bTAdapter = bTAdapter;
        this.mUUID = mUUID;
        this.context = context;
    }

    public void run() {
        BluetoothServerSocket temp = null;
        try {
            temp = bTAdapter.listenUsingRfcommWithServiceRecord("Service_Name", mUUID);
        } catch(IOException e) {
            Log.d("SERVERCONNECT", "Could not get a BluetoothServerSocket:" + e.toString());
        }
        while(true) {
            try {
                Log.d("AcceptThread", "Trying to Accept");
                bTSocket = temp.accept();
            } catch (IOException e) {
                Log.d("SERVERCONNECT", "Could not accept an incoming connection.");
                break;
            }
            if (bTSocket != null) {
                try {
                    temp.close();
                    Toast.makeText(context, "Successfully Paired with " + bTSocket.getRemoteDevice().getName() ,
                            Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.d("SERVERCONNECT", "Could not close ServerSocket:" + e.toString());
                }
                break;
            }
        }
    }

    public void cancel() {
        try {
            Toast.makeText(context, "Disconnected with " + bTSocket.getRemoteDevice().getName() ,
                    Toast.LENGTH_LONG).show();
            bTSocket.close();
        } catch(IOException e) {
            Log.d("SERVERCONNECT", "Could not close connection:" + e.toString());
        }
    }
    public void sendData() throws IOException {
        String data = MenuFragment.serializeData();
        byte[] bytes = data.getBytes("UTF-8");
        OutputStream outputStream = bTSocket.getOutputStream();
        outputStream.write(bytes);
    }
    public void receiveData() throws IOException {
        InputStream mmInStream = bTSocket.getInputStream();
        byte[] buffer = new byte[1024];
        int length = mmInStream.read(buffer);
        byte[] a = Arrays.copyOfRange(buffer, 0, length);
        String doc2 = new String(a, "UTF-8");
        String[] data = doc2.split("NEWTEAM");
        int counter = 0;
        for(int i = 0; i < data.length; i ++){
            boolean repeat = false;
            Team team = Team.deSerialize(data[i]);
            for(Team t : MenuFragment.teams){
                if(team.matchNumber == t.matchNumber && team.teamNumber == t.teamNumber){
                    repeat = true;
                    counter++;
                    break;
                }
            }
            if(!repeat) {
                MenuFragment.teams.add(team);
            }
        }
        Toast.makeText(context, "Got " + (data.length-counter) + " entries from client", Toast.LENGTH_LONG).show();
    }
}