package com.android11.filemanager.ui.category.picture;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android11.filemanager.base.BaseFragment;
import com.android11.filemanager.bean.ImageFolder;
import com.android11.filemanager.R;
import com.jiepier.filemanager.widget.DividerGridItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by panruijie on 17/1/18.
 * Email : zquprj@gmail.com
 */

public class PictureFragment extends BaseFragment implements PictureContact.View{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MaterialDialog mDialog;
    private PictureAdapter mAdapter;
    private PicturePresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category_item;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        mDialog = new MaterialDialog.Builder(getContext())
                .content(R.string.loading)
                .progress(true,0)
                .build();
    }

    @Override
    protected void initData() {
        mAdapter = new PictureAdapter();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
        recyclerView.setAdapter(mAdapter);

        mPresenter = new PicturePresenter(getContext());
        mPresenter.attachView(this);
        mPresenter.getData();
    }

    @Override
    protected void initListeners() {

        mAdapter.setOnItemClickListener(dir -> {
            mPresenter.onItemClick(dir);
        });
    }

    @Override
    public void showDialog() {
        mDialog.show();
    }

    @Override
    public void dimissDialog() {
        mDialog.dismiss();
    }

    @Override
    public void setData(ArrayList<ImageFolder> list) {

        /*for (ImageFolder folder:list){
            Log.w(TAG,folder.getDir());
        }*/
        mAdapter.setData(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
