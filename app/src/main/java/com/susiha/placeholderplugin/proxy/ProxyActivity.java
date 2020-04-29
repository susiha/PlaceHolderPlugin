package com.susiha.placeholderplugin.proxy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.susiha.placeholderplugin.manager.PluginManager;
import com.susiha.standard.protocol.ActivityInterface;
import com.susiha.standard.utils.Constants;

import androidx.annotation.Nullable;

/**
 * 代理Activity所谓占位式 就是在宿主中占位一个
 * ProxyActivity 插件中的一切操作都是通过ProxyActivity来参与操作的
 */
public class ProxyActivity extends Activity {


    //使用加载好的资源 这样才能使用到插件中的资源
    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getPluginLoader().getResources();
    }


    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getPluginLoader().getDexClassLoader();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String className = getIntent().getStringExtra(Constants.TargetClassName);
        try {
          Class targetClass = getClassLoader().loadClass(className);
          ActivityInterface targetActivity = (ActivityInterface) targetClass.newInstance();
          targetActivity.jumpIntoPlugin(this);
          targetActivity.onCreate(savedInstanceState);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    /**
     * 插件中调用了startActivity 所以这个方法必须拦截一下
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {

        //自己调用自己 只不过传递的参数改成了即将要调用的Activity
        Intent newIntent  = new Intent(this,ProxyActivity.class);
        newIntent.putExtra(Constants.TargetClassName,intent.getStringExtra(Constants.TargetClassName));
        super.startActivity(newIntent);
    }


    @Override
    public ComponentName startService(Intent service) {
        //调用ProxyService 同样传递的参数改成了即将要调用的Service
        Intent newIntent  = new Intent(this,ProxyService.class);
        newIntent.putExtra(Constants.TargetClassName,service.getComponent().getClassName());
        return super.startService(newIntent);
    }
}
