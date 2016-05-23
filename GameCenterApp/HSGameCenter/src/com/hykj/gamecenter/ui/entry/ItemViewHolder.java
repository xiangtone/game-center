
package com.hykj.gamecenter.ui.entry;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.ui.DownloadStateViewCustomization;
import com.hykj.gamecenter.ui.TextNameFlagView;
import com.hykj.gamecenter.ui.widget.SnapshotImageView;

public class ItemViewHolder {

    public View frame;
    public ImageView icon;
    public TextView apprank;
//    public TextView appname;
//    public LinearLayout apprecommlinear;

    //    public TextView appInfo;
    public TextView mAppDownload;
    public TextView mAppSize;
    public RatingBar rating;
    public TextView recommendInfo;
    public SnapshotImageView snapshot;
    public TextView dividerLine;
    public CheckBox checkBox;
    public DownloadStateViewCustomization downStateView;
    public TextNameFlagView appname;

    public ItemViewHolder(View view, boolean showSnapShot) {
        frame = view;
        apprank = (TextView) view.findViewById(R.id.app_rank);
        appname = (TextNameFlagView) view.findViewById(R.id.app_name_flag);
//        appname = (TextView) view.findViewById(R.id.app_name);
//        apprecommlinear = (LinearLayout) view.findViewById(R.id.linear_recomm);

        //        appInfo = (TextView) view.findViewById(R.id.app_info);
        mAppDownload = (TextView) view.findViewById(R.id.app_download);
        mAppSize = (TextView) view.findViewById(R.id.app_size);
        rating = (RatingBar) view.findViewById(R.id.app_rating);
        recommendInfo = (TextView) view.findViewById(R.id.app_recommendInfo);
        if (showSnapShot) {
            snapshot = (SnapshotImageView) view.findViewById(R.id.app_snapshot);
            snapshot.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            icon = (ImageView) view.findViewById(R.id.app_icon);
        }

        dividerLine = (TextView) view.findViewById(R.id.list_item_divider);
        // 得到选项checkBox
        checkBox = (CheckBox) view.findViewById(R.id.edit_check_box);

        // 下载状态按钮
        downStateView = (DownloadStateViewCustomization) view
                .findViewById(R.id.downloadstateview);

    }
}
