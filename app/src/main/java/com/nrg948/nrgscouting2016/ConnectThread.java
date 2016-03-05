package com.nrg948.nrgscouting2016;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Sean on 3/4/2016.
 */
public class ConnectThread extends Thread{

    private final BluetoothDevice bTDevice;
    private final BluetoothSocket bTSocket;
    Context context;
    public ConnectThread(BluetoothDevice bTDevice, UUID UUID, Context context) {
        BluetoothSocket tmp = null;
        this.bTDevice = bTDevice;

        try {
            tmp = this.bTDevice.createRfcommSocketToServiceRecord(UUID);
        }
        catch (IOException e) {
            Log.d("CONNECTTHREAD", "Could not start listening for RFCOMM");
        }
        bTSocket = tmp;
        this.context = context;
    }

    public boolean connect() {

        try {
            bTSocket.connect();
        } catch(IOException e) {
            Log.d("CONNECTTHREAD", "Could not connect: " + e.toString());
            try {
                bTSocket.close();
            } catch(IOException close) {
                Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString());
                return false;
            }
        }
        Toast.makeText(context, "Successfully Paired with " + bTDevice.getName(),
                Toast.LENGTH_LONG).show();
        return true;
    }

    public boolean cancel() {
        try {
            Toast.makeText(context, "Disconnected with " + bTSocket.getRemoteDevice().getName() ,
                    Toast.LENGTH_LONG).show();
            bTSocket.close();
        } catch(IOException e) {
            return false;
        }
        return true;
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
        Log.d("ConnectThread", doc2);
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
