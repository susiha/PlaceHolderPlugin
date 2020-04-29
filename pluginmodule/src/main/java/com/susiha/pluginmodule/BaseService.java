package com.susiha.pluginmodule;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.susiha.standard.protocol.ServiceInterface;

public class BaseService implements ServiceInterface {

    public  Service hostAppService;
    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void jumpIntoPlugin(Context context) {

        this.hostAppService = (Service) context;
    }
}
