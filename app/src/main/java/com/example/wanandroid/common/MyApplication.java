package com.example.wanandroid.common;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // 这里后续会用来初始化网络库、数据库等
    }

    public static Context getContext() {
        return context;
    }
}
