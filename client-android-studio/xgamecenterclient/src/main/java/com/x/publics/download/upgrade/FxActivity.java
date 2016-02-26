package com.x.publics.download.upgrade;

import java.io.File;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.Utils;

/**
 * 
 * @ClassName: FxActivity
 * @Description: 通知栏、详情、安装
 
 * @date 2014-5-23 上午10:58:15
 * 
 */
public class FxActivity extends Activity implements OnClickListener {

	private View dividerLineView;
	private boolean inited = false;
	private Context context = this;
	private TextView leftBtn, rightBtn, tvContent;
	private DownloadBroadcast mDownloadBroadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UpgradeManager.FxActivity(this);
		initData();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!inited)
			registDownloadReceiver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(mDownloadBroadcast);
	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 初始化数据
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void initData() {
		// 清除通知栏
		if (getIntent().getBooleanExtra("ACTION_PENDING", false)) {
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Service.NOTIFICATION_SERVICE);
			notificationManager.cancel(UpgradeManager.HANDLER_DOWNLOAD_SUCCESS);
		}

		// 根据参数，进入对应的界面
		switch (getIntent().getIntExtra("ACTION_ID", UpgradeManager.NOTI_DETAIL_BUTTON_ID)) {
		// 详情界面
		case UpgradeManager.NOTI_DETAIL_BUTTON_ID:
			setContentView(R.layout.float_window_view);
			Utils.autoScreenAdapter(this); // 自动适配平板、手机
			initView();
			break;

		// 安装界面
		case UpgradeManager.NOTI_INSTALL_BUTTON_ID:
			toInstall();
			FxActivity.this.finish();
			break;
		}
	}

	/**
	 * 
	 * @Title: initView
	 * @Description: 初始化界面
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void initView() {
		// TODO Auto-generated method stub
		tvContent = (TextView) findViewById(R.id.tv_content);
		/*
		 * 只有调用了该方法，TextView才能不依赖于ScrollView而实现滚动的效果。
		 * 要在XML中设置TextView的textcolor，否则，当TextView被触摸时，会灰掉。
		 */
		tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());

		leftBtn = (TextView) findViewById(R.id.btn_left);
		rightBtn = (TextView) findViewById(R.id.btn_right);
		dividerLineView = findViewById(R.id.divider_line);
		rightBtn.setText(R.string.dialog_btn_update);
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		setSkinTheme();
		setParams();
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(context, leftBtn, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, rightBtn, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, dividerLineView, SkinConstan.TITLE_BAR_BG);
	}

	/**
	 * 
	 * @Title: setParams
	 * @Description: 设置参数
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setParams() {
		// TODO Auto-generated method stub
		String msg = UpgradeManager.getInstence(context).getUpgradeInfo();
		Spanned spanned = Html.fromHtml(msg); // html格式处理
		tvContent.setText(spanned);
	}

	@Override
	public void onBackPressed() {
		// 3-强制升级
		if (3 == UpgradeManager.getInstence(context).getUpgradeType()
				&& !UpgradeManager.getInstence(context).isDownloading()) {
			// 停止下载任务
			stopDownload();
		} else {
			FxActivity.this.finish();
		}
	}

	/**
	 * 
	 * @Title: setParams
	 * @Description: 全局按钮事件处理
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		// 左边按钮
		case R.id.btn_left:
			onBackPressed();
			break;

		// 右边按钮
		case R.id.btn_right:
			toInstall();
			FxActivity.this.finish();
			break;
		}
	}

	/**
	 * @Title: toInstall
	 * @Description: 安装升级包
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void toInstall() {
		// TODO Auto-generated method stub
		String localPath = UpgradeManager.getInstence(context).getLocalPath();
		if (TextUtils.isEmpty(localPath)) {
			// 文件路径不存在，重新下载
			reDownload();
		} else {
			File file = new File(localPath);
			if (file.exists()) {
				// 文件存在，进行安装
				Tools.installAppCommon(context, localPath);
			} else {
				// 文件不存在，重新下载
				reDownload();
			}
		}
	}

	/**
	 * @Title: reDownload
	 * @Description: 重新下载文件
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void reDownload() {
		// TODO Auto-generated method stub
		UpgradeManager.getInstence(context).startDownload(UpgradeManager.START_ON_NOTIFICATION);

	}

	//=====================================注册广播===================================

	private void registDownloadReceiver() {
		mDownloadBroadcast = new DownloadBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_FINISH_ACTIVITY);
		BroadcastManager.registerReceiver(mDownloadBroadcast, filter);
		inited = true;
	}

	/**
	 * @Title: stopDownload
	 * @Description: 停止下载任务
	 * @param
	 * @return void
	 * @throws
	 */
	public void stopDownload() {
		Intent intent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		intent.putExtra(MyIntents.TYPE, MyIntents.Types.STOP);
		startService(intent);
	}

	private class DownloadBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// 强制升级、应用退出
			Utils.exitSystem();
			FxActivity.this.finish();
		}
	}

}
