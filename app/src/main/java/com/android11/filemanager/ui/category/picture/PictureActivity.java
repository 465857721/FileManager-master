package com.android11.filemanager.ui.category.picture;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.android11.filemanager.base.BaseActivity;
import com.android11.filemanager.R;
import com.android11.filemanager.constant.AppConstant;

import butterknife.BindView;

/**
 * Created by panruijie on 17/1/18.
 * Email : zquprj@gmail.com
 */

public class PictureActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
    public void initUiAndListener() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content,new PictureFragment())
                .commit();

        setTitle(AppConstant.PICTURE);
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
