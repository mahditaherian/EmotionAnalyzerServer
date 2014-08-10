package com.mtn.emotionanalyzer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.mtn.emotionanalyzer.listener.GpsLocationListener;
import com.mtn.emotionanalyzer.listener.SensorListener;
import com.mtn.messages.SensorMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private boolean enabeled = false;
    private final List<SensorMessage> messages;
    private final SensorListener sensorListener;
    private final GpsLocationListener gpsListener;
    private Timer timer;
    private TimerTask timerTask;
    private String ip;
    private int port;


    public MainActivity() {
        messages = new ArrayList<SensorMessage>();
        sensorListener = new SensorListener(this, messages);
        gpsListener = new GpsLocationListener(this, messages);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText ip_text = (EditText) findViewById(R.id.ip_text);
        final EditText port_text = (EditText) findViewById(R.id.port_text);

        final Button startBtn = (Button) findViewById(R.id.start);

        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!enabeled && ip_text.getText() != null && port_text.getText() != null) {
                    ip = ip_text.getText().toString();
                    port = Integer.valueOf(port_text.getText().toString());
                    sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
                    startBtn.setText("End".toCharArray(), 0, 3);
                    ip_text.setEnabled(false);
                    port_text.setEnabled(false);
                    enabeled = true;
                    timer = new Timer(true);
//                    timer.scheduleAtFixedRate(timerTask, 2000, 2000);
                } else {
                    ip_text.setEnabled(true);
                    port_text.setEnabled(true);
                    sensorManager.unregisterListener(sensorListener);
                    startBtn.setText("Start".toCharArray(), 0, 5);
                    enabeled = false;
//                    ip = "";
//                    port = -1;
                    timer.cancel();
                    timer.purge();
                }
            }
        });

        timerTask = new TimerTask() {
            //send messages at specific time
            @Override
            public void run() {
                sendMessages();
            }
        };

        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 2000, 2000);
    }


    private void sendMessages() {
        if (messages.isEmpty()) {
            return;
        }

        try {
            // Create the socket
            Socket clientSocket = new Socket(ip, port);
            // Create the input & output streams to the server
            ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            // ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

            // Send the Message Object to the server
            outToServer.writeObject(messages);
            messages.clear();

            clientSocket.close();

        } catch (UnknownHostException ignored) {
        } catch (IOException ignored) {
        }
    }
}
