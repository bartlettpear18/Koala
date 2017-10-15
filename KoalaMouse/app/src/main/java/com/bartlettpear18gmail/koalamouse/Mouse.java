package com.bartlettpear18gmail.koalamouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Mouse extends AppCompatActivity {

    private Client client;
    private String tag = "debug";

    public static boolean left = false;
    public static boolean right = true;
    public static double x = 0.0;
    public static double y = 5.0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        client = new Client("192.168.43.81");
        client.execute();

    }
}

