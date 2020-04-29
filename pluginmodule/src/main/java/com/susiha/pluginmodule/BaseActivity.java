package com.susiha.pluginmodule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.susiha.standard.protocol.ActivityInterface;
import com.susiha.standard.utils.Constants;

public class BaseActivity extends Activity implements ActivityInterface {
    public Activity hostAppActivity;

    @Override
    public void jumpIntoPlugin(Activity activity) {
        hostAppActivity = activity;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestory() {

    }

    /**
     * 从宿主上加载布局文件
     * @param layoutResID
     */
    public void setContentView( int layoutResID) {
        hostAppActivity.getWindow().setContentView(layoutResID);
    }


    /**
     *  从宿主上初始化View
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById( int id) {
        return hostAppActivity.getWindow().findViewById(id);
    }

    /**
     * 从宿主上启动页面
     * @param intent
     */
    public void startActivity(Intent intent) {

        Intent intentNew = new Intent();
        //把子类封装在Intent中的Target.class(比如:NextActivity.class) 以参数的形式传递过去
        intentNew.putExtra(Constants.TargetClassName,
                intent.getComponent().getClassName());
        hostAppActivity.startActivity(intentNew);
    }

}
