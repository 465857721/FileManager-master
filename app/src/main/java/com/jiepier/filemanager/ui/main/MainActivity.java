package com.jiepier.filemanager.ui.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseDrawerActivity;
import com.jiepier.filemanager.event.CleanChoiceEvent;
import com.jiepier.filemanager.task.PasteTaskExecutor;
import com.jiepier.filemanager.task.UnzipTask;
import com.jiepier.filemanager.task.ZipTask;
import com.jiepier.filemanager.util.ClipBoard;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.ResourceUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.StatusBarUtil;

import java.io.File;

public class MainActivity extends BaseDrawerActivity implements
        ActionMode.Callback, FolderChooserDialog.FolderCallback, MainContact.View {

    private MainPresenter mPresenter;
    private ActionMode mActionMode;
    private ScannerReceiver mScannerReceiver;
    private UnInstallReceiver mUninstallReceiver;
    private int mChoiceCount;

    @Override
    public void initUiAndListener() {
        super.initUiAndListener();

        //扫描sd卡的广播
        mScannerReceiver = new ScannerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addDataScheme("file");
        registerReceiver(mScannerReceiver, intentFilter);

        //进入界面就开始扫描，进行数据更新，时间特别长，一分钟左右
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath())));
        sendBroadcast(intent);

        //监听应用卸载的广播
        mUninstallReceiver = new UnInstallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");
        registerReceiver(mUninstallReceiver, filter);

        mPresenter = new MainPresenter(this);
        mPresenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.paste).setVisible(!ClipBoard.isEmpty());
        menu.findItem(R.id.cancel).setVisible(!ClipBoard.isEmpty());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                mPresenter.clickCancel();
                return true;
            case R.id.paste:
                new PasteTaskExecutor(this, mSdCardFragment.getCurrentPath()).start();
                return true;
            case R.id.folderinfo:
                mPresenter.clickFloderInfo(mSdCardFragment.getCurrentPath());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        this.mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.actionmode, menu);
        if (mChoiceCount > 1) {
            menu.removeItem(R.id.actionrename);
        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.actionmove:
                mPresenter.clickMove();
                return true;
            case R.id.actioncopy:
                mPresenter.clickCopy();
                return true;
            case R.id.actiondelete:
                mPresenter.clickDelete();
                return true;
            case R.id.actionshare:
                mPresenter.clickShare();
                return true;
            case R.id.actionshortcut:
                mPresenter.clickShortCut();
                return true;
            case R.id.actionzip:
                mPresenter.clickZip(mSdCardFragment.getCurrentPath());
                return true;
            case R.id.actionrename:
                mPresenter.clickRename(mSdCardFragment.getCurrentPath());
                return true;
            case R.id.actionall:
                mPresenter.clickSelectAll(mSdCardFragment.getCurrentPath());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.mActionMode = null;
        RxBus.getDefault().post(new CleanChoiceEvent());
    }

    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {

        mPresenter.folderSelect(dialog, folder);
    }

    @Override
    public void cretaeActionMode() {
        if (mActionMode == null) {
            mActionMode = startSupportActionMode(this);
            StatusBarUtil.setColor(this, ResourceUtil.getThemeColor(this), 0);
        }
    }

    @Override
    public void finishActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    @Override
    public void refreshMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void setActionModeTitle(String title) {
        mActionMode.setTitle(title);
    }

    @Override
    public void createShortCut(String[] files) {
        for (String file : files) {
            FileUtil.createShortcut(this, file);
        }
    }

    @Override
    public void showDialog(DialogFragment dialog) {
        dialog.show(fm_v4, TAG_DIALOG);
    }

    @Override
    public void showFolderDialog(String TAG) {
        new FolderChooserDialog.Builder(this)
                .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                .initialPath(mSdCardFragment.getCurrentPath())  // changes initial path, defaults to external storage directory
                .tag(TAG)
                .goUpLabel("Up") // custom go up label, default label is "..."
                .show();
    }

    @Override
    public void startShareActivity(Intent intent) {
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    @Override
    public void setChoiceCount(int count) {
        this.mChoiceCount = count;
    }

    @Override
    public void startZipTask(String fileName, String[] files) {
        final ZipTask task = new ZipTask(this, fileName);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, files);
    }

    @Override
    public void startUnZipTask(File unZipFile, File folder) {
        UnzipTask task = new UnzipTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, unZipFile, folder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        unregisterReceiver(mScannerReceiver);
        unregisterReceiver(mUninstallReceiver);
    }

}

