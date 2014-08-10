package com.mtn.emotionanalyzer.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;
import com.mtn.emotionanalyzer.MainActivity;
import com.mtn.emotionanalyzer.R;
import com.mtn.entity.GpsLocation;
import com.mtn.messages.GpsLocationMessage;
import com.mtn.messages.SensorMessage;

import java.util.List;

/**
 * @author Mahdi Taherian
 */
public class GpsLocationListener implements LocationListener {

    List<SensorMessage> messages;
    MainActivity mainActivity;

    public GpsLocationListener(MainActivity mainActivity, List<SensorMessage> messages) {
        this.messages = messages;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        GpsLocation lastGpsLocation = new GpsLocation();
        lastGpsLocation.setLatitude(location.getLatitude());
        lastGpsLocation.setLongitude(location.getLongitude());
        GpsLocationMessage message = new GpsLocationMessage(lastGpsLocation, System.currentTimeMillis());
        TextView textView = (TextView) mainActivity.findViewById(R.id.s4value);
        textView.setText(lastGpsLocation.toString());
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
