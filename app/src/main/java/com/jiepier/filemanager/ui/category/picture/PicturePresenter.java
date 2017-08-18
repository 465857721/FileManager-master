package com.jiepier.filemanager.ui.category.picture;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.ui.category.picture.dir.PictureDirActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by panruijie on 17/1/18.
 * Email : zquprj@gmail.com
 */

public class PicturePresenter implements PictureContact.Presenter {

    private Context mContext;
    private PictureContact.View mView;
    private CategoryManager mCategoryManager;
    private CompositeSubscription mCompositeSubscription;

    public PicturePresenter(Context context){
        this.mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        mCategoryManager = CategoryManager.getInstance();
    }

    @Override
    public void getData() {

        mView.showDialog();

        mCategoryManager.getPictureList()
                .subscribe(imageFolders -> {

                    mView.setData(imageFolders);
                    mView.dimissDialog();

                }, Throwable::printStackTrace);
    }

    @Override
    public void onItemClick(String dirPath) {
        Intent intent = new Intent(mContext, PictureDirActivity.class);
        intent.putExtra("dirPath", dirPath);
        mContext.startActivity(intent);
    }

    @Override
    public void attachView(@NonNull PictureContact.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (mCompositeSubscription.isUnsubscribed())
            this.mCompositeSubscription.unsubscribe();
        this.mCompositeSubscription = null;
    }
}
