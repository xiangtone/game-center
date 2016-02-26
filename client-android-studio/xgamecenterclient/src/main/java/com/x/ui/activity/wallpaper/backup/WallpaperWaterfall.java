package com.x.ui.activity.wallpaper.backup;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.WallpaperRequest;
import com.x.publics.http.model.WallpaperResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshWaterfallView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;


/**
 * @ClassName: WallpaperWaterfall
 * @Desciption: 瀑布流布局
 
 * @Date: 2014-4-2 下午1:38:46
 */
public class WallpaperWaterfall extends BaseActivity {

	private LinearLayout mContainer;
	private PullToRefreshWaterfallView mWaterfallView;
	private ArrayList<WallpaperBean> imageList = new ArrayList<WallpaperBean>();
	private ArrayList<LinearLayout> waterfallItems = new ArrayList<LinearLayout>();

	private int itemWidth;
	private int column = 2;// 显示列数

	private WallpaperRequest request;
	private Pager pager;
	private View loadingView;
	private View errorView;
	private int pageNum = 1;
	private int pageSize = 8;
	private int categoryId;
	private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper_category_detail);
		// 获取每列的宽度
		mActivity = this;
		itemWidth = Utils.getItemWidth(this, column);
		categoryId = getIntent().getIntExtra("categoryId", 0);

		initViews();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 加载数据
		if (imageList.isEmpty()) {
			getData(1);
		}
	}

	/**
	 * 初始化UI
	 */
	private void initViews() {
		mWaterfallView = (PullToRefreshWaterfallView) findViewById(R.id.waterfallview);
		mWaterfallView.setOnRefreshListener(onRefreshListener);
		mWaterfallView.setMode(Mode.BOTH);
		initItemLayout();
	}

	/**
	 * 初始化图片元素容器布局
	 */
	private void initItemLayout() {
		imageList.clear();
		waterfallItems.clear();
		if (mContainer != null) {
			mContainer.removeAllViews();
		}
		mContainer = (LinearLayout) findViewById(R.id.waterfallcontainer);
		for (int i = 0; i < column; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					itemWidth, LayoutParams.WRAP_CONTENT);
			itemLayout.setPadding(2, 2, 2, 2);
			itemLayout.setOrientation(LinearLayout.VERTICAL);
			itemLayout.setLayoutParams(itemParam);
			waterfallItems.add(itemLayout);
			mContainer.addView(itemLayout);
		}
	}

	/**
	 * 发送请求，获取数据
	 * 
	 * @param page
	 */
	private void getData(int page) {
		request = new WallpaperRequest();
		pager = new Pager(page);
		pager.setPs(pageSize);
		request.setPager(pager);
		request.setCategoryId(categoryId);
		DataFetcher.getInstance().getWallpaperCategoryDetail(request,
				myResponseListent, myErrorListener, true);
	}

	/**
	 * 数据响应
	 */
	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			mWaterfallView.onRefreshComplete();
			LogUtil.getLogger().d("response==>" + response.toString());
			WallpaperResponse wallpaperResponse = (WallpaperResponse) JsonUtil
					.jsonToBean(response, WallpaperResponse.class);
			if (wallpaperResponse.state.code == 200) {
				if (!wallpaperResponse.imagelist.isEmpty()) {

					imageList.addAll(wallpaperResponse.imagelist);
					// 添加图片元素到容器中
					addItemToContainer();

					if (wallpaperResponse.isLast) {
						cancleGridViewScorllable();
					} else {
						mWaterfallView.setMode(Mode.BOTH);
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
			mWaterfallView.onRefreshComplete();
			showErrorView();
			error.printStackTrace();
		}
	};

	/**
	 * 显示错误页面
	 */
	private void showErrorView() {
		--pageNum;
		if (imageList.isEmpty()) {
			mWaterfallView.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
			errorView.findViewById(R.id.e_retry_btn).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (!NetworkUtils.isNetworkAvailable(mActivity)) {
								ToastUtil.show(
										mActivity,
										ResourceUtil.getString(mActivity,
												R.string.network_canot_work),
										Toast.LENGTH_SHORT);
								return;
							}
							mWaterfallView.setVisibility(View.VISIBLE);
							errorView.setVisibility(View.GONE);
							// mWaterfallView.setEmptyView(loadingView);
							getData(1);
						}
						
					});
		}
	}

	private void cancleGridViewScorllable() {
		if (mWaterfallView != null)
			mWaterfallView.setMode(Mode.PULL_FROM_START);
	}

	/**
	 * 下拉刷新、上拉加载更多数据。。
	 */
	private OnRefreshListener2<ScrollView> onRefreshListener = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			if (pageNum == 1) {
				mWaterfallView.onRefreshComplete();
			} else {
				initItemLayout();
				pageNum = 1;
				getData(1);
			}
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
		int j = 0;
		int size = imageList.size();
		for (int i = (pageNum - 1) * pageSize; i < pageSize * pageNum
				&& i < size; i++) {
			j = j >= column ? j = 0 : j;
			addImage(imageList.get(i).getLogo(), j++);
		}
	}

	/**
	 * 添加图片
	 * @param imageUrl
	 * @param columnIndex
	 */
	private void addImage(String imageUrl, int columnIndex) {
		ImageView item = (ImageView) LayoutInflater.from(mActivity).inflate(
				R.layout.waterfall_item, null);
		waterfallItems.get(columnIndex).addView(item);
		// 加载图片资源
		NetworkImageUtils.load(mActivity, ImageType.NETWORK, imageUrl,
				R.drawable.banner_default_picture,
				R.drawable.banner_default_picture, item);
	}

}
