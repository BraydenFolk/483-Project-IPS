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
import java.util.Map;
import java.util.Random;
import android.graphics.Color;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import ca.hss.heatmaplib.HeatMap;


public class MainActivity extends AppCompatActivity  {

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

        //Heat map

        HeatMap map = (HeatMap)findViewById(R.id.example_map);
        map.setMinimum(0.0);
        map.setMaximum(100.0);
        Map<Float, Integer> colors = new ArrayMap<>();
        //build a color gradient in HSV from red at the center to green at the outside

        for (int i = 0; i < 21; i++) {
            float stop = ((float) i) / 20.0f;
            int color = doGradient(i * 5, 0, 100, 0xff00ff00, 0xffff0000);
            colors.put(stop, color);
        }
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

    private float clamp(float value, float min, float max) {
        return value * (max - min) + min;
    }

    private double clamp(double value, double min, double max) {
        return value * (max - min) + min;
    }
    private static int doGradient(double value, double min, double max, int min_color, int max_color) {
        if (value >= max) {
            return max_color;
        }
        if (value <= min) {
            return min_color;
        }
        float[] hsvmin = new float[3];
        float[] hsvmax = new float[3];
        float frac = (float)((value - min) / (max - min));
        Color.RGBToHSV(Color.red(min_color), Color.green(min_color), Color.blue(min_color), hsvmin);
        Color.RGBToHSV(Color.red(max_color), Color.green(max_color), Color.blue(max_color), hsvmax);
        float[] retval = new float[3];
        for (int i = 0; i < 3; i++) {
            retval[i] = interpolate(hsvmin[i], hsvmax[i], frac);
        }
        return Color.HSVToColor(retval);
    }

    private static float interpolate(float a, float b, float proportion) {
        return (a + ((b - a) * proportion));
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
                int rssi = ConvertRSSIToReadable(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
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

    private int ConvertRSSIToReadable(int rssi)
    {
        int rssiConverted = 0;
        if (rssi < 0 && rssi > -35)
        {
            rssiConverted = 5;
        }
        else if (rssi < -35 && rssi > -50)
        {
            rssiConverted = 10;
        }
        else if (rssi < -50)
        {
            rssiConverted = 15;
        }
        else
        {
            rssiConverted = 20;
        }

        return rssiConverted;
    }

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
