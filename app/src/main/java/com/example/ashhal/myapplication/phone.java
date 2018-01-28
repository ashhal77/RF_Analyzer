package com.example.ashhal.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthLte;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ashhal on 16-Oct-17.
 */



public class phone extends Fragment {


    String phone_data;

    TextView txt1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.phone_signal, container, false);

        return rootView;

    }



            @Override
            public void onActivityCreated(@Nullable Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);

                getActivity().setContentView(R.layout.phone_signal);

                //calling the phone listener and telephone manager together with gsm location
                try{

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
                    }




                    ;
                }
                catch (Exception e){
                    e.printStackTrace();



                }


                //*****************network operator value*************************
                TextView textView1 =   (TextView) getActivity().findViewById(R.id.operator);
                textView1.setText("Network Operator");

                TextView textView2 =   (TextView) getActivity().findViewById(R.id.value2);
                textView2.setText(phone_data.split("\t")[0]);
                //*****************operator value*************************


                //*****************sim operator *************************
                TextView textView3 =   (TextView) getActivity().findViewById(R.id.simoperator);
                textView3.setText("Sim Operator");

                TextView textView4 =   (TextView) getActivity().findViewById(R.id.value4);
                textView4.setText(String.valueOf(phone_data.split("\t")[1]));
                //*****************sim operator*************************

                //*****************country info*************************
                TextView textView5 =   (TextView) getActivity().findViewById(R.id.country);
                textView5.setText("Country Info");

                TextView textView6 =   (TextView) getActivity().findViewById(R.id.value6);
                textView6.setText(phone_data.split("\t")[2]);
                //*****************country info*************************

                //*****************data activity*************************
                TextView textView7 =   (TextView) getActivity().findViewById(R.id.data);
                textView7.setText("Data State");
                //converting string value to integer
                if (Integer.parseInt((phone_data.split("\t")[3]).trim()) == 2){
                    TextView textView8 =   (TextView) getActivity(). findViewById(R.id.value8);
                    textView8.setText("Connected");
                }
                else {
                    TextView textView8 =   (TextView) getActivity().findViewById(R.id.value8);
                    textView8.setText("Disconnected");

                }
                //*****************data activity*************************


                //*****************signal strength*************************
                TextView textView9 =   (TextView) getActivity().findViewById(R.id.dbm);
                textView9.setText("Signal Strength");

                TextView textView10 =   (TextView) getActivity().findViewById(R.id.value10);
                textView10.setText(phone_data.split("\t")[4] + " dBm");
                //*****************signal strength*************************

                //*****************data activity state*************************
                TextView textView11 =   (TextView) getActivity().findViewById(R.id.cellid);
                textView11.setText("Cell ID");
                TextView textView12 =   (TextView) getActivity().findViewById(R.id.value12);
                textView12.setText(phone_data.split("\t")[5]);
                //*****************sdata activiy state*************************




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

