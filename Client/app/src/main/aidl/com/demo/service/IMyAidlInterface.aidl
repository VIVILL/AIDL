// IMyAidlInterface.aidl
package com.demo.service;

// Declare any non-default types here with import statements
import com.demo.service.IMyAidlCallBack;
interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   /* void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);*/

   String getName();
   void setCallback(IMyAidlCallBack cb);
   void initData();

}
