package com.example.brayden.ips;

/**
 * Created by Brayden on 2017-04-07.
 */
public interface BluetoothBeaconInterface
{
    void calibrateRoomSide(float fltXDistance, float fltYDistance);
    void plotPosition(float fltDistanceFromA, float fltDistanceFromB, float fltDistanceFromC, float fltDistanceFromD);
}
