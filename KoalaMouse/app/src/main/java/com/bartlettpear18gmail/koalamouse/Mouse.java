package com.bartlettpear18gmail.koalamouse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.DataInput;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        mouseIP = getAddress();
        Log.d(tag, "IP: " + mouseIP);

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        update.start();
        worker.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
        worker.interrupt();
        Log.d(tag, "Stopping pipe");
    }

    @Override
    public void onStart() {
        super.onStart();
        running = true;
        Log.d(tag, "Connecting pipe");
    }

    public void leftClick (View view) {
        mouseLeft = !mouseLeft;
    }

//    /**
//     * Handler for left button
//     * @param e
//     * @throws IOException
//     */
//    public void leftClick(MotionEvent e) throws IOException {
//        if(e.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
//            mouseLeft = true;
//            Log.d(tag, "Left pressed");
//
//        } else if (e.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
//            mouseLeft = false;
//            Log.d(tag, "Left released");
//        }
//    }

    /**
     * Runs network activity
     */
    public void startNetwork() {
        Intent intent = new Intent(this, Network.class);
        startActivity(intent);
        finish();
    }
}