package com.susiha.placeholderplugin.proxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.susiha.placeholderplugin.manager.PluginManager;
import com.susiha.standard.protocol.ServiceInterface;
import com.susiha.standard.utils.Constants;

import androidx.annotation.Nullable;

public class ProxyService extends Service {


    private ServiceInterface service;

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getPluginLoader().getDexClassLoader();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.i(Constants.DebugTag,"susiha >>> ProxyService >>>onBind");

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Constants.DebugTag,"susiha >>> ProxyService >>>onCreate");


        if(service!=null){
            service.onCreate();
        }


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Constants.DebugTag,"susiha >>> ProxyService >>>onStartCommand");
        String className = intent.getStringExtra(Constants.TargetClassName);

        try {
            service =(ServiceInterface) getClassLoader().loadClass(className).newInstance();

            service.jumpIntoPlugin(this);

            service.onStartCommand(intent,flags,startId);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return super.onStartCommand(intent, flags, startId);
    }
}
