package com.x.ui.activity.wallpaper;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.wallpaper.WallpaperManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadProcesser;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.WallpaperRequest;
import com.x.publics.http.model.WallpaperResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.ThemeBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshScrollView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;
import com.x.ui.view.quiltview.RQuiltView;

/**
 * @ClassName: WallpaperAlbumActivity
 * @Desciption: 图片专辑、二级界面
 
 * @Date: 2014-3-26 下午6:41:03
 */

public class WallpaperAlbumActivity extends BaseActivity implements OnClickListener {

	private TextView title;
	private ImageView imageView;
	private TextView description,wallPaperNum;
	private ThemeBean themeBean;
	private View loadingPb, loadingLogo;
	private LinearLayout oneClickNormalBtn, oneClickPressedBtn;

	private RQuiltView mQuiltView;
	private PullToRefreshScrollView mScrollView;
	private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
	private ArrayList<WallpaperBean> imageList = new ArrayList<WallpaperBean>();

	private Pager pager;
	private int pageNum = 1;
	private int pageSize = 12;
	private ImageView mGobackIv;
	private WallpaperRequest request;
	private Activity mActivity = this;
	private TextView mTitleTv, mDownloadAll;
	private View errorView, loadingView, mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper_album);
		themeBean = (ThemeBean) getIntent().getSerializableExtra("themeBean");
		initViews();
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 加载数据
		if (imageList.isEmpty()) {
			getData(1);
		}
		if (themeBean != null) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.ALBUM + "-" + themeBean.getName(), true);
			DataEyeManager.getInstance().source(themeBean.getThemeId(), 0, null, 0L, null, null, false);
		}

	}

	/**
	 * 初始化UI
	 * @return void
	 */
	private void initViews() {
		initTitle();
		mQuiltView = (RQuiltView) findViewById(R.id.quiltview);
		errorView = (RelativeLayout) findViewById(R.id.e_error_rl);
		loadingView = (RelativeLayout) findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);

		mScrollView = (PullToRefreshScrollView) findViewById(R.id.ptr_scrollview);
		mScrollView.setOnRefreshListener(onRefreshListener);
		mScrollView.setMode(Mode.DISABLED);
		mScrollView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);

		SoundPullEventListener<ScrollView> soundListener = new SoundPullEventListener<ScrollView>(mActivity);
		soundListener.addSoundEvent(State.REFRESH_TO_RESET, R.raw.refresh);
		mScrollView.setOnPullEventListener(soundListener);
		wallPaperNum = (TextView) findViewById(R.id.wp_ablum_num_tv);
		title = (TextView) findViewById(R.id.wp_tv_name);
		description = (TextView) findViewById(R.id.wp_tv_brief);
		imageView = (ImageView) findViewById(R.id.wp_img_logo);
		mDownloadAll = (TextView) findViewById(R.id.tv_download_all);
		oneClickNormalBtn = (LinearLayout) findViewById(R.id.btn_one_click_download_normal);
		oneClickPressedBtn = (LinearLayout) findViewById(R.id.btn_one_click_download_pressd);

		oneClickNormalBtn.setOnClickListener(this);

		setParams();
	}

	private void initTitle() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mNavigationView.setOnClickListener(this);
		mTitleTv.setText(R.string.page_wallpaper_ablum);
	}

	/**
	 * 设置界面初始参数
	 * @return void 
	 */
	private void setParams() {
		title.setText(themeBean.getName());
		description.setText(themeBean.getDescription());
		wallPaperNum.setText(themeBean.getWallpaperNum()+" "+
				this.getResources().getString(R.string.wallpaper_ablum_num_text)); //设置专辑图片数量文字
		NetworkImageUtils.load(this, ImageType.NETWORK, themeBean.getBigicon(), R.drawable.banner_default_picture,
				R.drawable.banner_default_picture, imageView);
		// 判断是否已经一键下载
		if (SharedPrefsUtil.getThemeValue(mActivity, "theme_" + themeBean.getThemeId(), false)) {
			oneClickNormalBtn.setVisibility(View.GONE);
			oneClickPressedBtn.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 发送请求，获取数据
	 * @param page
	 * @return void
	 */
	private void getData(int page) {
		request = new WallpaperRequest();
		pager = new Pager(page);
		pager.setPs(pageSize);
		request.setPager(pager);
		request.setThemeId(themeBean.getThemeId());
		DataFetcher.getInstance().getWallpaperAlbumDetail(request, myResponseListent, myErrorListener, true);
	}

	/**
	 * 数据响应
	 */
	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			loadingView.setVisibility(View.GONE);
			mScrollView.setVisibility(View.VISIBLE);
			mScrollView.onRefreshComplete();
			LogUtil.getLogger().d("response==>" + response.toString());
			WallpaperResponse wallpaperResponse = (WallpaperResponse) JsonUtil.jsonToBean(response,
					WallpaperResponse.class);
			if (wallpaperResponse != null && wallpaperResponse.state.code == 200) {
				if (!wallpaperResponse.imagelist.isEmpty()) {
					imageList.addAll(wallpaperResponse.imagelist);
					// 添加图片元素到容器中
					addItemToContainer();

					if (wallpaperResponse.isLast) {
						cancleGridViewScorllable();
					} else {
						mScrollView.setMode(Mode.PULL_FROM_END); // 只能下拉加载更多，不允许上拉刷新
					}
				} else {
					if (imageList.isEmpty()) {
						showErrorView();
					} else {
						cancleGridViewScorllable();
					}
				}

			} else {
				showErrorView();
			}
		}
	};

	/**
	 * 数据响应，异常处理
	 */
	private ErrorListener myErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			loadingView.setVisibility(View.GONE);
			mScrollView.setVisibility(View.VISIBLE);
			mScrollView.onRefreshComplete();
			showErrorView();
			error.printStackTrace();
		}
	};

	/**
	 * 显示错误页面
	 */
	private void showErrorView() {
		mQuiltView.clear(); // 清空界面
		imageList.clear();
		if (pageNum > 1) {
			--pageNum;
		}
		if (imageList.isEmpty()) {
			mScrollView.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
			errorView.findViewById(R.id.e_retry_btn).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!NetworkUtils.isNetworkAvailable(mActivity)) {
						ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return;
					}
					errorView.setVisibility(View.GONE);
					mScrollView.setVisibility(View.GONE);
					loadingView.setVisibility(View.VISIBLE);
					getData(1);
				}
			});
		}
	}

	private void cancleGridViewScorllable() {
		if (mScrollView != null)
			mScrollView.setMode(Mode.DISABLED);
	}

	/**
	 * 下拉刷新、上拉加载更多数据。。
	 */
	private OnRefreshListener2<ScrollView> onRefreshListener = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			//			if (pageNum == 1) {
			//				if (!NetworkUtils.isNetworkAvailable(mActivity)) {
			//					showErrorView();
			//				} else {
			//					mScrollView.onRefreshComplete();
			//				}
			//			} else {
			mQuiltView.clear(); // 清空界面
			imageList.clear();
			pageNum = 1;
			getData(1);
			//			}
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			getData(++pageNum);
		}
	};

	/**
	 * 将图片元素到集合中
	 */
	private void addItemToContainer() {
		int size = imageList.size();
		imageViews.clear(); // 必须先清空，否则报错..
		for (int i = (pageNum - 1) * pageSize; i < pageSize * pageNum && i < size; i++) {
			imageViews.add(addImage(imageList.get(i), i));
		}
		handler.sendMessage(handler.obtainMessage(23, imageViews));
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 23:
				mQuiltView.addPatchImages((ArrayList<ImageView>) msg.obj, mActivity);
				break;
			}
		}
	};

	/**
	 * 添加图片
	 * 
	 * @param imageUrl
	 * @param columnIndex
	 */
	private ImageView addImage(final WallpaperBean wallpaperBean, final int index) {
		final ImageView imageView = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.quiltview_item, null);
		// 加载图片资源（中图）
		NetworkImageUtils.load(mActivity, ImageType.NETWORK, wallpaperBean.getBiglogo(),
				R.drawable.banner_default_picture, R.drawable.banner_default_picture, imageView);
		// 按钮触摸事件
		imageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					imageView.setAlpha(255);
					Intent intent = new Intent(mActivity, WallpaperDetailActivity.class);
					intent.putParcelableArrayListExtra("data", imageList);
					intent.putExtra("picIndex", index);
					mActivity.startActivity(intent);
					break;

				case MotionEvent.ACTION_MOVE:
					// imageView.setAlpha(255);
					break;

				case MotionEvent.ACTION_DOWN:
					imageView.setAlpha(180);
					break;

				case MotionEvent.ACTION_CANCEL:
					imageView.setAlpha(255);
					break;

				default:
					break;
				}

				return true;
			}
		});
		return imageView;
	}

	/**
	 * 全局按钮事件处理
	 * @return void
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		// 一键下载
		case R.id.btn_one_click_download_normal:
			// 网络检测
			if (!NetworkUtils.isNetworkAvailable(mActivity)) {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.network_canot_work),
						Toast.LENGTH_SHORT);
				return;
			}

			// 是否存在SD卡检测
			if (!StorageUtils.isSDCardPresent()) {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.sdcard_not_found),
						Toast.LENGTH_SHORT);
				return;
			}

			// 一键下载前，当前下载数量判断
			int downloadNum = DownloadEntityManager.getInstance().getAllUnfinishedDownloadCount();
			if (downloadNum >= DownloadProcesser.MAX_TASK_COUNT) {
				ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.download_too_many_downloads),
						ToastUtil.LENGTH_SHORT);
				return;
			}

			WallpaperManage.getInstance(mActivity).downloadAll(themeBean.getThemeId(), mHandler);
			break;
		}
	}

	/**
	 * 更新UI界面，Handler
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				SharedPrefsUtil.putThemeValue(mActivity, "theme_" + themeBean.getThemeId(), false); // 记录一键下载的主题
				oneClickNormalBtn.setVisibility(View.VISIBLE);
				oneClickPressedBtn.setVisibility(View.GONE);
				break;

			case 1:
				SharedPrefsUtil.putThemeValue(mActivity, "theme_" + themeBean.getThemeId(), true); // 记录一键下载的主题
				oneClickNormalBtn.setVisibility(View.GONE);
				oneClickPressedBtn.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	@Override
	public void onPause() {
		super.onPause();
		if (themeBean != null) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.ALBUM + "-" + themeBean.getName(), false);
		}
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(mActivity, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setDownloadAllSkin(mActivity, mDownloadAll);
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
