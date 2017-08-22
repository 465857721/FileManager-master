package com.android11.filemanager.ui.category.picture.dir;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.android11.filemanager.ui.actionmode.ActionModeActivity;
import com.android11.filemanager.R;

import butterknife.BindView;

/**
 * Created by panruijie on 17/1/18.
 * Email : zquprj@gmail.com
 */

public class PictureDirActivity extends ActionModeActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String dirPath;

    @Override
    public int initContentView() {
        return R.layout.activity_picture_manager;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void init() {

        dirPath = getIntent().getStringExtra("dirPath");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content,PictureDirFragment.newInstance(dirPath))
                .commit();

        setTitle(dirPath);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }
}
