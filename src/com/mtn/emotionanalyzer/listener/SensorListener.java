package com.mtn.emotionanalyzer.listener;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.widget.TextView;
import com.mtn.emotionanalyzer.MainActivity;
import com.mtn.emotionanalyzer.R;
import com.mtn.entity.Accelerometer;
import com.mtn.entity.MagneticField;
import com.mtn.entity.Orientation;
import com.mtn.messages.AccelerometerMessage;
import com.mtn.messages.MagneticFieldMessage;
import com.mtn.messages.OrientationMessage;
import com.mtn.messages.SensorMessage;

import java.util.List;

/**
 * @author Mahdi
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class SensorListener implements SensorEventListener {
    List<SensorMessage> messages;
    MainActivity mainActivity;

    public SensorListener(MainActivity mainActivity, List<SensorMessage> messages) {
        this.messages = messages;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        SensorMessage message = null;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            Accelerometer lastAccelerometer = new Accelerometer(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            message = new AccelerometerMessage(lastAccelerometer, System.currentTimeMillis());
            TextView textView = (TextView) mainActivity.findViewById(R.id.s1value);
            textView.setText(lastAccelerometer.toString());

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            MagneticField lastMagneticField = new MagneticField(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            message = new MagneticFieldMessage(lastMagneticField, System.currentTimeMillis());
            lastMagneticField.setX(sensorEvent.values[0]);
            lastMagneticField.setY(sensorEvent.values[1]);
            lastMagneticField.setZ(sensorEvent.values[2]);
            TextView textView = (TextView) mainActivity.findViewById(R.id.s2value);
            textView.setText(lastMagneticField.toString());

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            Orientation lastOrientation = new Orientation(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            message = new OrientationMessage(lastOrientation, System.currentTimeMillis());
            TextView textView = (TextView) mainActivity.findViewById(R.id.s3value);
            textView.setText(lastOrientation.toString());
        }
        messages.add(message);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
