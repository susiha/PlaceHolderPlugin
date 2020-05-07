package com.susiha.pluginmodule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.susiha.standard.protocol.BroadCastInterface;
import com.susiha.standard.utils.Constants;

public class MyBoradCast extends BroadcastReceiver implements BroadCastInterface {
    @Override
    public void onReceive(Context context, Intent intent) {

        String param = TextUtils.isEmpty(intent.getStringExtra(Constants.BroadCastParam))
                ?"没有参数":
                intent.getStringExtra(Constants.BroadCastParam);

        Log.i(Constants.DebugTag,"收到广播,参数 = "+param);
    }
}
