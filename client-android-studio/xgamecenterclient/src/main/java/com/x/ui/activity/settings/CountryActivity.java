package com.x.ui.activity.settings;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.x.R;
import com.x.business.country.CountryManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.CountryRequest;
import com.x.publics.http.model.CountryResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.CountryBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.adapter.CountryAdapter;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;

/**
 * 
* @ClassName: CountryActivity
* @Description: 多国家列表activity

* @date 2014-5-20 下午5:52:33
*
 */
public class CountryActivity extends Activity implements OnClickListener {

	private ListView lvCountry;
	private CountryAdapter mAdapter;
	private View loadingPb, loadingLogo;
	private PullToRefreshListView ptrListView;
	private View loadingView, cancelBtn, confirmBtn, dividerLineView;
	private ArrayList<CountryBean> mList = new ArrayList<CountryBean>();

	private int lastId;
	private String lastUrl;
	private boolean lastIsAuto;
	private int autoCountryId;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country);
		Utils.autoScreenAdapter(this); // 自动适配平板、手机
		initViews();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().onResume(this);
		// 记录最后一次国家信息
		lastId = CountryManager.getInstance().getCountryId(context);
		lastUrl = SharedPrefsUtil.getValue(context, "COUNTRY_URL", "");
		lastIsAuto = CountryManager.getInstance().isAutoCountry(context);
	}

	/**
	* @Title: initViews 
	* @Description: 初始化界面
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void initViews() {
		cancelBtn = findViewById(R.id.btn_left);
		confirmBtn = findViewById(R.id.btn_right);
		dividerLineView = findViewById(R.id.divider_line);
		cancelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);

		ptrListView = (PullToRefreshListView) findViewById(R.id.lv_country);
		lvCountry = ptrListView.getRefreshableView();
		ptrListView.setMode(Mode.DISABLED);
		loadingView = getLayoutInflater().inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		ptrListView.setEmptyView(loadingView);

		mAdapter = new CountryAdapter(this);
		lvCountry.setAdapter(mAdapter);
		mAdapter.setList(mList);

		getData();
	}

	/**
	 * 
	* @Title: getData 
	* @Description: 发送请求，获取数据 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void getData() {
		CountryRequest request = new CountryRequest(Constan.Rc.GET_COUNTRY);
		DataFetcher.getInstance().getCountryList(request, myResponseListent, myErrorListener, false);
	}

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString());
			CountryResponse countryResponse = (CountryResponse) JsonUtil.jsonToBean(response, CountryResponse.class);
			if (countryResponse != null && countryResponse.state.code == 200) {
				autoCountryId = countryResponse.autoRaveId;
				mList.addAll(countryResponse.countryList);
				mAdapter.setAutoCountryId(autoCountryId);
				mAdapter.setList(mList);
			} else {
				showErrorTips();
			}
		}
	};

	private ErrorListener myErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			showErrorTips();
		}
	};

	/**
	 * 
	* @Title: showErrorTips 
	* @Description: 获取数据失败提示
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void showErrorTips() {
		ToastUtil.show(context, ResourceUtil.getString(context, R.string.load_error_tips), ToastUtil.LENGTH_SHORT);
		finish();
	}

	@Override
	public void onBackPressed() {
		cancel();
	}

	/**
	 * 
	* @Title: cancel 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void cancel() {
		// 继续使用最后一次的记录
		if (lastIsAuto) {
			CountryManager.getInstance().saveCountryId(context, lastId, true);
		} else {
			CountryManager.getInstance().saveCountryId(context, lastId, false);
		}
		SharedPrefsUtil.putValue(context, "COUNTRY_URL", lastUrl);
		finish();
	}

	/**
	 * 
	* @Title: onClick 
	* @Description: 全局按钮数据处理
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		// 确定按钮
		case R.id.btn_right:
			finish();
			break;

		// 取消按钮	
		case R.id.btn_left:
			cancel();
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().onPause(this);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param     
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(context, cancelBtn, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, confirmBtn, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, dividerLineView, SkinConstan.TITLE_BAR_BG);
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
