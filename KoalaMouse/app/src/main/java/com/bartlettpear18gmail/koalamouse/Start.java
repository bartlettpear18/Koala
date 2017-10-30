package com.bartlettpear18gmail.koalamouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


//Splash screen for professional app

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, Mouse.class);
        startActivity(intent);
        finish();
    }
}
