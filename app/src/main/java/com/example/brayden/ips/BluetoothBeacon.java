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
    private int bluetoothRssiAct;
    private float bluetoothDistance;

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

    public int getBluetoothRssiAct() {
        return bluetoothRssiAct;
    }

    public void setBluetoothRssiAct(int bluetoothRssiAct) {
        this.bluetoothRssiAct = bluetoothRssiAct;
    }

    public float getBluetoothDistance() {
        return bluetoothDistance;
    }

    public void setBluetoothDistance(int bluetoothRssi) {
        double distance = Math.pow(8, (-47 - bluetoothRssi) / 10);
        if (distance > 20)
        {
            distance = 10;
        }
        this.bluetoothDistance = (float)distance;
    }
}
