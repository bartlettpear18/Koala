package com.bartlettpear18gmail.koalamouse;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Created by Joel.Bartlett18 on 10/30/2017.
 */
public class Worker extends Thread {

    //Debug tag
    private static String tag = "Debug";

    private DataInputStream workerIn;
    private DataOutputStream workerOut;

    private Client client;

    public Worker(DataOutputStream out, DataInputStream in, String ip) {
        workerOut = out;
        workerIn = in;

        client = new Client(ip);
    }

    @Override
    public void run() {
        try {
            client.init();

            while(true) {
                client.sendLeft(workerIn.readBoolean());
                Log.d(tag, "Sup dawg");
            }
        } catch (IOException e) {
        }
    }
}
