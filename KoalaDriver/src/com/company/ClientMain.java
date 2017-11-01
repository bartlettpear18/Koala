package com.company;

import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;

import javax.xml.crypto.Data;
import java.io.*;

/**
 * Created by Joel.Bartlett18 on 10/31/2017.
 */
public class ClientMain {

    public static void main(String[] args) throws IOException, InterruptedException {

        final PipedOutputStream output = new PipedOutputStream();
        final PipedInputStream  input  = new PipedInputStream(output);

        DataOutputStream o = new DataOutputStream(output);
        DataInputStream i = new DataInputStream(input);

//        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    output.write("Hello world, pipe!".getBytes());
//                } catch (IOException e) {
//                }
//            }
//        });

        Worker worker = new Worker(o, i);


        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    o.writeBoolean(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        worker.start();
        thread2.start();    }


}

class Worker extends Thread {

    private DataInputStream workerIn;
    private DataOutputStream workerOut;

    public Worker(DataOutputStream out, DataInputStream in) {
        workerOut = out;
        workerIn = in;
    }

    public void run() {
        try {
            System.out.println("" + workerIn.readBoolean());
        } catch (IOException e) {
        }


//        Client client = new Client("127.0.0.1");
//        client.init();
//        client.run();

    }
}