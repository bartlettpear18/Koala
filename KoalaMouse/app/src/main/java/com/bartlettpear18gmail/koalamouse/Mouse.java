package com.bartlettpear18gmail.koalamouse;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Mouse extends AppCompatActivity {

    private Client client;
    private String tag = "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        //Connect to the server
        new connectClient().execute();


    }

    //AsyncTask class to run network methods on Main UI thread
    public class connectClient extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            client = new Client("172.16.32.65");
            client.init();
            Log.d(tag, "Networking connection successful");
            return null;
        }
    }

}

