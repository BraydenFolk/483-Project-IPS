# 483-Project-IPS

Bluetooth permissions - add 

<manifest ... >
  <uses-permission android:name="android.permission.BLUETOOTH" />
  <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" /> // needed to initiate discovery 
  ...
</manifest>

to manifest. 


if bluetooth is disabled, can ask user to enable bluetooth without leaving the app.
for more info, see https://developer.android.com/guide/topics/connectivity/bluetooth.html#SettingUp -- Setting up bluetooth -- bluetooth adapter

-- see oncreate() method

add import android.bluetooth.*; to MainActivity.java

add... import java.util.Set; import android.content.BroadcastReceiver;

In order to receive information about each device discovered, 
your application must register a BroadcastReceiver for the ACTION_FOUND intent. 
The system broadcasts this intent for each device. 
The intent contains the extra fields EXTRA_DEVICE and EXTRA_CLASS, 
which in turn contain a BluetoothDevice and a BluetoothClass, respectively. 
The following code snippet shows how you can register to handle the broadcast when devices are discovered:

@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    nameTextView = (TextView) findViewById(R.id.nameTxtView);	
    // Register for broadcasts when a device is discovered.
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver(mReceiver, filter);
}

// Create a BroadcastReceiver for ACTION_FOUND.
private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            

	    //get name, address, rssi and save it in an array list of bluetooth beacons
        }
    }
};

@Override
protected void onDestroy() {
    super.onDestroy();
    ...

    // Don't forget to unregister the ACTION_FOUND receiver.
    unregisterReceiver(mReceiver);
}

I have a timer for the app to run every 2s, 
not sure if it needs to be done this much but we can discuss.

add import android.os.CountDownTimer;

CountDownTimer timer; // global

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
    } // reestablish timer every time it expires

On finish of the timer, the update() method is called to cancel and restart discovery,
this way, the discovery process does not timeout. 
