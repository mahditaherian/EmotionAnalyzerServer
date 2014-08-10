package com.mtn.emotionanalyzer.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.mtn.entity.GpsLocation;
import com.mtn.messages.GpsLocationMessage;
import com.mtn.messages.SensorMessage;

import java.util.List;

/**
 * @author Mahdi Taherian
 */
public class GpsLocationListener implements LocationListener {

    List<SensorMessage> messages;

    public GpsLocationListener(List<SensorMessage> messages) {
        this.messages = messages;
    }

    @Override
    public void onLocationChanged(Location location) {
        GpsLocation lastGpsLocation = new GpsLocation();
        lastGpsLocation.setLatitude(location.getLatitude());
        lastGpsLocation.setLongitude(location.getLongitude());
        GpsLocationMessage message = new GpsLocationMessage(lastGpsLocation, System.currentTimeMillis());
        messages.add(message);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
