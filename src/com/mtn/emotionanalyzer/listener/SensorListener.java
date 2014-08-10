package com.mtn.emotionanalyzer.listener;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
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

    public SensorListener(List<SensorMessage> messages) {
        this.messages = messages;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        SensorMessage message = null;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            Accelerometer lastAccelerometer = new Accelerometer(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            message = new AccelerometerMessage(lastAccelerometer, System.currentTimeMillis());

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            MagneticField lastMagneticField = new MagneticField(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            message = new MagneticFieldMessage(lastMagneticField, System.currentTimeMillis());
            lastMagneticField.setX(sensorEvent.values[0]);
            lastMagneticField.setY(sensorEvent.values[1]);
            lastMagneticField.setZ(sensorEvent.values[2]);

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            Orientation lastOrientation = new Orientation(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            message = new OrientationMessage(lastOrientation, System.currentTimeMillis());
        }
        messages.add(message);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
