package com.zhuzb.itsPet;

import android.app.Application;

import com.mob.MobSDK;

import org.xutils.x;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author zhuzb
 * @date on 2018/4/11 0011
 * @email zhuzhibo2017@163.com
 */

public class LYJApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init( this );//Xutils初始化
        MobSDK.init(this);
    }
}
