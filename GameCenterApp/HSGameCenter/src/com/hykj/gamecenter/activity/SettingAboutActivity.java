
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.utils.SystemBarTintManager;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utilscs.LogUtils;

public class SettingAboutActivity extends Activity implements
        OnActionBarClickListener {
    private CSCommonActionBar mActionBar;
    private int nClickLogoCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.dialog_about);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        if ((mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar)) != null) {
            mActionBar.SetOnActionBarClickListener(this);
        }

        LogUtils.d("App.VersionName : " + App.VersionName().trim());
        TextView versionView = (TextView) findViewById(R.id.app_about_version);
        LogUtils.d("App.VersionName( ).trim( ).substring( 0 , 2 ) ): "
                + App.VersionName().trim().substring(0, 3));
        versionView.setText(getString(R.string.setting_about_version, App
                .VersionName().trim().substring(0, 3)));

        ImageView imageView = (ImageView) findViewById(R.id.opendebug);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nClickLogoCount++;
                if (nClickLogoCount >= 10) {
                    App.openDebugMode();
                }
            }
        });

        LinearLayout officeWeb = (LinearLayout) findViewById(R.id.officeWeb);
        officeWeb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri web = Uri.parse("http://www.eaglenet.cn");
                Intent i = new Intent(Intent.ACTION_VIEW, web);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        TextView declaration = (TextView) findViewById(R.id.declaration);
        declaration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri web = Uri.parse("http://www.cs.cc/apps/disclaimer");
                Intent i = new Intent(Intent.ACTION_VIEW, web);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        LinearLayout telephone = (LinearLayout) findViewById(R.id.telephone);
        telephone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Tools.isHasSim()) {
                    Intent intentDial = new Intent(Intent.ACTION_CALL, Uri
                            .parse("tel:" + "075532998080"));
                    startActivity(intentDial);
                }

            }
        });
    }

    @Override
    public void onActionBarClicked(int position, View view) {
        switch (position) {
            case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
                onBackPressed();
                break;
            default:
                break;
        }

    }

}
