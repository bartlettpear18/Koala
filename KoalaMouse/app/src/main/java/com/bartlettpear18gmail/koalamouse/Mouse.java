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
    private final double Z_LIMIT = 0.01;
    private float xAccel;
    private float yAccel;

    //Inter-thread communications
    PipedOutputStream output;
    PipedInputStream input;

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    Worker worker;
    boolean running = true;

<<<<<<< HEAD
    //Motion analysis
    private final int SCALAR = 10;
    private final int DELTA_TIME = 10; //milliseconds
    private final int X_LIMIT = 1;
    private final int Y_LIMIT = 1;

    private int prevPosX = 0;
    private int prevPosY = 0;

    private int currentPosX;
    private int currentPosY;

    private int xVel0 = 0;
    private int yVel0 = 0;



=======
    //Sensor setup
    private SensorManager sensorManager;
    private final double Z_LIMIT = 0.1;
    private float xAccel;
    private float yAccel;
>>>>>>> refs/remotes/origin/master


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(tag, "Sensor Manager instantiated");

        mouseIP = "192.168.43.81";

<<<<<<< HEAD
=======
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
>>>>>>> refs/remotes/origin/master

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
<<<<<<< HEAD

                        if(xAccel > X_LIMIT) {
                            currentPosX = (int) (xAccel * DELTA_TIME * DELTA_TIME)/2 + (xVel0 * DELTA_TIME);
                            xVel0 = (int) (xVel0 + xAccel * DELTA_TIME);
                            mouseX = currentPosX - prevPosX;
                            prevPosX = currentPosX;
                        } else {
                            mouseX = 0;
                        }

                        if(yAccel > Y_LIMIT) {
                            currentPosY = (int) (yAccel * DELTA_TIME * DELTA_TIME)/2 + (yVel0 * DELTA_TIME);
                            yVel0 = (int) (yVel0 + yAccel * DELTA_TIME);
                            mouseY = currentPosY - prevPosY;
                            prevPosY = currentPosY;
                        } else {
                            mouseY = 0;
                        }


                        byte[] packet = {mouseLeft.byteValue(), mouseRight.byteValue(), mouseX.byteValue(), mouseY.byteValue()};
                        dataOutputStream.write(packet);

                        Thread.sleep(DELTA_TIME);

                    } catch (IOException | InterruptedException e) {
=======
                        dataOutputStream.writeBoolean(mouseLeft);
//                        dataOutputStream.writeBoolean(mouseRight);
//                        dataOutputStream.writeDouble((double) xAccel);
//                        dataOutputStream.writeDouble((double) yAccel);
//                        Thread.sleep(10);
                    } catch (IOException e) {
>>>>>>> refs/remotes/origin/master
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

<<<<<<< HEAD
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            if(event.values[2] < Z_LIMIT) {
                xAccel =  event.values[0] * SCALAR;
                yAccel =  event.values[1] * SCALAR;

            } else {
                xAccel = 0;
                yAccel = 0;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
=======
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
>>>>>>> refs/remotes/origin/master

}