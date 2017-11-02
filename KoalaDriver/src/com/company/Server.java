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

    //Zero parameter constructor
    public Server() {}

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

    public boolean getBoolState() throws IOException { return input.readBoolean(); }

    public double getDoubleDisplacement() throws IOException { return input.readDouble(); }

}