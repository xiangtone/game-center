package com.x.ui.activity.feedback;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.feedback.FeedbackManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.FeedbackListRequest;
import com.x.publics.http.model.FeedbackListResponse;
import com.x.publics.http.model.UserFeedbackBean;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.adapter.feedback.UserFeedbackAdapter;
import com.x.ui.adapter.feedback.UserFeedbackViewHoler;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;

/**
 * @ClassName: FeedbackActivity
 * @Desciption: 用户意见反馈界面
 
 * @Date: 2014-2-17 下午7:02:36
 */

public class FeedbackActivity extends BaseActivity implements OnClickListener {

	/* 提交反馈 按钮 */
	private RelativeLayout userfeedback_rel;
	private ImageView tipsIv;
	private ActionSlideExpandableListView userfeedback_lv;
	private UserFeedbackAdapter mUserFeedbackAdapter;
	private List<UserFeedbackBean> userFbList = new ArrayList<UserFeedbackBean>();
	private String ps = "50";//每页显示多个条数据
	private int pn = 1;//第几页
	private Context context = this;
	private RelativeLayout normalView;
	private RelativeLayout failView;// 加载失败（模块）视图
	private View loadingView;// loading（模块）视图
	private TextView retryTv;
	private boolean attention;
	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View loadingPb, loadingLogo;
	private View mNavigationView, mTitleView, mTitlePendant;

	/**
	 * 程序界面主入口
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initViews();
		getData();
	}

	/**
	 * 初始化界面控件
	 */
	private void initViews() {
		userfeedback_lv = (ActionSlideExpandableListView) findViewById(R.id.userfeedback_lv);
		userfeedback_rel = (RelativeLayout) findViewById(R.id.userfeedback_rel);
		tipsIv = (ImageView) findViewById(R.id.userfeedback_tip_iv);
		normalView = (RelativeLayout) findViewById(R.id.feedbackRel);
		normalView.setVisibility(View.GONE);
		loadingView = findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		failView = (RelativeLayout) findViewById(R.id.e_error_rl);
		retryTv = (TextView) findViewById(R.id.e_retry_btn);

		mUserFeedbackAdapter = new UserFeedbackAdapter(this);
		mUserFeedbackAdapter.setListView(userfeedback_lv);
		// 添加事件监听
		userfeedback_rel.setOnClickListener(this);
		retryTv.setOnClickListener(this);

		initNavigation();
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
		mTitleTv.setText(R.string.page_feedback);
		mNavigationView.setOnClickListener(this);
	}

	private void showFeedbackTips() {
		FeedbackManager.getInstance().setFeedbackRead(this, true);
		boolean attention = FeedbackManager.getInstance().getFeedbackAttention(this);
		if (attention) {
			tipsIv.setVisibility(View.VISIBLE);
		} else {
			tipsIv.setVisibility(View.GONE);
		}
	}

	/**
	 * 常见回馈列表 数据请求
	 */
	private void getData() {
		boolean isRead = FeedbackManager.getInstance().isFeedbackRead(this);
		String commondFeedBack = FeedbackManager.getInstance().getCommonFeedback(this);
		if (isRead && !TextUtils.isEmpty(commondFeedBack)) {
			try {
				JSONObject commonFeedbackJson = new JSONObject(commondFeedBack);
				final FeedbackListResponse feedbackResponse = (FeedbackListResponse) JsonUtil.jsonToBean(
						commonFeedbackJson, FeedbackListResponse.class);
				showCommonFeedbackList(feedbackResponse);
			} catch (Exception e) {
				e.printStackTrace();
				getFeedbackList();
			}

		} else {
			getFeedbackList();
		}
	};

	private void getFeedbackList() {
		FeedbackListRequest request = new FeedbackListRequest();
		request.setPs(ps);
		request.setPn(pn + "");
		DataFetcher.getInstance().feedbackList(request, feedbackListListent, myErrorListener);
	}

	/**
	 * < 常见回馈列表接口（rc=30023) >数据响应
	 */
	private Listener<JSONObject> feedbackListListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString());
			// 响应数据，进行操作
			final FeedbackListResponse feedbackResponse = (FeedbackListResponse) JsonUtil.jsonToBean(response,
					FeedbackListResponse.class);

			if (feedbackResponse != null && feedbackResponse.state.code == 200
					&& feedbackResponse.getFeedbackCommonList() != null) {
				//缓存数据
				FeedbackManager.getInstance().saveCommonFeedback(FeedbackActivity.this, response.toString());
				showCommonFeedbackList(feedbackResponse);
			} else {
				showErrorView();
			}
		}
	};

	private void showCommonFeedbackList(FeedbackListResponse feedbackResponse) {
		showCommonFeedbackListView();
		userFbList = feedbackResponse.getFeedbackCommonList();
		mUserFeedbackAdapter.setList(userFbList);
		userfeedback_lv.setAdapter(mUserFeedbackAdapter, R.id.feedback_man_top_rl, R.id.feedback_man_expand_ll,
				itemExpandCollapseListener);
	}

	/**
	 * 获取异常响应处理
	 */
	private ErrorListener myErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			showErrorView();
		}
	};

	private void showErrorView() {
		normalView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		failView.setVisibility(View.VISIBLE);
	}

	private void showCommonFeedbackListView() {
		loadingView.setVisibility(View.GONE);
		failView.setVisibility(View.GONE);
		normalView.setVisibility(View.VISIBLE);
	}

	/**
	 * 全局按钮事件处理
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		// 进入提交反馈的对话界面
		case R.id.userfeedback_rel:
			Intent intent = new Intent(FeedbackActivity.this, SubmitFeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.e_retry_btn:
			// 网络检测
			if (!NetworkUtils.isNetworkAvailable(context)) {
				ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
						Toast.LENGTH_SHORT);
				return;
			}
			getData();
			break;
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	/**
	 * 点击箭头处理
	 */
	private OnItemExpandCollapseListener itemExpandCollapseListener = new OnItemExpandCollapseListener() {

		@Override
		public void onExpand(View itemView, View parentView, int position) {
			String url = (String) parentView.getTag();
			View userListItem = userfeedback_lv.findViewWithTag(url);
			UserFeedbackViewHoler viewHolder = new UserFeedbackViewHoler(userListItem);
			if (viewHolder != null && viewHolder.arrowIv != null)
				viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_up);
		}

		@Override
		public void onCollapse(View itemView, View parentView, int position) {
			String url = (String) parentView.getTag();
			View userListItem = userfeedback_lv.findViewWithTag(url);
			UserFeedbackViewHoler viewHolder = new UserFeedbackViewHoler(userListItem);
			if (viewHolder != null && viewHolder.arrowIv != null)
				viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		showFeedbackTips();
		DataEyeManager.getInstance().module(ModuleName.FEEDBACK_USER, true);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().module(ModuleName.FEEDBACK_USER, false);
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setViewBackground(context, userfeedback_rel,
				SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
