package com.x.ui.activity.appdetail;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.account.AccountManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.CommentRequest;
import com.x.publics.http.model.DetailResponse;
import com.x.publics.http.model.MasUser;
import com.x.publics.http.model.CommentRequest.CommentData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.AppInfoBean;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ProgressDialogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;

/**
 * @ClassName: CommentActivity
 * @Desciption: 发布评论界面
 
 * @Date: 2014-1-22 下午5:38:00
 */
public class CommentActivity extends Activity implements OnClickListener {

	/* attr of views */
	private int mNum = 140; // 最大值
	private RatingBar stars;
	private EditText content;
	private ImageView appIcon;
	private TextView leftChars;
	private Context mContext = this;
	private TextView leftBtn, rightBtn;
	private AppInfoBean appInfoBean;
	private LinearLayout commentLayout;
	private TextView appName, appVersion;
	private boolean flag = false;
	private View dividerLineView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_comment);
		Utils.autoScreenAdapter(this); // 自动适配平板、手机
		appInfoBean = getIntent().getParcelableExtra("appInfoBean");
		initViews();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	/* initialize view */
	private void initViews() {
		dividerLineView = findViewById(R.id.divider_line);
		content = (EditText) findViewById(R.id.et_content);
		leftBtn = (TextView) findViewById(R.id.btn_left);
		rightBtn = (TextView) findViewById(R.id.btn_right);
		stars = (RatingBar) findViewById(R.id.rb_stars);
		stars.setStepSize(1);
		appName = (TextView) findViewById(R.id.tv_app_name);
		appIcon = (ImageView) findViewById(R.id.img_app_icon);
		leftChars = (TextView) findViewById(R.id.tv_left_chars);
		appVersion = (TextView) findViewById(R.id.tv_app_version);
		commentLayout = (LinearLayout) findViewById(R.id.layout_comment_content);
		// stars select
		stars.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				stars.setRating(rating);
			}
		});

		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		appName.setText(appInfoBean.getAppName());
		appVersion.setText(ResourceUtil.getString(mContext, R.string.app_version_text)+" " + appInfoBean.getVersionName());
		NetworkImageUtils.load(mContext, ImageType.NETWORK, appInfoBean.getLogo(),
				R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIcon);

		content.addTextChangedListener(new TextWatcher() {
			private CharSequence mTemp;
			private int mSelectionStart;
			private int mSelectionEnd;

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
				mTemp = charSequence;
			}

			@Override
			public void afterTextChanged(Editable editable) {
				leftChars.setText(editable.length() + "/" + mNum); // 动态改变数量
				mSelectionStart = content.getSelectionStart();
				mSelectionEnd = content.getSelectionEnd();
				Log.i("dsa", "" + mSelectionStart + " " + mSelectionEnd);
				if (mTemp.length() > mNum) {
					editable.delete(mSelectionStart - 1, mSelectionEnd);//删掉多输入的文字
					int tempSelection = mSelectionEnd;
					content.setText(editable);
					content.setSelection(tempSelection);
				}
			}
		});

	}

	// send data
	private void sendData() {
		// TODO Auto-generated method stub
		String info = content.getText().toString();
		int grade = (int) stars.getRating();

		if (!NetworkUtils.isNetworkAvailable(mContext)) {
			ToastUtil
					.show(mContext, mContext.getResources().getString(R.string.network_canot_work), Toast.LENGTH_SHORT);
			return;
		}
		if ("".equals(info)) {
			ToastUtil
					.show(mContext, ResourceUtil.getString(mContext, R.string.write_comments_tips), Toast.LENGTH_SHORT);
			return;
		}
		if (0 == grade) {
			ToastUtil.show(mContext, ResourceUtil.getString(mContext, R.string.choose_stars_tips), Toast.LENGTH_SHORT);
			return;
		} else {

			if (flag) {
				return;
			}
			flag = true;
			commentLayout.setVisibility(View.GONE);
			commentLayout.setClickable(false);

			ProgressDialogUtil.openProgressDialog(mContext, ResourceUtil.getString(mContext, R.string.loading));

			CommentRequest request = new CommentRequest();
			CommentData data = new CommentData();
			data.setStars(grade);
			data.setContent(info);
			data.setAppId(appInfoBean.getAppId());
			data.setClientId(AccountManager.getInstance().getClientId(mContext));
			data.setDeviceModel(android.os.Build.MODEL);
			data.setDeviceVendor(android.os.Build.MANUFACTURER);
			data.setDeviceType(Utils.isTablet(mContext) == false ? 1 : 2);
			data.setOsVersion(String.valueOf(android.os.Build.VERSION.SDK_INT));
			data.setOsVersionName(android.os.Build.VERSION.RELEASE);
//用户登录与否的情况
			if(AccountManager.getInstance().isLogin(mContext) == true){
				MasUser masUser = new MasUser();
				masUser.setUserId(AccountManager.getInstance().getUserId(mContext));
				masUser.setUserName(AccountManager.getInstance().getUserName(mContext));
				masUser.setUserPwd(AccountManager.getInstance().getPwd(mContext));
				request.setData(data);
				request.setMasUser(masUser);
				DataFetcher.getInstance().addCommentsData(request, myResponseListent, myErrorListener);
			}else{
				request.setData(data);
				request.setMasUser(null);
				DataFetcher.getInstance().addCommentsData(request, myResponseListent, myErrorListener);
			}
	
		}

	};

	// response listener
	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			ProgressDialogUtil.closeProgressDialog();
			LogUtil.getLogger().d("response==>" + response.toString());

			final DetailResponse detailResponse = (DetailResponse) JsonUtil.jsonToBean(response, DetailResponse.class);
			// response data
			if (detailResponse != null && detailResponse.state.code == 200) {
				ToastUtil.show(mContext, ResourceUtil.getString(mContext, R.string.comments_success_tips),
						Toast.LENGTH_SHORT);
				setResult(40);
				CommentActivity.this.finish();
			} else if (detailResponse != null && detailResponse.state.code == 201) {
				ToastUtil.show(mContext, ResourceUtil.getString(mContext, R.string.comments_error_tips),
						Toast.LENGTH_SHORT);
				CommentActivity.this.finish();
			} else {
				ToastUtil.show(mContext, ResourceUtil.getString(mContext, R.string.comments_fail_tips),
						Toast.LENGTH_SHORT);
				CommentActivity.this.finish();
			}

		}
	};

	// error listener
	private ErrorListener myErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			ProgressDialogUtil.closeProgressDialog();
			CommentActivity.this.finish();
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_left:
			CommentActivity.this.finish();
			break;
		case R.id.btn_right:
			sendData();
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
		SkinConfigManager.getInstance().setViewBackground(mContext, leftBtn, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(mContext, rightBtn, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(mContext, dividerLineView, SkinConstan.TITLE_BAR_BG);
	}
}
