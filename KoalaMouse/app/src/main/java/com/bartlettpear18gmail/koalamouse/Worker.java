package com.bartlettpear18gmail.koalamouse;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Joel.Bartlett18 on 10/30/2017.
 */
public class Worker extends Thread {

    //Debug tag
    private static String tag = "Debug";

    private DataInputStream workerIn;
    private DataOutputStream workerOut;

    private boolean running = true;

    private Client client;

    public Worker(DataOutputStream out, DataInputStream in, String ip) {
        workerOut = out;
        workerIn = in;

        client = new Client(ip);
    }

    public void stopThread() throws IOException {
        running = false;
        client.close();
    }

    @Override
    public void run() {
        try {
            client.init();

            while(workerIn.available() != -1 && running) {

                byte[] packet = new byte[4];
                workerIn.read(packet);
                client.sendPacket(packet);

            }
        } catch (IOException e) {
        }
    }
}
