package com.example.brayden.ips;

import android.content.Context;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.*;
import android.content.Intent; // intents
import android.content.BroadcastReceiver;
import android.widget.TextView;
import android.os.CountDownTimer;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothBeacon> arrayOfFoundBTDevices = new ArrayList<BluetoothBeacon>();
    public TextView nameTextView;

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        // Register for broadcasts when a device is discovered.
        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //registerReceiver(mReceiver, filter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        nameTextView = (TextView) findViewById(R.id.nameTxtView);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        if (mBluetoothAdapter == null)
        {
            finish();
        }
        // Check to see if bluetooth is enabled. Prompt to enable it
        if( !mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        mBluetoothAdapter.startDiscovery();

        TimerActivity();
    }

    public void TimerActivity()
    {
        timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish()
            {
                update();
            }
        }.start();
    }

    final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            try {
                boolean found = false;
                int index = 0;
                if (arrayOfFoundBTDevices.size() > 0) {
                    for (int i = 0; i < arrayOfFoundBTDevices.size(); i++) {
                        if (device.getAddress() != null) {
                            if (arrayOfFoundBTDevices.get(i).getBluetoothAddress().contains(device.getAddress())) {
                                index = i;
                                found = true;
                                //break;
                            }
                        }
                    }
                }
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                if (found) {
                    arrayOfFoundBTDevices.get(index).setBluetoothRssi(rssi);
                } else {
                    BluetoothBeacon bluetoothBeacon = new BluetoothBeacon();
                    bluetoothBeacon.setBluetoothName(device.getName());
                    bluetoothBeacon.setBluetoothAddress(device.getAddress());
                    bluetoothBeacon.setBluetoothRssi(rssi);
                    if (device.getName().contains("Brayden's iPhone") || device.getName().contains("BRAYDENSSURFACE")) {
                        arrayOfFoundBTDevices.add(bluetoothBeacon);
                    }
                    //nameTextView.setText(nameTextView.getText().toString() + " " + bluetoothBeacon.getBluetoothName() + "/" + bluetoothBeacon.getBluetoothRssi());
                }
            }
            catch(Exception ex)
            {
                //nameTextView.setText(ex.toString());
            }
        }
    };

    public void update()
    {
        nameTextView.setText("");
        mBluetoothAdapter.cancelDiscovery();

        try
        {
            for (int i = 0; i < arrayOfFoundBTDevices.size(); i++)
            {
                // Strictly for debugging, most likely will be where we will update the UI Grid
                nameTextView.setText(nameTextView.getText().toString() + " " + arrayOfFoundBTDevices.get(i).getBluetoothName() + "/" + arrayOfFoundBTDevices.get(i).getBluetoothRssi());
            }
            mBluetoothAdapter.startDiscovery();
        }
        catch (Exception ex) { }

        TimerActivity();
    }
}
