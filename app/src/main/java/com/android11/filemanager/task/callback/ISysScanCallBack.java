package com.android11.filemanager.task.callback;

import com.android11.filemanager.bean.JunkInfo;

import java.util.ArrayList;

/**
 * Created by panruijie on 2017/2/22.
 * Email : zquprj@gmail.com
 */

public interface ISysScanCallBack {

    void onBegin();

    void onProgress(JunkInfo junkInfo);

    void onCancel();

    void onFinish(ArrayList<JunkInfo> mChildren);

    void onOverTime();
}
