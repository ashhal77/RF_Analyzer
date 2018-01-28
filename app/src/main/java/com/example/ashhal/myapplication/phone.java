package com.example.ashhal.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthLte;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.github.anastr.speedviewlib.ProgressiveGauge;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.example.ashhal.myapplication.SettingActivity.adres;
import static com.example.ashhal.myapplication.SettingActivity.prt;

/**
 * Created by Ashhal on 16-Oct-17.
 */



public class phone extends Fragment {


    String phone_data;
    static String  test_phone_data;
    private ArrayList items;
    private ArrayAdapter arrayAdapter;
    ProgressiveGauge progressiveGauge;
    View rootView;
    TextView txt1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectToServer();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.phone_signal, container, false);
        Init();
        return rootView;

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

    public void Init()
    {
        progressiveGauge=(ProgressiveGauge)rootView.findViewById(R.id.progressiveGauge);
        progressiveGauge.setUnit("dBm");
    }


    //the code that creates a new bundle and passes its value to the datatransfer
    public static void test_abc(){
        Bundle bundle = new Bundle();
        bundle.putString("phone", test_phone_data);
        bundle.putString("ssid", "");
        new datatransfer().transfer(bundle);
    }

            @Override
            public void onActivityCreated(@Nullable Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);


                Button sendinfo = (Button) getActivity().findViewById(R.id.info1);
                sendinfo.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //Bundle bundleData = getActivity().getIntent().getExtras();
                        Log.d("address: ", adres);
                        Log.d("port: ", prt);
                        try{
                            socket2 = IO.socket("http://"+adres+":"+prt);
                        }catch (URISyntaxException e){
                            throw new RuntimeException(e);
                        }
                        socket2.connect();
                        socket2.emit("phone", "");
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", phone_data);
                        test_phone_data = phone_data;
                        bundle.putString("ssid", "");
                        new datatransfer().transfer(bundle);
                    }
                });


                //calling the phone listener and telephone manager together with gsm location
                try{
                    //permission for getting cell info from telehphony manager by using if condition
                    if(!CheckPermission(phone.this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)){
                        RequestPermission(phone.this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION, 123 );
                    }

                    pslistener = new myPhoneStateListener();
                    TelephonyManager abcd = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                    abcd.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


                    CellInfoLte cellinfogsm = (CellInfoLte)abcd.getAllCellInfo().get(0);
                    CellSignalStrengthLte cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();

                    GsmCellLocation cellLocation = (GsmCellLocation) abcd.getCellLocation();



                    //we are just getting permission form user for phone access
                    if (CheckPermission(phone.this.getActivity(), Manifest.permission.READ_PHONE_STATE)) {

                        phone_data = abcd.getNetworkOperatorName()+ " \t " +
                                abcd.getSimOperatorName() + " \t " +
                                abcd.getSimCountryIso()+ " \t " +
                                abcd.getDataState()+" \t "+
                                cellSignalStrengthGsm.getDbm() +" \t "+
                                abcd.getDeviceId();
                    } else {
                        RequestPermission(phone.this.getActivity(), Manifest.permission.READ_PHONE_STATE, 123 );
                        phone_data = "";
                    };
                }
                catch (Exception e){
                    e.printStackTrace();



                }





            if (phone_data != "" && phone_data != null) {
                //*****************network operator value*************************
                TextView textView1 = (TextView) getActivity().findViewById(R.id.operator);
                textView1.setText("Network Operator");

                TextView textView2 = (TextView) getActivity().findViewById(R.id.value2);
                textView2.setText(phone_data.split("\t")[0]);
                //*****************operator value*************************


                //*****************sim operator *************************
                TextView textView3 = (TextView) getActivity().findViewById(R.id.simoperator);
                textView3.setText("Sim Operator");

                TextView textView4 = (TextView) getActivity().findViewById(R.id.value4);
                textView4.setText(String.valueOf(phone_data.split("\t")[1]));
                //*****************sim operator*************************

                //*****************country info*************************
                TextView textView5 = (TextView) getActivity().findViewById(R.id.country);
                textView5.setText("Country Info");

                TextView textView6 = (TextView) getActivity().findViewById(R.id.value6);
                textView6.setText(phone_data.split("\t")[2]);
                //*****************country info*************************

                //*****************data activity*************************
                TextView textView7 = (TextView) getActivity().findViewById(R.id.data);
                textView7.setText("Data State");
                //converting string value to integer
                if (Integer.parseInt((phone_data.split("\t")[3]).trim()) == 2) {
                    TextView textView8 = (TextView) getActivity().findViewById(R.id.value8);
                    textView8.setText("Connected");
                } else {
                    TextView textView8 = (TextView) getActivity().findViewById(R.id.value8);
                    textView8.setText("Disconnected");

                }
                //*****************data activity*************************


                //*****************signal strength*************************


                TextView textView9 = (TextView) getActivity().findViewById(R.id.dbm);
                textView9.setText("Signal Strength");

//              /*  ArrayList items = phone_data.split[4];
//
//                if (!phone_data.isEmpty()) {
                   //phone_data.clear();*/
           //         TextView textView10 = (TextView) getActivity().findViewById(R.id.value10);
             //       textView10.setText(phone_data.split("\t")[4] + " dBm");



                //*****************signal strength*************************

                //*****************data activity state*************************
                TextView textView11 = (TextView) getActivity().findViewById(R.id.cellid);
                textView11.setText("Cell ID");
                TextView textView12 = (TextView) getActivity().findViewById(R.id.value12);
                textView12.setText(phone_data.split("\t")[5]);
                //*****************sdata activiy state*************************


            }

            }//End of onActivityCreated Dont write outside or below this line

            //**********code start for outside the activity******************************

                TelephonyManager  TelephonManager;
                myPhoneStateListener pslistener;
                int SignalStrength =0;

                //this part converts signalstrength to dbm
                class myPhoneStateListener extends PhoneStateListener {

                    @Override
                    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                        super.onSignalStrengthsChanged(signalStrength);
                        SignalStrength = signalStrength.getGsmSignalStrength();
                        SignalStrength = (2 * SignalStrength) - 113;
                        //  txt1.setText(String.valueOf(SignalStrength +  "dBm"));
                        TelephonyManager abcd = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                        CellInfoLte cellinfogsm = (CellInfoLte)abcd.getAllCellInfo().get(0);
                        CellSignalStrengthLte cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
                        Log.d("newSignal",String.valueOf(cellSignalStrengthGsm.getDbm()));
                        TextView textView10 = (TextView) getActivity().findViewById(R.id.value10);
                        textView10.setText(String.valueOf(cellSignalStrengthGsm.getDbm()) + " dBm");
                        progressiveGauge.speedTo(cellSignalStrengthGsm.getDbm());
                    }
                }

                // for permission about Device ID activity
                public void RequestPermission(Activity thisActivity, String Permission, int Code) {
                    if (ContextCompat.checkSelfPermission(thisActivity,
                            Permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                                Permission)) {
                        } else {
                            ActivityCompat.requestPermissions(thisActivity,
                                    new String[]{Permission},
                                    Code);
                        }
                    }
                }

                public boolean CheckPermission(Context context, String Permission) {
                    if (ContextCompat.checkSelfPermission(context,
                            Permission) == PackageManager.PERMISSION_GRANTED) {
                        return true;
                    } else {
                        return false;
                    }
                }

    //***************code end for the one outside the activity*********************


}//NOTHING BELOW THIS ONE

