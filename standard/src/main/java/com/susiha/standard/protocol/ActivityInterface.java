package com.susiha.standard.protocol;

import android.app.Activity;
import android.os.Bundle;

public interface ActivityInterface extends BaseProtocol {

    void onCreate(Bundle savedInstanceState);
    void onResume();
    void onStart();
    void onStop();
    void onDestory();
}
