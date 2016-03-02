package com.x.ui.activity.zerodata;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;

/**
 * 邀请好友安装Uplay页面
 * 
 
 * 
 */
public class InstallationUplayActivity extends BaseActivity implements OnClickListener {

	private TextView middleTv;
	private Context context = this;
	private ImageView browserIv, scanIv;
	private String browserStr, scanStr, websiteStr;
	private LinearLayout oneItem, twoItem, threeItem, fourItem;

	private ImageView mGobackIv;
	private TextView mTitleTv,mShareFreeTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_share_invite_to_install);
		setContentView(R.layout.activity_installation_uplay);
		init();
		setParams();
		initNavigation();
		// CreatHotspotManager.getInstance(this).closeOpenHotspot();
	}

	protected void onResume() {
		super.onResume();
		setSkinTheme();
	}

	/**
	* @Title: initNavigation 
	* @Description: 初始化导航栏 
	* @param     
	* @return void
	 */
	private void initNavigation() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mShareFreeTv = (TextView) findViewById(R.id.shareFreeTv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.page_share_invite_to_install);
		mShareFreeTv.setText(ResourceUtil.getString(context,R.string.sharing_invitation_title,ResourceUtil.getString(context,R.string.app_name)));
		mNavigationView.setOnClickListener(this);
	}

	private void init() {
		browserIv = (ImageView) findViewById(R.id.browserIv);
		scanIv = (ImageView) findViewById(R.id.scanIv);
		middleTv = (TextView) findViewById(R.id.middleTv);
		middleTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		oneItem = (LinearLayout) findViewById(R.id.oneItemll);
		twoItem = (LinearLayout) findViewById(R.id.twoItemll);
		threeItem = (LinearLayout) findViewById(R.id.threeItemll);
		fourItem = (LinearLayout) findViewById(R.id.fourItemll);
		oneItem.setOnClickListener(this);
		twoItem.setOnClickListener(this);
		threeItem.setOnClickListener(this);
		fourItem.setOnClickListener(this);

		browserStr = ResourceUtil.getString(context,R.string.sharing_invitation_browserdownload,ResourceUtil.getString(context,R.string.app_name));
		scanStr = getResources().getString(R.string.sharing_invitation_scanorcode);
	}

	private void setParams() {
		// 获取网址信息
		websiteStr = "http://" + getResources().getString(R.string.website_url);
		// 根据信息，生成二维码
		Utils.setQRCodeBackground(scanIv, R.drawable.mas_ic_launcher, websiteStr, context);
	}

	@Override
	public void finish() {
		ZeroDataResourceHelper.saveFromActivity(this, ZeroDataConstant.ZERO_DATA_INVITE_ACTIVITY_KEY, "");
		super.finish();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		case R.id.oneItemll:
			ConnectHotspotManage.getInstance(this).saveNetworkState(this);
			Intent intent = new Intent(this, AcceptTheWayActivity.class);
			startActivity(intent);
			break;
		case R.id.twoItemll:
			try {
				Uri smsToUri = Uri.parse("smsto:");
				Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
				smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				smsIntent.putExtra("sms_body", ResourceUtil.getString(context,R.string.sms_body,ResourceUtil.getString(context,R.string.app_name),ResourceUtil.getString(context,R.string.website_url)));
				startActivity(smsIntent);
			} catch (Exception e) {
				ToastUtil.show(this, R.string.no_sms_tips, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.threeItemll:
			browserIv.setVisibility(View.VISIBLE);
			scanIv.setVisibility(View.GONE);
			middleTv.setText(browserStr);
			break;
		case R.id.fourItemll:
			scanIv.setVisibility(View.VISIBLE);
			browserIv.setVisibility(View.GONE);
			middleTv.setText(scanStr);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Utils.recycleBackgroundResource(scanIv);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setTextViewColor(context, middleTv, SkinConstan.THEME_TEXT_LINK);
	}

}
