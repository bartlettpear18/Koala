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
    public Integer mouseLeft = 0;
    public Integer mouseRight = 0;
    public Integer mouseX = 0;
    public Integer mouseY = 0;

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

    //Motion analysis
    private final int SCALAR = 10;
    private final int DELTA_TIME = 10; //milliseconds

    private int prevPosX = 0;
    private int prevPosY = 0;

    private int currentPosX;
    private int currentPosY;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(tag, "Sensor Manager instantiated");

        mouseIP = "192.168.43.81";


        try {
            output = new PipedOutputStream();
            input  = new PipedInputStream(output);

            dataOutputStream = new DataOutputStream(output);
            dataInputStream = new DataInputStream(input);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Worker Thread instantiation
        worker = new Worker(dataOutputStream, dataInputStream, mouseIP);

        //Left Button Handler
        final Button leftButton = (Button) findViewById(R.id.left);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                mouseLeft = (event.getAction() == MotionEvent.ACTION_DOWN) ? 1 : 0;
                return false;
            }
        });

        //Right Button Handler
        final Button rightButton = (Button) findViewById(R.id.right);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                mouseRight = (event.getAction() == MotionEvent.ACTION_DOWN) ? 1 : 0;
                return false;
            }
        });


        Thread update = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running) {
                    try {

                        currentPosX = (int) (xAccel * DELTA_TIME * DELTA_TIME)/2;
                        currentPosY = (int) (yAccel * DELTA_TIME * DELTA_TIME)/2;

                        mouseX = currentPosX - prevPosX;
                        mouseY = currentPosY - prevPosY;

                        Log.d(tag, mouseX + " " + mouseY);
                        byte[] packet = {mouseLeft.byteValue(), mouseRight.byteValue(), mouseX.byteValue(), mouseY.byteValue()};
                        dataOutputStream.write(packet);

                        prevPosX = currentPosX;
                        prevPosY = currentPosY;

                        Thread.sleep(DELTA_TIME);
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
            if(event.values[2] < Z_LIMIT) {
                xAccel =  event.values[0];
                yAccel =  event.values[1];

            } else {
                xAccel = 0;
                yAccel = 0;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

}