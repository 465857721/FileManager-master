package com.android11.filemanager.ui.category.categorybottom;

import com.android11.filemanager.base.BasePresenter;
import com.android11.filemanager.base.BaseView;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class CategoryBottomContact {

    interface View extends BaseView{

        void showDialog();

        void dimissDialog();

        void setData(ArrayList<String> list);

        void selectAll();

        void clearSelect();

        void setDataByObservable(Observable<ArrayList<String>> observable);
    }

    interface Presenter extends BasePresenter<View>{

        void setIndex(int index);

        void onItemClick(String path);

    }
}
