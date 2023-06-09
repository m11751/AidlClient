package com.example.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.aidlclient.databinding.ActivityMainBinding;
import com.example.aidlserver.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity:evan";
    private ActivityMainBinding activityMainBinding;
    IMyAidlInterface myAidlInterface ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(activityMainBinding.getRoot());

        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                try {
                    myAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
                    String st= myAidlInterface.get15RandomString();
                    Log.i(TAG,"st:" + st);
                    activityMainBinding.tvShow.setText(st);
                } catch (RemoteException e) {
                    Log.i(TAG,"onServiceConnected:" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };  //end connection

        Intent intent = new Intent();
        intent.setPackage("com.example.aidlserver");
        intent.setAction("com.example.service.action");
        bindService(intent,connection,BIND_AUTO_CREATE);

//        点击刷新按钮
        activityMainBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String st= myAidlInterface.get15RandomStringPhrase(8,"123abc");
//                    String st= myAidlInterface.get15RandomString;
                    Log.i(TAG,"Click st:" + st);
                    activityMainBinding.tvShow.setText(st);
                } catch (RemoteException e) {
                    Log.i(TAG,"onclick error:" + e);
                    e.printStackTrace();
                }
            }
        });

    }
}