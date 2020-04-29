package com.susiha.placeholderplugin;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.susiha.placeholderplugin.manager.PluginManager;
import com.susiha.standard.utils.Constants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isLoadPlugin = false;

    /**
     * 加载插件
     * @param view
     */
    public void loadPlugin(View view) {

        isLoadPlugin = PluginManager.getInstance().loadPlugin(this,
                "plugin.apk");

        if(isLoadPlugin){
            Toast.makeText(this,"插件加载成功",Toast.LENGTH_LONG).show();

            Log.i(Constants.DebugTag,"susiha >>> 插件加载成功");
        }

    }

    /**
     * 跳转到插件的Activity
     * @param view
     */
    public void jumpIntoPluginActivity(View view) {

       if(isLoadPlugin){

           XXPermissions.with(this)
                   //.constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                   //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //支持请求6.0悬浮窗权限8.0请求安装权限
                   .permission(Permission.Group.STORAGE) //不指定权限则自动获取清单中的危险权限
                   .request(new OnPermission() {

                       @Override
                       public void hasPermission(List<String> granted, boolean isAll) {
                           PluginManager.getInstance().jumpIntoPluginActivity(MainActivity.this,"plugin.apk");
                       }

                       @Override
                       public void noPermission(List<String> denied, boolean quick) {

                       }
                   });

       }else {

           Toast.makeText(this,"请首先加载插件",Toast.LENGTH_LONG).show();
       }
    }
}
