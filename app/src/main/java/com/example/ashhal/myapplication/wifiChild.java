package com.example.ashhal.myapplication;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.anastr.speedviewlib.Gauge;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.util.OnSpeedChangeListener;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.example.ashhal.myapplication.SettingActivity.adres;
import static com.example.ashhal.myapplication.SettingActivity.prt;
import static com.example.ashhal.myapplication.phone.test_phone_data;

/**
 * Created by Ashhal on 04-Oct-17.
 */

public class wifiChild  extends Activity  {

    WifiManager wifiManager;
    WifiInfo wifiInfo;
    TextView textView8;
    Timer scheduler;
    PointerSpeedometer awesomeSpeedometer;
    static String test_wifi_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.wifi);

        ConnectToServer();

        //using button for going back to wifi class using set onclicklistener (scan wifi result)
        Button goBack = (Button) findViewById(R.id.button2);
        goBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });



        Button sendinfo = (Button) findViewById(R.id.button1);
        sendinfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundleData = getIntent().getExtras();
                new datatransfer().transfer(bundleData);
                socket2.emit("clientButtonClick", "Connection established--message from android application");
                //Bundle bundle = new Bundle();
               // bundle.putString("ssid", test_wifi_data);
                //new datatransfer().transfer(bundle);
            }
        });




        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String stuff = bundle.getString("ssid");
        test_wifi_data = stuff;
        //stuff= stuff.split("\t")[0];

        //*****************ssid value*************************
        TextView textView1 =   (TextView) findViewById(R.id.ssid);
        textView1.setText("SSID");

        TextView textView2 =   (TextView) findViewById(R.id.value1);
        textView2.setText(stuff.split("\t")[0]);

        //*********************BSSID value ****************************

        TextView textView3 =   (TextView) findViewById(R.id.bssid);
        textView3.setText("BSSID");

        TextView textView4 =   (TextView) findViewById(R.id.value4);
        textView4.setText(stuff.split("\t")[1]);

        //*********************speed value ****************************

        TextView textView5 =   (TextView) findViewById(R.id.speed);
        textView5.setText("Speed Mbps");

        TextView textView6 =   (TextView) findViewById(R.id.value6);
        textView6.setText(stuff.split("\t")[2] + "Mbps");

        //*********************strength in dbm ****************************

        TextView textView7 =   (TextView) findViewById(R.id.strength);
        textView7.setText("Strength in dBm");

        textView8 =   (TextView) findViewById(R.id.value8);
        textView8.setText(stuff.split("\t")[3] + "dBm");

        //*********************ip address ****************************

        TextView textView9 =   (TextView) findViewById(R.id.ipaddress);
        textView9.setText("IP Address");

        TextView textView10 =   (TextView) findViewById(R.id.value10);
        textView10.setText(stuff.split("\t")[4]);

        //*********************Mac address ****************************

        TextView textView11 =   (TextView) findViewById(R.id.macaddress);
        textView11.setText("MAC Address");

        TextView textView12 =   (TextView) findViewById(R.id.value12);
        textView12.setText(stuff.split("\t")[5]);


        //*************************************************
        TextView textView13 =   (TextView) findViewById(R.id.freq);
        textView13.setText("Frequency Mhz");

        TextView textView14 =   (TextView) findViewById(R.id.value14);
        textView14.setText(stuff.split("\t")[6] +" Mhz");

        Init();
        SetListeners();

    }

    private static Socket socket2;
    public static void ConnectToServer(){
        try{
            socket2 = IO.socket("http://"+adres+":"+prt);
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
        socket2.connect();
    }

    public static void test_abc(){
        Log.d("msd", "Wifi test abc");
        Bundle bundle = new Bundle();
        bundle.putString("wifi", "");
        bundle.putString("ssid", test_wifi_data);
        new datatransfer().transfer(bundle);
    }

    public  void Init()
    {
        wifiManager = (WifiManager) getSystemService(wifiChild.this.WIFI_SERVICE);
        awesomeSpeedometer= (PointerSpeedometer) findViewById(R.id.awesomeSpeedometer);
        awesomeSpeedometer.setUnit("dbm");
    }

    private Handler updateUI = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            Utils.ShowToast(getApplicationContext(),msg.getData().getString("dbm"));

        }
    };
    public void SetListeners()
    {
        scheduler=new Timer();
        scheduler.schedule(new TimerTask() {
            @Override
            public void run() {

                if(wifiManager.isWifiEnabled()) {
                    wifiInfo = wifiManager.getConnectionInfo();
                    if(wifiInfo != null) {
                        final int dbm = wifiInfo.getRssi();
                        //Utils.ShowToast(wifiChild.this,dbm+"");
                        //Bundle data=new Bundle();
                        //data.putString("dbm",dbm+"");
                        wifiChild.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                awesomeSpeedometer.speedTo(dbm,100);
                                textView8.setText(dbm+ " dBm");
                                Log.d("Speed_dbm",dbm+"");
                            }
                        });
                    }
                }
            }
        },0,10000);

        awesomeSpeedometer.setOnSpeedChangeListener(new OnSpeedChangeListener() {
            @Override
            public void onSpeedChange(Gauge gauge, boolean isSpeedUp, boolean isByTremble) {
                //textView8.setText(gauge.getSpeed()+ " dBm");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduler.cancel();
    }
}
