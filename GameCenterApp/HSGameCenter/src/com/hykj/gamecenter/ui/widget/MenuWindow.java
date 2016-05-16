package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.IMenuAdapter;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.UITools;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/28.
 */
public class MenuWindow extends PopupWindow {
    private static final String TAG = "MenuWindow";
    private final Context mContext;
    private IMenuAdapter mAdapter;
    private ArrayList<String> mListTitle;

//    public MenuWindow(Context context) {
//        super(context);
//    }

    public MenuWindow(Context context, IMenuAdapter adapter) {
        super(context);
        this.mContext = context;
        this.mAdapter = adapter;
        initLayout();
    }

    private void initLayout() {
        LinearLayout contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
//        contentLayout.setGravity(Gravity.RIGHT);
        for (int i = 0; i < mAdapter.getCount(); i++) {
//            if (i != 0) {
            //添加分隔线
            View view = mAdapter.getView(i, contentLayout);
            contentLayout.addView(view);

            View divider = mAdapter.getDivider();
            contentLayout.addView(divider);
//            }

        }
        this.setContentView(contentLayout);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setBackgroundDrawable(new ColorDrawable(0000000000));
    }

    private View selectedView;

    public void setSelectItem(int count) {
        View view = ((ViewGroup) getContentView()).getChildAt(2 * count + 1);
        for (int i = 0; i < ((ViewGroup) getContentView()).getChildCount(); i++) {
            Logger.i(TAG, ((ViewGroup) getContentView()).getChildAt(i).toString(), "oddshou");
        }
        view.setSelected(true);
        if (selectedView != null) {
            selectedView.setSelected(false);
        }
            selectedView = view;
    }

    public void showPopupWindow(View parent) {
        if(UITools.isPortrait()){
            if (!this.isShowing()) {
                this.showAsDropDown(parent, parent.getWidth(), 0);
            } else {
                this.dismiss();
            }
        }else {
            int value = mContext.getResources().getDimensionPixelSize(R.dimen.dropdown_value);
            if (!this.isShowing()) {
                this.showAsDropDown(parent, parent.getWidth() - value, 0);
            } else {
                this.dismiss();
            }
        }
    }

    public void showPopupWindowConfigurationChanged(View parent) {
        if (this.isShowing()) {
            this.dismiss();
        }
    }
}
