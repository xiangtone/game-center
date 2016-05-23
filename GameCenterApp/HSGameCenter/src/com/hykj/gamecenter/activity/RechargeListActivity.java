
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.fragment.RechargeListFragment;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.utils.SystemBarTintManager;

public class RechargeListActivity extends Activity {
    private CSCommonActionBar mActionBar;
    private Resources mRes = null;
    //    private final String TAG = "RechargeListActivity";
    private RechargeListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mRes = getResources();
        setContentView(R.layout.activity_recharge_list);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        initView();
        initFragment();
    }

    private void initView() {

        if ((mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar)) != null)
        {
            mActionBar.SetOnActionBarClickListener(actionBarListener);
            mActionBar.setSettingTipVisible(View.GONE);
            mActionBar.setSettingImage(R.drawable.csls_comm_actionbar_help_icon);
        }
        mActionBar.setTitle(mRes.getString(R.string.recharge_history));
    }

    public void initFragment() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (mFragment == null) {
            mFragment = new RechargeListFragment();
        }
        ft.replace(R.id.fragment_detail, mFragment);
        ft.commit();
    }

    private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener() {

        @Override
        public void onActionBarClicked(int position, View view) {

            switch (position) {
                case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
                    onBackPressed();
                    break;
                case CSCommonActionBar.OnActionBarClickListener.SETTING_BNT:
                    Intent intentSetting = new Intent(RechargeListActivity.this,
                            Html5HelpActivity.class);
                    intentSetting.putExtra(StatisticManager.KEY_HTML5_HELP_TITLE,
                            getString(R.string.recharge_actionbar_help_title));
                    intentSetting.putExtra(StatisticManager.KEY_HTML5_HELP_URL,
                            StatisticManager.getConstantRechargeHelp());
                    startActivity(intentSetting);
                    break;
                default:
                    break;
            }
        }
    };

    public void setmDetailFragment(
            RechargeListFragment mDetailFragment) {
        this.mFragment = mDetailFragment;
    }
}
