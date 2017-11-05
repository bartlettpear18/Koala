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

public class Mouse extends AppCompatActivity {

    private Client client;
    private String mouseIP;
    private String tag = "debug";

    public boolean mouseLeft = false;
    public boolean mouseRight = true;
    public double mouseX = 0.0;
    public double mouseY = 5.0;

    PipedOutputStream output;
    PipedInputStream input;

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    Worker worker;

    boolean running = true;

    //Sensor setup
    private SensorManager sensorManager;
    private final double Z_LIMIT = 0.1;
    private float xAccel;
    private float yAccel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        mouseIP = getAddress();

        //Left button handler
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

//        //Right button handler
//        final Button rightButton = (Button) findViewById(R.id.right);
//        rightButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                    mouseRight = true;
//                } else if (event.getAction() == MotionEvent.ACTION_UP){
//                    mouseRight = false;
//                } else {
//                    mouseRight = false;
//                }
//                return false;
//            }
//        });

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
//                        dataOutputStream.writeBoolean(mouseRight);
//                        dataOutputStream.writeDouble((double) xAccel);
//                        dataOutputStream.writeDouble((double) yAccel);
//                        Thread.sleep(10);
                    } catch (IOException e) {
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

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
//            if(event.values[2] < Z_LIMIT) {
//                xAccel =  event.values[0];
//                yAccel =  event.values[1];
//            } else {
//                xAccel = 0;
//                yAccel = 0;
//            }
//            Log.d(tag, "X Acc: " + xAccel + " Y acc: " + yAccel);
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {}

}