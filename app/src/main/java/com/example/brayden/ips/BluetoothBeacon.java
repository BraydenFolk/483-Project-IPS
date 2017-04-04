package com.example.brayden.ips;

/**
 * Created by Brayden on 2017-04-03.
 */
public class BluetoothBeacon
{
    private String bluetoothName;
    private String bluetoothAddress;
    private int bluetoothState;
    private int bluetoothRssi;

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public int getBluetoothState() {
        return bluetoothState;
    }

    public void setBluetoothState(int bluetoothState) {
        this.bluetoothState = bluetoothState;
    }

    public int getBluetoothRssi() {
        return bluetoothRssi;
    }

    public void setBluetoothRssi(int bluetoothRssi) {
        this.bluetoothRssi = bluetoothRssi;
    }
}
