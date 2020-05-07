package com.susiha.pluginmodule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.susiha.standard.utils.Constants;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(hostAppActivity,"Hello",Toast.LENGTH_LONG).show();

        findViewById(R.id.next_Activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(hostAppActivity,NextActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.next_boardcast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Constants.BroadCastAction);
                intent.putExtra(Constants.BroadCastParam,"Hello pluginBoardCast");
                sendBroadcast(intent);
               Toast.makeText(hostAppActivity,"发送广播",Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.next_Service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(hostAppActivity,MyService.class);

                hostAppActivity.startService(intent);
                Toast.makeText(hostAppActivity,"启动服务",Toast.LENGTH_LONG).show();

            }
        });


    }
}
