package com.jiepier.filemanager.ui.sdcard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.base.BaseFragmentPagerAdapter;
import com.jiepier.filemanager.event.NewTabEvent;
import com.jiepier.filemanager.event.SnackBarEvent;
import com.jiepier.filemanager.ui.common.CommonFragment;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.Settings;

import java.util.LinkedList;

import butterknife.BindView;

/**
 * Created by JiePier on 16/12/7.
 */

public class SDCardFragment extends BaseFragment {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private String path;
    private LinkedList<Fragment> fragmentList;
    private LinkedList<String> titleList;
    private BaseFragmentPagerAdapter mAdapter;

    public static SDCardFragment newInstance(String path){
        SDCardFragment instance = new SDCardFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments() != null ? getArguments().getString("path") : Settings.getDefaultDir();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

        String[] filePath = path.split("/");

        fragmentList = new LinkedList<>();
        titleList = new LinkedList<>();

        for (int i = 0; i < filePath.length; i++) {
            fragmentList.add(CommonFragment.newInstance(FileUtil.getPath(filePath, i)));
            titleList.add(FileUtil.getFileName(FileUtil.getPath(filePath, i)));
        }

        mAdapter = new BaseFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);

        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewpager.setCurrentItem(mAdapter.getCount() - 1);
    }

    @Override
    protected void initListeners() {

        RxBus.getDefault()
                .IoToUiObservable(NewTabEvent.class)
                .subscribe(event -> {
                    removeFrgament(viewpager.getCurrentItem() + 1);
                    BaseFragment baseFragment = CommonFragment.newInstance(event.getPath());
                    fragmentList.add(baseFragment);
                    titleList.add(FileUtil.getFileName(event.getPath()));
                    this.mAdapter.notifyDataSetChanged();
                    viewpager.setCurrentItem(mAdapter.getCount() - 1);
                    setCurrentPath();
                }, Throwable::printStackTrace);
    }

    private void setCurrentPath() {

        if (titleList.size() == 1) {
            path = "/";
        }

        path = "";
        for (int i = 1; i < titleList.size(); i++) {
            path += titleList.get(i);
        }

    }

    private void removeFrgament(int currentItem) {

        int count = mAdapter.getCount();
        for (int i = currentItem; i < count; i++) {
            fragmentList.remove(currentItem);
            titleList.remove(currentItem);
        }
        setCurrentPath();
    }

    @Override
    protected void initData() {

    }

    public boolean onBackPressed() {
        if (mAdapter.getCount() != 1) {
            removeFrgament(mAdapter.getCount() - 1);
            mAdapter.notifyDataSetChanged();
            viewpager.setCurrentItem(mAdapter.getCount() - 1);
            return true;
        } else {
            RxBus.getDefault().post(new SnackBarEvent(getString(R.string.sure_to_exit)));
        }
        return false;
    }

    public String getPath() {
        return path;
    }

    public String getCurrentPath() {
        if (titleList.size() == 1) {
            path = "/";
        }

        path = "";
        for (int i = 1; i <= viewpager.getCurrentItem(); i++) {
            path += titleList.get(i);
        }
        return path;
    }
}
