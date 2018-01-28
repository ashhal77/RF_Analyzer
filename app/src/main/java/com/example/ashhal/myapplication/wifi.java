package com.example.ashhal.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.level;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.WIFI_SERVICE;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Ashhal on 16-Oct-17.
 */

public class wifi extends Fragment implements View.OnClickListener {


    WifiManager wifi;
    ListView lv;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;
    Button buttonCheck;

    String ITEM_KEY = "key";
    List<String> arraylist = new ArrayList<String>();
    List<String> listOfNetworks = new ArrayList<String>();
    ArrayAdapter adapter;
    String whole_data;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_wifi_scanner, container, false);

     //DONT fuck here

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
                //DO everything here
                buttonScan = (Button) getActivity().findViewById(R.id.scan);
               buttonScan.setOnClickListener(this);

                lv = (ListView) getActivity().findViewById(R.id.wifilist);
                //arraylist.add("hello");

                // enables wifi if its disabled
                wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
                if (wifi.isWifiEnabled() == false)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
                    wifi.setWifiEnabled(true);
                }//putting the data in the array

                this.adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arraylist);
                lv.setAdapter(this.adapter);

        //*****starting code for set on item click listener**********************************************************
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                final int position, long rowId) {


                            AlertDialog.Builder adb = new AlertDialog.Builder(
                                    wifi.this.getActivity());
                            adb.setTitle(String.valueOf(parent.getItemAtPosition(position)));
                            adb.setMessage("HEllo HELLO");
                            //Context context = mapView.getContext();
                            final String ssid = String.valueOf(parent.getItemAtPosition(position));
                            final EditText input = new EditText(wifi.this.getActivity());
                            //input.setText("purplered");
                            adb.setView(input);
                            adb.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String value = input.getText().toString().trim();
            //                    Toast.makeText(getApplicationContext(), value,
            //                            Toast.LENGTH_SHORT).show();

                                    WifiConfiguration wifiConfig = new WifiConfiguration();
//                                    wifiConfig.SSID = "\"HUAWEI P10\"";
//                                    wifiConfig.preSharedKey = "\"11223344\"";

                                    wifiConfig.SSID = String.format("\"%s\"",listOfNetworks.get(position));
                                    wifiConfig.preSharedKey =  String.format("\"%s\"",input.getText().toString().trim());


                                    WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
                                    int netId = wifiManager.addNetwork(wifiConfig);
                                    wifiManager.disconnect();
                                    wifiManager.enableNetwork(netId, true);
                                    wifiManager.reconnect();
                                    boolean abc = false;
                                    ConnectivityManager connManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                                    try {
                                        //checking if Wifi is connected
                                        int count = 0;
                                        while (abc == false && count < 10) {
                                            NetworkInfo wifi2 = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                            abc = wifi2.isConnected();
                                            //Wait to connect
                                            count++;
                                            Thread.sleep(1000);
                                            //checking END if Wifi is connected
                                        }

                                    }catch (Exception e){}
                                    if(abc) {
                                        //if wifi connected then show TOAST and start new Activity
                                        Toast.makeText(wifi.this.getActivity(), "Connected", Toast.LENGTH_LONG).show();


                                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                                        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
                                        wifiInfo.getLinkSpeed();


                                        whole_data = wifiInfo.getSSID()+ " \t " +
                                                wifiInfo.getBSSID()  + " \t " +
                                                wifiInfo.getLinkSpeed()+ " \t " +
                                                wifiInfo.getRssi()+ " \t " +
                                                wifiInfo.getIpAddress()+ " \t " +
                                                wifiInfo.getMacAddress()+ " \t " +
                                                wifiInfo.getFrequency();


                                        Intent i = new Intent(wifi.this.getActivity(), wifiChild.class);
                                        //Create the bundle
                                        Bundle bundle = new Bundle();

                                        //Add your data to bundle
                                        bundle.putString("ssid", whole_data);

                                        //Add the bundle to the intent
                                        i.putExtras(bundle);
                                        startActivity(i);
                                    }
                                    else
                                        Toast.makeText(wifi.this.getActivity(), "Wrong Password",Toast.LENGTH_LONG).show();
                                }
                            });
                            adb.setNegativeButton("Cancel",null);

                            adb.show();

                        }

                    });

//        scanWifiNetworks();
        //**********End of code for on item click listener************************************************************************



    }//End of onActivityCreated Dont write outside or below this line


    //*************start to add code that was outside activity ****************************************************

                    //runs on scan button click
                    public void onClick(View view)
                    {
                        if(Build.VERSION.SDK_INT >= M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    1);
                            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

                        }else {
                            scanWifiNetworks();
                        }
                    }

                    private void scanWifiNetworks(){

                        arraylist.clear();
                        listOfNetworks.clear();
                        getActivity().registerReceiver(wifi_receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

                        wifi.startScan();

                        Log.d("WifScanner", "scanWifiNetworks");

                        Toast.makeText(this.getActivity(), "Scanning....", Toast.LENGTH_SHORT).show();

                    }

                    BroadcastReceiver wifi_receiver= new BroadcastReceiver()
                    {

                        @Override
                        public void onReceive(Context c, Intent intent)
                        {
                            Log.d("WifScanner", "onReceive");
                            results = wifi.getScanResults();
                            size = results.size();
                            getActivity(). unregisterReceiver(this);

                            try
                            {
                                for (ScanResult scanResult : results) {
                                    //int level = WifiManager.calculateSignalLevel(scanResult.level, 5);
                                    arraylist.add(scanResult.SSID + " \t\t\t\t\t\t " +scanResult.level+" dbm  " +   scanResult.frequency + "  MHz");
                                    listOfNetworks.add(scanResult.SSID);
                                    adapter.notifyDataSetChanged();
                                    System.out.println("Level is " + level + " out of 5");

                                }
                            }
                            catch (Exception e)
                            {
                                Log.w("WifScanner", "Exception: "+e);

                            }


                        }
                    };



    //*****************end of code that is outside activity **********************

}//NOTHING BELOW THIS ONE

