package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by Joel.Bartlett18 on 10/12/2017.
 */
public class Server {

    private ServerSocket server;
    private Socket socket;
    private final int PORT = 5000;

    private DataInputStream input;
    private DataOutputStream output;

    private boolean leftState;
    private boolean rightState;
    private double xDisplacement;
    private double yDisplacement;

    //Zero parameter constructor
    public Server() {}

    //Accessor methods
    public boolean getsLeftState() {
        return leftState;
    }
    public boolean getRightState() {
        return rightState;
    }
    public double getxDisplacement() {
        return xDisplacement;
    }
    public double getyDisplacement() {
        return yDisplacement;
    }
    public Socket getSocket() { return socket; }

    /**
     * Setup server and socket
     * @throws IOException
     */
    public void init() throws IOException {

        //setup server
        server = new ServerSocket(PORT);
        socket = server.accept();

        //setup streams
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());

        System.out.println("Connection bound");
    }


    private void updateLeft() throws IOException {
        leftState = input.readBoolean();
        System.out.println("Left State: " + leftState);
    }

    private void updateRight() throws IOException {
        rightState = input.readBoolean();
        System.out.println("Right State: " + rightState);
    }

    private void updateX() throws IOException {
        xDisplacement = input.readDouble();
        System.out.println("X Displacement: " + xDisplacement);
    }

    private void updateY() throws IOException {
        yDisplacement = input.readDouble();
        System.out.println("Y Displacement: " + yDisplacement);
    }

    public void updateStates() throws IOException {
        updateLeft();
        updateRight();
        updateX();
        updateY();
    }


}