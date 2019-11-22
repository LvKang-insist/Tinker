package com.testdemo.www.tinker;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.File;

/**
 * 应用程序 Tinker 更新服务
 * 1，从服务器下载 patch 文件
 * 2，使用 TinkerManager 完成 patch 文件的加载
 * 3，patch 文件会在下次启动时生效
 */
public class TinkerService extends Service {

    /**
     * 文件后缀名
     */
    private static final String FILE_END = ".apk";
    /**
     * 下载 patch 文件信息
     */
    private static final int DOWNLOAD_PATCH = 0x01;
    /**
     * 检查是否有 patch 更新
     */
    private static final int UPDATE_PATCH = 0x02;


    /**
     * patch 要保存的文件夹
     */
    private String mPatchFileDir;
    /**
     * patch 文件保存路径
     */
    private String mFilePath;



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PATCH:
                    checkPatchInfo();
                    break;
                case DOWNLOAD_PATCH:
                    downloadPatch();
                    break;
            }
        }
    };

    private void downloadPatch() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mFilePath = mPatchFileDir.concat("tinker")
                        .concat(FILE_END);
                TinkerManager.loadPatch(mFilePath);
                Log.e("----", "downloadPatch: "+Thread.currentThread() );
            }
        });
       /* RequestCenter.downloadFile(mBasePatchInfo.data.downloadUrl, mFilePath, new DisposeDownloadListener() {
            @Override
            public void onProgress(int progrss) {
                //下载进度
            }

            @Override
            public void onSuccess(Object responseObj) {
                TinkerManager.loadPatch(mFilePath);
            }

            @Override
            public void onFailure(Object reasonObj) {
                stopSelf();
            }
        });*/
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //检查是否有 patch 更新
        mHandler.sendEmptyMessage(UPDATE_PATCH);
        return START_NOT_STICKY;
    }

    private void init() {
        mPatchFileDir = getExternalCacheDir().getAbsolutePath() + "/tPatch/";
        File patchFileDir = new File(mPatchFileDir);
        try {
            if (!patchFileDir.exists()) {
                //文件夹不存在则创建
                patchFileDir.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //无法创建文件，终止服务
            stopSelf();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 检查是否有更新
     */
    private void checkPatchInfo() {

        mHandler.sendEmptyMessage(DOWNLOAD_PATCH);

       /* RequestCenter.requestPatchUpdateInfo(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
               *//* mBasePatchInfo = (BasePatch) responseObj;
                mHandler.sendEmptyMessage(DOWNLOAD_PATCH);*//*
                String str = (String) responseObj;
                Log.e("--------", "onSuccess: " + str);
            }

            @Override
            public void onFailure(Object reasonObj) {
                stopSelf();
            }
        });*/
    }
}
