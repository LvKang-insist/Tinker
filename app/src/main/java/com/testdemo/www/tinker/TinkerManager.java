package com.testdemo.www.tinker;

import android.content.Context;

import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

/**
 * 对 TinkerManager api 做一层封装
 */
public class TinkerManager {

    /**
     * 是否初始化
     */
    private static boolean isInstalled = false;

    private static ApplicationLike mApplike;

    /**
     * 完成 Tinker初始化
     *
     * @param applicationLike
     */
    public static void installTinker(ApplicationLike applicationLike) {
        mApplike = applicationLike;
        if (isInstalled) {
            return;
        }

        //加载补丁文件加载时的异常监听
        LoadReporter loadReporter = new DefaultLoadReporter(applicationLike.getApplication());
        //补丁文件合成阶段的异常监听，
        PatchReporter patchReporter = new DefaultPatchReporter(applicationLike.getApplication());
        PatchListener patchListener = new DefaultPatchListener(applicationLike.getApplication());
        AbstractPatch upgradePatchProcessor = new UpgradePatch();
        //完成 tinker 初始化
        TinkerInstaller.install(applicationLike,
                loadReporter,
                patchReporter,
                patchListener,
                CustomResultService.class,
                upgradePatchProcessor);
        isInstalled = true;
    }

    public static void loadPatch(String path) {
        if (Tinker.isTinkerInstalled()) {
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path);
        }
    }

    /**
     * 通过 ApplicationLike 获取 Context
     */
    private static Context getApplicationContext() {
        if (mApplike != null) {
            return mApplike.getApplication().getApplicationContext();
        }
        return null;
    }

}
