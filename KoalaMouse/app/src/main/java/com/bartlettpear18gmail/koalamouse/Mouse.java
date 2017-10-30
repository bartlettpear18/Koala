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
import java.io.IOException;
import static com.bartlettpear18gmail.koalamouse.Network.getAddress;

public class Mouse extends AppCompatActivity {

    private Client client;
    private String mouseIP;
    private String tag = "debug";

    private HandlerThread handlerThread = null;
    private MouseHandler mouseHandler = null;

    public boolean mouseLeft = false;
    public boolean mouseRight = true;
    public double mouseX = 0.0;
    public double mouseY = 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        new Worker().execute();

        handlerThread = new HandlerThread("HandlerThread");
        handlerThread.setDaemon(true);
        handlerThread.start();

        Looper loop = handlerThread.getLooper();
        mouseHandler = new MouseHandler(loop);

        mouseIP = getAddress();
        Log.d(tag, "IP: " + mouseIP);

    }
    @Override
    protected void onStart() {
        super.onStart();
        while(true) {
            Message left = mouseHandler.obtainMessage();
            left.obj = mouseLeft;
            mouseHandler.sendMessage(left);
            Log.d(tag, "Left sent to client: " + mouseLeft);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        handlerThread.quit();
        Log.d(tag,"Handler quit");

    }

    /**
     * Handler for left button
     * @param e
     * @throws IOException
     */
    public void leftClick(MotionEvent e) throws IOException {
        if(e.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
            mouseLeft = true;
            Log.d(tag, "Left pressed");

        } else if (e.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
            mouseLeft = false;
            Log.d(tag, "Left released");
        }
    }

    /**
     * Runs network activity
     */
    public void startNetwork() {
        Intent intent = new Intent(this, Network.class);
        startActivity(intent);
        finish();
    }
}

class MouseHandler extends Handler {

    public MouseHandler(Looper looper) { super(looper); }
    public void handleMessage(Message msg) {}
}

