package com.susiha.placeholderplugin.manager;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.susiha.placeholderplugin.proxy.ProxyActivity;
import com.susiha.standard.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 插件管理器
 */
public class PluginManager {

    private  static volatile PluginManager INSTANCE = null;

    private PluginManager(){}

    /**
     * 单例模式
     * @return
     */
    public static PluginManager getInstance(){
        if(INSTANCE ==null){
            synchronized (PluginManager.class){
                if(INSTANCE ==null){
                    INSTANCE = new PluginManager();
                }
            }
        }
        return INSTANCE;
    }

    private PluginLoader pluginLoader;

    /**
     * 加载插件
     * @param context 上下问环境
     * @param apkName apk的名称，带后缀 比如说是 plugin.apk
     * @return
     */
    public boolean loadPlugin(Context context, String apkName){
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"plugin"+File.separator+apkName);
        if(!file.exists()){
            Log.i(Constants.DebugTag,"susiha >>> 加载插件时,插件文件不存在");
            return false;
        }

        //缓存路径与去掉后缀的文件名一样
        String dirName = apkName.substring(0,apkName.indexOf("."));
        String apkPath =file.getAbsolutePath();
        if(pluginLoader ==null){
            pluginLoader = new PluginLoader(context,apkPath,dirName);
        }
        return pluginLoader.initPluginLoader();
    }

    public PluginLoader getPluginLoader(){
        return pluginLoader;
    }



    public void jumpIntoPluginActivity(Context context,String apkName){
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"plugin"+File.separator+apkName);
        if(!file.exists()){
            Log.i(Constants.DebugTag,"susiha >>> 使用插件时,插件文件不存在");
            return ;
        }

        Log.i(Constants.DebugTag,"susiha >>> 使用插件时,插件文件大小"+file.length());


//        context.getExternalFilesDir()

        PackageManager pm = context.getPackageManager();

        String filePath = copy(context,apkName,file);

        //获取指定路径的包信息
        PackageInfo packageInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);

        if(packageInfo==null){
            Log.i(Constants.DebugTag,"susiha >>> packageInfo 为null");
            return;
        }
        ActivityInfo[] activities =packageInfo.activities;

        for (ActivityInfo activity : activities) {

            Log.i(Constants.DebugTag,"susiha >>> Activity >>>"+ activity.name);
        }


        Intent intent = new Intent(context, ProxyActivity.class);
        intent.putExtra(Constants.TargetClassName,activities[0].name);
        context.startActivity(intent);
    }


    /**
     * 为什么要复制一个apk文件出来，这是因为在9.0之后 getPackageArchiveInfo()方法的参数路径
     * 需要是context.getExternalFilesDir()的路径 否则 会返回null
     * 在华为手机上 要对清单文件的application 标签上 使用        android:requestLegacyExternalStorage="true"
     * 否则 会出现读写权限受限的问题
     * @param context
     * @param apkName
     * @param oldFile
     * @return
     */
    private String copy(Context context,String apkName,File oldFile){
      File file =new File(context.getExternalFilesDir("plugin")+File.separator+apkName);

            try {
                int bytesum = 0;
                int byteread = 0;
                if (oldFile.exists()) { //文件存在时
                    InputStream inStream = new FileInputStream(oldFile); //读入原文件
                    FileOutputStream fs = new FileOutputStream(file);
                    byte[] buffer = new byte[1444];
                    int length;
                    while ( (byteread = inStream.read(buffer)) != -1) {
                        bytesum += byteread; //字节数 文件大小
                        System.out.println(bytesum);
                        fs.write(buffer, 0, byteread);
                    }
                    inStream.close();
                }
            } catch (Exception e) {
                System.out.println("复制单个文件操作出错");
                e.printStackTrace();
        }

        Log.i(Constants.DebugTag,"susiha >>> 复制的文件的大小 >>>>> "+file.length());


        return file.getAbsolutePath();
    }







}
