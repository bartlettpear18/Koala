package com.bartlettpear18gmail.koalamouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Network extends AppCompatActivity {

    private static String tag = "debug";
    public static String ip = "192.168.43.81";

    private EditText text;
    private EditText text2;
    private EditText text3;
    private EditText text4;
    private TextView status;

    //Regex Base String
    private static final String IP_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
    }


    /**
     * Get user input for host ip
     * @param view
     */
    public void changeIP(View view) {
        text = (EditText) findViewById(R.id.editText);
        text2 = (EditText) findViewById(R.id.editText2);
        text3 = (EditText) findViewById(R.id.editText3);
        text4 = (EditText) findViewById(R.id.editText4);

        status= (TextView) findViewById(R.id.statusText);

        String input =  text.getText().toString() + "." + text2.getText().toString() + "." + text3.getText().toString() + "." + text4.getText().toString();
        Log.d(tag,"Set Host IP in Client to " + input);

        if(setAddress(input)) {
            status.setText("Host IP set to: " + input);
        } else {
            status.setText("IP Input rejected. Please try again");
        }
        status.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, Mouse.class);
        startActivity(intent);
        finish();

    }

    /**
     * Confirm IP to pattern
     * @param text
     * @return
     */
    private static boolean checkAddress(String text) {
        Pattern p = Pattern.compile(IP_PATTERN);
        Matcher m = p.matcher(text);
        return m.find();
    }

    /**
     * Initialize ip with confirmed address
     * @param newIp
     * @return
     */
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

    /**
     * Return ip address
     * @return
     */
    public static String getAddress() { return ip; }

}
