package com.demo.service;

import android.os.AsyncTask;
import android.util.Log;

public class Data {
    private static final String TAG = "Data";
    private static Data mDataInstance;
    private  DataTask mDataTask;
    private  static DataCallBackInterface mDataCallBackInterface;//如果不加static，在AsyncTask中会为null
    public static  Data getInstance(){
        if (mDataInstance ==null){
            return new Data();
        }
       return mDataInstance;
    }

    public void setDataCallBack(DataCallBackInterface dataCallBackInterface){
        Log.d(TAG,"setDataCallBack");
        mDataCallBackInterface = dataCallBackInterface;
    }

    public void createData(){
        Log.d(TAG,"createData");
        mDataTask = new DataTask();
        mDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
    }

    private class DataTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            Log.d(TAG,"doInBackground");
            byte[] bytes = {0x1,0x2,0x3};
            if (mDataCallBackInterface==null){
                Log.d(TAG,"mDataCallBackInterface null" );
            }else {
                Log.d(TAG,"mDataCallBackInterface  onCallBack" );
                mDataCallBackInterface.onCallBack(bytes);

            }
            return null;
        }
    }
}
