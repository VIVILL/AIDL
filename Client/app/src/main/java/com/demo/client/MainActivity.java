package com.demo.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.TagLostException;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.service.IMyAidlCallBack;
import com.demo.service.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private IMyAidlInterface myAidlInterface;
    private Button mButton;

    private IMyAidlCallBack mIMyAidlCallBack = new IMyAidlCallBack.Stub() {
        @Override
        public void onCallBack(byte[] data) throws RemoteException {
            // AIDL传输的数据最终输出至client中的此处
            Log.d(TAG,"IMyAidlCallBack onCallBack, data length = "+ data.length + " data [0] = " + data[0] + " data [1] = " + data[1]);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button_test);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(MainActivity.this, myAidlInterface.getName(), Toast.LENGTH_SHORT).show();
                    myAidlInterface.setCallback(mIMyAidlCallBack);
                    myAidlInterface.initData();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        bindMyService();

    }
    private void bindMyService() {
        Intent intent = new Intent();
        intent.setPackage("com.demo.service");
        intent.setAction("aidl.test.action");
       /* intent.setComponent(new ComponentName("com.demo.service", "com.demo.service.MyService"));
        startService(intent);*/
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myAidlInterface = null;
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        unbindService(connection);
    }

}
