package com.company;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Created by Joel.Bartlett18 on 10/12/2017.
 */
public class Client {

    private Socket socket;
    private final int PORT = 5000;

    private DataOutputStream output;
    private DataInputStream input;

    private boolean left = false;
    private boolean right = false;
    private double x = 0.0;
    private double y = 0.0;

    private String host;

    //Zero Parameter Constructor
    public Client() {}

    public Client(String host) { this.host = host; }

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

    /**
     * Setup socket
     * @throws IOException
     */

    public void init() {

        //create and connect socket
        try {
            socket = new Socket(host, PORT);
        } catch (IOException e) {
            System.out.println("Start network activity");
        }

        //setup streams
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Client side connection made");
    }

    public void run() throws IOException, InterruptedException {

        Integer x = 22;
        Integer y = 33;

        Byte left = new Byte("1");
        Byte right = new Byte("0");
        Byte xb = x.byteValue();
        Byte yb = y.byteValue();

        byte[] arr = {left, right, xb, yb};


        output.write(arr);

//        while(true){
//            sendUpdate();
//            sleep(10);
//        }

    }

    public void changeLeft() {
        left = !left;
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
        x++;
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
}

