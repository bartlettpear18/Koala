package com.bartlettpear18gmail.koalamouse;

import android.os.Handler;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    //Debug tag
    private static String tag = "Debug";

    //Server variables
    private static Socket socket;
    private static int port = 5000;
    private static String hostIP;

    //Stream variables
    private DataInputStream input = null;
    private DataOutputStream output = null;

    //Zero-parameter constructor
    public Client() {}

    //Initialize hostIP constructor
    public Client(String ip)  {
        this.hostIP = ip;
    }

    //Socket methods
    public boolean init() throws IOException {
        socket = new Socket(hostIP, port);
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
        Log.d(tag, "Streams created");
        return socket.isConnected();
    }
    public void close() throws IOException {
        output.flush();
        output.close();
        input.close();
        Log.d(tag, "Socket and Streams closed");
    }

    //Unit methods to send to server
    public void sendLeft(boolean left) throws IOException {
        output.writeBoolean(left);
        output.flush();
    }
    public void sendRight(boolean right) throws IOException {
        output.writeBoolean(right);
        output.flush();
    }
    public void sendX(double x) throws IOException {
        output.writeDouble(x);
        output.flush();
    }
    public void sendY(double y) throws IOException {
        output.writeDouble(y);
        output.flush();
    }

}

