package com.bartlettpear18gmail.koalamouse;

import android.util.Log;
import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bartlettpear18gmail.koalamouse.Mouse.left;
import static com.bartlettpear18gmail.koalamouse.Mouse.right;
import static com.bartlettpear18gmail.koalamouse.Mouse.x;
import static com.bartlettpear18gmail.koalamouse.Mouse.y;



public class Client extends AsyncTask<Void, Void, Void> {

    //Debug tag
    private static String tag = "Debug";

    //Server variables
    private static Socket socket;
    private static int port = 5000;
    private static String ip; //Hotspot IP: 192.168.43.81, Barty IP: 10.0.0.162

    //Stream variables
    private DataInputStream input = null;
    private DataOutputStream output = null;


    //Regex Base String
    private static final String IP_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    //No parameter constructor
    public Client() {}

    public Client(String ip)  {
        this.ip = ip;
    }



    //IP Address Methods
    private static boolean checkAddress(String text) {
        Pattern p = Pattern.compile(IP_PATTERN);
        Matcher m = p.matcher(text);
        return m.find();
    }
    public static boolean setAddress(String newIp) {
        boolean change = false;
        if(checkAddress(newIp)) {
            ip = newIp;
            change = true;
            Log.d(tag, "IP Confirmed and set");
        } else {
            Log.d(tag, "IP Submission rejected");
            change = false;
        }
        return change;
    }
    public String getAdddress() { return ip; }

    //Socket methods
    private void init() {
        try{
            socket = new Socket(ip, port);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            Log.d(tag, "Streams created");
        } catch (IOException e) {
            Log.d(tag, "Start network activity here");
        }

    }

    private void close() throws IOException {
        output.flush();
        output.close();
        input.close();
        Log.d(tag, "Socket and Streams closed");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Log.d(tag, "Checkpoint 1");
            init();

            while(true) {
                sendUpdate();
                wait(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
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

}

