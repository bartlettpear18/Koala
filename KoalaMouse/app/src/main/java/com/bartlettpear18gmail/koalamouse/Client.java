package com.bartlettpear18gmail.koalamouse;

import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private String tag = "Debug";
    private Socket socket;
    private final int PORT = 5000;

    private DataOutputStream output;
    private DataInputStream input;

    private boolean left = false;
    private boolean right = false;
    private double x = 0.0;
    private double y = 0.0;

    private String host;
    private Mouse mouse;

    //Zero Parameter Constructor
    public Client() {}


    public Client(String host) {
        this.host = host;
        Log.d(tag, host);
    }


    //Mutator methods
    public void setLeft(boolean left) {
        this.left = left;
    }
    public void setRight(boolean right) {
        this.right = right;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }


    public void sendLeft() throws IOException {
        output.writeBoolean(left);
        output.flush();
    }

    public void sendRight() throws IOException {
        output.writeBoolean(right);
        output.flush();
    }

    public void sendX() throws IOException {
        output.writeDouble(x);
        output.flush();
    }

    public void sendY() throws IOException {
        output.writeDouble(y);
        output.flush();
    }

    public void sendUpdate() throws IOException {
        sendLeft();
        sendRight();
        sendX();
        sendY();
    }

    /**
     * Setup socket
     * @throws IOException
     */

    public void init() {

        try {
            Log.d(tag, "Here should be fine");
            socket = new Socket(host, PORT);
            Log.d(tag, "Here?");
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            Log.d(tag, "Or here?");
        } catch (IOException e) {
            Log.d(tag, "Fail here");
            e.printStackTrace();
        }

        Log.d(tag, "Connection made");

    }

}
