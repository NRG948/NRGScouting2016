package com.nrg948.nrgscouting2016;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    MySimpleArrayAdapter adapter;
    private static final UUID uuid = UUID.fromString("0e0e54bc-e290-11e5-9730-9a79f06e9478");
    private ArrayAdapter<DeviceItem> mAdapter;
    ConnectThread connectThread;
    AcceptThread acceptThread;
    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Create a new device item
                DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), false, device);
                Log.d("BlueToothFragment", "Got a device! " + device.getName());
                // Add it to our adapter
                adapter.add(newDevice);
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
        ArrayList<DeviceItem> d = new ArrayList<DeviceItem>(pairedDevices.size());
        for(BluetoothDevice b : pairedDevices){
            d.add(new DeviceItem(b.getName(), b.getAddress(), false, b));
        }
        adapter = new MySimpleArrayAdapter(getActivity(), R.layout.bluetooth_device_list_item, d);

        list.setAdapter(adapter);
        acceptThread = new AcceptThread(BTAdapter, uuid, getActivity());
        ToggleButton scan = (ToggleButton) v.findViewById(R.id.scan);
        scan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                if (isChecked) {
                    Intent discoverableIntent = new
                            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 45);
                    startActivity(discoverableIntent);
                    adapter.clear();
                    getActivity().registerReceiver(bReciever, filter);
                    BTAdapter.startDiscovery();
                    if (TopActivity.mode == 1) {
                        acceptThread.run();
                    }
                } else {
                    getActivity().unregisterReceiver(bReciever);
                    BTAdapter.cancelDiscovery();
                    if (TopActivity.mode == 1) {
                        acceptThread.cancel();
                    }
                }
            }
        });
        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        ((TextView)v.findViewById(R.id.mac_address)).setText("My MAC Address is " + address);
        v.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TopActivity.mode == 0) {
                    try {
                        connectThread.sendData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        acceptThread.sendData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        v.findViewById(R.id.receive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TopActivity.mode == 1){
                    try {
                        acceptThread.receiveData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        connectThread.receiveData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        v.findViewById(R.id.disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws NullPointerException{
                if(TopActivity.mode == 1){
                    acceptThread.cancel();
                }else{
                    connectThread.cancel();
                }
            }
        });
        return v;
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<DeviceItem>{
        Context context;
        List<DeviceItem> objects;
        public MySimpleArrayAdapter(Context context, int resource, List<DeviceItem> objects) {
            super(context, resource, objects);
            this.context = context;
            this.objects = objects;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.bluetooth_device_list_item, parent, false);
            ((TextView)v.findViewById(R.id.name)).setText(objects.get(position).name);
            ((TextView)v.findViewById(R.id.mac_address)).setText(objects.get(position).address);
            if(TopActivity.mode == 1){
                ((Button)v.findViewById(R.id.connect)).setVisibility(View.GONE);
            }else {
                v.findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connectThread = new ConnectThread(objects.get(position).device, uuid, getContext());
                        connectThread.connect();
                    }
                });
            }
            return v;
        }
    }

    private class DeviceItem{
        String name;
        String address;
        Boolean connected;
        BluetoothDevice device;
        public DeviceItem(String name, String address, Boolean connected, BluetoothDevice device){
            this.name = name;
            this.address = address;
            this.connected = connected;
            this.device = device;
        }
    }


}
