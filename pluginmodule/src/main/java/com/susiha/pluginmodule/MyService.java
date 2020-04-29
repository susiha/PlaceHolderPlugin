package com.susiha.pluginmodule;

import android.content.Intent;
import android.util.Log;

import com.susiha.standard.utils.Constants;

/**
 * 插件中的Service
 */
public class MyService extends BaseService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Constants.DebugTag,"susiha >>> MyService >>> onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.i(Constants.DebugTag,"susiha >>> MyService >>> Running");


                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Constants.DebugTag,"susiha >>> MyService >>> onCreate");

    }


}
