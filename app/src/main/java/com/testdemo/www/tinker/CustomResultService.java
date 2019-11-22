package com.testdemo.www.tinker;

import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;

/**
 * 决定 patch 安装完成后的继续操作，默认实现是杀死进程
 */
public class CustomResultService extends DefaultTinkerResultService {

    private static final String TAG = "CustomResultService";

    //返回 patch 文件的安装结果
    @Override
    public void onPatchResult(PatchResult result) {
        if (result == null) {
            TinkerLog.e(TAG, "DefaultTinkerResultService received null result!!!!");
            return;
        }
        TinkerLog.i(TAG, "DefaultTinkerResultService received a result:%s ", result.toString());

        //first, we want to kill the recover process
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        // if success and newPatch, it is nice to delete the raw file, and restart at once
        // only main process can load an upgrade patch!
        if (result.isSuccess) {
            deleteRawPatchFile(new File(result.rawPatchFilePath));
            if (checkIfNeedKill(result)) {
                TinkerLog.e(TAG, "patch加载成功，重启生效");
            } else {
                TinkerLog.i(TAG, "I have already install the newly patch version!");
            }
        }
    }
}
