package com.android11.filemanager.ui.category.picture;


import com.android11.filemanager.base.BaseViewHolder;
import com.android11.filemanager.bean.ImageFolder;
import com.android11.filemanager.R;
import com.android11.filemanager.base.BaseAdapter;

import java.util.List;

/**
 * Created by panruijie on 17/1/18.
 * Email : zquprj@gmail.com
 */

public class PictureAdapter extends BaseAdapter<ImageFolder, BaseViewHolder> {

    private OnItemClickListener mListener;

    public PictureAdapter() {
        super(R.layout.item_picture);
    }

    @Override
    protected void convert(BaseViewHolder holder, ImageFolder item) {

        holder.loadLocal(R.id.iv_firstImageView, item.getFirstImagePath())
                .setText(R.id.tv_dir_name, item.getName())
                .setText(R.id.tv_count, "(" + item.getCount() + ")");

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(item.getDir());
            }
        });
    }

    public void setData(List<ImageFolder> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {

        void onItemClick(String dir);
    }
}
