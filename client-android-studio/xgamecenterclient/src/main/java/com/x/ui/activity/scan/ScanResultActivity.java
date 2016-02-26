package com.x.ui.activity.scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;

/**
 * @ClassName: ScanResultActivtiy
 * @Desciption: 二维码扫描结果处理
 
 * @Date: 2014-3-11 下午7:03:08
 */

public class ScanResultActivity extends Activity implements OnClickListener {

	private String result;
	private TextView content;
	private View dividerLineView;
	private LinearLayout firstLayout;
	private LinearLayout secondLayout;
	private TextView leftBtn, rightBtn, cancelBtn;
	private Context context = this;
	private boolean isUrl = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.page_qr_scan_result);
		setContentView(R.layout.activity_scan_result);
		Utils.autoScreenAdapter(this); // 自动适配平板、手机
		result = getIntent().getStringExtra("result");
		initViews();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ScanResultActivity.this.finish();
		return false;
	}

	/**
	 * 初始化UI
	 * @return
	 */
	private void initViews() {
		content = (TextView) findViewById(R.id.tv_content);
		leftBtn = (TextView) findViewById(R.id.btn_left);
		rightBtn = (TextView) findViewById(R.id.btn_right);
		cancelBtn = (TextView) findViewById(R.id.btn_cancel);
		dividerLineView = findViewById(R.id.divider_line);
		firstLayout = (LinearLayout) findViewById(R.id.layout_first);
		secondLayout = (LinearLayout) findViewById(R.id.layout_second);
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		setParams();
	}

	/**
	 * 设置参数，扫描结果处理
	 * 
	 * @return
	 */
	private void setParams() {
		if (result.contains(Constan.HTTP_START) || result.contains(Constan.HTTPS_START)) {

			// 调用浏览器
			isUrl = true;
			firstLayout.setVisibility(View.VISIBLE);
			secondLayout.setVisibility(View.GONE);
			content.setText(changeFontColor(ResourceUtil.getString(context, R.string.scan_qr_result_tips, result)));

		} else {

			// 普通文本
			isUrl = false;
			firstLayout.setVisibility(View.GONE);
			secondLayout.setVisibility(View.VISIBLE);
			content.setText(changeFontColor(ResourceUtil.getString(context, R.string.scan_br_result_tips, result)));
		}

	}

	/**
	 * 转化特定字体颜色
	 * @return
	 */
	private SpannableString changeFontColor(String message) {
		SpannableString spannable = null;
		spannable = new SpannableString(message);
		int end = 0;
		if (isUrl) {
			end = message.indexOf(",");
		} else {
			end = message.length();
		}
		spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.url_link_color)),
				message.indexOf(":") + 1, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// 取消按钮
		case R.id.btn_left:
		case R.id.btn_cancel:
			finish();
			break;

		// 确定按钮
		case R.id.btn_right:
			try {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(result);
				intent.setData(content_url);
				startActivity(intent);
				SharedPrefsUtil.putValue(context, "isFromScanQRCode", true);
			} catch (Exception e) {
				ToastUtil.show(this, getResources().getString(R.string.no_browser_tips), Toast.LENGTH_LONG);
			} finally {
				finish();
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().onPause(this);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(context, leftBtn, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, rightBtn, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, cancelBtn, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, dividerLineView, SkinConstan.TITLE_BAR_BG);
	}
}
