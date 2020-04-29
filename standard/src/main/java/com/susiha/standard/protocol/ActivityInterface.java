package com.susiha.standard.protocol;

import android.app.Activity;
import android.os.Bundle;

public interface ActivityInterface {
    /**
     * 跳入到插件中去
     * @param activity
     */
    void jumpIntoPlugin(Activity activity);

    void onCreate(Bundle savedInstanceState);
    void onResume();
    void onStart();
    void onStop();
    void onDestory();
}
