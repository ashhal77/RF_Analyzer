package com.example.ashhal.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.R.attr.button;
import static android.R.attr.port;

public class SettingActivity extends Activity {
    Button button;
    static String adres;
    static String prt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // Locate the button in activity_main.xml
        button = (Button) findViewById(R.id.button3);




        // Capture button clicks
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    EditText address = (EditText)findViewById(R.id.value1);
                    EditText port = (EditText)findViewById(R.id.value2);
                    adres      =  address.getText().toString();
                    prt      =  port.getText().toString();
                    ConnectToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        Button Backbutton = (Button) findViewById(R.id.button2);
        Backbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });


    }

    private static Socket socket2;
  /*  {
        try{
            socket2 = IO.socket("http://"+adres+":"+prt);
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }*/
    public static void ConnectToServer(){
        try{
            socket2 = IO.socket("http://"+adres+":"+prt);
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
        socket2.connect();
        socket2.on("message",handleIncomingMessage);
        check();
    }
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        socket2.connect();
        socket2.on("message",handleIncomingMessage);
        check();
    }*/


    //the code below used to send data on receiving messages from the server code while the
    //pallet moves on different zones
    //in this code different bundles are made for both wifi and phone
    //so the similar bundle needs to be created in the wifi zone and then put it below
    private static Emitter.Listener handleIncomingMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String message = data.getString("message");
                phone.test_abc();
                if (message == "true") {
                    Log.d("msd", "message received from server");

                    /*if(MainActivity.isPhone)
                        phone.test_abc();
                    // TODO if .isWifie true
                    else if(true)
                        wifiChild.test_abc();*/
                }
                    ///test


                Log.d("msd",message);
                System.out.print(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private static void check(){
        socket2.emit("message", "Connection established--message from android application");
    }

}
