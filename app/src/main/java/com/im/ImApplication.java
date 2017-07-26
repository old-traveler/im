package com.im;

import android.app.Application;
import android.graphics.Color;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by hyc on 2017/7/26 08:34
 */

public class ImApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "63252e937865d980c6059d6c5bf426de");
        BmobIM.init(this);
        BmobIM.registerDefaultMessageHandler(new ImMessageHandler());
    }
}
