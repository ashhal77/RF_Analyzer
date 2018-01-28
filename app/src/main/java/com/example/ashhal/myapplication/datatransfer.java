package com.example.ashhal.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;



/**
 * Created by Ashhal on 01-Nov-17.
 */

public class datatransfer {

    Bundle dataRecv = null;

    public void transfer(Bundle bundleData){

        dataRecv = bundleData;
        new MyTask().execute();


    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        //String msg = msgTextField.getText().toString();
        @Override
        protected Void doInBackground(Void... params)  {
            Bundle dataInner = datatransfer.this.dataRecv;
            String wifiData = dataInner.getString("ssid");
            String phoneData = dataInner.getString("phone");

            if (true){

                try {
                    //this URl is the url for the API and it needs to be same as the one
                    // for which we enter from the user interface
                    //
                    URL url = new URL("http://192.168.43.63:8081/notes"); // here is your URL path

                    JSONObject postDataParams = new JSONObject();
                    if(wifiData != "") {
                        postDataParams.put("SSID", wifiData.split("\t")[0]);
                        postDataParams.put("BSSID", wifiData.split("\t")[1]);
                        postDataParams.put("Speed Mbps", wifiData.split("\t")[2]);
                        postDataParams.put("Strength in dBm", wifiData.split("\t")[3]);
                        postDataParams.put("IP Address", wifiData.split("\t")[4]);
                        postDataParams.put("MAC Address", wifiData.split("\t")[5]);
                        postDataParams.put("Frequency Mhz", wifiData.split("\t")[6]);
                    }
                    else {
                        postDataParams.put("Network Operator", phoneData.split("\t")[0]);
                        postDataParams.put("Sim Operator", phoneData.split("\t")[1]);
                        postDataParams.put("Country Info", phoneData.split("\t")[2]);
                        postDataParams.put("Data State", phoneData.split("\t")[3]);
                        postDataParams.put("Signal Strength", phoneData.split("\t")[4]);
                        postDataParams.put("Cell ID", phoneData.split("\t")[5]);

                    }
                    Log.d("params", postDataParams.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
                    wr.write(postDataParams.toString());
                    wr.flush();
                    conn.connect();
                    int responseCode=conn.getResponseCode();

                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    //JSONObject post = new JSONObject(br.toString());

                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = br.readLine()) != null)
                        responseStrBuilder.append(inputStr);

                    JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());

                    int responseCode1=conn.getResponseCode();

                }catch(Exception e){
                    e.getMessage();
                }
            }
            else {
                // display message if text fields are empty
                //Toast.makeText(getBaseContext(), "All field are required", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    };




}
