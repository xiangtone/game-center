package com.x.ui.activity.appdetail;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.account.AccountManager;
import com.x.business.audio.AudioEffectManager;
import com.x.business.favorite.FavoriteManage;
import com.x.business.search.SearchConstan;
import com.x.business.shake.ShakeDetector;
import com.x.business.shake.ShakeDetector.OnShakeListener;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.SrcName;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.CommentRequest;
import com.x.publics.http.model.CommentResponse;
import com.x.publics.http.model.DetailRequest;
import com.x.publics.http.model.DetailResponse;
import com.x.publics.http.model.RecommendRequest;
import com.x.publics.http.model.RecommendResponse;
import com.x.publics.http.model.CommentRequest.CommentData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.CommentBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.PictureBean;
import com.x.publics.model.RecommendBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.SilentUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Tools;
import com.x.publics.utils.Utility;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.home.SplashActivity;
import com.x.ui.activity.tools.ToolsActivity;
import com.x.ui.adapter.CommentListAdapter;
import com.x.ui.adapter.PicListAdapter;
import com.x.ui.adapter.RecommendListAdapter;
import com.x.ui.view.HorizontalListView;
import com.x.ui.view.RoundProgress;
import com.x.ui.view.ScrollViewExtend;
import com.x.ui.view.ScrollViewExtend.OnBorderListener;
import com.x.ui.view.slidinguppanel.SlidingUpPanelLayout;
import com.x.ui.view.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.x.ui.view.slidinguppanel.SlidingUpPanelLayout.PanelState;

/**
 * @ClassName: AppDetailActivity
 * @Desciption: 应用详情、应用评论、应用推荐
 
 * @Date: 2014-2-12 下午2:06:44
 */

public class AppDetailActivity extends BaseActivity implements OnClickListener {

	private static final int HANDLE_SLIDEVIEW_ANCHORED = 14;
	private static final int HANDLE_SLIDEVIEW_EXPAND = 17;
	private static final int HANDLE_SLIDEVIEW_COLLAPSED = 15;

	/* 文本、图片、视图 */
	private int ct;
	private final Context context = this;
	private TextView stars;// 应用星级
	private TextView appName;// 应用名称
	private TextView appSize;// 应用大小
	private TextView appPatchSize;// 增量大小
	private ImageView upgradeLineIv;// 增量划线
	private TextView appDesc;// 应用描述
	private TextView verName;// 版本名称
	private TextView appSource;// 应用来源
	private ImageView zoom;// 文字伸展图标
	private ImageView appLogo;// 应用图标
	private TextView comments;// 评论人数
	private TextView downloads;// 下载次数
	private TextView publishTime;// 发布时间
	private RelativeLayout errorView;// 异常界面
	private RelativeLayout normalView;// 正常界面
	private LinearLayout failView;// 加载失败（模块）视图
	private View loadingView;// loading（模块）视图
	private LinearLayout loadMoreView;// 加载更多（分页）视图
	private LinearLayout commentsView;// 评论列表（模块）视图
	private LinearLayout recommendView;// 推荐列表（模块）视图
	private LinearLayout issuerRecommendView;// 开发商推荐列表（模块）视图
	private FrameLayout topModuleView; // 顶部模块区域
	private ScrollViewExtend scrollViewExtend; // 滚动区域
	private ProgressBar pg1, pg2, pg3, pg4, pg5;// 星级百分比
	private RatingBar appStars, avgStars; // 应用平均星级

	/* 按钮 */
	private TextView refreshBtn;// 刷新按钮
	private ImageView collectBtn;// 收藏按钮
	private RelativeLayout commentsBtn;// 评论按钮

	private LinearLayout appPauseView; // 暂停按钮View
	private TextView pauseBtn; // 暂停按钮
	private TextView launchBtn; // 启动按钮
	private TextView upgradeBtn; // 升级按钮
	private TextView installBtn; // 安装按钮
	private TextView continueBtn; // 继续按钮
	private TextView downloadBtn; // 下载按钮
	private TextView loadMoreBtn; // 加载更多按钮
	public RoundProgress downloadPercentPv;
	public AnimationDrawable downloadAnimation;

	/* 列表 */
	private ListView lvComments; // 评论列表listView
	private GridView gvRecommend;// 应用推荐列表gridView
	private HorizontalListView lvPictures; // 图片列表listView（水平滚动）

	/* 实体对象、集合 */
	private DetailRequest detailRequest;// 详情，请求对象
	private DetailResponse detailResponse;// 详情，响应对象
	private CommentRequest commentRequest;// 评论，请求对象
	private CommentResponse commentResponse;// 评论，响应对象
	private RecommendRequest recommendRequest;// 推荐，请求对象
	private RecommendResponse recommendResponse;// 推荐，响应对象
	private AppInfoBean appInfoBean; // 应用实体类（封装参数、便于操作）
	private DownloadBean downloadBean; // 下载实体类（封装参数、便于操作）
	private DownloadUiReceiver mDownloadUiReceiver;// 广播接收对象
	private PicListAdapter picListAdapter;// 图片列表（水平）适配器
	private CommentListAdapter commentListAdapter;// 评论列表适配器
	private RecommendListAdapter recommendListAdapter;// 推荐列表（网格）适配器
	private final ArrayList<PictureBean> picList = new ArrayList<PictureBean>();// 装载应用图片路径集合
	private final ArrayList<CommentBean> commentsList = new ArrayList<CommentBean>();// 评论数据集合
	private ArrayList<RecommendBean> recommendList = new ArrayList<RecommendBean>();// 应用推荐数据集合

	private View loadingPb, loadingLogo, dividerLineView;

	/* 基本数据类型 */
	private int pageNo = 1; // 当前第1页
	private final int pageSize = 5;// 每页显示5条
	private Boolean flag = true;
	private Boolean isLoadMore = false;
	private boolean isFavoriteApp = false;
	private String packageName, actionUrl;

	private ShakeDetector shakeDetector = null;

	public String searchKey = null;
	public boolean isFromBanner;
	public boolean isFromSearch;
	public boolean isFromThird = true;
	private boolean RequestRecommendIng, isPanelExpanded;

	private static final int[] btns = { R.id.btn_text_zoom, R.id.btn_app_share, R.id.hali_app_pause_ll,
			R.id.hali_app_launch_btn, R.id.hali_app_install_btn, R.id.hali_app_upgrade_btn, R.id.btn_refresh,
			R.id.btn_app_collect, R.id.btn_app_comments, R.id.hali_app_download_btn, R.id.hali_app_continue_btn };

	// 新功能：更改UI样式
	private ImageView topIndicator;
	private SlidingUpPanelLayout slidingUpPanelLayout;

	// 发行商应用推荐
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	private ArrayList<RecommendBean> issuerRecommendList = new ArrayList<RecommendBean>();

	/**
	 * 消息队列处理，更新UI
	 */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 评论消息队列
			case 10:
				commentsView.setVisibility(View.VISIBLE);
				commentListAdapter.setList((List<CommentBean>) msg.obj);
				lvComments.setAdapter(commentListAdapter);
				Utility.setListViewHeightBasedOnChildren(lvComments);
				break;

			// 图片消息队列
			case 11:
				loadingView.setVisibility(View.GONE);
				picListAdapter.setList((List<PictureBean>) msg.obj);
				lvPictures.setAdapter(picListAdapter);
				break;

			// 推荐消息队列
			case 12:
				recommendListAdapter.setList((List<RecommendBean>) msg.obj);
				gvRecommend.setAdapter(recommendListAdapter);
				break;

			// 收藏消息队列
			case 13:
				if ((Boolean) msg.obj) {
					collectBtn.setBackgroundResource(R.drawable.app_ic_favorite);
				} else {
					collectBtn.setBackgroundResource(R.drawable.app_ic_unfavorite);
				}
				collectBtn.setClickable(true);
				break;

			case HANDLE_SLIDEVIEW_ANCHORED:
				slidingUpPanelLayout.setPanelState(PanelState.ANCHORED);
				break;

			case HANDLE_SLIDEVIEW_COLLAPSED:
				slidingUpPanelLayout.setPanelState(PanelState.COLLAPSED);
				break;

			case 16:
				initIssuerRecommend();
				break;
			case HANDLE_SLIDEVIEW_EXPAND:
				slidingUpPanelLayout.setPanelState(PanelState.EXPANDED);
				break;
			}
		}
	};

	/**
	 * 程序界面主入口
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_app_detail);
		Utils.addActivity(this);
		initViews();
	}

	@Override
	protected void onStart() {
		super.onStart();
		startShakeDetector();
	}

	/**
	 * 界面创建时，注册广播接收
	 */
	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		registUiReceiver();// <方法调用>注册广播接收者
		if (appInfoBean != null && appInfoBean.getFileType() > 0) {
			DataEyeManager.getInstance().view(appInfoBean.getFileType(), appInfoBean.getCategoryId(),
					appInfoBean.getAppName(), appInfoBean.getFileSize(), appInfoBean.getVersionName(), null);
		}

		if (!isPanelExpanded)
			mHandler.sendEmptyMessageDelayed(HANDLE_SLIDEVIEW_ANCHORED, 20);
	}

	/**
	 * 界面销毁时，注销广播接收
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
	}

	/**
	 * 程序停止时，回收内存（大图片处理），防止内存溢出
	 */
	@Override
	protected void onStop() {
		try {
			int count = lvPictures.getChildCount();
			for (int i = 0; i < count; i++) {
				View v = lvPictures.getChildAt(i);
				try {
					ImageView imageView = (ImageView) (((LinearLayout) v).getChildAt(0));
					NetworkImageUtils.cancleDisplay(imageView, this);
					// NetworkImageUtils.recycle(imageView);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		stopShakeDetector();
		super.onStop();
	}

	/**
	 * 初始化界面控件
	 */
	private void initViews() {
		searchKey = getIntent().getStringExtra("searchKey");
		actionUrl = getIntent().getStringExtra(SearchConstan.ACTION_URL);
		isFromBanner = getIntent().getBooleanExtra("isFromBanner", false);
		isFromSearch = getIntent().getBooleanExtra(SearchConstan.FROM_SEARCH, false);
		ct = getIntent().getIntExtra("ct", Constan.Ct.MARKET_APP_DETAIL);
		appInfoBean = (AppInfoBean) getIntent().getParcelableExtra("appInfoBean");

		// 新功能：更改UI样式
		initSlidingUpPanel();

		dividerLineView = findViewById(R.id.divider_line);
		pg1 = (ProgressBar) findViewById(R.id.pg_1);
		pg2 = (ProgressBar) findViewById(R.id.pg_2);
		pg3 = (ProgressBar) findViewById(R.id.pg_3);
		pg4 = (ProgressBar) findViewById(R.id.pg_4);
		pg5 = (ProgressBar) findViewById(R.id.pg_5);
		zoom = (ImageView) findViewById(R.id.img_zoom);
		stars = (TextView) findViewById(R.id.tv_avg_stars);
		appStars = (RatingBar) findViewById(R.id.app_stars);
		avgStars = (RatingBar) findViewById(R.id.avg_stars);
		appName = (TextView) findViewById(R.id.tv_app_name);
		appSize = (TextView) findViewById(R.id.tv_app_size);
		appPatchSize = (TextView) findViewById(R.id.tv_app_patch_size);
		upgradeLineIv = (ImageView) findViewById(R.id.iv_patch_upgrade_line);
		comments = (TextView) findViewById(R.id.tv_comments);
		appLogo = (ImageView) findViewById(R.id.img_app_logo);
		verName = (TextView) findViewById(R.id.tv_app_version);
		appSource = (TextView) findViewById(R.id.tv_app_source);
		failView = (LinearLayout) findViewById(R.id.layout_fail);
		errorView = (RelativeLayout) findViewById(R.id.e_error_rl);
		downloads = (TextView) findViewById(R.id.tv_app_downloads);
		appDesc = (TextView) findViewById(R.id.tv_app_description);
		publishTime = (TextView) findViewById(R.id.tv_publish_time);
		loadingView = findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		normalView = (RelativeLayout) findViewById(R.id.ly_normal_content);
		commentsView = (LinearLayout) findViewById(R.id.layout_comments_view);
		recommendView = (LinearLayout) findViewById(R.id.layout_recommend_view);
		issuerRecommendView = (LinearLayout) findViewById(R.id.layout_issuer_recommend_view);
		loadMoreView = (LinearLayout) getLayoutInflater().inflate(R.layout.load_more_data, null);
		topModuleView = (FrameLayout) findViewById(R.id.layout_module_top);
		scrollViewExtend = (ScrollViewExtend) findViewById(R.id.layout_scrollviewextend_view);

		/* 按钮 */
		refreshBtn = (TextView) findViewById(R.id.btn_refresh);
		collectBtn = (ImageView) findViewById(R.id.btn_app_collect);
		commentsBtn = (RelativeLayout) findViewById(R.id.btn_app_comments);
		loadMoreBtn = (TextView) loadMoreView.findViewById(R.id.btn_load);
		appPauseView = (LinearLayout) findViewById(R.id.hali_app_pause_ll);
		pauseBtn = (TextView) findViewById(R.id.hali_app_pause_btn);
		downloadPercentPv = (RoundProgress) findViewById(R.id.hali_app_pause_rp);
		downloadPercentPv.setBackgroundResource(R.anim.download);
		downloadAnimation = (AnimationDrawable) downloadPercentPv.getBackground();

		downloadBtn = (TextView) findViewById(R.id.hali_app_download_btn);
		continueBtn = (TextView) findViewById(R.id.hali_app_continue_btn);
		installBtn = (TextView) findViewById(R.id.hali_app_install_btn);
		upgradeBtn = (TextView) findViewById(R.id.hali_app_upgrade_btn);
		launchBtn = (TextView) findViewById(R.id.hali_app_launch_btn);

		/* 列表 */
		lvComments = (ListView) findViewById(R.id.lv_common);
		gvRecommend = (GridView) findViewById(R.id.gv_recommend);
		lvPictures = (HorizontalListView) findViewById(R.id.hlv_pictures);

		// 监听按钮事件
		for (int btn : btns)
			findViewById(btn).setOnClickListener(this);
		loadMoreBtn.setOnClickListener(this);
		topModuleView.setOnTouchListener(new MyListener());
		picListAdapter = new PicListAdapter(this);
		commentListAdapter = new CommentListAdapter(this);
		recommendListAdapter = new RecommendListAdapter(this, Constan.Rc.GET_APP_RECOMMEND);
		lvPictures.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(context, ViewPagerActivity.class);
				intent.putParcelableArrayListExtra("picList", picList);
				intent.putExtra("picIndex", position);
				startActivity(intent);
			}
		});

		refreshData(); // 刷新参数
	}

	/**
	 * @Title: intSlidingUpPanel
	 * @Description: TODO
	 * @param
	 * @return void
	 */

	/**
	 * @desc: 刷新参数
	 * @params:
	 * @return: void
	 */
	private void refreshData() {
		/* appInfoBean 非空判断、避免空指针异常 */
		if (appInfoBean != null) {
			setCurrentProgress();
		} else {
			// 来源第三方市场
			getMarketData();
		}

		setParams(); // 设置参数
		loadDetailData(); // 加载应用详情数据
	}

	/**
	 * 设置界面参数、赋值操作
	 */
	private void setParams() {
		zoom.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
		// 赋值=设置异常界面隐藏
		errorView.setVisibility(View.GONE);
		// 赋值=设置评论不显示
		commentsView.setVisibility(View.GONE);
		// 赋值=去除GridView点击时，默认的黄色背景色
		gvRecommend.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 赋值=默认download按钮不能点击
		downloadBtn.setEnabled(false);
		commentsBtn.setEnabled(true);
		collectBtn.setClickable(false);
	}

	/**
	 * @desc: 设置当前进度条进度
	 * @params:
	 * @return: void
	 */
	private void setCurrentProgress() {
		downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId("" + appInfoBean.getApkId());
		// 赋值=设置进度条的进度
		if (downloadBean != null) {
			if (downloadBean.getTotalBytes() == 0) {
				downloadPercentPv.setProgress(0);
			} else {
				int percent = (int) (downloadBean.getCurrentBytes() * 100 / downloadBean.getTotalBytes());
				downloadPercentPv.setProgress(percent);
			}
		} else {
			downloadPercentPv.setProgress(0);
		}
	}

	/**
	 * 初始化当前应用状态，显示按钮状态 0正常 1下载中 2暂停 3可升级 4安装 5启动 6等待 7连接中
	 */
	private void refreshAppStatus(int status, String percent) {

		switch (status) {
		// 正常状态，显示下载按钮
		case AppInfoBean.Status.NORMAL:
			downloadBtn.setVisibility(View.VISIBLE);
			appPauseView.setVisibility(View.GONE);
			continueBtn.setVisibility(View.GONE);
			installBtn.setVisibility(View.GONE);
			upgradeBtn.setVisibility(View.GONE);
			launchBtn.setVisibility(View.GONE);
			break;
		// 下载中状态，显示下载进度条（暂停按钮）
		case AppInfoBean.Status.DOWNLOADING:
			appPauseView.setVisibility(View.VISIBLE);
			pauseBtn.setText(context.getResources().getString(R.string.app_status_pause));
			continueBtn.setVisibility(View.GONE);
			installBtn.setVisibility(View.GONE);
			upgradeBtn.setVisibility(View.GONE);
			launchBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			if (percent != null) {
				downloadPercentPv.setProgress(Integer.valueOf(percent));
			}
			startDownloadAnimation();
			break;
		// 暂停状态，显示继续按钮
		case AppInfoBean.Status.PAUSED:
			continueBtn.setVisibility(View.VISIBLE);
			appPauseView.setVisibility(View.GONE);
			installBtn.setVisibility(View.GONE);
			upgradeBtn.setVisibility(View.GONE);
			launchBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			stopDownloadAnimation();
			break;
		// 可升级状态，显示更新按钮
		case AppInfoBean.Status.CANUPGRADE:
			upgradeBtn.setVisibility(View.VISIBLE);
			appPauseView.setVisibility(View.GONE);
			continueBtn.setVisibility(View.GONE);
			installBtn.setVisibility(View.GONE);
			launchBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			break;
		// 可安装状态，显示安装按钮
		case AppInfoBean.Status.CANINSTALL:
			installBtn.setVisibility(View.VISIBLE);
			appPauseView.setVisibility(View.GONE);
			continueBtn.setVisibility(View.GONE);
			upgradeBtn.setVisibility(View.GONE);
			launchBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			stopDownloadAnimation();
			break;
		// 等待状态，显示等待按钮
		case AppInfoBean.Status.WAITING:
			appPauseView.setVisibility(View.VISIBLE);
			pauseBtn.setText(context.getResources().getString(R.string.app_status_waiting));
			continueBtn.setVisibility(View.GONE);
			launchBtn.setVisibility(View.GONE);
			installBtn.setVisibility(View.GONE);
			upgradeBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setProgress(RoundProgress.SPECIAL_PROGRESS);
			startDownloadAnimation();
			break;
		// 可运行状态，显示启动按钮
		case AppInfoBean.Status.CANLAUNCH:
			launchBtn.setVisibility(View.VISIBLE);
			appPauseView.setVisibility(View.GONE);
			continueBtn.setVisibility(View.GONE);
			installBtn.setVisibility(View.GONE);
			upgradeBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			break;
		// 连接中状态，显示连接中按钮
		case AppInfoBean.Status.CONNECTING:
			appPauseView.setVisibility(View.VISIBLE);
			pauseBtn.setText(context.getResources().getString(R.string.app_status_connecting));
			continueBtn.setVisibility(View.GONE);
			launchBtn.setVisibility(View.GONE);
			installBtn.setVisibility(View.GONE);
			upgradeBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setProgress(RoundProgress.SPECIAL_PROGRESS);
			startDownloadAnimation();
			break;
		// 安装中状态，显示安装中按钮
		case AppInfoBean.Status.INSTALLING:
			installBtn.setVisibility(View.VISIBLE);
			installBtn.setText(R.string.app_status_installing);
			appPauseView.setVisibility(View.GONE);
			upgradeBtn.setVisibility(View.GONE);
			launchBtn.setVisibility(View.GONE);
			continueBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			stopDownloadAnimation();
			break;
		}
	}

	/**
	 * <request_1>发送请求数据，加载应用详情数据
	 */
	private void loadDetailData() {
		if (isFromSearch) {
			// 来源应用搜索
			detailRequest = new DetailRequest(Constan.Rc.SEARCH_APP_DETAIL);
			detailRequest.setActionUrl(actionUrl);
		} else if (packageName != null) {
			// 来源第三方市场
			detailRequest = new DetailRequest(Constan.Rc.MARKET_APP_DETAIL);
			detailRequest.setPackageName(packageName);
			detailRequest.setCt(Constan.Ct.MARKET_APP_DETAIL);
		} else {
			// 来源应用列表
			detailRequest = new DetailRequest(Constan.Rc.GET_APP_DETAIL);
			detailRequest.setApkId(appInfoBean.getApkId());
			detailRequest.setCt(ct);
		}

		DataFetcher.getInstance().getAppDetailData(detailRequest, detailResponseListener, detailErrorListener);
	}

	/**
	 * <request_2>发送请求数据，加载应用推荐数据
	 */
	private void loadRecommendData() {
		if (appInfoBean == null)
			return;
		RequestRecommendIng = true;
		recommendRequest = new RecommendRequest();
		recommendRequest.setCategoryId(appInfoBean.getCategoryId());
		recommendRequest.setAppId(appInfoBean.getAppId());
		recommendRequest.setPn(1);
		recommendRequest.setPs(4);
		DataFetcher.getInstance()
				.loadRecommendData(recommendRequest, recommendResponseListener, recommendErrorListener);
	}

	/**
	 * <request_3>发送请求数据，加载评论列表数据
	 */
	private void loadCommentsData() {
		commentRequest = new CommentRequest();
		CommentData data = new CommentData();
		data.setAppId(appInfoBean.getAppId());
		commentRequest.setPs(pageSize);
		commentRequest.setPn(pageNo);
		commentRequest.setData(data);
		DataFetcher.getInstance().loadCommentsData(commentRequest, commentsResponseListener, commentsErrorListener);
	}

	/**
	 * <request_4>发送请求数据，加载发行商应用数据
	 */
	private void loadIssuerAppsData() {
		if (appInfoBean == null || appInfoBean.getIssuer() == null)
			return;
		RecommendRequest recommendRequest = new RecommendRequest();
		recommendRequest.setAppId(appInfoBean.getAppId());
		recommendRequest.setIssuer(appInfoBean.getIssuer());
		DataFetcher.getInstance().loadIssuerRecommendData(recommendRequest, issuerRecommendResponseListener,
				issuerRecommendErrorListener);
	}

	/**
	 * <response_1>获取响应数据，填充应用详情区域
	 */
	private final Listener<JSONObject> detailResponseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString()); // 日志输出
			// 应用详情，响应数据处理
			detailResponse = (DetailResponse) JsonUtil.jsonToBean(response, DetailResponse.class);
			if (detailResponse != null && detailResponse.state.code == 200 && detailResponse.app != null) {

				// appInfoBean（应用实体类）
				appInfoBean = detailResponse.app;
				compareStatus(); // <方法调用> 比对当前应用是否已安装
				downloadBtn.setEnabled(true); // 启用download按钮

				appName.setText(detailResponse.app.getAppName());// 赋值=应用名称
				// 赋值=应用图标
				NetworkImageUtils.load(context, ImageType.NETWORK, detailResponse.app.getLogo(),
						R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appLogo);
				appDesc.setText(detailResponse.app.getDescription());// 赋值=应用描述
				appStars.setRating(detailResponse.app.getStars()); // 赋值=评论平均星级
				avgStars.setRating(detailResponse.app.getStars()); // 赋值=评论平均星级
				stars.setText(detailResponse.app.getStarsReal() + ""); // 赋值=评论得分（平均分）
				publishTime.setText(detailResponse.app.getPublishTime());// 赋值=发布时间
				verName.setText(ResourceUtil.getString(context, R.string.app_version_text)
						+ detailResponse.app.getVersionName());// 赋值=版本名称
				appSize.setText(Utils.sizeFormat(detailResponse.app.getFileSize()));// 赋值=应用大小
				appSource.setText(ResourceUtil.getString(context, R.string.app_source_text)
						+ detailResponse.app.getAppSource());// 赋值=应用来源

				if (appInfoBean.isPatch() && appInfoBean.getPatchFileSize() != 0) {
					appPatchSize.setText(Utils.sizeFormat(appInfoBean.getPatchFileSize()));
					appPatchSize.setVisibility(View.VISIBLE);
					upgradeLineIv.setVisibility(View.VISIBLE);
				}
				downloads.setText(ResourceUtil.getString(context, R.string.app_downloads_text)
						+ Utils.changeDownloads(detailResponse.app.getRealDowdload()));// 赋值=下载次数

				initFavoriteApp(); // <调用方法> 初始化应用收藏
				// initStatus(); // <调用方法>初始化app状态，根据appInfoBean
				loadRecommendData();// <调用方法>加载应用推荐
				loadPercents(response);// <调用方法>加载星级百分比
				loadCommentsData();// <调用方法>加载评论
				getPicUrls(detailResponse);// <调用方法>获取应用图片（截图）路径
				loadIssuerAppsData();//<调用方法>加载开发商应用推荐

				/*** dataeye ***/
				if (!TextUtils.isEmpty(packageName) && isFromThird) {
					DataEyeManager.getInstance().source(SrcName.THRIDPART, appInfoBean.getFileType(),
							appInfoBean.getAppName(), appInfoBean.getFileSize(), appInfoBean.getVersionName(), null,
							false);
					DataEyeManager.getInstance().view(appInfoBean.getFileType(), appInfoBean.getCategoryId(),
							appInfoBean.getAppName(), appInfoBean.getFileSize(), appInfoBean.getVersionName(), null);
					isFromThird = false;
				}

				/*** dataeye ***/
				if (isFromBanner) {
					DataEyeManager.getInstance().view(appInfoBean.getFileType(), appInfoBean.getCategoryId(),
							appInfoBean.getAppName(), appInfoBean.getFileSize(), appInfoBean.getVersionName(), null);
					isFromBanner = false;
				}

				/*** dataeye ***/
				if (!TextUtils.isEmpty(searchKey)) {
					DataEyeManager.getInstance().view(appInfoBean.getFileType(), appInfoBean.getCategoryId(),
							appInfoBean.getAppName(), appInfoBean.getFileSize(), appInfoBean.getVersionName(), null);
					searchKey = "";
				}

			} else if (detailResponse.state.code == 200 && detailResponse.trxrc == Constan.Rc.MARKET_APP_DETAIL) {
				// 没有数据处理
				setContentView(R.layout.empty);
			} else {
				// 异常数据处理
				showErrorView();
				return;
			}
		}
	};

	/**
	 * <response_2>获取响应数据，填充应用推荐区域
	 */
	private final Listener<JSONObject> recommendResponseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			RequestRecommendIng = false;
			LogUtil.getLogger().d("response==>" + response.toString()); // 日志输出
			// 应用推荐，响应数据处理
			recommendResponse = (RecommendResponse) JsonUtil.jsonToBean(response, RecommendResponse.class);
			if (recommendResponse != null && recommendResponse.recommendNum > 0) {
				recommendList = recommendResponse.recommendlist;
				// 判断评论集合是否为空
				if (recommendList.size() > 0) {
					mHandler.sendMessage(mHandler.obtainMessage(12, recommendList));
				}
			} else {
				// 没有推荐数据，隐藏推荐区域
				recommendView.setVisibility(View.GONE);
				return;
			}
		}
	};

	/**
	 * <response_3>获取响应数据，填充评论列表区域
	 */
	private final Listener<JSONObject> commentsResponseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString()); // 日志输出
			// 评论列表，响应数据处理
			commentResponse = (CommentResponse) JsonUtil.jsonToBean(response, CommentResponse.class);
			LogUtil.getLogger().d("response==>" + commentResponse.toString());

			if (commentResponse != null && commentResponse.commentlist != null
					&& !commentResponse.commentlist.isEmpty()) {

				// 评论数据处理
				commentsList.addAll(commentResponse.commentlist);
				if (commentResponse.isLast) {
					lvComments.removeFooterView(loadMoreView);
				} else {
					lvComments.addFooterView(loadMoreView);
				}
				// 判断评论集合是否为空
				if (commentsList.size() > 0) {
					mHandler.sendMessage(mHandler.obtainMessage(10, commentsList));
				}
			} else {
				// 判断是否点击加载更多
				if (isLoadMore) {
					ToastUtil.show(context, ResourceUtil.getString(context, R.string.No_more_data), Toast.LENGTH_SHORT);
					return;
				} else {
					// 没有评论数据，隐藏评论模块
					commentsView.setVisibility(View.GONE);
				}
			}
		}
	};

	/**
	 * <response_4>获取响应数据，填充发行商应用推荐区域
	 */
	private final Listener<JSONObject> issuerRecommendResponseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString()); // 日志输出
			// 应用推荐，响应数据处理
			RecommendResponse issuerRecommendResponse = (RecommendResponse) JsonUtil.jsonToBean(response,
					RecommendResponse.class);
			if (issuerRecommendResponse != null && issuerRecommendResponse.recommendNum > 0) {
				issuerRecommendList = issuerRecommendResponse.recommendlist;
				// 判断评论集合是否为空
				if (issuerRecommendList.size() > 0) {
					mHandler.sendEmptyMessage(16);
				} else {
					// 空数据，隐藏开发商应用推荐区域
					issuerRecommendView.setVisibility(View.GONE);
				}
			}
		}
	};

	/**
	 * <response_1>获取应用详情，异常响应处理
	 */
	private final ErrorListener detailErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			showErrorView();
			error.printStackTrace();
		}
	};

	/**
	 * <response_2>获取应用推荐，异常响应处理
	 */
	private final ErrorListener recommendErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			RequestRecommendIng = false;
			// 数据异常，隐藏应用推荐区域
			// recommendView.setVisibility(View.GONE);
			// 提示用户重新尝试
			ToastUtil.show(context, ResourceUtil.getString(context, R.string.load_error_tips), Toast.LENGTH_SHORT);
			error.printStackTrace();
		}
	};

	/**
	 * <response_3>获取应用评论，异常响应处理
	 */
	private final ErrorListener commentsErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			// 数据异常，隐藏评论模块区域
			// commentsView.setVisibility(View.GONE);
			ToastUtil.show(context, ResourceUtil.getString(context, R.string.load_error_tips), Toast.LENGTH_SHORT);
			error.printStackTrace();
		}
	};

	/**
	 * <response_4>获取开发商应用推荐，异常响应处理
	 */
	private final ErrorListener issuerRecommendErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			// 数据异常，隐藏开发商应用推荐区域
			issuerRecommendView.setVisibility(View.GONE);
		}
	};

	/**
	 * 初始化收藏功能
	 */
	private void initFavoriteApp() {
		isFavoriteApp = FavoriteManage.getInstance(this).getIsFavoriteApp(appInfoBean.getApkId());
		changeFavoriteIcon(isFavoriteApp);
	}

	/**
	 * 处理点击收藏按钮
	 */
	private void onClickFavorite() {
		boolean result;
		if (!isFavoriteApp) {
			result = FavoriteManage.getInstance(this).addFavoriteApp(appInfoBean);
			if (result) {
				ToastUtil.show(context, ResourceUtil.getString(context, R.string.add_favorite_success),
						Toast.LENGTH_SHORT);
				isFavoriteApp = true;
			} else {
				ToastUtil.show(context, ResourceUtil.getString(context, R.string.favorite_over_size),
						Toast.LENGTH_SHORT);
				isFavoriteApp = false;
			}
		} else {
			result = FavoriteManage.getInstance(this).cancelFavoriteApp(appInfoBean.getApkId());
			if (result) {
				ToastUtil.show(context, ResourceUtil.getString(context, R.string.cancel_favorite_success),
						Toast.LENGTH_SHORT);
				isFavoriteApp = false;
			} else {
				ToastUtil.show(context, ResourceUtil.getString(context, R.string.cancel_favorite_fail),
						Toast.LENGTH_SHORT);
			}
		}
		changeFavoriteIcon(isFavoriteApp);
	}

	/**
	 * 改变收藏图标
	 * 
	 * @param isFavoriteApp
	 */
	private void changeFavoriteIcon(boolean isFavoriteApp) {
		mHandler.sendMessage(mHandler.obtainMessage(13, isFavoriteApp));
	}

	/**
	 * 发布评论完成时回调，更新评论列表
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (40 == resultCode) {
			cleanCommentsList(); // 先清空原有的列表数据
			pageNo = 1;
			loadCommentsData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 清空评论列表数据
	 */
	private void cleanCommentsList() {
		lvComments.removeFooterView(loadMoreView);
		int size = commentsList.size();
		if (size > 0) {
			commentsList.removeAll(commentsList);
			commentsList.clear();
			commentListAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 清空推荐列表数据
	 */
	private void cleanRecommendList() {
		int size = recommendList.size();
		if (size > 0) {
			recommendList.clear();
		}
	}

	/**
	 * 计算评论总数 及 百分比
	 * 
	 * @param response
	 */
	private void loadPercents(JSONObject response) {
		try {
			JSONArray jsonArray = response.getJSONArray("starslist");
			JSONObject jsonObject = null;
			if (jsonArray != null && jsonArray.length() > 0) {
				int total = 0;
				int[] num = new int[5];
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					num[i] = jsonObject.getInt("amount");
					total = total + jsonObject.getInt("amount");
				}
				if (total == 0) { // 计算评论人数，
					pg5.setProgress(100);
					return;
				} else {
					comments.setText("(" + total + ")");// 赋值=评论人数
					// 赋值=星级百分比
					pg1.setMax(total);
					pg2.setMax(total);
					pg3.setMax(total);
					pg4.setMax(total);
					pg5.setMax(total);
					pg1.setProgress(num[0]);
					pg2.setProgress(num[1]);
					pg3.setProgress(num[2]);
					pg4.setProgress(num[3]);
					pg5.setProgress(num[4]);
				}
			} else {
				pg5.setProgress(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取应用图片（截图）路径，封装到picList集合中
	 * 
	 * @param detailResponse
	 */
	private void getPicUrls(DetailResponse detailResponse) {

		if (detailResponse.picNum > 0 && detailResponse.piclist != null) {

			int size = detailResponse.picNum >= 6 ? 6 : detailResponse.picNum; // 限制图片数量
																				// 6
																				// 张

			for (int i = 0; i < size; i++) {
				picList.add(detailResponse.piclist.get(i));
			}

			mHandler.sendMessage(mHandler.obtainMessage(11, picList));
		} else {
			// 获取图片异常，显示失败图片
			loadingView.setVisibility(View.GONE);
			failView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 注册广播接收者
	 */
	private void registUiReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);
		LogUtil.getLogger().d("AppDetailActivity==================registUiReceiver");
	}

	/**
	 * @ClassName: DownloadUiReceiver
	 * @Desciption: 下载广播实现内部类
	 
	 * @Date: 2014-2-14 下午2:10:05
	 */
	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (appInfoBean != null && appInfoBean.getUrl() != null)
				handleIntent(intent);
		}

		private void handleIntent(Intent intent) {
			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String url = null;
				String percent = null;
				String localPath;
				int silentInstallResult;
				switch (type) {
				// 已删除状态，出现下载按钮，隐藏其他所有按钮
				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)) {
						commentsBtn.setEnabled(false);
						commentsBtn.setBackgroundResource(R.drawable.ic_btn_gray);
						refreshAppStatus(AppInfoBean.Status.NORMAL, percent);
					}
					break;
				// 下载完成状态，出现安装按钮，隐藏下载进度条（暂停按钮）
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)) {
						refreshAppStatus(AppInfoBean.Status.CANINSTALL, percent);
					}
					break;
				// 安装完成状态，出现启动按钮，隐藏安装按钮
				case MyIntents.Types.COMPLETE_INSTALL:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)) {
						commentsBtn.setEnabled(true);
						commentsBtn.setBackgroundResource(R.drawable.selector_btn_comments);
						refreshAppStatus(AppInfoBean.Status.CANLAUNCH, percent);
					}
					break;
				// 准备下载状态，初始化操作
				case MyIntents.Types.PREDOWNLOAD:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)) {
						percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
						refreshAppStatus(AppInfoBean.Status.CONNECTING, percent);
					}
					break;
				// 等待状态，显示等待按钮
				case MyIntents.Types.WAIT:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)) {
						refreshAppStatus(AppInfoBean.Status.WAITING, percent);
					}
					break;
				// 异常、暂停状态，出现继续按钮，隐藏暂停按钮
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)) {
						refreshAppStatus(AppInfoBean.Status.PAUSED, percent);
					}
					break;
				// 下载中状态，实时更新下载进度条
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)) {
						percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
						refreshAppStatus(AppInfoBean.Status.DOWNLOADING, percent);
					}
					break;
				case MyIntents.Types.MAKE_UPGRADE:
					notifyPatchUpgrade();
					break;
				case MyIntents.Types.MERGE_PATCH:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)) {
					}
					break;
				// 安装中（静默安装）
				case MyIntents.Types.INSTALLING:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)) {
						refreshAppStatus(AppInfoBean.Status.INSTALLING, percent);
					}
					break;
				// 安装结果（静默安装）
				case MyIntents.Types.INSTALL_RESULT:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					silentInstallResult = intent.getIntExtra(MyIntents.INSTALL_RESULT_CODE, 0);
					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url) && appInfoBean.getUrl().equals(url)
							&& silentInstallResult != SilentUtil.INSTALL_SUCCEEDED) {
						refreshAppStatus(AppInfoBean.Status.INSTALLING, percent);
						ToastUtil.show(context, R.string.toast_silent_install_failed, Toast.LENGTH_LONG);
						DownloadEntityManager.getInstance().updateDownloadInstallStatus(url);
						PackageUtil.normalInstall(context, localPath);
					}
					break;
				}
			}
		}
	}

	/**
	 * @Title: notifyPatchUpgrade
	 * @Description: 获取增量更新数据 更新界面
	 * @param
	 * @return void
	 */

	private void notifyPatchUpgrade() {
		if (appInfoBean == null)
			return;
		InstallAppBean updateAppBean = UpdateManage.getInstance(this).getUpdateAppBeanByPackageName(
				appInfoBean.getPackageName());
		if (updateAppBean != null && updateAppBean.getIsPatch()) {
			appInfoBean.setStatus(AppInfoBean.Status.CANUPGRADE); // 增量更新
			appInfoBean.setPatch(true);
			appInfoBean.setUrl(updateAppBean.getUrl());
			appInfoBean.setPatchUrl(updateAppBean.getUrlPatch());
			appInfoBean.setPatchFileSize(updateAppBean.getPatchSize());
		}
		if (appInfoBean.isPatch() && appInfoBean.getPatchFileSize() != 0) {
			appPatchSize.setText(Utils.sizeFormat(appInfoBean.getPatchFileSize()));
			appPatchSize.setVisibility(View.VISIBLE);
			upgradeLineIv.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 应用下载、升级
	 * 
	 * @param appInfoBean
	 * @param downloadIntent
	 */

	private void addDownload(AppInfoBean appInfoBean) {
		SharedPrefsUtil.putValue(context, "ct_" + appInfoBean.getApkId(), ct); // 存储ct值，方便下载统计使用
		boolean isUpdatePatch = false;
		String downloadUrl = appInfoBean.getUrl();
		if (appInfoBean.isPatch() && !TextUtils.isEmpty(appInfoBean.getPatchUrl())) {
			isUpdatePatch = true;
			downloadUrl = appInfoBean.getPatchUrl();
		}
		String mediaType = MediaType.APP;
		if (appInfoBean.getFileType() == 1) {
			mediaType = MediaType.APP;
		} else if (appInfoBean.getFileType() == 2) {
			mediaType = MediaType.GAME;
		}
		DownloadBean downloadBean = new DownloadBean(downloadUrl, appInfoBean.getAppName(), appInfoBean.getFileSize(),
				0, appInfoBean.getLogo(), mediaType, appInfoBean.getApkId(), appInfoBean.getVersionName(),
				appInfoBean.getPackageName(), DownloadTask.TASK_DOWNLOADING, appInfoBean.getFileSize(),
				appInfoBean.getVersionCode(), appInfoBean.getAppId(), appInfoBean.getCategoryId(),
				appInfoBean.getStars(), isUpdatePatch, appInfoBean.getUrl());
		DownloadManager.getInstance().addDownload(context, downloadBean);

	}

	/**
	 * app 当前状态比对，获取当前app包名，比对是否已安装
	 */
	private void compareStatus() {

		String packageName = appInfoBean.getPackageName();
		String versionCode = "" + appInfoBean.getVersionCode();
		InstallAppBean installAppBean = LocalAppEntityManager.getInstance().getLocalAppByPackageName(packageName);
		InstallAppBean updateAppBean = UpdateManage.getInstance(this).getUpdateAppBean(packageName,
				Integer.valueOf(versionCode));
		if (installAppBean != null) {
			// 启用评论按钮
			commentsBtn.setEnabled(true);
			commentsBtn.setBackgroundResource(R.drawable.selector_btn_comments);

			if (updateAppBean != null) {
				appInfoBean.setStatus(AppInfoBean.Status.CANUPGRADE); // 增量更新
				appInfoBean.setPatch(true);
				appInfoBean.setUrl(updateAppBean.getUrl());
				appInfoBean.setPatchUrl(updateAppBean.getUrlPatch());
				appInfoBean.setPatchFileSize(updateAppBean.getPatchSize());
			} else if (appInfoBean.getVersionCode() > Integer.valueOf(installAppBean.getVersionCode())) {

				appInfoBean.setStatus(AppInfoBean.Status.CANUPGRADE); // 可更新
			} else {
				appInfoBean.setStatus(AppInfoBean.Status.CANLAUNCH); // 可运行
			}
		}

		DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByPkgName(packageName,
				versionCode);
		if (downloadBean != null) {

			appInfoBean.setTotalBytes(downloadBean.getTotalBytes());
			appInfoBean.setCurrentBytes(downloadBean.getCurrentBytes());

			int status = downloadBean.getStatus();
			if (status == DownloadTask.TASK_DOWNLOADING) {
				appInfoBean.setStatus(AppInfoBean.Status.DOWNLOADING);
			} else if (status == DownloadTask.TASK_WAITING) {
				appInfoBean.setStatus(AppInfoBean.Status.WAITING);
			} else if (status == DownloadTask.TASK_CONNECTING) {
				appInfoBean.setStatus(AppInfoBean.Status.CONNECTING);
			} else if (status == DownloadTask.TASK_PAUSE) {
				appInfoBean.setStatus(AppInfoBean.Status.PAUSED);
			} else if (status == DownloadTask.TASK_FINISH) {
				appInfoBean.setStatus(AppInfoBean.Status.CANINSTALL);
			} else if (status == DownloadTask.TASK_INSTALLING) {
				appInfoBean.setStatus(AppInfoBean.Status.INSTALLING);
			}
		}

		long totalBytes = appInfoBean.getTotalBytes();
		long currentBytes = appInfoBean.getCurrentBytes();
		String percent = null;
		if (totalBytes != 0)
			percent = (currentBytes * 100 / totalBytes) + "";

		// 刷新状态
		refreshAppStatus(appInfoBean.getStatus(), percent);
	}

	/**
	 * 重新下载对话框
	 * 
	 * @param downloadBean
	 * @param tips
	 */
	private void showReDownloadDialog(final String url, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, url);
				addDownload(appInfoBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	/**
	 * 发生异常时，显示异常界面
	 */
	private void showErrorView() {
		normalView.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
		errorView.findViewById(R.id.e_retry_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, ResourceUtil.getString(context, R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}
				errorView.setVisibility(View.GONE);
				normalView.setVisibility(View.VISIBLE);

				refreshData();// 刷新数据
			}
		});
	}

	/**
	 * @desc: 获取来源第三方市场的应用包名
	 * @params:
	 * @return: void
	 */
	private String getMarketData() {
		String msg = getIntent().getDataString();
		if (msg != null) {

			packageName = msg.substring(msg.indexOf("=") + 1, msg.length());
			return packageName;
		}
		return msg;
	}

	/**
	 * 全局按钮事件处理
	 */
	@Override
	public void onClick(View v) {

		// 非空判断，防止程序报错
		if (appInfoBean == null) {
			return;
		}
		downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId("" + appInfoBean.getApkId(),
				"" + appInfoBean.getVersionCode());
		switch (v.getId()) {
		// 文字伸展按钮事件
		case R.id.btn_text_zoom:
			if (flag) {
				flag = false;
				appDesc.setEllipsize(null); // 文字展开
				appDesc.setSingleLine(flag);
				zoom.setBackgroundResource(R.drawable.ic_download_manager_arrow_up);
			} else {
				flag = true;
				appDesc.setEllipsize(TextUtils.TruncateAt.END); // 文字收缩
				appDesc.setMaxLines(4);
				zoom.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
			}
			break;

		// 应用分享按钮事件
		case R.id.btn_app_share:
			Utils.shareMsg(context, appInfoBean.getAppName(), appInfoBean.getPackageName());
			break;
		// 应用收藏按钮事件
		case R.id.btn_app_collect:
			onClickFavorite();
			break;
		// 加载更多（分页）按钮事件
		case R.id.btn_load:
			pageNo++;
			isLoadMore = true;
			lvComments.removeFooterView(loadMoreView);
			loadCommentsData();
			break;

		// 应用评论按钮事件
		case R.id.btn_app_comments:
			if (!NetworkUtils.isNetworkAvailable(context)) {
				ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
						Toast.LENGTH_SHORT);
				return;
			}
			String packageName = detailResponse.app.getPackageName();
			if(Tools.isAppInstalled(AppDetailActivity.this, packageName) == false)
			{
				ToastUtil.show(AppDetailActivity.this, R.string.app_commentstips_text, 1);
				return;
			}
				Intent commentsIntent = new Intent(context, CommentActivity.class);
				commentsIntent.putExtra("appInfoBean", appInfoBean);
				startActivityForResult(commentsIntent, 98);
			break;

		// 应用推荐刷新按钮事件
		case R.id.btn_refresh:
			if (loadingView != null && loadingView.isShown())
				return;
			if (RequestRecommendIng)
				return;
			if (!NetworkUtils.isNetworkAvailable(context)) {
				ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
						Toast.LENGTH_SHORT);
				return;
			}
			refreshRecommendApps();
			break;

		// 下载按钮事件，点击显示下载进度条（暂停按钮），隐藏下载按钮
		case R.id.hali_app_download_btn:
			addDownload(appInfoBean);
			break;

		// 暂停按钮事件，点击显示继续按钮，隐藏暂停按钮
		case R.id.hali_app_pause_ll:
			if (downloadBean == null)
				return;
			DownloadManager.getInstance().pauseDownload(context, downloadBean.getUrl());
			break;

		// 继续按钮事件，点击显示下载进度条（暂停按钮），并隐藏继续按钮
		case R.id.hali_app_continue_btn:
			if (downloadBean == null)
				return;
			DownloadManager.getInstance().continueDownload(context, downloadBean.getUrl());
			break;

		// 安装按钮事件，点击打开系统安装界面
		case R.id.hali_app_install_btn:
			if (downloadBean == null)
				return;
			// to install
			if (downloadBean.isPatch()) {
				if (UpdateManage.getInstance(context).isNewApkFileExit(downloadBean.getPackageName(),
						downloadBean.getVersionName())) {
					PackageUtil.installApk(
							context,
							UpdateManage.getInstance(context).getNewPatchApkPath(downloadBean.getPackageName(),
									downloadBean.getVersionName()));
				} else if (!Utils.isAppExit(downloadBean.getPackageName(), context)) { // 低版本被卸载，下载全量包
					showReDownloadFulldoseDialog(
							downloadBean,
							ResourceUtil.getString(context, R.string.dialog_redownload_full_dose,
									downloadBean.getName()));
				} else {// 合并失败，下载增量包
					showReDownloadPatchDialog(downloadBean, ResourceUtil.getString(context,
							R.string.dialog_redownload_incremental_upgrade, downloadBean.getName()));
				}
			} else if (StorageUtils.isFileExit(downloadBean.getLocalPath())) {
				PackageUtil.installApk(context, downloadBean.getLocalPath());
			} else { // 文件不存在，正常重下流程
				showReDownloadDialog(
						downloadBean,
						ResourceUtil.getString(context, R.string.dialog_redownload_file_not_found,
								downloadBean.getName()));
			}

			break;

		// 升级按钮事件，点击出现下载进度条（暂停按钮），隐藏升级按钮
		case R.id.hali_app_upgrade_btn:
			addDownload(appInfoBean);
			break;

		// 启动按钮事件，点击启动应用
		case R.id.hali_app_launch_btn:
			if (Utils.isAppExit(appInfoBean.getPackageName(), context)) {
				Utils.launchAnotherApp(context, appInfoBean.getPackageName());
			} else {
				showReDownloadDialog(
						appInfoBean.getUrl(),
						ResourceUtil.getString(context, R.string.dialog_redownload_app_not_found,
								appInfoBean.getAppName()));
			}
			break;
		}
	}

	private void showReDownloadDialog(final DownloadBean downloadBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getUrl(), downloadBean.getName(),
						downloadBean.getTotalBytes(), 0, downloadBean.getIconUrl(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), downloadBean.getStars(), false,
						downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	/**
	 * @Title: showReDownloadFulldoseDialog
	 * @Description: 重新下载全量包
	 * @param @param downloadBean
	 * @param @param tips
	 * @return void
	 */

	private void showReDownloadFulldoseDialog(final DownloadBean downloadBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getOriginalUrl(), downloadBean.getName(),
						downloadBean.getTotalBytes(), 0, downloadBean.getIconUrl(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), downloadBean.getStars(), false,
						downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	/**
	 * @Title: showReDownloadPatchDialog
	 * @Description: 重下增量包
	 * @param @param downloadBean
	 * @param @param tips
	 * @return void
	 */

	private void showReDownloadPatchDialog(final DownloadBean downloadBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getUrl(), downloadBean.getName(),
						downloadBean.getTotalBytes(), 0, downloadBean.getIconUrl(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), downloadBean.getStars(), true,
						downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	/**
	 * 摇一摇监听回调
	 */
	private final OnShakeListener onShakeListener = new OnShakeListener() {

		@Override
		public void onShake() {
			LogUtil.getLogger().d("ShakeDetector", "shake....");
			if (errorView != null && errorView.isShown())
				return;
			if (loadingView != null && loadingView.isShown())
				return;
			if (RequestRecommendIng)
				return;
			if (!isPanelExpanded)
				return;
			if (!NetworkUtils.isNetworkAvailable(context)) {
				ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
						Toast.LENGTH_SHORT);
				return;
			}
			AudioEffectManager.getInstance().playShakeAudioEffect(context);
			refreshRecommendApps();
		}
	};

	/**
	 * 开始摇一摇监听
	 */
	private void startShakeDetector() {
		try {
			initShakeDetector();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止摇一摇监听
	 */
	private void stopShakeDetector() {
		if (shakeDetector != null)
			shakeDetector.stop();
		AudioEffectManager.getInstance().releaseMusicPlayer();
	}

	/**
	 * 初始化摇一摇
	 */
	private void initShakeDetector() {
		shakeDetector = new ShakeDetector(context);
		shakeDetector.registerOnShakeListener(onShakeListener);
		shakeDetector.start();
	}

	/**
	 * 刷新引用推荐
	 */
	private void refreshRecommendApps() {
		// cleanRecommendList();
		loadRecommendData();
	}

	@Override
	public void onBackPressed() {
		if (isFromSearch) {
			// 来源引用搜索，直接finish
		} else if (ct == Constan.Ct.MARKET_APP_DETAIL) {
			if (SharedPrefsUtil.getValue(context, "isFromScanQRCode", false)) {
				// 来源第三方市场，并且是使用zapp扫描跳转的,重新跳转到ToolsActivity
				SharedPrefsUtil.removeValue(context, "isFromScanQRCode");
				startActivity(new Intent(this, ToolsActivity.class));
			} else {
				// 来源第三方市场
				startActivity(new Intent(this, SplashActivity.class));
			}
		}
		mHandler.sendEmptyMessage(HANDLE_SLIDEVIEW_COLLAPSED);
	}

	/**
	 * @Title: setSkinTheme
	 * @Description: TODO
	 * @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTextViewDrawableLeft(context, refreshBtn, SkinConstan.APP_REFRESH_BTN);
		SkinConfigManager.getInstance().setViewBackground(context, dividerLineView, SkinConstan.TITLE_BAR_BG);
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
		SkinConfigManager.getInstance().setTextViewStringColor(context, appSource, SkinConstan.APP_THEME_COLOR);
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, launchBtn, SkinConstan.LAUNCH_BTN);
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, installBtn, SkinConstan.INSTALL_BTN);
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, upgradeBtn, SkinConstan.UPGRADE_BTN);
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, downloadBtn, SkinConstan.DOWNLOAD_BTN);
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, continueBtn, SkinConstan.CONTINUE_BTN);
		// RoundProgress Color
		SkinConfigManager.getInstance().setRoundProgressColor(context, downloadPercentPv,
				SkinConstan.ROUND_PROGRESS_COLOR);
	}

	private void startDownloadAnimation() {
		if (downloadAnimation == null) {
			downloadAnimation = (AnimationDrawable) downloadPercentPv.getBackground();
		}
		if (downloadAnimation.isRunning()) {
			return;
		}
		downloadAnimation.start();
	}

	private void stopDownloadAnimation() {
		if (downloadAnimation != null && downloadAnimation.isRunning()) {
			downloadAnimation.stop();
			Utils.setBackgroundDrawable(downloadPercentPv, null);
			downloadPercentPv.setBackgroundResource(R.anim.download);
			downloadAnimation = null;
		}
	}

	/**
	 * @Title: initSlidingUpPanel
	 * @Description: 新功能：更改UI样式
	 * @param
	 * @return void
	 */
	private void initSlidingUpPanel() {
		topIndicator = (ImageView) findViewById(R.id.img_top_indicator);
		slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		slidingUpPanelLayout.setAnchorPoint(0.75f);
		slidingUpPanelLayout.setPanelSlideListener(new PanelSlideListener() {

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// TODO Auto-generated method stub
				if (slideOffset > 0.98f) {
					topIndicator.setVisibility(View.VISIBLE);
				} else {
					topIndicator.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPanelHidden(View panel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPanelExpanded(View panel) {
				// TODO Auto-generated method stub
				isPanelExpanded = true;
				slidingUpPanelLayout.setTouchEnabled(false);
				scrollViewExtend.setEnableDispatchTouchEvent(false);
				scrollViewExtend.setCloseDispatchTouchEvent(true);
			}

			@Override
			public void onPanelCollapsed(View panel) {
				// TODO Auto-generated method stub
				isPanelExpanded = false;
				finish();
			}

			@Override
			public void onPanelAnchored(View panel) {
				// TODO Auto-generated method stub]
				isPanelExpanded = false;
			}
		});
	}

	/**
	* @ClassName: MyListener
	* @Description: 头部区域时间监听
	
	* @date 2015-4-2 下午2:56:12
	*
	 */
	class MyListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (isPanelExpanded) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					slidingUpPanelLayout.setTouchEnabled(true);
					break;

				case MotionEvent.ACTION_UP:
					slidingUpPanelLayout.setTouchEnabled(false);
					break;
				}
			}
			return false;
		}
	}

	/**
	* @Title: initIssuerRecommend 
	* @Description: 发行商应用推荐
	* @param     
	* @return void
	 */
	private void initIssuerRecommend() {
		issuerRecommendView.setVisibility(View.VISIBLE);
		viewPager = (ViewPager) findViewById(R.id.issuer_viewpager);
		viewPagerAdapter = new ViewPagerAdapter();
		viewPager.setAdapter(viewPagerAdapter);
	}

	/**
	* @ClassName: ViewPagerAdapter
	* @Description: 发行商应用推荐PagerAdapter
	
	* @date 2015-4-7 上午9:48:31
	*
	 */
	private class ViewPagerAdapter extends PagerAdapter {

		private static final int itemCount = 4;

		@Override
		public int getCount() {
			int size = issuerRecommendList.size();
			int count = (size % itemCount == 0) ? size / itemCount : size / itemCount + 1;
			return count;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			View rootView = getLayoutInflater().inflate(R.layout.issuer_viewpager_item, null);

			GridView gridView = (GridView) rootView.findViewById(R.id.issuer_gridview);

			ArrayList<RecommendBean> myLists = new ArrayList<RecommendBean>();

			for (int i = itemCount * position; i < itemCount * (1 + position) && i < issuerRecommendList.size(); i++) {
				myLists.add(issuerRecommendList.get(i));
			}

			RecommendListAdapter mAdapter = new RecommendListAdapter(AppDetailActivity.this,
					Constan.Rc.ISSUER_APP_RECOMMEND);

			mAdapter.setList(myLists);

			gridView.setAdapter(mAdapter);

			((ViewPager) container).addView(rootView, 0);
			return rootView;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == ((View) object);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView((View) object);
		}
	}
}
