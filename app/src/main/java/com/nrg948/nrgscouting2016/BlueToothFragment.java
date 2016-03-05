package com.nrg948.nrgscouting2016;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlueToothFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlueToothFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlueToothFragment extends Fragment {

    ArrayAdapter<String> adapter;
    private static final UUID uuid = UUID.fromString("0e0e54bc-e290-11e5-9730-9a79f06e9478");
    private ArrayAdapter<DeviceItem> mAdapter;
    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("BlueToothFragment", "Got a device!");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Create a new device item
                DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), false  );
                // Add it to our adapter
                adapter.add(device.getName());
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blue_tooth, container, false);
        final BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 1);
        }
        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        ListView list = (ListView)v.findViewById(R.id.connected_devices);
        ArrayList<String> d = new ArrayList<String>(pairedDevices.size());
        for(BluetoothDevice b : pairedDevices){
            d.add(b.getName());
        }
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.basic_list_item, R.id.data, d);
        //mAdapter = new DeviceListAdapter(getActivity(), deviceItemList, bTAdapter);
        list.setAdapter(adapter);
        ToggleButton scan = (ToggleButton) v.findViewById(R.id.scan);
        scan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                if (isChecked) {
                    adapter.clear();
                    getActivity().registerReceiver(bReciever, filter);
                    BTAdapter.startDiscovery();
                } else {
                    getActivity().unregisterReceiver(bReciever);
                    BTAdapter.cancelDiscovery();
                }
            }
        });
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        return v;
    }

    private class DeviceItem{
        String name;
        String address;
        Boolean connected;
        public DeviceItem(String name, String address, Boolean connected){
            this.name = name;
            this.address = address;
            this.connected = connected;
        }
    }
}
