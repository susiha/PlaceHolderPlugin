package com.susiha.standard.protocol;

import android.content.Intent;

public interface ServiceInterface extends BaseProtocol{
     void onCreate();
     int onStartCommand(Intent intent, int flags, int startId);
}
