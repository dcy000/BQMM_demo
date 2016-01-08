package com.siyanhui.bqmm_demo;

import android.app.Application;

import com.melink.bqmmsdk.sdk.BQMMSdk;

public class DemoApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        BQMMSdk.getInstance().initConfig(this, Constants.appId, Constants.appSecret);
    }
}
