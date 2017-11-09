package com.bartlettpear18gmail.koalamouse;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static com.bartlettpear18gmail.koalamouse.Network.getAddress;

public class Mouse extends AppCompatActivity implements SensorEventListener{

    //TCP Setup
    private Client client;
    private String mouseIP;
    private String tag = "debug";

    //TCP Data
    public boolean mouseLeft = false;
    public boolean mouseRight = false;
    public double mouseX = 0.0;
    public double mouseY = 5.0;

    //Sensor setup
    private SensorManager sensorManager;
    private final double Z_LIMIT = 0.1;
    private float xAccel;
    private float yAccel;

    //Inter-thread communications
    PipedOutputStream output;
    PipedInputStream input;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Worker worker;
    boolean running = true;

    private final int SCALAR = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(tag, "Sensor Manager instantiated");

        mouseIP = "192.168.43.81";

        final Button leftButton = (Button) findViewById(R.id.left);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mouseLeft = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    mouseLeft = false;
                } else {
                    mouseLeft = false;
                }
                return false;
            }
        });

        final Button rightButton = (Button) findViewById(R.id.right);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mouseRight = true;
                } else {
                    mouseRight = false;
                }
                return false;
            }
        });

        try {
            output = new PipedOutputStream();
            input  = new PipedInputStream(output);

            dataOutputStream = new DataOutputStream(output);
            dataInputStream = new DataInputStream(input);

        } catch (IOException e) {
            e.printStackTrace();
        }

        worker = new Worker(dataOutputStream, dataInputStream, mouseIP);

        Thread update = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running) {
                    try {
                        dataOutputStream.writeBoolean(mouseLeft);
                        dataOutputStream.writeBoolean(mouseRight);
                        dataOutputStream.writeDouble(mouseX);
                        dataOutputStream.writeDouble(mouseY);
                        Thread.sleep(10);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        update.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
        try {
            worker.stopThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(tag, "Stopping pipe");
    }

    @Override
    public void onStart() {
        super.onStart();
        running = true;
        worker.start();
        Log.d(tag, "Connecting pipe");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            if(event.values[2] < 1) {
                xAccel =  event.values[0];
                yAccel =  event.values[1];

                mouseX = (double) (xAccel * SCALAR);
                mouseY = (double) (yAccel * SCALAR);
            } else {
                xAccel = 0;
                yAccel = 0;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}