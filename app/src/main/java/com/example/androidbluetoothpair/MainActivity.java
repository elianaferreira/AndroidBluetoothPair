package com.example.androidbluetoothpair;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static int REQUEST_BT_ENABLE = 97;
    public static String TAG = MainActivity.class.getName();

    BluetoothAdapter bluetoothAdapter;

    ListView devicesList;
    DeviceArrayAdapter devicesArrayAdapter;
    ArrayList<BluetoothDevice> auxBluetoothDeviceArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        devicesList = findViewById(R.id.list_devices);
        devicesArrayAdapter = new DeviceArrayAdapter(this);
        devicesList.setAdapter(devicesArrayAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BT_ENABLE);
        } else {
            discoverDevices();
        }


        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                boolean isBonded = false;
                try {
                    BluetoothDevice bdDevice = auxBluetoothDeviceArrayList.get(position);
                    isBonded = createBond(bdDevice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "The bond was created: " + isBonded);

            }
        });
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName() != null) {
                    auxBluetoothDeviceArrayList.add(device);
                    devicesArrayAdapter.add(device.getName());
                }
            }
        }
    };


    private void discoverDevices() {
        bluetoothAdapter.startDiscovery();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(receiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_BT_ENABLE) {
            if(resultCode == RESULT_OK) discoverDevices();
        }
    }


    private boolean createBond(BluetoothDevice btDevice) throws Exception {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        return (Boolean) createBondMethod.invoke(btDevice);
    }
}
