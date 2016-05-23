
package com.hykj.gamecenter.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.ImproperDetailActivity;
import com.hykj.gamecenter.activity.ImproperReportActivity;
import com.hykj.gamecenter.protocol.Apps;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.Permission;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.utils.Logger;

import java.util.ArrayList;

public class MorePopWindow extends PopupWindow {
    private View conentView;
    private MorePopWindow pop;
    private AppInfo mApp;

    public MorePopWindow(final Activity context, AppInfo app) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popwindow_dropdown_item, null);
        pop = this;
        mApp = app;
        //        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        //        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        final Apps.Permission[] pers = mApp.permission;
        Button btnViewPermissions = (Button) conentView.findViewById(R.id.btn_view_permissions);
        if (pers.length > 0) {
            btnViewPermissions.setVisibility(View.VISIBLE);
        } else {
            btnViewPermissions.setVisibility(View.GONE);
        }

        btnViewPermissions.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, ImproperDetailActivity.class);
                Logger.i("ImproperDetailActivity", "pers == " + pers.length);
                if (pers.length > 0) {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < pers.length; i++) {
                        Permission per = pers[i];
                        String str = per.toString().trim();
                        list.add(str);
                    }
                    Logger.i("ImproperDetailActivity", "list == " + list);
                    intent.putStringArrayListExtra(StatisticManager.APP_PERMISSION, list);
                }

                // 上报访问当前页面
                ReportedInfo builder = new ReportedInfo();
                builder.statActId = ReportConstants.STATACT_ID_IMPROPER_DETAIL;
                //                builder.statActId2 = PAGE_INDEX.IMPROPER_DETAIL;
                builder.ext1 = "" + mApp.appId;
                ReportConstants.getInstance().reportReportedInfo(builder);
                Logger.i("ImproperDetailActivity", "builder == " + builder);
                context.startActivity(intent);
                //                CSToast.show(context, "btnViewPermissions click");
                pop.dismiss();
            }
        });

        Button btnReportErrors = (Button) conentView.findViewById(R.id.btn_report_errors);
        btnReportErrors.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //                CSToast.show(context, "btnReportErrors click");
                Intent intent = new Intent(context, ImproperReportActivity.class);
                intent.putExtra(StatisticManager.APP_ID, mApp.appId);
                context.startActivity(intent);
                pop.dismiss();
            }
        });

        // 设置SelectPicPopupWindow的View  
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽  
        //        this.setWidth(w / 2 + 50);
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态  
        this.update();
        // 实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
        // 设置SelectPicPopupWindow弹出窗体动画效果  
        //        this.setAnimationStyle(R.style.AnimationPreview);

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
//            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, -24);
            this.showAsDropDown(parent, parent.getWidth(), 10);
            //            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }
}
