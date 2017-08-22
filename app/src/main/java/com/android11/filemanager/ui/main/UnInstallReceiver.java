package com.android11.filemanager.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android11.filemanager.event.PackageEvent;
import com.jiepier.filemanager.util.RxBus.RxBus;

/**
 * Created by panruijie on 2017/3/29.
 * Email : zquprj@gmail.com
 */

public class UnInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case Intent.ACTION_PACKAGE_REMOVED: {
                String pkgName = intent.getDataString();
                boolean replacing = intent.getBooleanExtra(
                        Intent.EXTRA_REPLACING, false);
                if (!TextUtils.isEmpty(pkgName) && !replacing) {
                    RxBus.getDefault().post(new PackageEvent(pkgName, PackageEvent.REMOVE));
                }
                break;
            }
            case Intent.ACTION_PACKAGE_REPLACED: {
                String pkgName = intent.getDataString();
                boolean replacing = intent.getBooleanExtra(
                        Intent.EXTRA_REPLACING, false);
                if (!TextUtils.isEmpty(pkgName) && replacing) {
                    RxBus.getDefault().post(new PackageEvent(pkgName, PackageEvent.REPLACE));
                }
                break;
            }
            case Intent.ACTION_PACKAGE_ADDED: {
                String pkgName = intent.getDataString();
                boolean replacing = intent.getBooleanExtra(
                        Intent.EXTRA_REPLACING, false);
                if (!TextUtils.isEmpty(pkgName) && !replacing) {
                    RxBus.getDefault().post(new PackageEvent(pkgName, PackageEvent.ADD));
                }
                break;
            }
        }
    }
}
