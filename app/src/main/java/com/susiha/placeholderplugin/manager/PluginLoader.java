package com.susiha.placeholderplugin.manager;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * 资源和类加载器
 */
public class PluginLoader {

    private Context mContext;
    private String mApkPath;
    private String mDirName;

    private DexClassLoader dexClassLoader;
    private Resources resources;

    /**
     * 初始化DexClassLoader和Resource
     * @param context 上下文环境
     * @param apkPath apk的路径
     * @param dirName 缓存的名称
     */
    public PluginLoader(Context context,String apkPath,String dirName){
        this.mContext = context;
        this.mApkPath = apkPath;
        this.mDirName = dirName;

    }


    /**
     * 初始化PluginLoader
     * @return
     */
    public boolean initPluginLoader(){
        return initDexClassLoader()&&initResource();
    }


    /**
     * 初始化DexClassLoader
     * @return
     */
    private boolean  initDexClassLoader(){

        dexClassLoader = new DexClassLoader
                (mApkPath,
                        mContext.getDir(mDirName,Context.MODE_PRIVATE).getAbsolutePath(),
                        null,
                        mContext.getClassLoader());

        return dexClassLoader ==null?false:true;
    }

    /**
     * 初始化资源
     * @return
     */
    private boolean  initResource(){


        AssetManager assetManager = mContext.getAssets();

        /**
         *     public void setApkAssets(@NonNull ApkAssets[] apkAssets, boolean invalidateCaches) {
         */
        try {
            Method method =AssetManager.class.getMethod("addAssetPath", String.class);
            //构建一个数组
//            Object[] objects = {getApkAssetsInstance()};
            method.invoke(assetManager,mApkPath);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        resources = new Resources(assetManager,
                mContext.getResources().getDisplayMetrics(),
                mContext.getResources().getConfiguration());

        return resources ==null?false:true;

    }

    /**
     * 使用推荐的方法来加载资源 首先是通过反射创建一个ApkAssets对象
     * @return
     */
    private Object getApkAssetsInstance(){

        Object apkAssetsInstance= null;
        try {
           Class apkAssetClass = Class.forName("android.content.res.ApkAssets");
            Method loadFromPath =  apkAssetClass.getMethod("loadFromPath",String.class);
            apkAssetsInstance = loadFromPath.invoke(null,mApkPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return apkAssetsInstance;

    }









    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public void setDexClassLoader(DexClassLoader dexClassLoader) {
        this.dexClassLoader = dexClassLoader;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }
}
