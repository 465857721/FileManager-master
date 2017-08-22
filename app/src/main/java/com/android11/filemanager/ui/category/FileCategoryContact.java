package com.android11.filemanager.ui.category;

import com.android11.filemanager.base.BasePresenter;
import com.android11.filemanager.base.BaseView;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public interface FileCategoryContact {

    interface View extends BaseView {

        void setMemoryProgress(float progress);

        void setStorageProgress(float progress);

        void setMemoryText(String memory);

        void setStorageText(String storage);
    }

    interface Presenter extends BasePresenter<View> {

        void clickMemoryProgressbar();

        void clickStorageProgressbar();

        void updateMemoryInfo();

        void updateStorageInfo();

        void clickMusic();

        void clickVideo();

        void clickApk();

        void clickDoc();

        void clickPicture();

        void clickZip();
    }
}
