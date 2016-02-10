/**   
 * @Title: AppsUpgradeFragment.java
 * @Package com.x.activity
 * @Description: TODO 
 
 * @date 2014-1-24 上午10:28:28
 * @version V1.0   
 */

package com.x.ui.activity.zerodata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.connection.manager.CreatHotspotManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;

/**
 * @Description: 零流量分享中的扫描页面
 */

public class ZeroDataQrScanFragment extends BaseFragment {

	private TextView sharePromptTv;
	private ImageView scannerPicIv, sharePromptIv;

	public static Fragment newInstance(Bundle bundle) {
		ZeroDataQrScanFragment fragment = new ZeroDataQrScanFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_scan_wifi_ap_code, null);
		scannerPicIv = (ImageView) rootView.findViewById(R.id.scannerPicIv);
		sharePromptIv = (ImageView) rootView.findViewById(R.id.iv_share_prompt);
		sharePromptTv = (TextView) rootView.findViewById(R.id.tv_share_prompt);

		// 获取信息
		String msg = Constan.ZERO_START + CreatHotspotManager.getCreatHotSpotName(mActivity);
		// 根据信息，生成二维码
		Utils.setQRCodeBackground(scannerPicIv, R.drawable.mas_ic_launcher, msg, mActivity);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Utils.recycleBackgroundResource(scannerPicIv);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTextViewColor(mActivity, sharePromptTv, SkinConstan.THEME_TEXT_LINK);
		SkinConfigManager.getInstance().setViewBackground(mActivity, sharePromptIv, SkinConstan.SHARE_PROMPT_BG);
	}

}
