package com.bartlettpear18gmail.koalamouse;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by Joel.Bartlett18 on 10/30/2017.
 */
public class Worker extends AsyncTask {

    //Debug tag
    private static String tag = "Debug";

    //Inter-thread comms
    public Handler handler;


    @Override
    protected Object doInBackground(Object[] objects) {
        Looper.prepare();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                Log.d(tag, "Msg: " + msg.toString());
            }
        };
        Looper.loop();
        return null;
    }
}
