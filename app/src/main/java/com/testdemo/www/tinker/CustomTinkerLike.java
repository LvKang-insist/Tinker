package com.testdemo.www.tinker;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.meituan.android.walle.WalleChannelReader;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.commonsdk.UMConfigure;

//通过 DefaultLifeCycle 注解来生成我们程序中需要用到的 Application
@DefaultLifeCycle(application = ".MyTinkerApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class CustomTinkerLike extends ApplicationLike {

    public CustomTinkerLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //初始化
        TinkerManager.installTinker(this);

        //当前渠道
        String channel = WalleChannelReader.getChannel(getApplication());
        UMConfigure.init(getApplication(), "5dd5faa24ca357ce6a000f7e", channel, UMConfigure.DEVICE_TYPE_PHONE, "");
// log
        UMConfigure.setLogEnabled(true);
    }
}
