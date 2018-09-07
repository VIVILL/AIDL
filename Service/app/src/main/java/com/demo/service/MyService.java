package com.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";
    final RemoteCallbackList<IMyAidlCallBack> mAidlCallbacks = new RemoteCallbackList<>();

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return new MyBinder();
    }

    @Override
    public void  onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }




    class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public String getName() throws RemoteException {
            return "test";
        }

        @Override
        public void setCallback(IMyAidlCallBack cb) throws RemoteException {
            if (cb != null){
                Log.d(TAG,"setCallback");
                mAidlCallbacks.register(cb);
            }

        }

        @Override
        public void initData() throws RemoteException {
            if (mDataCallBackInterface == null){
                Log.d(TAG,"mDataCallBackInterface null");
            }else {
                Log.d(TAG,"mDataCallBackInterface setDataCallBack");
                Data.getInstance().setDataCallBack(mDataCallBackInterface);
            }

            Data.getInstance().createData();
        }


    }

    //   CallBack 基本流程 ：A通知 B做某事，B做好了，通知A做好。


    private DataCallBackInterface mDataCallBackInterface = new DataCallBackInterface() {
        @Override
        public void onCallBack(byte[] data) {
            sendDataToClientByAidlCallBack(data);
        }
    };

    private void sendDataToClientByAidlCallBack(byte[] data) {
        Log.d(TAG, "data length="+data.length);
        final int N = mAidlCallbacks.beginBroadcast();
        Log.d(TAG,"n is : " + N);
        for (int i=0; i<N; i++) {
            Log.d(TAG,"inner for");
            try {
                if(data.length>0){
                    Log.d(TAG," getBroadcastItem");
                    mAidlCallbacks.getBroadcastItem(i).onCallBack(data);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        mAidlCallbacks.finishBroadcast();
    }

}
