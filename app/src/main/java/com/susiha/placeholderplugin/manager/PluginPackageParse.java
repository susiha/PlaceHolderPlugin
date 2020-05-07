package com.susiha.placeholderplugin.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 插件包解析
 */
public class PluginPackageParse {


    private final String packageParsePath ="android.content.pm.PackageParser";

    private final String getPackageMethodName ="parsePackage";
    private Context mContext;

    private File pluginFile;
    public PluginPackageParse(File file,Context context){

        this.pluginFile = file;
        this.mContext = context;

    }


    /**
     * 注册插件的Receivers
     */
    public void registerPluginReceiver(PluginLoader loader){
        /**
         *  步骤 1 首先通过反射 拿到PackageParse 对象
         *  步骤 2 通过 PackageParse的方法获取指定的插件包中package对象
         *  步骤 3 通过反射拿到 package对象中的receivers属性的值，
         *        这个是List对象,List内的对象是Activity 这个Activity不是界面Activity而是packageParse
         *        中的内部类,是注册在Manifest中的动作信息，不光是receiver有，Activity，Service
         *        都是这个Activity的集合
         */
        ArrayList receivers  = (ArrayList) getPackageReceives();

        for (Object activity : receivers) {
            ActivityInfo info = findPackageActivityInfo(activity);
            String activityName = info.name;
            
            BroadcastReceiver receiver = null;
            try {
                Class broadCastReceive = loader.getDexClassLoader().loadClass(activityName);
                receiver = (BroadcastReceiver) broadCastReceive.newInstance();

                ArrayList<IntentFilter> intentFilters = getIntentFilterArray(activity);
                
                if(intentFilters!=null){

                    for (IntentFilter filter : intentFilters) {
                        mContext.registerReceiver(receiver,filter);
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            
            
        }
        
    }


    /**
     * 获取IntentFilter
     * @param activity
     * @return
     */
    private ArrayList<IntentFilter> getIntentFilterArray(Object activity){

        Class mComponentClass = null;
        try {
            mComponentClass = Class.forName("android.content.pm.PackageParser$Component");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field intentsField = null;
        try {
            intentsField = mComponentClass.getDeclaredField("intents");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            ArrayList<IntentFilter> intents = (ArrayList) intentsField.get(activity); // intents 所属的对象是谁 ?

            return intents;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return  null;
    }
    


    /**
     * 通过Activity找到ActivityInfo,仅仅是为了找到这个组件的name
     * 这个是通过PackageParse中的 generateActivityInfo方法来找到的，这个方法签名是这样的
     * 
     *  public static final ActivityInfo generateActivityInfo(Activity a, int flags,
     *             PackageUserState state, int userId) {
     * 
     * @param activity
     */
    private ActivityInfo findPackageActivityInfo(Object activity){
        
       
        

        try {

            //首先通过反射找到PackageUserState这个类

            Class mPackageUserStateClass = Class.forName("android.content.pm.PackageUserState");

            //其次找到UserID 在PackageParse中调用generateActivityInfo的时候往上追溯，调用的
            // 是UserHandle.getCallingUserId() 来获取的userID
            //因此首先找到UserHandle这个类，通过反射获取userID

            Class mUserHandleClass = Class.forName("android.os.UserHandle");
            Method method = mUserHandleClass.getMethod("getCallingUserId");

            int userId = (int) method.invoke(null);
            
            //拿到packageParse实例
            Object packageParseInstance = getPackageParse();
            
            
            Method generateActivityInfo = packageParseInstance.getClass().getDeclaredMethod(
                    "generateActivityInfo",activity.getClass(),int.class,
                    mPackageUserStateClass,int.class);

            ActivityInfo info = (ActivityInfo) generateActivityInfo.invoke(packageParseInstance,activity,0,mPackageUserStateClass.newInstance(),userId);
            
            return  info;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        return null;


    }
    




    /**
     * 获取插件的Package信息，也就是Manifest.xml 声明的信息
     * @return
     */
    private Object getPackage(){
        Object packageParse = getPackageParse();
        try {
            Method method = packageParse.getClass().getMethod(getPackageMethodName, File.class,int.class);
           Object packageInstance =  method.invoke(packageParse,pluginFile, PackageManager.GET_ACTIVITIES);
           return packageInstance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 获取Package中静态注册的广播
     * @return
     */
    private Object getPackageReceives(){

        Object packageInstance = getPackage();

        try {
            Field field = packageInstance.getClass().getField("receivers");
            Object receivers =  field.get(packageInstance);
            return receivers;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }







    /**
     * 获取packageParse对象
     * @return
     */
    private Object getPackageParse(){
        try {
            Object object = Class.forName(packageParsePath).newInstance();
            return object;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }




}
