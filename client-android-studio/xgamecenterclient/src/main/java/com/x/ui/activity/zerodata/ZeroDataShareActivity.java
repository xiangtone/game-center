package com.x.ui.activity.zerodata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.guide.ZeroDataGuideActivity;
import com.x.ui.activity.resource.ResourceManagementActivity;

/**
 * 零流量分享首页
 * 
 
 */
public class ZeroDataShareActivity extends BaseActivity implements OnClickListener {

	private TextView deviceName;
	private Context context = this;
	private Button sendBtn, receiveBtn, okBtn;
	private ImageView btnGuide, btnHistory, btnInvitation, shareBtnPendant, okBtnPendant;

	// 最新版界面
	private ImageView currentHead, indicateIcon;
	private View btnLayout, headLayout, currentHeadBg;

	private static final int[] btns = new int[] { R.id.btn_head1, R.id.btn_head2, R.id.btn_head3, R.id.btn_head4,
			R.id.btn_head5, R.id.btn_head6, R.id.btn_head7, R.id.btn_head8, R.id.btn_head9, R.id.btn_ok };

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_share_title);
		setContentView(R.layout.activity_share);
		initUi();
		initNavigation();
	}

	private void initUi() {
		btnLayout = findViewById(R.id.ly_btn_part1_ll);
		headLayout = findViewById(R.id.ly_head_part2_ll);
		btnGuide = (ImageView) findViewById(R.id.btn_guide);
		btnHistory = (ImageView) findViewById(R.id.btn_history);
		btnInvitation = (ImageView) findViewById(R.id.btn_invitation);
		indicateIcon = (ImageView) findViewById(R.id.img_indicate);
		deviceName = (TextView) findViewById(R.id.tv_device_name);
		deviceName.setText(ZeroDataConnectHelper.getZeroShareNickName(this));
		sendBtn = (Button) findViewById(R.id.sharing_send_btn);
		receiveBtn = (Button) findViewById(R.id.sharing_receive_btn);
		okBtn = (Button) findViewById(R.id.btn_ok);
		shareBtnPendant = (ImageView) findViewById(R.id.iv_share_pendant);
		okBtnPendant = (ImageView) findViewById(R.id.iv_ok_pendant);

		// 注册监听器
		for (int btn : btns) {
			findViewById(btn).setOnClickListener(this);
		}
		// 初始化头像资源
		currentHead = (ImageView) findViewById(btns[8]);
		currentHead.setImageResource(ZeroDataResourceHelper.getSelfZerodataHeadPortrait(context));
		// 初始化头像背景资源
		currentHeadBg = findViewById(R.id.img_head_bg);
		Utils.setBackgroundResource(currentHeadBg, ZeroDataResourceHelper.getSelfZerodataHeadBgPortrait(context),
				context);

		sendBtn.setOnClickListener(this);
		receiveBtn.setOnClickListener(this);
		btnInvitation.setOnClickListener(this);
		btnHistory.setOnClickListener(this);
		btnGuide.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		// History
		case R.id.btn_history:
			btnHistory.setEnabled(false);
			ConnectHotspotManage.getInstance(context).saveNetworkState(context);
			startActivity(new Intent(context, ZeroDataTransferHistoryActivity.class));
			finish();
			break;

		// Invitation
		case R.id.btn_invitation:
			btnInvitation.setEnabled(false);
			Intent instatllIntent = new Intent(context, InstallationUplayActivity.class);
			startActivity(instatllIntent);
			break;

		// Guide Me
		case R.id.btn_guide:
			Intent guideIntent = new Intent(context, ZeroDataGuideActivity.class);
			startActivity(guideIntent);
			break;

		// 我要分享
		case R.id.sharing_send_btn:
			sendBtn.setEnabled(false);
			Intent resourceintent = new Intent(context, ResourceManagementActivity.class);
			resourceintent.putExtra("MODE", NativeResourceConstant.SHARE_MODE);
			startActivity(resourceintent);
			// finish();
			break;

		// 我要接收
		case R.id.sharing_receive_btn:
			receiveBtn.setEnabled(false);
			if (!StorageUtils.isSDCardPresent()) {
				ToastUtil.show(this, this.getResources().getString(R.string.sdcard_not_found), Toast.LENGTH_SHORT);
			} else {
				ConnectHotspotManage.getInstance(context).saveNetworkState(context);
				Intent receiveIntent = new Intent(context, ZeroDataClientActivity.class);
				startActivity(receiveIntent);
				// finish();
			}
			break;

		// 头像1
		case R.id.btn_head1:
			mHandler.sendMessage(mHandler.obtainMessage(0, 0));
			break;
		// 头像2
		case R.id.btn_head2:
			mHandler.sendMessage(mHandler.obtainMessage(0, 1));
			break;
		// 头像3
		case R.id.btn_head3:
			mHandler.sendMessage(mHandler.obtainMessage(0, 2));
			break;
		// 头像4
		case R.id.btn_head4:
			mHandler.sendMessage(mHandler.obtainMessage(0, 3));
			break;
		// 头像5
		case R.id.btn_head5:
			mHandler.sendMessage(mHandler.obtainMessage(0, 4));
			break;
		// 头像6
		case R.id.btn_head6:
			mHandler.sendMessage(mHandler.obtainMessage(0, 5));
			break;
		// 头像7
		case R.id.btn_head7:
			mHandler.sendMessage(mHandler.obtainMessage(0, 6));
			break;
		// 头像8
		case R.id.btn_head8:
			mHandler.sendMessage(mHandler.obtainMessage(0, 7));
			break;
		// 头像0（当前头像）
		case R.id.btn_head9:
			if (btnLayout.getVisibility() == View.VISIBLE) {
				btnLayout.setVisibility(View.GONE);
				headLayout.setVisibility(View.VISIBLE);
				indicateIcon.setVisibility(View.VISIBLE);
			} else {
				headLayout.setVisibility(View.GONE);
				indicateIcon.setVisibility(View.GONE);
				btnLayout.setVisibility(View.VISIBLE);
			}
			break;
		// ok按钮
		case R.id.btn_ok:
			headLayout.setVisibility(View.GONE);
			indicateIcon.setVisibility(View.GONE);
			btnLayout.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Utils.recycleBackgroundResource(currentHeadBg);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		if (btnHistory != null) {
			btnHistory.setEnabled(true);
			btnInvitation.setEnabled(true);
			sendBtn.setEnabled(true);
			receiveBtn.setEnabled(true);
		}
	}

	/**
	 * Handler 更新UI界面
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// Setter ImageView src
				currentHead.setImageResource(ZeroDataResourceHelper.headPortraitArray[(Integer) msg.obj]);

				// Setter View Background
				Utils.setBackgroundResource(currentHeadBg, ZeroDataResourceHelper.headBgArray[(Integer) msg.obj],
						context);

				ZeroDataResourceHelper.saveZerodataHeadPortrait(context, (Integer) msg.obj);
				break;
			}
		};
	};

	@Override
	public void onBackPressed() {
		if (btnLayout.getVisibility() == View.VISIBLE) {
			super.onBackPressed();
		} else {
			headLayout.setVisibility(View.GONE);
			indicateIcon.setVisibility(View.GONE);
			btnLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			onBackPressed();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
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
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.page_share_title);
		mNavigationView.setOnClickListener(this);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, sendBtn, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, okBtn, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, btnInvitation, SkinConstan.SHARE_INVITE_BTN);
		SkinConfigManager.getInstance().setViewBackground(context, btnHistory, SkinConstan.SHARE_HISTORY_BTN);
		SkinConfigManager.getInstance().setViewBackground(context, btnGuide, SkinConstan.SHARE_GUIDE_BTN);
		SkinConfigManager.getInstance().setViewBackground(context, shareBtnPendant, SkinConstan.SHARE_BTN_PENDANT);
		SkinConfigManager.getInstance().setViewBackground(context, okBtnPendant, SkinConstan.SHARE_BTN_PENDANT);
	}
}
