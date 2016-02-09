/**   
* @Title: SubmitFeedbackActivity.java
* @Package com.mas.amineappstore.ui.activity.feedback
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-23 下午2:17:03
* @version V1.0   
*/

package com.x.ui.activity.feedback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.x.publics.http.model.FeedbackDialogueBean;
import com.x.publics.http.model.FeedbackDialogueRequest;
import com.x.publics.http.model.FeedbackDialogueResponse;
import com.x.publics.http.model.FeedbackResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.adapter.feedback.SubmitFeedbackAdapter;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

/**
 * @ClassName: SubmitFeedbackActivity
 * @Description: TODO(提交反馈界面)
 
 * @date 2014-7-23 下午2:17:03
 * 
 */

public class SubmitFeedbackActivity extends BaseActivity implements OnClickListener {

	/* 发送 按钮 */
	private Button sendBtn;
	private EditText inputEt;
	private TextView retryTv;
	private Context context = this;
	private PullToRefreshListView pulltoRefreshLv;
	private ListView feedBackLv;
	private List<FeedbackDialogueBean> submitFbLv = new ArrayList<FeedbackDialogueBean>();
	private List<FeedbackDialogueBean> submitFb_TempLv = new ArrayList<FeedbackDialogueBean>();
	private List<FeedbackDialogueBean> feedbackFailList = new ArrayList<FeedbackDialogueBean>();

	private SubmitFeedbackAdapter ada = null;
	private String ps = "10";//每个页显示多少条数据
	private int pn = 1;//第几页
	private int mNum = 1025;//允许输入的字数
	private String content = "";
	private FeedbackDialogueBean tempBean;
	private View loadingView, normalView, e_error_rl;
	private boolean conisLast = true;
	private boolean isFirstCome = true;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View loadingPb, loadingLogo;
	private View mNavigationView, mTitleView, mTitlePendant;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_feedback);
		initViews();
		ada = new SubmitFeedbackAdapter(SubmitFeedbackActivity.this);
		ada.setList(submitFbLv);
		feedBackLv.setAdapter(ada);
		//		inputEt.requestFocus();
		inputEt.setFocusable(true);
		inputEt.setFocusableInTouchMode(true);
		makeServerData();
	}

	/**
	 * 初始化界面控件
	 */
	private void initViews() {
		sendBtn = (Button) findViewById(R.id.submit_fb_sendBtn);
		sendBtn.setEnabled(false);
		retryTv = (TextView) findViewById(R.id.e_retry_btn);
		inputEt = (EditText) findViewById(R.id.submit_fb_inputEt);
		pulltoRefreshLv = (PullToRefreshListView) findViewById(R.id.submit_fb_PullToLoadlv);
		pulltoRefreshLv.setOnRefreshListener(onRefreshListener);
		pulltoRefreshLv.setMode(Mode.DISABLED);
		feedBackLv = pulltoRefreshLv.getRefreshableView();
		feedBackLv.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		normalView = (RelativeLayout) findViewById(R.id.normalRel);
		e_error_rl = (RelativeLayout) findViewById(R.id.e_error_rl);
		loadingView = this.getLayoutInflater().inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshLv.setEmptyView(loadingView);
		inputEt.addTextChangedListener(new TextWatcher() {
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
				if (mTemp.length() == mNum)
					Toast.makeText(context, R.string.feedback_input_tips, 1).show();
				mSelectionStart = inputEt.getSelectionStart();
				mSelectionEnd = inputEt.getSelectionEnd();
				if (mTemp.length() > mNum) {
					editable.delete(mSelectionStart - 1, mSelectionEnd);
					int tempSelection = mSelectionEnd;
					inputEt.setText(editable);
					inputEt.setSelection(tempSelection);
				}
			}
		});
		sendBtn.setOnClickListener(this);
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
		mTitleTv.setText(R.string.page_my_questions);
		mNavigationView.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.submit_fb_sendBtn:
			sendData(true, null);
			break;
		case R.id.e_retry_btn:
			if (!NetworkUtils.isNetworkAvailable(this)) {
				ToastUtil.show(this, ResourceUtil.getString(this, R.string.network_canot_work), Toast.LENGTH_SHORT);
				return;
			}
			e_error_rl.setVisibility(View.GONE);
			normalView.setVisibility(View.VISIBLE);
			makeServerData();
			break;
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		default:
			break;
		}

	}

	/**
	 * 对往上拉的事件处理
	 */
	private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>() {

		@SuppressLint("NewApi")
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

			feedBackLv.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
			++pn;
			if (e_error_rl.isShown())
				pn = 1;
			makeServerData();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		}

	};

	/**
	 * 请求对话列表数据
	 */
	private void makeServerData() {

		FeedbackDialogueRequest request = new FeedbackDialogueRequest();
		request.setImei(Utils.getIMEI(context));
		request.setPs(ps);
		request.setPn(pn + "");
		DataFetcher.getInstance().feedbackDialogueData(request, listener, errorListener);
	}

	/**
	 * 提交用户的反馈信息
	 * isResend: true=正常发送；false=重新发送
	 * position 返回操作的是那个Item，用作重发成功时候
	 */
	public void sendData(boolean isResend, FeedbackDialogueBean conBean) {
		// 判断是否有网络
		if (!NetworkUtils.isNetworkAvailable(context)) {
			ToastUtil.show(context, R.string.network_canot_work, 1000);
		}

		if (isResend) {
			content = Utils.removeSpace(inputEt.getText().toString());
			if (TextUtils.isEmpty(content)) {
				ToastUtil.show(context, R.string.write_feedback_tips, 1000);
				return;
			}
			//在界面上添加一条数据
			FeedbackDialogueBean bean = new FeedbackDialogueBean();
			bean.setContent(content);
			bean.setSendTime(System.currentTimeMillis());
			bean.setFeedbackType(1);
			bean.setShow(false);
			tempBean = bean;
			submitFbLv.add(bean);
			feedBackLv.setSelection(submitFbLv.size());
			inputEt.setText("");
		} else {//重新发送
			if (conBean != null) {
				tempBean = conBean;
				content = tempBean.getContent();
			}
		}

		Listener<JSONObject> feedbackResponseListent = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogUtil.getLogger().d("response==>" + response.toString());
				// 响应数据，进行操作
				final FeedbackResponse feedbackResponse = (FeedbackResponse) JsonUtil.jsonToBean(response,
						FeedbackResponse.class);
				if (feedbackResponse != null && feedbackResponse.state.code == 200) {

					for (FeedbackDialogueBean failFeedbackBean : feedbackFailList) {
						if (failFeedbackBean.getContent().equals(content)) {
							feedbackFailList.remove(failFeedbackBean);
							FeedbackManager.getInstance().saveFeedBackFailList(context, feedbackFailList);
							break;
						}
					}
				} else {
					handleSendFail();
				}
			}
		};

		ErrorListener feedbackErrorListener = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				handleSendFail();
			}
		};
		FeedbackManager.getInstance().sendFeedBack(context, content, feedbackResponseListent, feedbackErrorListener);
	}

	private void handleSendFail() {
		if (tempBean != null) {
			for (int i = 0; i < submitFbLv.size(); i++) {
				FeedbackDialogueBean contentBean = submitFbLv.get(i);
				if (contentBean.getSendTime() == (tempBean.getSendTime())) {
					if (feedbackFailList.isEmpty()) {
						submitFbLv.get(i).setShow(true);
						feedbackFailList.add(tempBean);
						FeedbackManager.getInstance().saveFeedBackFailList(context, feedbackFailList);
						break;

					} else {
						boolean isExit = false;
						for (FeedbackDialogueBean feedbackDialogueBean : feedbackFailList) {
							if (feedbackDialogueBean.getContent().equals(tempBean.getContent())) {
								isExit = true;
								break;
							} else {
								continue;
							}
						}
						if (!isExit) {
							submitFbLv.get(i).setShow(true);
							feedbackFailList.add(tempBean);
							FeedbackManager.getInstance().saveFeedBackFailList(context, feedbackFailList);
						} else {
							submitFbLv.get(i).setShow(true);
						}
					}
					break;
				}
			}
		}
		ada.notifyDataSetChanged();
	}

	/**
	 * < 获取反馈信息列表 >数据响应
	 */
	private Listener<JSONObject> listener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("respoonse==>" + response.toString());
			pulltoRefreshLv.onRefreshComplete();
			sendBtn.setEnabled(true);
			// 响应数据，进行操作
			final FeedbackDialogueResponse fbResponse = (FeedbackDialogueResponse) JsonUtil.jsonToBean(response,
					FeedbackDialogueResponse.class);
			if (fbResponse != null && fbResponse.state.code == 200) {

				FeedbackManager.getInstance().setFeedbackAttention(SubmitFeedbackActivity.this, false);

				conisLast = fbResponse.isLast;
				normalView.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);
				e_error_rl.setVisibility(View.GONE);
				if (fbResponse.getFeedbackClientList() != null) {
					submitFb_TempLv = fbResponse.getFeedbackClientList();
					Collections.reverse(submitFb_TempLv);
					submitFbLv.addAll(0, submitFb_TempLv);
				}
				feedbackFailList = FeedbackManager.getInstance().getFeedBackFailList(context);
				if (isFirstCome == true && !feedbackFailList.isEmpty()) {//第一次进入此界面时,给发送失败的标志状态
					for (int i = 0; i < feedbackFailList.size(); i++) {
						FeedbackDialogueBean bean = (FeedbackDialogueBean) feedbackFailList.get(i);
						bean.setShow(true);
					}
					submitFbLv.addAll(feedbackFailList);
					isFirstCome = false;
				}
				addWarnData();
				ada.notifyDataSetChanged();
				feedBackLv.setSelectionFromTop(submitFb_TempLv.size(), 0);
				setRefreshMode();
			} else {
				showErrorView();
			}
		}
	};

	/**
	 * < 获取反馈信息列表 >获取异常响应处理
	 */
	private ErrorListener errorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			showErrorView();
		}
	};

	private void setRefreshMode() {
		if (conisLast) {
			pulltoRefreshLv.setMode(Mode.DISABLED);
		} else {
			pulltoRefreshLv.setMode(Mode.PULL_FROM_START);
		}
	}

	private void showErrorView() {
		if (pn > 1) {
			--pn;
		}
		if (submitFbLv.isEmpty()) {
			normalView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			e_error_rl.setVisibility(View.VISIBLE);
		}
	}

	/**
	 *第一条提醒问候语
	 */
	private void addWarnData() {
		if (conisLast == true) {
			FeedbackDialogueBean feedbackDialogueBean = new FeedbackDialogueBean();
			content = SubmitFeedbackActivity.this.getResources().getString(R.string.feedback_first_tips);
			feedbackDialogueBean.setContent(content);
			feedbackDialogueBean.setFeedbackType(2);
			if (submitFbLv.isEmpty()) {
				feedbackDialogueBean.setSendTime(System.currentTimeMillis());
			} else {
				feedbackDialogueBean.setSendTime(submitFbLv.get(0).getSendTime());
			}
			submitFbLv.add(0, feedbackDialogueBean);
			e_error_rl.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			normalView.setVisibility(View.VISIBLE);
			ada.setList(submitFbLv);
			pulltoRefreshLv.setMode(Mode.DISABLED);
		}
	}

	/**
	 * 数据反馈回来之后，重新弹出软键盘
	 */
	private void showSoftInput() {
		//		inputEt.setFocusable(true);
		//		inputEt.setFocusableInTouchMode(true);
		//		inputEt.requestFocus();
		//		InputMethodManager inputManager = (InputMethodManager) inputEt.getContext().getSystemService(
		//				Context.INPUT_METHOD_SERVICE);
		//		inputManager.showSoftInput(inputEt, 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().module(ModuleName.FEEDBACK_MY_FEEDBACK, true);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().module(ModuleName.FEEDBACK_MY_FEEDBACK, false);
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, sendBtn, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
